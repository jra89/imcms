package com.imcode.imcms.servlet.admin;

import imcode.server.ApplicationServer;
import imcode.server.IMCServiceInterface;
import imcode.server.WebAppGlobalConstants;
import imcode.server.document.DocumentDomainObject;
import imcode.server.document.DocumentMapper;
import imcode.server.user.UserDomainObject;
import imcode.util.*;
import org.apache.commons.lang.NotImplementedException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.imcode.imcms.flow.DocumentPageFlow;
import com.imcode.imcms.flow.HttpPageFlow;

public class DocumentComposer extends HttpServlet {

    public static final String REQUEST_ATTR_OR_PARAM__DOCUMENT_SESSION_ATTRIBUTE_NAME = "document.sessionAttributeName";
    public static final String REQUEST_ATTR_OR_PARAM__NEW_DOCUMENT_PARENT_INFORMATION_SESSION_ATTRIBUTE_NAME = "newDocumentParentInformation.sessionAttributeName";

    public static final String REQUEST_ATTRIBUTE_OR_PARAMETER__ACTION = "action";

    public static final String PARAMETER__PREVIOUS_ACTION = "previousAction";

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        doPost( request, response );
    }

    public void doPost( HttpServletRequest r, HttpServletResponse response ) throws ServletException, IOException {
        r.setCharacterEncoding( WebAppGlobalConstants.DEFAULT_ENCODING_WINDOWS_1252 );
        MultipartHttpServletRequest request = new MultipartHttpServletRequest( r );

        IMCServiceInterface service = ApplicationServer.getIMCServiceInterface();
        DocumentMapper documentMapper = service.getDocumentMapper();
        UserDomainObject user = Utility.getLoggedOnUser( request );

        DocumentPageFlow pageFlow = getDocumentPageFlowFromRequest( request );
        DocumentDomainObject document = pageFlow.getDocument();
        if ( null != document && documentMapper.userHasMoreThanReadPermissionOnDocument( user, document ) ) {
            pageFlow.dispatch( request, response );

            if ( !response.isCommitted() ) {
                throw new NotImplementedException( pageFlow.getClass() );
            }
        }
    }

    public static DocumentPageFlow getDocumentPageFlowFromRequest( HttpServletRequest request ) {
        return (DocumentPageFlow)HttpSessionUtils.getSessionAttributeWithNameInRequest( request, HttpPageFlow.REQUEST_ATTRIBUTE_OR_PARAMETER__FLOW );
    }

    public static void forwardToDocumentComposerWithFlow( HttpServletRequest req, HttpServletResponse res,
                                                             HttpPageFlow httpPageFlow ) throws ServletException, IOException {
        HttpSessionUtils.setSessionAttributeAndSetNameInRequestAttribute( httpPageFlow, req, HttpPageFlow.REQUEST_ATTRIBUTE_OR_PARAMETER__FLOW );
        req.getRequestDispatcher( "DocumentComposer" ).forward( req, res );
    }
}