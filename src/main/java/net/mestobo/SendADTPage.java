package net.mestobo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.message.ADT_A01;
import ca.uhn.hl7v2.model.v25.segment.EVN;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.model.v25.segment.PV1;
import ca.uhn.hl7v2.parser.Parser;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import net.mestobo.form.DateFormField;
import net.mestobo.form.Form;
import net.mestobo.form.IntegerFormField;
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
		form.addField("host", new TextFormField(I18N.get("Host"))).required();
		form.addField("port", new IntegerFormField(I18N.get("Port"))).defaultValue(2575).required();
		form.addField("patientid", new TextFormField(I18N.get("PatientID"))).recommended();
		form.addField("lastname", new TextFormField(I18N.get("LastName"))).recommended();
		form.addField("firstname", new TextFormField(I18N.get("FirstName"))).maxLength(30);
		form.addField("birthdate", new DateFormField(I18N.get("BirthDate"))).recommended();
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
			ADT_A01 request = new ADT_A01();
			request.initQuickstart("ADT", "A01", "P");
			
			MSH mshSegment = request.getMSH();
			mshSegment.getSendingApplication().getNamespaceID().setValue("Mestobo");
			mshSegment.getSequenceNumber().setValue("" + counter.getAndIncrement());
			
			PID pid = request.getPID();
			pid = request.getPID();
			// TODO :: get values from form ...
			pid.getPatientName(0).getFamilyName().getSurname().setValue(form.getValue("lastname"));
			pid.getPatientName(0).getGivenName().setValue(form.getValue("firstname"));
			pid.getPatientIdentifierList(0).getIDNumber().setValue(form.getValue("patientid")); // TODO :: correct segment?			
			LocalDate bday = form.getValue("birthdate");
			if (bday != null) {
				pid.getDateTimeOfBirth().getTime().setValue(Date.from(bday.atStartOfDay().toInstant(ZoneOffset.UTC)));
			}
			
			EVN evn = request.getEVN();
			evn.getRecordedDateTime().getTime().setValue(new Date());
			
			PV1 pv1 = request.getPV1();
			pv1.getSetIDPV1().setValue("1");
			pv1.getPatientClass().setValue("I"); // inpatient ... make selectable?
			pv1.getVisitNumber().getIDNumber().setValue(form.getValue("visitnumber"));
			
			try(HapiContext context = new DefaultHapiContext()) {
				Parser parser = context.getPipeParser();
				System.out.println("Sending:\n" + parser.encode(request));
				Connection connection = context.newClient(form.getValue("host"), form.getValue("port"), false);				
				Initiator initiator = connection.getInitiator();
				Message response = initiator.sendAndReceive(request);
				System.out.println("Response:\n" + parser.encode(response));
			} 
		} catch (HL7Exception | IOException | LLPException e) {
			throw new RuntimeException(e);	// TODO error handling?
		}
	}
}
