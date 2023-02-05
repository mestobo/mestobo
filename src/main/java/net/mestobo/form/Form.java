package net.mestobo.form;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import net.mestobo.GUIFactory;
import net.mestobo.ImageButton;
import net.mestobo.ImageToggleButton;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;

/** A Form is a collection of fields and buttons to execute actions. */
public class Form extends BorderPane {
	
	private Map<String, FormField<?, ?>> formFields = new LinkedHashMap<>();
	private FlowPane flow;
	private Validator validator = new Validator();
	private HBox topBar;
		
	public Form() {
		topBar = GUIFactory.create(HBox.class, this, "topbar");
		setTop(topBar);
		
		flow = GUIFactory.create(FlowPane.class, this, "flow");
		getStyleClass().add("Form");
		setId("form");
		setCenter(flow);
	}
	
	/** Add field to form.
	 * @param <T> The type of the field. 
	 * @param dataIndex The data index (must be unique within the form) of the field
	 * @param formField The field itself
	 * @return The given formField
	 */
	public <T extends FormField<?, T>> T addField(String dataIndex, T formField) {
		formFields.put(dataIndex, formField);
		formField.setForm(this);
		flow.getChildren().add(formField.getPresentation());
		return formField;
	}
	
	public ImageToggleButton addToggleButton(String label, String id) {
		ImageToggleButton button = GUIFactory.create(ImageToggleButton.class, this, id);
		button.setText(label);
		topBar.getChildren().add(button);
		return button;
	}
	
	public ImageButton addButton(String label, String id, EventHandler<ActionEvent> eventHandler) {
		ImageButton button = GUIFactory.create(ImageButton.class, this, id);
		button.setText(label);
		button.setOnAction(eventHandler);
		topBar.getChildren().add(button);
		return button;
	}
	
	public ImageButton addValidationButton(String label, String id, EventHandler<ActionEvent> eventHandler) {
		ImageButton button = GUIFactory.create(ImageButton.class, this, id);
		button.setText(label);
		button.setOnAction(eventHandler);
		TooltipWrapper<Button> wrappedButton = new TooltipWrapper<>(
			button,
			validator.containsErrorsProperty(),
			Bindings.concat(validator.createStringBinding())
		);
		GUIFactory.prepare(wrappedButton, this, id + "-wrapper");
		topBar.getChildren().add(wrappedButton);
		return button;
	}
	
	public void addTopBarItem(Node topBarItem) {
		topBar.getChildren().add(topBarItem);
	}
	
	public Validator getValidator() {
		return validator;
	}
	
	/** Get value of a field.
	 * @param <T>
	 * @param dataIndex The data index of the field to get
	 * @return The current value of the field or null if no field exists with the given dataIndex
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(String dataIndex) {
		FormField<T, ?> formField = (FormField<T, ?>) formFields.get(dataIndex);
		return formField != null ? formField.valueProperty().getValue() : null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> void setValue(String dataIndex, T value) {
		FormField<T, ?> formField = (FormField<T, ?>) formFields.get(dataIndex);
		if (formField != null) {
			formField.valueProperty().setValue(value);
		}
	}
}
