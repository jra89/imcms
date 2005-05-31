package com.imcode.imcms.servlet.billboard;

import imcode.external.diverse.VariableManager;
import imcode.server.Imcms;
import imcode.util.Html;
import imcode.util.Utility;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Vector;

/**
 * Html template in use:
 * BillBoard_Section.htm
 * BillBoard_Section_ext.htm
 * BillBoardSectionSnippet.htm not in use for the moment
 * <p/>
 * Html parstags in use:
 * #SECTION_LIST#
 * #ADMIN_LINK_HTML#
 * <p/>
 * stored procedures in use:
 * B_GetAllSection
 *
 * @author Rickard Larsson REBUILD TO BillBoardForum BY Peter Ístergren
 * @version 1.2 20 Aug 2001
 */

public class BillBoardForum extends BillBoard {//ConfForum

    private final static String ADMIN_LINK_TEMPLATE = "billboard_section_admin_link.htm";//Conf_Forum_Admin_Link.htm

    private final static String HTML_TEMPLATE = "billboard_section.htm";
    private final static String HTML_TEMPLATE_EXT= "billboard_section_ext.htm" ;

	public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

		String htmlFile = HTML_TEMPLATE ;
		if(req.getParameter("advancedView") != null) htmlFile = HTML_TEMPLATE_EXT ;

		// Lets get an user object

        imcode.server.user.UserDomainObject user = Utility.getLoggedOnUser( req ) ;
		if(user == null) return ;

		if ( !isUserAuthorized( req, res, user ) ) {
			return;
		}

		HttpSession session = req.getSession(false) ;
		String aMetaId = (String) session.getAttribute("BillBoard.meta_id") ;

		// Lets get the information from DB
        String[] sqlAnswer = Imcms.getServices().getDatabase().executeArrayProcedure( "B_GetAllSection", new String[] {aMetaId} );
        Vector sectionV = super.convert2Vector(sqlAnswer) ;

		// Lets fill the select box
		String sectionList = Html.createOptionList( sectionV, "" ) ;

		// Lets build the Responsepage
		VariableManager vm = new VariableManager() ;
		vm.addProperty( "SECTION_LIST", sectionList ) ;
		vm.addProperty( "ADMIN_LINK_HTML", ADMIN_LINK_TEMPLATE );

		this.sendHtml(req,res,vm, htmlFile) ;

		return ;
	}

	public void service (HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {

		String action = req.getMethod() ;

        if (action.equals("POST")) {
            this.doPost(req, res);
        } else {
            this.doPost(req, res);
		}
	}

	/**
	Log function, will work for both servletexec and Apache
	**/

	public void log( String msg) {
		super.log("BillBoardForum: " + msg) ;
	}

} // End of class
