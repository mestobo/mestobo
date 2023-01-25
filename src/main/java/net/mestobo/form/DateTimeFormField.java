package net.mestobo.form;

import java.time.LocalDateTime;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import net.mestobo.GUIFactory;
import tornadofx.control.DateTimePicker;

/** DateTimeFormField allows to give a single point in time. */
public class DateTimeFormField extends FormField<LocalDateTime, DateTimeFormField> {

	private DateTimePicker datePicker;
	
	public DateTimeFormField(String label) {
		super(label);
	}

	@Override
	protected Node createInputControl(Region parent) {
		datePicker = GUIFactory.create(DateTimePicker.class, parent, "formfield-input");
		return datePicker;
	}

	@Override
	protected Property<LocalDateTime> valueProperty() {
		return datePicker.dateTimeValueProperty();
	}
	
	@Override
	protected ObservableBooleanValue emptyProperty() {
		return datePicker.dateTimeValueProperty().isNull();
	}
	
	@Override
	protected DateTimeFormField self() {
		return this;
	}
}
