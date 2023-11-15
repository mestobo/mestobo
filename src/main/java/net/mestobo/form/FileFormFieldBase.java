package net.mestobo.form;

import java.io.File;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import net.mestobo.GUIFactory;
import net.mestobo.ImageButton;

/** FileFormFieldBase servers as base class for directory and file form fields */
public abstract class FileFormFieldBase<SELF extends FormField<File, SELF>> extends FormField<File, SELF> {

	private TextField textField;
	private Button openChooserButton;
	private SimpleObjectProperty<File> valueProperty;
	
	public FileFormFieldBase(String label) {
		super(label);
	}
	
	protected abstract File chooseFile();
	
	@Override
	protected Node createInputControl(Region parent) {
		valueProperty = new SimpleObjectProperty<File>(null);
		HBox box = GUIFactory.create(HBox.class, parent, "outer");
		textField = GUIFactory.create(TextField.class, box, "formfield-input");
		textField.setEditable(false);
		textField.textProperty().bind(Bindings.createStringBinding(this::fileToString, valueProperty));
		openChooserButton = GUIFactory.create(ImageButton.class, box, "chooser").withIcon("fth-more-horizontal");
		openChooserButton.setOnAction(this::openChooser);
		box.getChildren().addAll(textField, openChooserButton);
		return box;
	}
	
	private String fileToString() {
		return valueProperty.get() == null ? null : valueProperty.get().getAbsolutePath();
	}
	
	private void openChooser(ActionEvent e) {
		valueProperty.set(chooseFile());
	}
	
	@Override
	public Property<File> valueProperty() {
		return valueProperty;
	}

	@Override
	protected ObservableBooleanValue emptyProperty() {
		return Bindings.isNull(valueProperty);
	}
}
