package net.mestobo.form;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.Region;
import net.mestobo.GUIFactory;

public class IntegerFormField extends FormField<Integer, IntegerFormField> {

	private Spinner<Integer> spinner;
	private IntegerSpinnerValueFactory valueFactory;

	public IntegerFormField(String label) {
		super(label);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Node createInputControl(Region parent) {
		spinner = GUIFactory.create(Spinner.class, parent, "formfield-input");
		valueFactory = new IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE);
		spinner.setValueFactory(valueFactory);
		spinner.setEditable(true);
		return spinner;
	}

	@Override
	protected Property<Integer> valueProperty() {
		return spinner.getValueFactory().valueProperty();
	}

	@Override
	protected ObservableBooleanValue emptyProperty() {
		return spinner.getValueFactory().valueProperty().isNull();
	}

	@Override
	protected IntegerFormField self() {
		return this;
	}
	
	protected IntegerFormField min(int min) {
		valueFactory.setMin(min);
		return self();
	}

	protected IntegerFormField max(int max) {
		valueFactory.setMin(max);
		return self();
	}
}
