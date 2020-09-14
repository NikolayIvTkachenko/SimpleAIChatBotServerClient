/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatBot;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

/**
 *
 * @author tkach
 */
public class SimpleChatBot extends JFrame implements ActionListener{
    
    
    private final String TITLE_OF_PROGRAM = "Chatter: simple chatbot";
    private final int START_LOCATION = 200;
    private final int WINDOW_WIDTH = 350;
    private final int WINDOW_HEIGHT = 450;
    
    private JTextArea dialogue;
    //private JTextPane dialogue;
    private JCheckBox ai;
    private JTextField message;
    SimpleBot sbot;
    //SimpleAttributesSet botStyle;
    
    
    public static void main(String[] args){
        
        new SimpleChatBot();
        
    }
    
    private SimpleChatBot(){
        
        sbot = new SimpleBot();
        
        setTitle(TITLE_OF_PROGRAM);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(START_LOCATION, START_LOCATION, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        //dialogue = new JTextPane();
        dialogue = new JTextArea();
        dialogue.setEditable(false);
        dialogue.setLineWrap(true);
        //dialogue.setContentType("text/html");
        JScrollPane scrollBar = new JScrollPane(dialogue);
        
        JPanel bp = new JPanel();
        bp.setLayout(new BoxLayout(bp, BoxLayout.X_AXIS));
        
        ai = new JCheckBox("AI");
        //ai.doClick();
        
        message = new JTextField();
        message.addActionListener(this);
        
        JButton enter = new JButton("Enter");
        enter.addActionListener(this);
        
        bp.add(ai);
        bp.add(message);
        bp.add(enter);
        
        add(BorderLayout.CENTER, scrollBar);
        add(BorderLayout.SOUTH, bp);
        
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(message.getText().trim().length() > 0){
            dialogue.append(message.getText() + "\r\n");
            dialogue.append(TITLE_OF_PROGRAM.substring(0,9)+sbot.sayInReturn(message.getText(), ai.isSelected())+"\r\n");
        }
        message.setText("");
        message.requestFocusInWindow();
    }
    
}
