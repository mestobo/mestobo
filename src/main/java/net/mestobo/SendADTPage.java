package net.mestobo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.v27.message.ADT_A01;
import ca.uhn.hl7v2.model.v27.segment.EVN;
import ca.uhn.hl7v2.model.v27.segment.MSH;
import ca.uhn.hl7v2.model.v27.segment.PID;
import ca.uhn.hl7v2.model.v27.segment.PV1;
import ca.uhn.hl7v2.parser.Parser;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import net.mestobo.form.DateFormField;
import net.mestobo.form.Form;
import net.mestobo.form.TextFormField;

/** SendADTPage allows to create/send an ADT message. */
public class SendADTPage extends MenuPage {
	
	private static AtomicLong counter = new AtomicLong(1);
	private Form form;
	
	public SendADTPage() {
		super(I18N.get("SendADT"));
	}

	@Override
	protected Node createPresentation() {
		form = new Form();
		form.addButton(I18N.get("Send"), "send", this::send);
		form.addField("patientid", new TextFormField(I18N.get("PatientID"))).required();
		form.addField("lastname", new TextFormField(I18N.get("LastName"))).required();
		form.addField("firstname", new TextFormField(I18N.get("FirstName"))).maxLength(30);
		form.addField("birthdate", new DateFormField(I18N.get("BirthDate"))).required();
		form.addField("visitnumber", new TextFormField(I18N.get("VisitNumber")));
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
			// TODO :: make event selectable
			ADT_A01 message = new ADT_A01();
			message.initQuickstart("ADT", "A01", "T");
			
			MSH mshSegment = message.getMSH();
			mshSegment.getSendingApplication().getNamespaceID().setValue("Mestobo");
			mshSegment.getSequenceNumber().setValue("" + counter.getAndIncrement());
			
			PID pid = message.getPID();
			pid = message.getPID();
			// TODO :: get values from form ...
			pid.getPatientName(0).getFamilyName().getSurname().setValue(form.getValue("lastname"));
			pid.getPatientName(0).getGivenName().setValue(form.getValue("firstname"));
			pid.getPatientIdentifierList(0).getIDNumber().setValue(form.getValue("patientid")); // TODO :: correct segment?			
			LocalDate bday = form.getValue("birthdate");
			pid.getDateTimeOfBirth().setValue(Date.from(bday.atStartOfDay().toInstant(ZoneOffset.UTC)));
			
			EVN evn = message.getEVN();
			evn.getRecordedDateTime().setValue(new Date());
			
			PV1 pv1 = message.getPV1();
			pv1.getSetIDPV1().setValue("1");
			pv1.getPatientClass().getIdentifier().setValue("I"); // inpatient ... make selectable?
			pv1.getPatientClass().getText().setValue("Inpatient");
			pv1.getVisitNumber().getIDNumber().setValue(form.getValue("visitnumber"));
			
			try(HapiContext context = new DefaultHapiContext()) {
				Parser parser = context.getPipeParser();
				String encodedMessage = parser.encode(message);
				System.out.println(encodedMessage);
			}
		} catch (HL7Exception | IOException e) {
			throw new RuntimeException(e);	// TODO error handling?
		}
	}
}
