package net.mestobo.dicomgenerator;

import java.time.LocalDate;

import net.mestobo.form.DateFormField;
import net.mestobo.form.Form;

/** DicomGeneratorForm acts as a facade to Form, allowing to easily add fields to a form that will serve as input for dicom file creation. */
public class DicomGeneratorForm {
	Form form;

	public DicomGeneratorForm() {
		form = new Form();
	}
	
	public void addDate(String key) {
		addDate(key, null);		
	}
	
	public void addDate(String key, LocalDate defaultValue) {
		form.addField(key, new DateFormField(key));
	}
	
	public Form getForm() {
		return form;
	}	
}
