package com.imcode.imcms.servlet;

import com.imcode.imcms.flow.Page;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SearchDocuments extends HttpServlet {

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        if (null != Page.fromRequest( request )) {
            request.getRequestDispatcher( "PageDispatcher" ).forward( request, response );
        } else {
            SearchDocumentsPage searchDocumentsPage = new SearchDocumentsPage() ;
            DocumentFinder documentFinder = new DocumentFinder(searchDocumentsPage);
            searchDocumentsPage.updateFromRequest( request );
            documentFinder.forward(request, response);
        }
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        doGet( request, response );
    }

} // End class
