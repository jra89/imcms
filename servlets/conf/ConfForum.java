import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import imcode.external.diverse.* ;
import java.rmi.* ;
import java.rmi.registry.* ;
import imcode.util.* ;

public class ConfForum extends Conference {

	private final static String ADMIN_LINK_TEMPLATE = "Conf_Forum_Admin_Link.htm";

	String HTML_TEMPLATE ;
	String HTML_TEMPLATE_EXT ;
	String A_HREF_HTML ;   // The code snippet where the aHref list with all discussions
	// will be placed.

	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {

		//log( "Forum" );
		// Lets validate the session, e.g has the user logged in to Janus?
		if (super.checkSession(req,res) == false)	return ;

		// Lets get the standard parameters and validate them
		Properties params = super.getSessionParameters(req) ;
		if (super.checkParameters(req, res, params) == false) {
			/*
			String header = "ConfForum servlet. " ;
			String msg = params.toString() ;
			ConfError err = new ConfError(req,res,header,1) ;
			*/
			return;
		}


		String htmlFile = HTML_TEMPLATE ;
		if(req.getParameter("advancedView") != null) htmlFile = HTML_TEMPLATE_EXT ;

		// 	log("Parametrar var: " + params.toString()) ;

		// Lets get an user object
		imcode.server.User user = super.getUserObj(req,res) ;
		if(user == null) return ;

		if ( !isUserAuthorized( req, res, user ) ) {
			return;
		}

		RmiConf rmi = new RmiConf(user) ;
		HttpSession session = req.getSession(false) ;
		String aMetaId = (String) session.getValue("Conference.meta_id") ;
		String aForumId = (String) session.getValue("Conference.forum_id") ;
		String discIndex = params.getProperty("DISC_INDEX") ;

		// Lets get the url to the servlets directory
		String servletHome = MetaInfo.getServletPath(req) ;

		// Lets get serverinformation
		String host = req.getHeader("Host") ;
		String imcServer = Utility.getDomainPref("userserver",host) ;
		String confPoolServer = Utility.getDomainPref("conference_server",host) ;

		// Lets get the information from DB
		String sqlStoredProc = "GetAllForum " + aMetaId ;
		String sqlAnswer[] = rmi.execSqlProcedure(confPoolServer, sqlStoredProc ) ;
		Vector forumV = super.convert2Vector(sqlAnswer) ;

		// Lets fill the select box
		String forumList = Html.createHtmlCode("ID_OPTION", "", forumV ) ;

		// Lets build the Responsepage
		VariableManager vm = new VariableManager() ;
		vm.addProperty( "FORUM_LIST", forumList ) ;
		vm.addProperty( "ADMIN_LINK_HTML", this.ADMIN_LINK_TEMPLATE );

		this.sendHtml(req,res,vm, htmlFile) ;
		//log("ConfForum OK") ;
		return ;
	}

	public void service (HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {

		String action = req.getMethod() ;
		//	log("Action:" + action) ;
		if(action.equals("POST")) {
			this.doPost(req,res) ;
		}
		else {
			this.doPost(req,res) ;
		}
	}



	/**
	Detects paths and filenames.
	*/

		public void init(ServletConfig config) throws ServletException {
		super.init(config);
		HTML_TEMPLATE = "Conf_Forum.htm" ;
		HTML_TEMPLATE_EXT = "Conf_Forum_ext.htm" ;
		A_HREF_HTML = "ConfForumSnippet.htm" ;
	} // End init

	/**
	Log function, will work for both servletexec and Apache
	**/

	public void log( String str) {
		super.log(str) ;
		System.out.println("ConfForum: " + str ) ;
	}
} // End of class
