package com.imcode.imcms.servlet.tags;

import imcode.server.Imcms;
import imcode.server.document.DocumentDomainObject;
import imcode.server.document.textdocument.MenuItemDomainObject;
import imcode.server.parser.MenuParser;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.imcode.imcms.api.DocumentVersionSelector;

public class MenuItemLinkTag extends TagSupport {

    public int doStartTag() throws JspException {
        MenuTag menuTag = (MenuTag) findAncestorWithClass(this, MenuTag.class) ;
        if (menuTag == null) {
            throw new JspTagException("menuitem must be enclosed in a menuloop or menu.") ;
        }
        MenuItemDomainObject menuItem = menuTag.getMenuItem();
        if (null == menuItem) {
            return SKIP_BODY;
        }
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        DocumentDomainObject document = menuItem.getDocument();
        String pathToDocument = MenuParser.getPathToDocument(request, document, menuTag.getTemplate());
        String selector = Imcms.getUser().getDocumentShowSettings().getVersionSelector() == DocumentVersionSelector.WORKING_SELECTOR
        	? "w"
        	: "p";
        try {
        	if (pathToDocument.contains("?")) {
        		 pathToDocument += "&" + selector;
        	} else {
        		pathToDocument += "?" + selector;
        	}
            pageContext.getOut().print("<a href=\"" + pathToDocument+"\" target=\""+ document.getTarget()+"\">");
        } catch ( IOException e ) {
            throw new JspException(e);
        }
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().print("</a>");
        } catch ( IOException e ) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

}
