package imcode.server ;

import java.util.Vector ;
import imcode.server.User ;


/******************************************************************************************
* INTERFACE: IMCPoolInterface                                                             *
*-----------------------------------------------------------------------------------------*
* SYNOPSIS :                                                                              *
* Interface for the Imcode Net Server.                                                    *
*-----------------------------------------------------------------------------------------*
* REVISION HISTORY :                                                                      *
* 30-09-1999 : MI  : parseDoc                                                             *
* 30-09-1999 : MI  : sqlQuery    return String array                                      *
* 30-09-1999 : MI  : sqlUpdate                                                            *
* 30-09-1999 : MI  : sqlQueryStr   return String                                          *
* 30-09-1999 : MI  : parseDoc, uses two vectors                                           *
* 08-10-1999 : MI  : sqlProcedure()     return String array                               *
* 08-10-1999 : MI  : sqlProcedureStr()  return String                                     *
* 08-10-1999 : MI  : sqlUpdateProcedure()                                                 *
* 18-11-1999 : MI  : sqlQuery(String sqlQuery,String catalog) return String array         *
******************************************************************************************/
public interface IMCPoolInterface {

	final static String CVS_REV = "$Revision$" ;
	final static String CVS_DATE = "$Date$" ;

    // Send a sqlquery to the database and return a string array
	String[] sqlQuery(String sqlQuery)
	;

    // Send a sql update query to the database
	void sqlUpdateQuery(String sqlStr) ;


    // Send a procedure to the database and return a string array
	public String[] sqlProcedure(String procedure)
	;

    // Send a procedure to the database and return a string array
    public String[] sqlProcedure(String procedure, String[] params)
	;

	// Send a procedure to the database and return a string
	public String sqlProcedureStr(String procedure)
	;

	// Send a update procedure to the database
	public void sqlUpdateProcedure(String procedure)
	;


    // Send a sqlquery to the database and return a string array and metadata
	String[] sqlQueryExt(String sqlQuery)
	;

	// Send a procedure to the database and return a string array
	public String[] sqlProcedureExt(String procedure)
	;

	// Send a sqlquery to the database and return a Hashtable
	public java.util.Hashtable sqlQueryHash(String sqlQuery)
	;

    // Send a procedure to the database and return a multistring array
	public String[][] sqlProcedureMulti(String procedure)
	;

    // Send a procedure to the database and return a multistring array
	public String[][] sqlProcedureMulti(String procedure, String[] params)
	;

    String sqlProcedureStr( String procedure, String[] params );

    int sqlUpdateProcedure( String procedure, String[] params );

}
