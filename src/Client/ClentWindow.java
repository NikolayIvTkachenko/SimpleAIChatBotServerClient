/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Network.TCPConnection;
import Network.TCPConnectionListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 *
 * @author tkach
 */
public class ClentWindow extends JFrame implements ActionListener, TCPConnectionListener{
    
    //private static final String IP_ADDR = "192.168.0.12";
	private static final String IP_ADDR = "127.0.0.1";
    private static final int PORT = 8189;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    
    private TCPConnection connection;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClentWindow();
            }
        });
    }
    
    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickName = new JTextField("Nick");
    private final JTextField fieldInput = new JTextField();
    
    
    
    private ClentWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        
        log.setEditable(false);
        log.setLineWrap(true);
        
        add(log, BorderLayout.CENTER);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNickName, BorderLayout.NORTH);
        
        fieldInput.addActionListener(this);
        
        setVisible(true);
        
        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (IOException ex) {
            Logger.getLogger(ClentWindow.class.getName()).log(Level.SEVERE, null, ex);
            printMsg("Connection exception: " + ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if(msg.equals(""))return;
        fieldInput.setText("");
        connection.sendString(fieldNickName.getText().toString()+": "+ msg);
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMsg("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String message) {
        printMsg(message);
    }

    @Override
    public void onDisconnection(TCPConnection tcpConnection) {
        printMsg("Connection close");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMsg("Connection exception: " + e);
    }
    
    
    private synchronized void printMsg(String msg){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg+"\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
    
}
