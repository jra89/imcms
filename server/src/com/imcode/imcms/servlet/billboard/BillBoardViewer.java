package com.imcode.imcms.servlet.billboard;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import imcode.external.diverse.*;
import imcode.server.ImcmsServices;
import imcode.server.Imcms;
import imcode.server.user.UserDomainObject;
import imcode.util.Utility;
import com.imcode.imcms.servlet.billboard.BillBoard;

/**
 * Html template in use:
 * BillBoard_set.htm
 * <p/>
 * Html parstags in use:
 * #BILLBOARD_SECTION#
 * #BILLBOARD_DISC_VIEW#
 * stored procedures in use:
 * -
 *
 * @author Rickard Larsson, Jerker Drottenmyr REBUILD TO BillBoardViewer BY Peter �stergren
 * @version 1.2 20 Aug 2001
 */

public class BillBoardViewer extends BillBoard {//ConfViewer

    private final static String HTML_TEMPLATE = "billboard_set.htm";         // the relative path from web root to where the servlets are

    public void doGet( HttpServletRequest req, HttpServletResponse res )
            throws ServletException, IOException {

        // Lets get the standard SESSION parameters and validate them
        Properties params = MetaInfo.createPropertiesFromMetaInfoParameters( super.getBillBoardSessionParameters( req ) );

        // Lets get an user object

        imcode.server.user.UserDomainObject user = Utility.getLoggedOnUser( req );
        if ( user == null ) {
            log( "user = null so return" );
            return;
        }

        if ( !isUserAuthorized( req, res, user ) ) {
            log( "user not Authorized so return" );
            return;
        }

        // Lets get all parameters in a string which we'll send to every servlet in the frameset
        String paramStr = MetaInfo.passMeta( params );

        // Lets build the Responsepage
        VariableManager vm = new VariableManager();
        vm.addProperty( "BILLBOARD_SECTION", "BillBoardForum?" + paramStr );
        vm.addProperty( "BILLBOARD_DISC_VIEW", "BillBoardDiscView?" + paramStr );
        this.sendHtml( req, res, vm, HTML_TEMPLATE );
        //log("Nu �r BillBoardViewer klar") ;
        return;
    }

} // End of class
