package net.mestobo;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;

/** A Form is a collection of fields and buttons to execute actions. */
public class Form extends BorderPane {
	
	private List<Field<?>> fields = new ArrayList<>();
	private FlowPane flow;
	private Validator validator = new Validator();
	private VBox topBar;
	
	record Field<T extends Node>(String label, T presentation) { }
	
	public Form() {
		topBar = GUIFactory.create(VBox.class, this, "topbar");
		setTop(topBar);
		
		flow = GUIFactory.create(FlowPane.class, this, "flow");
		getStyleClass().add("Form");
		setId("form");
		setCenter(flow);
	}
	
	public <T extends Node> Field<T> addField(String label, T presentation) {
		return addField(new Field<>(label, presentation));
	}
	
	public <T extends Node> Field<T> addField(Field<T> field) {
		fields.add(field);
		VBox box = GUIFactory.create(VBox.class, this, "field-box");
		box.getChildren().add(new Label(field.label));
		box.getChildren().add(field.presentation);
		
		flow.getChildren().add(box);
		return field;
	}
	
	public void addButton(String label, String id, EventHandler<ActionEvent> eventHandler) {
		Button button = GUIFactory.create(Button.class, this, id);
		button.setText(label);
		button.setOnAction(eventHandler);
		TooltipWrapper<Button> wrappedButton = new TooltipWrapper<>(
			button,
			validator.containsErrorsProperty(),
			Bindings.concat(validator.createStringBinding())
		);
		GUIFactory.prepare(wrappedButton, this, id + "-wrapper");
		topBar.getChildren().add(wrappedButton);
	}
	
	public Validator getValidator() {
		return validator;
	}
}
