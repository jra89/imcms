import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.rmi.* ;

import imcode.server.* ;
import imcode.util.* ;
/**
   Verify a user.
*/
public class VerifyUser extends HttpServlet {
    private final static String CVS_REV = "$Revision$" ;
    private final static String CVS_DATE = "$Date$" ;

    /**
       init()
    */
    public void init( ServletConfig config ) throws ServletException {
		super.init( config ) ;
    }
	
	/**
		doGet()
	*/

	public void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
		doPost(req, res);
		return;
	} 
	/** end of doGet() */
	

    /**
       doPost()
    */
    public void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
		String host 				= req.getHeader("Host") ;
		IMCServiceInterface imcref = IMCServiceRMI.getIMCServiceInterfaceByHost(host) ;
		String start_url        	= imcref.getStartUrl() ;
		String servlet_url       	= Utility.getDomainPref( "servlet_url",host ) ;
		String admin_url       		= Utility.getDomainPref( "admin_url",host ) ;
		String access_denied_url   	= Utility.getDomainPref( "access_denied_url",host ) ;

		imcode.server.User user ;
		res.setContentType( "text/html" );
		PrintWriter out = res.getWriter( );
		String test = "" ; 
		String type = "";
		String version = "" ;
		String plattform = "" ;

		String scheme = req.getScheme( );
		String serverName = req.getServerName( );
		int p = req.getServerPort( );
		String port = (p == 80 || p == 443) ? "" : ":" + p;

		String name = req.getParameter( "name" );
		String passwd = req.getParameter( "passwd" );
		String value = req.getHeader( "User-Agent" ) ; 
		
		
		// Check the name and password for validity
		user = imcref.verifyUser( name, passwd );
		
		// Get session 
		HttpSession session = req.getSession( true );

		// if we don't have got any user from IMCService lets check out next url for redirect 
		if( user == null ) {
		
			String nexturl = access_denied_url; // default
			
		    // lets set session next_meta if we have got any from request, we will use it later when 
			// login is successfull 
			if ( req.getParameter("next_meta") != null ){
				session.setAttribute( "next_meta", req.getParameter("next_meta") );
			}
			
			// or lets set session next_url if we have got any from request, we will use it later when 
			// login is successfull 
			else if ( req.getParameter("next_url") != null ){
				session.setAttribute( "next_url", req.getParameter("next_url") );
			}
			
			// lets get different access_denied url instead of the default url
			if ( req.getParameter("access_denied_url") != null ){
				nexturl = req.getParameter("access_denied_url");
			}
			res.sendRedirect(nexturl) ;
		    return ;
			
					
		} else { // we have a valid user
		
		    // Valid login.  Make a note in the session object.
		    session.setAttribute( "logon.isDone", user );  // just a marker object
		    session.setAttribute("browser_id",value) ;
			
			StartDoc.incrementSessionCounter(imcref,user,req) ;

		   	user.setLoginType("verify") ;
			
			// Lets now find out nexturl to redirect the user
			String nexturl = "StartDoc";  // default value

			// if user have pushed button "�ndra" from login page 
			if (req.getParameter("�ndra")!=null){
			
				//if next_url was passed
				if ( req.getParameter("next_url") !=null ){
					nexturl = req.getParameter("next_url") ;
				}
				//or if next_meta was passed
				else if ( req.getParameter("next_meta") !=null ){ 
					nexturl = "GetDoc?meta_id=" + req.getParameter("next_meta") ;
				}
				
				session.setAttribute("userToChange", ""+user.getUserId() );
				session.setAttribute("next_url", nexturl);
					
				res.sendRedirect("AdminUserProps?CHANGE_USER=true");
				return;
		
			
			} else {
	
				// lets check if we have got a next_meta by session 
				if ( session.getAttribute( "next_meta" ) !=null ){
					nexturl = "GetDoc?meta_id=" +(String)session.getAttribute("next_meta");
					session.removeAttribute("next_meta") ;
				}	
				
				// or if we have got next_url by session 	
				else if ( session.getAttribute( "next_url" ) !=null ){
					nexturl = (String)session.getAttribute("next_url");
					session.removeAttribute("next_url") ;
				}
				// or if we have got next_url from request object
				else if ( req.getParameter("next_url") !=null ){
					nexturl = req.getParameter("next_url") ;
				}
				//or if we have got next_meta from request object
				else if ( req.getParameter("next_meta") !=null ){ 
					nexturl = "GetDoc?meta_id=" + req.getParameter("next_meta") ;
				} 
				
				// or try redirecting the client to the page he first tried to access
				else if ( session.getAttribute( "login.target" ) !=null ){
					nexturl = (String) session.getAttribute("login.target");
		    		session.removeAttribute("login.target") ;
				
				
				// Couldn't redirect to the target.  Redirect to the site's home page.
			    } else {
					nexturl = scheme + "://" + serverName + port + servlet_url + "StartDoc";
				}
		    					
				res.sendRedirect(nexturl);
				return;
				
			} // end else
		} // end else		
		
    } // end doPost()                 
} // end class

