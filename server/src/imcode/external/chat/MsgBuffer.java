

package imcode.external.chat;

import java.util.*;

public class MsgBuffer{
	private final static String CVS_REV = "$Revision$" ;
	private final static String CVS_DATE = "$Date$" ;

	private List _msgBuffer;
	private final int _maxSize = 100;
	
	/**
	*Default constructor
	*/
	protected MsgBuffer(){
		_msgBuffer = Collections.synchronizedList(new LinkedList());
	}

	/**
	*Gets an Iterator of all ChatMsg in the list
	*@return An Iterator of all ChatMsg Object in the list
	*/
	public ListIterator getAllMsg()	{
		return _msgBuffer.listIterator();
	}

	/**
	*Gets an Iterator of ChatMsg
	*@param
	*@param
	*@return An Iterator of ChatMsg Object in the list
	*/
	public ListIterator getMessages(ChatMsg lastMsg, int nrOfOldOnes){
		//get the number for the last read msg
		int start = _msgBuffer.indexOf(lastMsg);
		start = start - nrOfOldOnes;
		if (start < 0) start = 0;
		return _msgBuffer.listIterator(start);
	}

	/**
	*Gets the Number of messages in the list
	*@return The number of messages in the list
	*/
	public int getNrOfMsg(){
		return _msgBuffer.size();
	}

	/**
	*Adds the supplied ChatMessage into the list
	*if the list has reashed the max size then the oldest is removed
	*@param message The ChatMsg object you want to add to the list.
	*/
	public void addNewMsg(ChatMsg message){
		_msgBuffer.add(message);
		if (_msgBuffer.size() < _maxSize){
			_msgBuffer.remove(0);
		}
	}
}//end class
