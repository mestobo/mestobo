package net.mestobo.form;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import net.mestobo.GUIFactory;
import net.mestobo.I18N;
import net.synedra.validatorfx.Check;

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
	
	// TODO what about subclasses of TextForm field -> fluent API ?!
	public TextFormField minLength(int minLength) {
		form.getValidator().createCheck()
			.dependsOn("value", valueProperty())
			.withMethod(context -> checkMinLength(context, minLength))
			.decorates(getPresentation())
			.immediate();
		return self();
	}

	private void checkMinLength(Check.Context context, int minLength) {
		if (valueProperty().getValue().length() < minLength) {
			context.error(I18N.get("TooShort", getLabel(), Integer.toString(minLength)));
		}
	}
	
	public TextFormField maxLength(int maxLength) {
		form.getValidator().createCheck()
			.dependsOn("value", valueProperty())
			.withMethod(context -> checkMaxLength(context, maxLength))
			.decorates(getPresentation())
			.immediate();
		return self();
	}

	private void checkMaxLength(Check.Context context, int maxLength) {
		if (valueProperty().getValue().length() > maxLength) {
			context.error(I18N.get("TooLong", getLabel(), Integer.toString(maxLength)));
		}
	}
	
	
	
}
