package com.imcode.imcms.servlet.tags;

import imcode.server.Imcms;
import imcode.server.document.textdocument.TextDocumentDomainObject;
import imcode.server.parser.ParserParameters;
import imcode.server.parser.TextDocumentParser;
import imcode.server.user.UserDomainObject;
import imcode.util.Html;
import imcode.util.Utility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class AdminTag extends TagSupport {
    public final static String PARAMETER_SELECT__DOCUMENT_TO_SHOW = "doc_to_show";
    
    public int doStartTag() throws JspException {
        try {
            UserDomainObject user = Utility.getLoggedOnUser((HttpServletRequest) pageContext.getRequest());
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            ParserParameters parserParameters = ParserParameters.fromRequest(request);
            if ( parserParameters.getFlags() >= 0 && parserParameters.isAdminButtonsVisible() ) {
                TextDocumentDomainObject document = (TextDocumentDomainObject) parserParameters.getDocumentRequest().getDocument();
                HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
                String adminButtons = Html.getAdminButtons(user, document, request, response);
                if ( null != document && ( user.canEdit( document ) || user.isUserAdminAndCanEditAtLeastOneRole() || user.canAccessAdminPages() ) ) {
                    pageContext.getOut().print("<div style='background-color: lightblue; border: 1px solid navy; padding: 5px; margin: 0px;'>");
                    pageContext.getOut().print(adminButtons);
                    pageContext.getOut().print(TextDocumentParser.createChangeTemplateUi(parserParameters.isTemplateMode(), user, document, Imcms.getServices()));
                    pageContext.getOut().print("</div>");
                }    
            }
        } catch ( Exception e ) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }
}
