/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import ChatBot.SimpleBot;
import Network.TCPConnection;
import Network.TCPConnectionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 *
 * @author tkach
 */
public class ChatServer implements TCPConnectionListener{
    
    
    SimpleBot sBot;
    boolean useChatBot = true;
    
    public static void main(String[] args) {
        new ChatServer();
    }
    
    private final ArrayList<TCPConnection> connections = new ArrayList<>();
    
    private ChatServer(){
        System.out.println("Server running");
        
        sBot = new SimpleBot();
        
        
        try(ServerSocket serverSocket = new ServerSocket(8189)){
            while(true){
                try{
                    new TCPConnection(this, serverSocket.accept());
                }catch(IOException e){
                    System.out.println("TCPConnection exception : "+e);
                }
            }
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client connected: "+ tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String message) {
        sendToAllConnections(message);
        //add answer chatbot
        message = sBot.sayInReturn(message, useChatBot);
        sendToAllConnections(message);
        
    }

    @Override
    public synchronized void onDisconnection(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Client disconnected: "+ tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exeption: "+ e);
    }
    
    private void sendToAllConnections(String value){
        System.out.println(value);
        final int cht = connections.size();
        //for(int i = 0; i < connections.size(); i++){
        for(int i = 0; i < cht; i++){
            connections.get(i).sendString(value);
        }
    }
    
}
