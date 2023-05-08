package net.mestobo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.github.javafaker.Faker;
import com.google.inject.Inject;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.message.ADT_A01;
import ca.uhn.hl7v2.model.v25.segment.EVN;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.model.v25.segment.PV1;
import ca.uhn.hl7v2.parser.Parser;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import net.mestobo.form.ComboFormField;
import net.mestobo.form.DateFormField;
import net.mestobo.form.DateTimeFormField;
import net.mestobo.form.Form;
import net.mestobo.form.IntegerFormField;
import net.mestobo.form.TextFormField;

/** SendADTPage allows to create/send an ADT message. */
public class SendADTPage extends MenuPage {
	
	private static final int DEFAULT_DELAY = 5; 
	
	private static AtomicLong counter = new AtomicLong(1);
	private Form form;
	private Timeline autoFire = new Timeline(new KeyFrame(Duration.seconds(5), this::autoFire));
	private CheckBox autoFireToggle;
	private TextField delayInput;
	
	@Inject
	private BackgroundTaskExecutor backgroundTaskExecutor;
	
	public SendADTPage() {
		super(I18N.get("SendADT"));
		autoFire.setCycleCount(Timeline.INDEFINITE);
	}

	@Override
	protected Node createPresentation() {
		form = new Form();
		form.addButton(I18N.get("RandomValues"), "randomvalues", this::fillWithRandomValues).withIcon("dashicons-randomize");
		autoFireToggle = GUIFactory.create(CheckBox.class, form, "autofire-checkbox");
		autoFireToggle.selectedProperty().addListener((observable, oldValue, newValue) -> updatePlayStop());
		form.addTopBarItem(autoFireToggle);
		form.addTopBarItem(createDelayInput());		
		form.addTopBarItem(new Text("s"));		
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
		form.addField("streetname", new TextFormField(I18N.get("StreetName")));
		form.addField("streetnumber", new TextFormField(I18N.get("StreetNumber")));
		form.addField("city", new TextFormField(I18N.get("City")));
		form.addField("state", new TextFormField(I18N.get("State")));
		form.addField("zipcode", new TextFormField(I18N.get("Zipcode")));
		form.addField("country", new TextFormField(I18N.get("Country")));
		form.addField("visitnumber", new TextFormField(I18N.get("VisitNumber")));
		form.addField("admitdatetime", new DateTimeFormField(I18N.get("AdmitDateTime")));
		return form;
	}
	
	private Node createDelayInput() {		
		delayInput  = new TextField();
		TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(), DEFAULT_DELAY);
		delayInput.setTextFormatter(formatter);
		formatter.valueProperty().addListener(this::delayChanged);
		return delayInput;
	}
	
	private void delayChanged(ObservableValue<? extends Integer> observable, Integer oldValue, Integer  newValue) {
		autoFire.stop();
		if (newValue != null && newValue > 0) {
			autoFire = new Timeline(new KeyFrame(Duration.seconds(newValue), this::autoFire));
			autoFire.setCycleCount(Timeline.INDEFINITE);
		} else {
			delayInput.setText("" + DEFAULT_DELAY);
		}
		updatePlayStop();		
	}
	
	private void updatePlayStop() {
		if (autoFireToggle.isSelected()) {
			autoFire.play();
		} else {
			autoFire.stop();
		}
	}
	
	private void autoFire(ActionEvent e) {
		fillWithRandomValues(e);
		if (!form.getValidator().containsErrors()) { 
			send(e);
		}
	}

	private void fillWithRandomValues(ActionEvent e) {
		Faker faker = new Faker();
		form.setValue("patientid", faker.idNumber().valid());
		form.setValue("firstname", faker.name().firstName());
		form.setValue("lastname", faker.name().lastName());
		form.setValue("sex", faker.demographic().sex().substring(0, 1));
		form.setValue("birthdate", toLocalDate(faker.date().birthday(0, 105)));
		form.setValue("streetname", faker.address().streetName());		
		form.setValue("streetnumber", faker.address().streetAddressNumber());		
		form.setValue("city", faker.address().city());		
		form.setValue("state", faker.address().state());		
		form.setValue("zipcode", faker.address().zipCode());		
		form.setValue("country", faker.address().country());		
		form.setValue("visitnumber", faker.idNumber().valid());		
		form.setValue("admitdatetime", toLocalDateTime(faker.date().future(14, TimeUnit.DAYS)));		
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
	
	public void send(ActionEvent __) {
		try {
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
			pid.getDateTimeOfBirth().getTime().setValue(toDate(form.getValue("birthdate")));
			
			EVN evn = request.getEVN();
			evn.getEventOccurred().getTime().setValue(new Date());
			evn.getRecordedDateTime().getTime().setValue(new Date());
			
			PV1 pv1 = request.getPV1();
			pv1.getSetIDPV1().setValue("1");
			pv1.getPatientClass().setValue("I"); 
			pv1.getVisitNumber().parse(form.getValue("visitnumber"));
			pv1.getAdmitDateTime().getTime().setValue(toDateWithTime(form.getValue("admitdatetime")));

			backgroundTaskExecutor.submitTask(new SendADTTask(request, form.getValue("host"), form.getValue("port")));
						
		} catch (HL7Exception | IOException e) {
			throw new RuntimeException(e);	// TODO error handling?
		}
	}
	
	private class SendADTTask extends Task<Void> {
		
		private AbstractMessage request;
		private String host;
		private int port;

		public SendADTTask(AbstractMessage request, String host, int port) {
			this.request = request;
			this.host = host;
			this.port = port;
			updateTitle(I18N.get("SendADT"));
		}

		@Override
		protected Void call() throws Exception {
			try(HapiContext context = new DefaultHapiContext()) {
				context.setSocketFactory(new ConnectTimeoutSocketFactory());
				Parser parser = context.getPipeParser();
				updateMessage(I18N.get("ConnectingWith", host, port));
				Connection connection = context.newClient(host, port, false);				
				Initiator initiator = connection.getInitiator();
				initiator.setTimeout(30, TimeUnit.SECONDS);
				updateMessage(I18N.get("SendingMessage"));
				Message response = initiator.sendAndReceive(request);
				updateMessage(I18N.get("ResponseReceived"));
				System.out.println("Response:\n" + parser.encode(response));
			} 
			return null;
		}
		
	}

	private LocalDate toLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	private LocalDateTime toLocalDateTime(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
	
	private Date toDate(LocalDate localDate) {
		Date date = null;
		if (localDate != null) {
			date = Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));
		}
		return date;
	}
	
	private Date toDateWithTime(LocalDateTime localDateTime) {
		Date date = null;
		if (localDateTime != null) {
			date = Date.from(localDateTime.toInstant(ZoneOffset.UTC));
		}
		return date;
	}
	

	
}
