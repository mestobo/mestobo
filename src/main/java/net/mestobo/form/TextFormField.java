package net.mestobo.form;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import net.mestobo.GUIFactory;

public class TextFormField extends FormField<String, TextFormField> {
	
	private TextField textField;

	public TextFormField(String label) {
		super(label);
	}

	@Override
	protected Node createInputControl(Region parent) {
		textField = GUIFactory.create(TextField.class, parent, "formfield-input");
		return textField;
	}

	@Override
	protected Property<String> valueProperty() {
		return textField.textProperty();
	}

	@Override
	protected ObservableBooleanValue emptyProperty() {
		return textField.textProperty().isEmpty();
	}
	
	@Override
	protected TextFormField self() {
		return this;
	}
}
