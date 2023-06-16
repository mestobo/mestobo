package net.mestobo;


import javafx.scene.Node;
import net.mestobo.form.Form;
import net.mestobo.form.TextFormField;

/** DicomGeneratorPage allows to generate fictitious DICOM studies. */
public class DicomGeneratorPage extends MenuPage {
	
	public DicomGeneratorPage() {
		super(I18N.get("GenerateDicomStudy"));
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
		return I18N.get("GenerateDicomStudy");
	}

	@Override
	protected Node createPresentation() {
		Form form = new Form();
		form.addField("destination_directory", new TextFormField(I18N.get("DestinationDirectory"))).required();
		return form;
	}
}
