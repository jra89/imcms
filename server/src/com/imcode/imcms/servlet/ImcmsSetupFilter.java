package com.imcode.imcms.servlet;

import imcode.server.Imcms;
import imcode.server.ImcmsServices;
import imcode.server.document.DocumentDomainObject;
import imcode.server.user.UserDomainObject;
import imcode.util.Utility;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.NDC;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class ImcmsSetupFilter implements Filter {

    public static final String JSESSIONID_COOKIE_NAME = "JSESSIONID";

    public void doFilter( ServletRequest r, ServletResponse response, FilterChain chain ) throws IOException, ServletException {
        r.setCharacterEncoding(Imcms.DEFAULT_ENCODING);

        HttpServletRequest request = (HttpServletRequest) r;

        HttpSession session = request.getSession();

        ImcmsServices service = Imcms.getServices();
        if ( session.isNew() ) {
            service.incrementSessionCounter();
            setDomainSessionCookie( response, session );
        }

        String workaroundUriEncoding = service.getConfig().getWorkaroundUriEncoding();
        String method = request.getMethod();
        if ( null != workaroundUriEncoding && ( "GET".equals(method) || "HEAD".equals(method) ) ) {
            request = new UriEncodingWorkaroundWrapper(request, workaroundUriEncoding);
        }

        UserDomainObject user = Utility.getLoggedOnUser(request) ;
        if ( null == user ) {
            user = service.verifyUserByIpOrDefault(request.getRemoteAddr()) ;
            assert user.isActive() ;
            Utility.makeUserLoggedIn(request, user);
        }

        Utility.initRequestWithApi(request, user);

        NDC.setMaxDepth( 0 );
        String contextPath = request.getContextPath();
        if ( !"".equals( contextPath ) ) {
            NDC.push( contextPath );
        }
        NDC.push( StringUtils.substringAfterLast( request.getRequestURI(), "/" ) );

        handleDocumentUri(chain, request, response, service);
        NDC.setMaxDepth( 0 );
    }

    private void handleDocumentUri(FilterChain chain, HttpServletRequest httpServletRequest, ServletResponse response,
                                   ImcmsServices service
    ) throws ServletException, IOException {
        String path = httpServletRequest.getRequestURI() ;
        path = StringUtils.substringAfter( path, httpServletRequest.getContextPath() ) ;
        String documentPathPrefix = service.getConfig().getDocumentPathPrefix() ;
        String documentIdString = null ;
        if (StringUtils.isNotBlank( documentPathPrefix ) && path.startsWith( documentPathPrefix )) {
            documentIdString = path.substring( documentPathPrefix.length() );
        }
        HttpSession session = httpServletRequest.getSession();
        boolean isResourcePath = null != session.getServletContext().getResourcePaths(path);
        DocumentDomainObject document = !isResourcePath ? service.getDocumentMapper().getDocumentFromId(documentIdString) : null ;
        if ( null != document ) {
            try {
                GetDoc.output( document.getId(), httpServletRequest, (HttpServletResponse)response );
            } catch( NumberFormatException nfe ) {
                chain.doFilter( httpServletRequest, response );
            }
        } else {
            chain.doFilter( httpServletRequest, response );
        }
    }

    private void setDomainSessionCookie( ServletResponse response, HttpSession session ) {

        String domain = Imcms.getServices().getConfig().getSessionCookieDomain();
        if (StringUtils.isNotBlank(domain)) {
            Cookie cookie = new Cookie( JSESSIONID_COOKIE_NAME, session.getId());
            cookie.setDomain( domain );
            cookie.setPath( "/" );
            ((HttpServletResponse)response).addCookie( cookie );
        }
    }

    public void init( FilterConfig config ) throws ServletException {
    }

    public void destroy() {
    }

}
