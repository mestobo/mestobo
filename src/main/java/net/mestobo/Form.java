package net.mestobo;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class Form extends FlowPane {
	
	private List<Field> fields = new ArrayList<>();
	
	record Field(String label, Node presentation) { }
	
	public Form() {
		getStyleClass().add("Form");
		setId("flow");
	}
	
	public void addField(String label, Node presentation) {
		addField(new Field(label, presentation));
	}
	
	public void addField(Field field) {
		fields.add(field);
		VBox box = GUIFactory.create(VBox.class, this, "field-box");
		box.getChildren().add(new Label(field.label));
		box.getChildren().add(field.presentation);
		
		getChildren().add(box);
	}
}
