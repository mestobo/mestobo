package net.mestobo;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class ConnectTimeoutSocket extends Socket {
	@Override
	public void connect(SocketAddress endpoint) throws IOException {
		connect(endpoint, 10000);
	}
}
