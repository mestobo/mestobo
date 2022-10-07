package net.mestobo;

import javafx.scene.Node;

public abstract class Page {
	
	private String title;
	private Node presentation;
	
	public Page(String title) {
		this.title = title;
	}
	
	public void init() {
		presentation = createPresentation();
		presentation.getStyleClass().add("mestoboPage");
	}
	
	public String getTitle() {
		return title;
	}
	
	protected abstract Node createPresentation();
	
	public Node getPresentation() {
		return presentation;
	}	
}
