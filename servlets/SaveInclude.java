import javax.servlet.* ;
import javax.servlet.http.* ;

import imcode.server.IMCConstants ;
import imcode.server.User ;

import imcode.util.Check ;
import imcode.util.Utility ;
import imcode.util.IMCServiceRMI ;

import java.io.IOException ;
import java.util.Vector ;

public class SaveInclude extends HttpServlet {

    public void init (ServletConfig config) throws ServletException {
	super.init( config ) ;
    }

    public void doPost (HttpServletRequest req, HttpServletResponse res) throws ServletException, java.io.IOException {
	String host 			= req.getHeader("Host") ;
	String imcserver 		= imcode.util.Utility.getDomainPref("adminserver",host) ;
	String start_url        	= imcode.util.Utility.getDomainPref( "start_url",host ) ;

	res.setContentType("text/html") ;

	ServletOutputStream out = res.getOutputStream() ;

	imcode.server.User user ;

	String meta_id_str = req.getParameter("meta_id") ;
	int meta_id = Integer.parseInt(meta_id_str) ;

	// Check if the user logged on
	if ( (user = Check.userLoggedOn(req,res,start_url )) == null ) {
	    return ;
	}

	// Check if user has permission to edit includes for this document
	if ( !IMCServiceRMI.checkDocAdminRights(imcserver,meta_id,user,imcode.server.IMCConstants.PERM_DT_TEXT_EDIT_INCLUDES ) ) {	// Checking to see if user may edit this
	    sendPermissionDenied(imcserver,out,meta_id,user) ;
	    return ;
	}

	String included_meta_id = req.getParameter("include_meta_id") ;
	
	String include_id = req.getParameter("include_id") ;
	if (included_meta_id != null && include_id != null) {
	    if ("".equals(included_meta_id.trim())) {
		IMCServiceRMI.sqlUpdateProcedure(imcserver,"DeleteInclude "+meta_id_str+","+include_id) ; 
	    } else {
		try {
		    int included_meta_id_int = Integer.parseInt(included_meta_id) ;
		    
		    // Make sure the user has permission to share the included document
		    if (IMCServiceRMI.checkUserDocSharePermission(imcserver,user,included_meta_id_int)) {
			IMCServiceRMI.sqlUpdateProcedure(imcserver,"SetInclude "+meta_id_str+","+include_id+","+included_meta_id) ; 
		    } else {
			sendPermissionDenied(imcserver,out,meta_id,user) ;
			return ;
		    }
		} catch (NumberFormatException ignored) {
		    sendBadId(imcserver,out,meta_id,user) ;
		    return ;
		}
	    }
	}
	
	byte[] tempbytes = AdminDoc.adminDoc(meta_id,meta_id,host,user,req,res) ;
	if ( tempbytes != null ) {
	    out.write(tempbytes) ;
	}
	return ;
    }
    
    protected void sendPermissionDenied(String imcserver, ServletOutputStream out, int meta_id, User user) throws IOException {
	Vector vec = new Vector(2) ;
	vec.add("#meta_id#") ;
	vec.add(String.valueOf(meta_id)) ;
	String htmlStr = IMCServiceRMI.parseDoc(imcserver,vec,"include_permission_denied.html",user.getLangPrefix()) ;
	out.print(htmlStr) ;
    }

    protected void sendBadId(String imcserver, ServletOutputStream out, int meta_id, User user) throws IOException {
	Vector vec = new Vector(2) ;
	vec.add("#meta_id#") ;
	vec.add(String.valueOf(meta_id)) ;
	String htmlStr = IMCServiceRMI.parseDoc(imcserver,vec,"include_bad_id.html",user.getLangPrefix()) ;
	out.print(htmlStr) ;
    }

}
