package com.imcode.imcms.servlet.conference;

import imcode.external.diverse.MetaInfo;
import imcode.external.diverse.VariableManager;
import imcode.server.Imcms;
import imcode.server.ImcmsServices;
import imcode.server.user.UserDomainObject;
import imcode.util.Utility;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

public class ConfCreator extends Conference {

    private final static String HTML_TEMPLATE = "conf_creator.htm";

    /**
     * The POST method creates the html page when this side has been
     * redirected from somewhere else.
     */

    public void doPost( HttpServletRequest req, HttpServletResponse res )
            throws ServletException, IOException {

        // Lets get the standard parameters and validate them
        Properties params = MetaInfo.createPropertiesFromMetaInfoParameters( super.getConferenceSessionParameters( req ) );

        // Lets get the new conference parameters
        Properties confParams = this.getNewConfParameters( req );

        UserDomainObject user = Utility.getLoggedOnUser( req );
        if ( !isUserAuthorized( req, res, user ) ) {
            return;
        }

        String action = req.getParameter( "action" );
        if ( action == null ) {
            String header = "ConfCreator servlet. ";
            ConfError err = new ConfError( req, res, header, 3, user );
            log( header + err.getErrorMsg() );
            return;
        }

        // Lets get serverinformation
        ImcmsServices imcref = Imcms.getServices();

        // ********* NEW ********
        if ( action.equalsIgnoreCase( "ADD_CONF" ) ) {
            log( "OK, nu skapar vi konferens" );

            // Added 000608
            // Ok, Since the conference db can be used from different servers
            // we have to check when we add a new conference that such an meta_id
            // doesnt already exists.
            String metaId = params.getProperty( "META_ID" );
            String foundMetaId = imcref.getDatabase().executeStringProcedure( "A_FindMetaId", new String[] {metaId} );
            if ( !foundMetaId.equals( "1" ) ) {
                String header = "ConfCreator servlet. ";
                ConfError err = new ConfError( req, res, header, 90, user );
                log( header + err.getErrorMsg() );
                return;
            }

            // Lets add a new Conference to DB
            // AddNewConf @meta_id int, @confName varchar(255)
            String confName = confParams.getProperty( "CONF_NAME" );
            // String sortType = "1" ;	// Default value, unused so far
            imcref.getDatabase().executeUpdateProcedure( "A_AddNewConf", new String[] {metaId,
                                                                                            confName} );

            // Lets add a new forum to the conference
            final String archiveMode = "A";
            final String archiveTime = "30";
            imcref.getDatabase().executeUpdateProcedure( "A_AddNewForum", new String[] {metaId,
                                                                                            confParams.getProperty( "FORUM_NAME" ),
                                                                                            archiveMode, archiveTime} );

            // Lets get the administrators user_id
            String user_id = "" + user.getId();

            // Lets add this user into the conference if hes not exists there before
            addUserToOneConference(user, metaId+"", imcref);

            // Ok, Were done adding the conference, Lets go back to the Manager
            String loginPage = "ConfLogin?login_type=login";
            res.sendRedirect( loginPage );
            return;
        }

    } // End POST

    /**
     * The GET method creates the html page when this side has been
     * redirected from somewhere else.
     */

    public void doGet( HttpServletRequest req, HttpServletResponse res )
            throws ServletException, IOException {

        UserDomainObject user = Utility.getLoggedOnUser( req );
        if ( !isUserAuthorized( req, res, user ) ) {
            return;
        }

        String action = req.getParameter( "action" );
        if ( action == null ) {
            String header = "ConfCreator servlet. ";
            ConfError err = new ConfError( req, res, header, 3, user );
            log( header + err.getErrorMsg() );
            return;
        }

        // ********* NEW ********
        if ( action.equalsIgnoreCase( "NEW" ) ) {
            // Lets build the Responsepage to the loginpage
            VariableManager vm = new VariableManager();
            vm.addProperty( "SERVLET_URL", "" );
            sendHtml( req, res, vm, HTML_TEMPLATE );
            return;
        }
    } // End doGet

    /**
     * Collects the parameters from the request object
     */

    private Properties getNewConfParameters( HttpServletRequest req ) {

        Properties confP = new Properties();
        String conf_name = ( req.getParameter( "conference_name" ) == null ) ? "" : ( req.getParameter( "conference_name" ) );
        String forum_name = ( req.getParameter( "forum_name" ) == null ) ? "" : ( req.getParameter( "forum_name" ) );

        confP.setProperty( "CONF_NAME", conf_name.trim() );
        confP.setProperty( "FORUM_NAME", forum_name.trim() );
        return confP;
    }

} // End class
