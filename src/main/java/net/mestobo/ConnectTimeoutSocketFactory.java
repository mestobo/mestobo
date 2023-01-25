package net.mestobo;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

import ca.uhn.hl7v2.util.StandardSocketFactory;

/** Workaround for https://github.com/hapifhir/hapi-hl7v2/issues/89
 * @author Robert Lichtenberger
 */
public class ConnectTimeoutSocketFactory extends StandardSocketFactory {

	private class ConnectTimeoutSocket extends Socket {
		@Override
		public void connect(SocketAddress endpoint) throws IOException {
			connect(endpoint, 10000);
		}
	}
	
	@Override
	public Socket createSocket() throws IOException {
		Socket socket = new ConnectTimeoutSocket();
		socket.setKeepAlive(true);
		socket.setTcpNoDelay(true);
		return socket;
	}

}
