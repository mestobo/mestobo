package net.mestobo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import ca.uhn.hl7v2.util.SocketFactory;

public class ConnectTimoutSocketFactory implements SocketFactory {

	@Override
	public Socket createSocket() throws IOException {
		return new ConnectTimeoutSocket();
	}

	@Override
	public Socket createTlsSocket() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServerSocket createServerSocket() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServerSocket createTlsServerSocket() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void configureNewAcceptedSocket(Socket theSocket) throws SocketException {
		// TODO Auto-generated method stub
		
	}

}
