package net.mestobo.form;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/** FileFormField allows to choose a file. */
public class FileFormField extends FileFormFieldBase<FileFormField> {

	private 	FileChooser chooser = new FileChooser();

	public FileFormField(String label) {
		super(label);
	}

	@Override
	protected FileFormField self() {
		return this;
	}

	@Override
	protected File chooseFile() {
		return chooser.showOpenDialog(getPresentation().getScene().getWindow());
	}
	
	public FileFormField withExtensionFilter(String description, String ... extensions) {
		chooser.getExtensionFilters().add(new ExtensionFilter(description, extensions));
		return this;
	}
}
