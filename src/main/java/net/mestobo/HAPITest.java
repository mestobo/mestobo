package net.mestobo;

import java.io.IOException;

import ca.uhn.hl7v2.*;
import ca.uhn.hl7v2.app.Connection;

// Test program for https://github.com/hapifhir/hapi-hl7v2/issues/89
public class HAPITest {

	static volatile Connection connection;

	public static void main(String[] args) throws IOException, HL7Exception, InterruptedException {
		try(HapiContext context = new DefaultHapiContext()) {
			connection = context.newClient("google.com", 6666, false);
			System.out.println("Established connection " + connection);
		} 
	}
}
