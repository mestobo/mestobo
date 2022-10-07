package net.mestobo;

import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class SendHL7Page extends Page {

	
	public SendHL7Page() {
		super(I18N.get("SendADT"));
	}

	@Override
	protected Node createPresentation() {
		Form form = new Form();
		form.addField(I18N.get("FirstName"), GUIFactory.create(TextField.class, form, "firstname"));
		form.addField(I18N.get("LastName"), GUIFactory.create(TextField.class, form, "lastname"));
		form.addField(I18N.get("BirthDate"), GUIFactory.create(DatePicker.class, form, "birthdate"));
		form.addField(I18N.get("VisitNumber"), GUIFactory.create(TextField.class, form, "visitnumber"));
		return form;
	}

}
