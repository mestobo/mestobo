package net.mestobo;

import javafx.scene.Node;

/** Booch utility class for style handling.
 */
public class GUIFactory {
	
	public static <E extends Node> E create(Class<E> clazz, Object parent, String id) {
		try {
			E instance = clazz.getConstructor().newInstance();
			prepare(instance, parent, id);
			return instance;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	/** Set style classes and id for a node. This method will add 3 styles: "mestobo", the name of the class of the passed parent, the name of the package of the passed parent. 
	 * @param node The node to prepare
	 * @param parent The parent class to derive style names from
	 * @param id The id to set for the node
	 * @return node
	 */
	public static <E extends Node> E prepare(E node, Object parent, String id) {
		Class<?> parentClass = parent.getClass();
		Package parentPackage = parentClass.getPackage();
		
		node.getStyleClass().addAll("mestobo", parentPackage.getName());
		Class<?> current = parentClass;
		while (current != null && current.getPackage().getName().startsWith("net.mestobo")) {
			node.getStyleClass().add(current.getSimpleName());
			current = current.getSuperclass();
		}
		
		node.setId(id);
		return node;
	}	
}
