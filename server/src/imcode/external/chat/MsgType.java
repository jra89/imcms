
package imcode.external.chat;

public class MsgType{
	private final static String CVS_REV = "$Revision$" ;
	private final static String CVS_DATE = "$Date$" ;

	private int _msgIdNr;
	private String _name;

	/**
	*Default constructor
	*@param msgIdNr The id Number that this msgType will have
	*@param name The name of this msgType
	*/
	public MsgType(int msgIdNr, String name){
		_msgIdNr = msgIdNr;
		_name = name;
	}

	/**
	*Gets the id number
	*@return The idnumber of this msgtype
	*/
	public int getIdNr(){
		return _msgIdNr;
	}
	
	/**
	*Gets the name
	*@return The name of this msgtype
	*/
	public String getName()	{
		return _name;
	}
}//end class
