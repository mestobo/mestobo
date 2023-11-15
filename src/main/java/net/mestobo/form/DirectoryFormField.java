package net.mestobo.form;

import java.io.File;

import javafx.stage.DirectoryChooser;

/** DirectoryFormField allows to choose a directory. */
public class DirectoryFormField extends FileFormFieldBase<DirectoryFormField> {

	public DirectoryFormField(String label) {
		super(label);
	}

	@Override
	protected DirectoryFormField self() {
		return this;
	}

	@Override
	protected File chooseFile() {
		DirectoryChooser chooser = new DirectoryChooser();
		return chooser.showDialog(getPresentation().getScene().getWindow());
	}
}
