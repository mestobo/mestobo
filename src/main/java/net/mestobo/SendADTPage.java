package net.mestobo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import com.github.javafaker.Faker;

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
import net.mestobo.form.ComboFormField;
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
		form.addButton(I18N.get("RandomValues"), "randomvalues", this::fillWithRandomValues).withIcon("dashicons-randomize");
		form.addValidationButton(I18N.get("Send"), "send", this::send).withIcon("fth-send");
		form.addField("host", new TextFormField(I18N.get("Host"))).required();
		form.addField("port", new IntegerFormField(I18N.get("Port"))).defaultValue(2575).required();
		form.addField("receiving_application", new TextFormField(I18N.get("ReceivingApplication"))).required().defaultValue("APP");
		form.addField("receiving_facility", new TextFormField(I18N.get("ReceivingApplication"))).required().defaultValue("FACILITY");
		form.addField("patientid", new TextFormField(I18N.get("PatientID"))).recommended();
		form.addField("lastname", new TextFormField(I18N.get("LastName"))).recommended();
		form.addField("firstname", new TextFormField(I18N.get("FirstName"))).maxLength(30);
		form.addField("sex", new ComboFormField(I18N.get("Sex"))).withValues("A", "F", "M", "N", "O", "U");
		form.addField("birthdate", new DateFormField(I18N.get("BirthDate"))).recommended();
		form.addField("visitnumber", new TextFormField(I18N.get("VisitNumber")));
		form.addField("streetname", new TextFormField(I18N.get("StreetName")));
		form.addField("streetnumber", new TextFormField(I18N.get("StreetNumber")));
		form.addField("city", new TextFormField(I18N.get("City")));
		form.addField("state", new TextFormField(I18N.get("State")));
		form.addField("zipcode", new TextFormField(I18N.get("Zipcode")));
		form.addField("country", new TextFormField(I18N.get("Country")));
		return form;
	}
	
	private void fillWithRandomValues(ActionEvent e) {
		Faker faker = new Faker();
		form.setValue("patientid", faker.idNumber().valid());
		form.setValue("firstname", faker.name().firstName());
		form.setValue("lastname", faker.name().lastName());
		form.setValue("sex", faker.demographic().sex().substring(0, 1));
		form.setValue("birthdate", faker.date().birthday(0, 105).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		form.setValue("visitnumber", faker.idNumber().valid());		
		form.setValue("streetname", faker.address().streetName());		
		form.setValue("streetnumber", faker.address().streetAddressNumber());		
		form.setValue("city", faker.address().city());		
		form.setValue("state", faker.address().state());		
		form.setValue("zipcode", faker.address().zipCode());		
		form.setValue("country", faker.address().country());		
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
			mshSegment.getSendingFacility().getNamespaceID().setValue("SendADTPage");
			mshSegment.getReceivingApplication().getNamespaceID().setValue(form.getValue("receiving_application"));
			mshSegment.getReceivingFacility().getNamespaceID().setValue(form.getValue("receiving_facility"));
			mshSegment.getSequenceNumber().setValue("" + counter.getAndIncrement());
			
			PID pid = request.getPID();
			pid = request.getPID();
			pid.getPatientName(0).getFamilyName().getSurname().setValue(form.getValue("lastname"));
			pid.getPatientName(0).getGivenName().setValue(form.getValue("firstname"));
			pid.getPatientName(0).getNameTypeCode().setValue(getMenuCategory());
			pid.getPatientIdentifierList(0).parse(form.getValue("patientid")); 
			pid.getAdministrativeSex().setValue(form.getValue("sex"));
			pid.getPatientAddress(0).getStreetAddress().getStreetName().setValue(form.getValue("streetname"));
			pid.getPatientAddress(0).getStreetAddress().getDwellingNumber().setValue(form.getValue("streetnumber"));
			pid.getPatientAddress(0).getCity().setValue(form.getValue("citry"));
			pid.getPatientAddress(0).getStateOrProvince().setValue(form.getValue("state"));
			pid.getPatientAddress(0).getZipOrPostalCode().setValue(form.getValue("zipcode"));
			pid.getPatientAddress(0).getCountry().setValue(form.getValue("country"));
			LocalDate bday = form.getValue("birthdate");
			if (bday != null) {
				pid.getDateTimeOfBirth().getTime().setValue(Date.from(bday.atStartOfDay().toInstant(ZoneOffset.UTC)));
			}
			
			EVN evn = request.getEVN();
			evn.getEventOccurred().getTime().setValue(new Date());
			evn.getRecordedDateTime().getTime().setValue(new Date());
			
			PV1 pv1 = request.getPV1();
			pv1.getSetIDPV1().setValue("1");
			pv1.getPatientClass().setValue("I"); // inpatient ... make selectable?
			pv1.getVisitNumber().parse(form.getValue("visitnumber"));

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
