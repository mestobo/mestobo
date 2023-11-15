package net.mestobo.form;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Region;
import net.mestobo.GUIFactory;

/** ComboFormField allows to select a single value from a list of predefined values. */
public class ComboFormField extends FormField<String, ComboFormField> {

	private ComboBox<String> combobox; 
	
	public ComboFormField(String label) {
		super(label);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Node createInputControl(Region parent) {
		combobox = GUIFactory.create(ComboBox.class, parent, "formfield-input");
		return combobox;
	}

	@Override
	public Property<String> valueProperty() {
		return combobox.valueProperty();
	}

	@Override
	protected ObservableBooleanValue emptyProperty() {
		return combobox.selectionModelProperty().isNull();
	}

	@Override
	protected ComboFormField self() {
		return this;
	}
	
	public ComboFormField withValues(String ... values) {
		combobox.getItems().addAll(values);
		return this;
	}
}
