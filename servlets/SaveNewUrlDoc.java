
import imcode.server.ApplicationServer;
import imcode.server.IMCServiceInterface;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;

/**
 * Save a new urldocument.
 */
public class SaveNewUrlDoc extends HttpServlet {

    /**
     * doPost()
     */
    public void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
        IMCServiceInterface imcref = ApplicationServer.getIMCServiceInterface();
        String start_url = imcref.getStartUrl();

        imcode.server.user.UserDomainObject user;

        res.setContentType( "text/html" );
        Writer out = res.getWriter();

        // get meta_id
        String meta_id = req.getParameter( "meta_id" );
        // get new_meta_id
        String new_meta_id = req.getParameter( "new_meta_id" );
        // get url_ref
        String url_ref = req.getParameter( "url_ref" );

        // Get the session
        HttpSession session = req.getSession( true );

        // Does the session indicate this user already logged in?
        Object done = session.getAttribute( "logon.isDone" );  // marker object
        user = (imcode.server.user.UserDomainObject)done;

        if ( done == null ) {
            // No logon.isDone means he hasn't logged in.
            // Save the request URL as the true target and redirect to the login page.
            String scheme = req.getScheme();
            String serverName = req.getServerName();
            int p = req.getServerPort();
            String port = ( p == 80 ) ? "" : ":" + p;
            res.sendRedirect( scheme + "://" + serverName + port + start_url );
            return;
        }

        String target = req.getParameter( "target" );
        if ( "_other".equals( target ) ) {
            target = req.getParameter( "frame_name" );
        }

        if ( req.getParameter( "cancel" ) != null ) {
            String output = AdminDoc.adminDoc( Integer.parseInt( meta_id ), Integer.parseInt( meta_id ), user, req, res );
            if ( output != null ) {
                out.write( output );
            }
            return;
        }

        String userLanguage = user.getLangPrefix();
        String sqlStr = "insert into url_docs (meta_id, frame_name,target,url_ref,url_txt,lang_prefix)\n" +
                "values (?,'','',?,''," + userLanguage + ")\n" +
                "update meta set activate = 1, target = ? where meta_id = ?";
        imcref.sqlUpdateQuery( sqlStr, new String[]{new_meta_id, url_ref, target, new_meta_id} );

        String output = AdminDoc.adminDoc( Integer.parseInt( new_meta_id ), Integer.parseInt( new_meta_id ), user, req, res );
        if ( output != null ) {
            out.write( output );
        }
    }
}
