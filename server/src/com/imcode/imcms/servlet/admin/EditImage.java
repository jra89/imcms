package com.imcode.imcms.servlet.admin;

import imcode.server.document.textdocument.ImageDomainObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.imcode.imcms.api.I18nLanguage;
import com.imcode.imcms.flow.DispatchCommand;

public class EditImage extends HttpServlet {

    private static final String REQUEST_ATTRIBUTE__IMAGE = EditImage.class+".image";
    public static final String REQUEST_PARAMETER__RETURN = "return";

    public void doGet(final HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        final String returnPath = request.getParameter(REQUEST_PARAMETER__RETURN);
        final ImageRetrievalCommand imageCommand = new ImageRetrievalCommand();
        DispatchCommand returnCommand = new DispatchCommand() {
            public void dispatch(HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {
                request.setAttribute(REQUEST_ATTRIBUTE__IMAGE, imageCommand.getImage());
                request.getRequestDispatcher(returnPath).forward(request, response);
            }
        };
        ImageEditPage imageEditPage = new ImageEditPage(null, null, null, "", getServletContext(), imageCommand, returnCommand, false);
        imageEditPage.updateFromRequest(request);
        imageEditPage.forward(request, response);
    }

    public static String linkTo(HttpServletRequest request, String returnPath) {
        return request.getContextPath()+"/servlet/EditImage?"+REQUEST_PARAMETER__RETURN+"="+returnPath ;
    }
    
    public static ImageDomainObject getImage(HttpServletRequest request) {
        return (ImageDomainObject) request.getAttribute(REQUEST_ATTRIBUTE__IMAGE);
    }

    private static class ImageRetrievalCommand implements Handler<Map<I18nLanguage, ImageDomainObject>> {

        private Map<I18nLanguage, ImageDomainObject> map;

        public ImageDomainObject getImage() {
            //return images.get(0);
        	return null;
        }

        public void handle(Map<I18nLanguage, ImageDomainObject> map) {
            this.map = map;
        }
    }
}
