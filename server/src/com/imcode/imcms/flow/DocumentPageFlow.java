package com.imcode.imcms.flow;

import imcode.server.document.DocumentDomainObject;
import imcode.server.user.UserDomainObject;
import imcode.util.Utility;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.imcode.imcms.servlet.WebComponent;

public abstract class DocumentPageFlow extends HttpPageFlow {

    protected CreateDocumentPageFlow.SaveDocumentCommand saveDocumentCommand;

    protected DocumentPageFlow( WebComponent.DispatchCommand returnCommand,
                       SaveDocumentCommand saveDocumentCommand ) {
        super( returnCommand );
        this.saveDocumentCommand = saveDocumentCommand;
    }

    public abstract DocumentDomainObject getDocument() ;

    protected void saveDocument( HttpServletRequest request ) throws IOException, ServletException {
        saveDocumentCommand.saveDocument( getDocument(), Utility.getLoggedOnUser( request ) );
    }

    protected void saveDocumentAndReturn( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException {
        saveDocument( request );
        dispatchReturn( request, response );
    }

    public static interface SaveDocumentCommand {
        void saveDocument( DocumentDomainObject document, UserDomainObject user ) ;
    }
}