/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tkach
 */
public class TCPConnection {
    
    private final String charsetName = "UTF-8";
    
    private final TCPConnectionListener eventListener;
    
    private final Socket socket;
    private final Thread rxThread;
    private final BufferedReader in;
    private final BufferedWriter out;
    
    public TCPConnection(TCPConnectionListener eventListener, String ipAdress, int port) throws IOException{
        this(eventListener, new Socket(ipAdress, port));
    }
    
    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException{
        this.eventListener = eventListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName(charsetName)));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName(charsetName)));
        
        rxThread = new Thread(new Runnable(){
            @Override
            public void run() {
                
                try {
                    eventListener.onConnectionReady(TCPConnection.this);
                    while(!rxThread.isInterrupted()){
                        String msg = in.readLine();
                        eventListener.onReceiveString(TCPConnection.this, msg);
                    }
                    
                    
                } catch (IOException ex) {
                    Logger.getLogger(TCPConnection.class.getName()).log(Level.SEVERE, null, ex);
                    eventListener.onException(TCPConnection.this, ex);
                }finally{
                    eventListener.onDisconnection(TCPConnection.this);
                }
            }
            
        });
        rxThread.start();
        
    }
    
    public synchronized void sendString(String value){
        try {
            out.write(value + "\r\n");
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(TCPConnection.class.getName()).log(Level.SEVERE, null, ex);
            eventListener.onException(TCPConnection.this, ex);
            disconnect();
        }
        
    }
    
    public synchronized void disconnect(){
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(TCPConnection.class.getName()).log(Level.SEVERE, null, ex);
            eventListener.onException(TCPConnection.this, ex);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ": "+socket.getPort();
    }
    
    
    
}
