
import imcode.server.IMCServiceInterface;
import imcode.util.Check;
import imcode.util.IMCServiceRMI;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Save a framesetdocument.
 * Shows a change_meta.html which calls SaveMeta
 */
public class SaveFrameset extends HttpServlet {

    /**
     * doPost()
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        IMCServiceInterface imcref = IMCServiceRMI.getIMCServiceInterface(req);
        String start_url = imcref.getStartUrl();

        imcode.server.User user;
        int meta_id;

        res.setContentType("text/html");
        Writer out = res.getWriter();

        // get meta_id
        meta_id = Integer.parseInt(req.getParameter("meta_id"));

        String frame_set = req.getParameter("frame_set");

        // Check if user logged on
        if ((user = Check.userLoggedOn(req, res, start_url)) == null) {
            return;
        }

        // Check if user has write rights
        if (!imcref.checkDocAdminRights(meta_id, user, 65536)) {	// Checking to see if user may edit this
            String output = AdminDoc.adminDoc(meta_id, meta_id, user, req, res);
            if (output != null) {
                out.write(output);
            }
            return;
        }

        if (req.getParameter("ok") != null) {	//User pressed ok on form in change_frameset_doc.html
            imcref.saveFrameset(meta_id, user, frame_set);
            imcref.touchDocument(meta_id);

            String output = AdminDoc.adminDoc(meta_id, meta_id, user, req, res);
            if (output != null) {
                out.write(output);
            }
            return;
        }
    }
}

