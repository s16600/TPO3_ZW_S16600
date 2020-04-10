//Generated by GuiGenie - Copyright (c) 2004 Mario Awad.
//Home Page http://guigenie.cjb.net - Check often for new versions!

package zad1;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.*;

public class ChatWindow extends JFrame {
    private JTextArea chatText;
    private JTextField messageText;
    private JLabel userName;
    private String myNick;

    public ChatWindow(Client client) {
    	super("Chat panel");
    	
        //construct components
        chatText = new JTextArea (5, 5);
        messageText = new JTextField ();
        userName = new JLabel ("User Name: ");

        //adjust size and set layout
        setPreferredSize (new Dimension (944, 587));
        setLayout (null);

        //add components
        add (chatText);
        add (messageText);
        add (userName);

        //set component bounds (only needed by Absolute Positioning)
        chatText.setBounds (5, 5, 935, 515);
        messageText.setBounds (5, 555, 935, 25);
        userName.setBounds (5, 530, 200, 25);   
        
        messageText.addActionListener(e->{
        	try {
				client.sendToServer(myNick + ": " + messageText.getText());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	messageText.setText("");
        });
        
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        //pack();
        
        int delta = (int) (Math.random()*300);
        
        setLocation(100+delta,100+delta);
        setSize(952,615);
        setResizable(false);
        setVisible (true);
    }
    
    public void setNick(String mynick) {
    	this.myNick = mynick;
    	userName.setText("User Name: " + this.myNick);
    }
    
    public void addText(String text) {
    	chatText.append(text+"\n");
    }
}