package net.mestobo.form;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import net.mestobo.GUIFactory;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;

/** A Form is a collection of fields and buttons to execute actions. */
public class Form extends BorderPane {
	
	private Map<String, FormField<?, ?>> formFields = new LinkedHashMap<>();
	private FlowPane flow;
	private Validator validator = new Validator();
	private VBox topBar;
		
	public Form() {
		topBar = GUIFactory.create(VBox.class, this, "topbar");
		setTop(topBar);
		
		flow = GUIFactory.create(FlowPane.class, this, "flow");
		getStyleClass().add("Form");
		setId("form");
		setCenter(flow);
	}
	
	public <T extends FormField<?, T>> T addField(String dataIndex, T formField) {
		formFields.put(dataIndex, formField);
		formField.setForm(this);
		flow.getChildren().add(formField.getPresentation());
		return formField;
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
