package pl.pewnypl.wiiutablet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

/**
 * A simple WebSocketServer implementation. Keeps track of a "chatroom".
 */
public class Main extends WebSocketServer {

	Robot robot;
	
	int lastState=0;
	
	static int resX = 1920;
	static int resY = 1080;
	static int resWiiX = 854;
	static int resWiiY = 480;
	
	float xFact=1;
	float yFact=1;
	
	int packets=0;
	int del=2;
	
	
	public Main( int port ) throws UnknownHostException {
		super( new InetSocketAddress( port ) );
		try {
			robot = new Robot();
			robot.setAutoDelay(del);
			robot.setAutoWaitForIdle(true);
		} catch (AWTException e) {
		}
		xFact = (float)resX / (float)resWiiX;
		yFact = (float)resY / (float)resWiiY;
	}

	public Main( InetSocketAddress address ) {
		super( address );
	}

	@Override
	public void onOpen( WebSocket conn, ClientHandshake handshake ) {
		packets=0;
	}

	@Override
	public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
	}

	@Override
	public void onMessage( WebSocket conn, String message ) {
		packets++;
		String[] sp = message.split(";");
		try{
			if (Integer.parseInt(sp[0])==1)
			{
				robot.mouseMove((int)(Integer.parseInt(sp[1])*xFact), (int)(Integer.parseInt(sp[2])*yFact));
				if(lastState == 0)
				{
					lastState = 1;
					robot.mousePress(InputEvent.BUTTON1_MASK);
				}
			}
			else if(lastState == 1)
			{
				lastState = 0;
				robot.mouseRelease(InputEvent.BUTTON1_MASK);			
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println( packets );
	}

	public void onFragment( WebSocket conn, Framedata fragment ) {
	}

	public static void main( String[] args ) throws InterruptedException , IOException {
		int port = 8887;
		try {
			port = Integer.parseInt( args[ 0 ] );
		} catch ( Exception ex ) {
		}
		Main s = new Main( port );
		s.start();
		System.out.println( "Main started on port: " + s.getPort() );
	}
	
	@Override
	public void onError( WebSocket conn, Exception ex ) {
		ex.printStackTrace();
		if( conn != null ) {
			// some errors like port binding failed may not be assignable to a specific websocket
		}
	}
}
