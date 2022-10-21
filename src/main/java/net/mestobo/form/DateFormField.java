package net.mestobo.form;

import java.time.LocalDate;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Region;
import net.mestobo.GUIFactory;

public class DateFormField extends FormField<LocalDate, DateFormField> {

	private DatePicker datePicker;
	
	public DateFormField(String label) {
		super(label);
	}

	@Override
	protected Node createInputControl(Region parent) {
		datePicker = GUIFactory.create(DatePicker.class, parent, "formfield-input");
		return datePicker;
	}

	@Override
	protected Property<LocalDate> valueProperty() {
		return datePicker.valueProperty();
	}
	
	@Override
	protected ObservableBooleanValue emptyProperty() {
		return datePicker.valueProperty().isNull();
	}
	
	@Override
	protected DateFormField self() {
		return this;
	}
}
