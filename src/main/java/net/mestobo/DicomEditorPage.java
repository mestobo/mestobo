package net.mestobo;


import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.mestobo.form.Form;
import net.mestobo.form.TextFormField;

public class DicomEditorPage extends MenuPage {
	
	public DicomEditorPage() {
		super(I18N.get("EditDicomFile"));
	}

	@Override
	public String getMenuLabel() {
		return I18N.get("Dicom");
	}
	
	@Override
	public String getMenuCategory() {
		return "dicom";
	}

	@Override
	public String getMenuItemLabel() {
		return I18N.get("EditDicomFile");
	}

	@Override
	protected Node createPresentation() {
		Form openFileForm = new Form();
		openFileForm.addField("File", new TextFormField("File"));
		
		return new VBox(
			openFileForm,
			new HBox(10L, new TextArea("Dicom Dump"), new TextArea("Hex values"))
		);
	}
}
