///////////////////////////////////////////////////////////
//
//  ChatGroup.java
//  Implementation of the Class ChatGroup
//  Generated by Enterprise Architect
//  Created on:      2001-07-11
//  Original author: 
//  
///////////////////////////////////////////////////////////
//  Modification history:
//  
//
///////////////////////////////////////////////////////////

package imcode.external.chat;

import java.util.*;

public class ChatGroup
{

	private int _groupId;
	private String _name;
	private MsgBuffer _msgPool;
	private List _groupMembers;
	private Counter _msgNrCounter;
	private Counter _membersCounter;
	


	/**
	*Default constructor
	*@param groupNumber The groupNumber that this ChatGroup will have
	*/
	public ChatGroup(int groupNumber,String groupName)
	{
		_groupId = groupNumber;
		_name = groupName;
		_groupMembers = Collections.synchronizedList(new LinkedList());
		_msgPool = new MsgBuffer();
		_msgNrCounter = new Counter();
		_membersCounter = new Counter();
	
	}
	
	/**
	*Gets the id for this group
	*@return The idnumber for this group
	*/
	
	public int getGroupId()
	{
		return _groupId;
	}
	
	/**
	*Sets the name of the ChatGroup
	*@param chatGroupName The name of the ChatGroup
	*/
	public void setChatGroupName(String chatGroupName)
	{
		_name = chatGroupName;
	}
	
	/**
	*Gets the name of this ChatGroup
	*return The name of this ChatGroup or an empty string if the name hasn't
	*been set
	*/
	public String getChatGroupName()
	{
	
		return (_name == null) ? "" : _name;
	}

	/**
	*Gets the currently number of ChatMembers in this ChatGroup. 
	*@return The currently number of ChatMembers in this ChatGroup.
	*/
	public int getNrOfGroupMembers()
	{
		return _groupMembers.size();

	}
	
	/**
	*Gets an Iterator of all GroupMembers currently in this ChatGroup
	*@return An Iterator of all GroupMembers currently in this ChatGroup
	*/
	public Iterator getAllGroupMembers()
	{
		return _groupMembers.iterator();

	}

	/**
	*Adds a ChatMember to this ChatGroup
	*@param member The ChatMember to add into the ChatGroup
	*/
	public void addNewGroupMember(ChatMember member)
	{
		_groupMembers.add(member);
		member.setCurrentGroup(this);
		
	}

	/**
	*Removes a ChatMember from this ChatGroup
	*@param member The ChatMember you want to remove
	*If not the  ChatMember exists in this group no action is taken.
	*/
	public void removeGroupMember(ChatMember member)
	{
		_groupMembers.remove(member);

	}
	
	/**
	*Adds a ChatMsg into this ChatGroup
	*@param msg The ChatMsg you want to add
	*/
	protected void addNewMsg(ChatMsg msg)
	{
		_msgNrCounter.increment();
		msg.setIdNumber(_msgNrCounter.getValue());
		_msgPool.addNewMsg(msg);
	}
	
	
	/**
	*Gets an Iterator fore my unreade messages and a number of oldones
	*@param lastMsg The referens fore last msg-object that the user has read
	*@param nrOfOldOnes The number of read messages the user want to reread
	*@return An ListIterator of the ChatMsg:s
	*/
	protected ListIterator getMessages(ChatMsg lastMsg, int nrOfOldOnes)
	{
		return _msgPool.getMessages(lastMsg, nrOfOldOnes);
		
	}
	
	public String toString()
	{
		return "Group: " + _name + " GroupId: " + _groupId;
	}


}
