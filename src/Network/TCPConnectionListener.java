/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

/**
 *
 * @author tkach
 */
public interface TCPConnectionListener {
    
    void onConnectionReady(TCPConnection tcpConnection);
    void onReceiveString(TCPConnection tcpConnection, String message);
    void onDisconnection(TCPConnection tcpConnection);
    void onException(TCPConnection tcpConnection, Exception e);
    
}
