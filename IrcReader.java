package com.sinfonier.spouts;

import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.Session;
import jerklib.events.*;
import jerklib.events.IRCEvent.Type;
import jerklib.listeners.IRCEventListener;

public class IrcReader implements IRCEventListener extends BaseSinfonierSpout {

	private ConnectionManager manager;
	private String username;
	private String servername;
	private String channel;
      
    public IrcReader(String spoutName, String xmlPath) {
        super(xmlPath, spoutName);
      
    }

    public void useropen() {
      
		this.name = (String)this.getParam("username");
		this.servername = (String)this.getParam("servername");
		this.channel = (String)this.getParam("channel");

		manager = new ConnectionManager(new Profile(this.name));
		Session session = manager.requestConnection(this.servername);
		session.addIRCEventListener(this);
    }
   
    public void usernextTuple() {
       this.emit()
    }

    public void userclose() {
       this.emit()
    }

	public void receiveEvent(IRCEvent e)
	{
		if (e.getType() == Type.CONNECT_COMPLETE) {
			e.getSession().join(this.channel);
		}
		else if (e.getType() == Type.CHANNEL_MESSAGE) {
			MessageEvent me = (MessageEvent) e;
			//System.out.println("<" + me.getNick() + ">"+ ":" + me.getMessage());
			this.addField(me.getNick(), me.getMessage());
		}
		else if (e.getType() == Type.JOIN_COMPLETE) {
			JoinCompleteEvent jce = (JoinCompleteEvent) e;
			//jce.getChannel().say("Hello from Jerklib ");
		}
		else {
			//System.out.println(e.getType() + " " + e.getRawEventData());
		}
	}

}
