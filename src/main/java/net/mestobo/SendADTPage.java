package net.mestobo;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.v27.message.ADT_A01;
import ca.uhn.hl7v2.model.v27.segment.MSH;
import ca.uhn.hl7v2.model.v27.segment.PID;
import ca.uhn.hl7v2.parser.Parser;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import net.mestobo.Form.Field;

/** SendADTPage allows to create/send an ADT message. */
public class SendADTPage extends MenuPage {
	
	private static AtomicLong counter = new AtomicLong(1);
	
	public SendADTPage() {
		super(I18N.get("SendADT"));
	}

	@Override
	protected Node createPresentation() {
		Form form = new Form();
		form.addButton(I18N.get("Send"), "send", this::send);
		Field<TextField> fn = form.addField(I18N.get("FirstName"), GUIFactory.create(TextField.class, form, "firstname"));
		// TODO :: this is way too clumsy ...
		form.getValidator().createCheck()
			.dependsOn("text", fn.presentation().textProperty())
			.withMethod(c -> {
				String text = c.get("text");
				if (text.isBlank()) {
					c.error("Required field"); // TODO: i18n strings with parameters ...
				}
			})
			.decorates(fn.presentation()) // or the outer box?
			.immediate();
			
		form.addField(I18N.get("LastName"), GUIFactory.create(TextField.class, form, "lastname"));
		form.addField(I18N.get("BirthDate"), GUIFactory.create(DatePicker.class, form, "birthdate"));
		form.addField(I18N.get("VisitNumber"), GUIFactory.create(TextField.class, form, "visitnumber"));
		return form;
	}

	@Override
	public String getMenuLabel() {
		return I18N.get("HL7");
	}
	
	@Override
	public String getMenuCategory() {
		return "adt";
	}

	@Override
	public String getMenuItemLabel() {
		return I18N.get("SendADT");
	}
	
	public void send(ActionEvent __) { // TODO style: do we want to make unused (bind) params invisible like this?
		try {
			// TODO :: make hl7-version selectable?
			ADT_A01 adt = new ADT_A01();
			adt.initQuickstart("ADT", "A01", "P");
			
			MSH mshSegment = adt.getMSH();
			mshSegment.getSendingApplication().getNamespaceID().setValue("Mestobo");
			mshSegment.getSequenceNumber().setValue("" + counter.getAndIncrement());
			
			PID pid = adt.getPID();
			pid = adt.getPID();
			// TODO :: get values from form ...
			pid.getPatientName(0).getFamilyName().getSurname().setValue("Lichtenberger");
			pid.getPatientName(0).getGivenName().setValue("Robert");
			pid.getPatientIdentifierList(0).getIDNumber().setValue("4711"); // TODO :: correct segment?			
			pid.getDateTimeOfBirth().setValue(new Date());
			
			try(HapiContext context = new DefaultHapiContext()) {
				Parser parser = context.getPipeParser();
				String encodedMessage = parser.encode(adt);
				System.out.println(encodedMessage);
			}
		} catch (HL7Exception | IOException e) {
			throw new RuntimeException(e);	// TODO error handling?
		}
	}
}
