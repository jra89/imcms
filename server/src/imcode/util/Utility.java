package imcode.util;

import com.imcode.imcms.servlet.admin.AdminDoc;
import imcode.server.ApplicationServer;
import imcode.server.WebAppGlobalConstants;
import imcode.server.document.DocumentDomainObject;
import imcode.server.user.UserDomainObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;

public class Utility {

    private static final String PREFERENCES_FILENAME = "host.properties";

    private Utility() {

    }

    /**
     * Takes a path-string and returns a file. The path is prepended with the webapp dir if the path is relative.
     */
    public static File getAbsolutePathFromString( String pathString ) {
        File path = new File( pathString );
        if ( !path.isAbsolute() ) {
            path = new File( imcode.server.WebAppGlobalConstants.getInstance().getAbsoluteWebAppPath(), pathString );
        }
        return path;
    }

    /**
     * Fetches a preference from the config file for a domain,
     * as a File representing an absolute path, with the webapp dir prepended if the path is relative.
     *
     * @param pref The name of the preference to fetch.
     */
    public static File getDomainPrefPath( String pref ) throws IOException {
        return getAbsolutePathFromString( getDomainPref( pref ) );
    }

    /**
     * Fetches a preference from the config file for a domain.
     *
     * @param pref The name of the preference to fetch.
     */
    public static String getDomainPref( String pref ) throws IOException {
        return Prefs.get( pref, PREFERENCES_FILENAME );
    }

    /**
     * Transforms a long containing an ip into a String.
     */
    public static String ipLongToString( long ip ) {
        return ( ( ip >>> 24 ) & 255 ) + "." + ( ( ip >>> 16 ) & 255 ) + "." + ( ( ip >>> 8 ) & 255 ) + "."
               + ( ip & 255 );
    }

    /**
     * Transforms a String containing an ip into a long.
     */
    public static long ipStringToLong( String ip ) {
        long ipInt = 0;
        StringTokenizer ipTok = new StringTokenizer( ip, "." );
        for ( int exp = 3; ipTok.hasMoreTokens(); --exp ) {
            int ipNum = Integer.parseInt( ipTok.nextToken() );
            ipInt += ( ipNum * Math.pow( 256, exp ) );
        }
        return ipInt;
    }

    /**
     * Make a HttpServletResponse non-cacheable
     */
    public static void setNoCache( HttpServletResponse res ) {
        res.setHeader( "Cache-Control", "no-cache; must-revalidate;" );
        res.setHeader( "Pragma", "no-cache;" );
    }

    public static UserDomainObject getLoggedOnUser( HttpServletRequest req ) {
        HttpSession session = req.getSession( true );
        UserDomainObject user = (UserDomainObject)session.getAttribute( WebAppGlobalConstants.LOGGED_IN_USER );
        return user;
    }

    public static boolean toBoolean( String property ) {
        if ( null == property ) {
            return false;
        }
        property = property.toLowerCase();
        if ( "1".equals( property ) || "y".equals( property ) || "yes".equals( property ) || "true".equals( property ) ) {
            return true;
        }
        return false;
    }

    public static int compareDatesWithNullFirst( Date date1, Date date2 ) {
        if ( null == date1 && null == date2 ) {
            return 0;
        } else if ( null == date1 ) {
            return -1;
        } else if ( null == date2 ) {
            return +1;
        } else {
            return date1.compareTo( date2 );
        }
    }

    public static void setDefaultHtmlContentType( HttpServletResponse res ) {
        res.setContentType( "text/html; charset=" + WebAppGlobalConstants.DEFAULT_ENCODING_WINDOWS_1252 );
    }

    public static void redirectToStartDocument( HttpServletRequest req, HttpServletResponse res ) throws IOException {

        res.sendRedirect( req.getContextPath() + "/servlet/StartDoc" );

    }

    public static String getLinkedStatusIconTemplate( DocumentDomainObject document, UserDomainObject user ) {
        return "<a href=\"AdminDoc?meta_id=" + document.getId() + "&" + AdminDoc.PARAMETER__DISPATCH_FLAGS + "=1\">" +
               ApplicationServer.getIMCServiceInterface().getDocumentMapper().getStatusIconTemplate( document, user ) +
               "</a>";

    }

}
