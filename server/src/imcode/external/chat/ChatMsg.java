
package imcode.external.chat;

import java.util.*;

public class ChatMsg{
	private final static String CVS_REV = "$Revision$" ;
	private final static String CVS_DATE = "$Date$" ;

	private String _senderStr;
	private int _msgType;
	private String _msgTypeStr;
	private String _chatMsg;
	private String _recieverStr;
	private int _reciever;
	private int _number;
	private int _sender;
	private String _dateTime;
	//	private Date date;
	/**
	*Default constructor
	*/
	public ChatMsg(String chatMsg, String recieverStr,int reciever, int msgType, String msgTypeStr, String senderStr,int sender, String dateTime){
		_chatMsg = chatMsg;
		_reciever =	reciever;
		_recieverStr = recieverStr;
		_msgType = msgType;
		_msgTypeStr = msgTypeStr;
		_senderStr = senderStr;
		_dateTime = dateTime;
		_sender = sender;
	}

	public String toString(){
		return _senderStr+" "+ _msgTypeStr+" "+_recieverStr+" : "+_chatMsg;
	}
	
	protected void setIdNumber(int number){
		_number = number;
	}
	
	public int getIdNumber(){
		return _number;
	}

	public String getDateTime(){
		return _dateTime;
	}

	public String getMessage(){
		return _chatMsg;
	}

	public int getMsgType(){
		return _msgType;
	}

	public int getReciever(){
		return _reciever;
	}

	public String getRecieverStr(){
		return _recieverStr;
	}

	public String getMsgTypeStr(){
		return _msgTypeStr;
	}
	
	public String getSenderStr(){
		return _senderStr;
	}

	public int getSender(){
		return _sender;
	}
}//end class
