package net.mestobo;

import javafx.scene.Node;
import net.mestobo.form.Form;

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
		return new Form();
	}

}
