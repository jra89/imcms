package com.imcode.imcms.servlet.tags;

import imcode.server.Imcms;
import imcode.server.parser.ParserParameters;
import imcode.server.parser.TagParser;
import imcode.server.parser.TextDocumentParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Properties;

public class ImageTag extends TagSupport {

    private Properties attributes = new Properties();

    public int doStartTag() throws JspException {
        try {
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            ParserParameters parserParameters = ParserParameters.fromRequest(request);
            TagParser tagParser = new TagParser(new TextDocumentParser(Imcms.getServices()), parserParameters, 0);
            String imageTag = tagParser.tagImage(attributes);
            pageContext.getOut().print(imageTag);
        } catch ( Exception e ) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    public void setLabel(String label) {
        attributes.setProperty("label", label) ;
    }

    public void setMode(String mode) {
        attributes.setProperty("mode", mode) ;
    }

    public void setNo(int no) {
        attributes.setProperty("no", ""+no) ;
    }

    public void setStyle(String style) {
        attributes.setProperty("style", style) ;
    }

    public void setStyleClass(String styleClass) {
        attributes.setProperty("class", styleClass) ;
    }

    public void setUsemap(String usemap) {
        attributes.setProperty("usemap", usemap) ;
    }
    
    public void setId(String id) {
        attributes.setProperty("id", id) ;
    }
}
