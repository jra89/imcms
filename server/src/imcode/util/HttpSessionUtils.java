package imcode.util;

import org.apache.commons.lang.ClassUtils;

import javax.servlet.http.HttpServletRequest;

public class HttpSessionUtils {

    private HttpSessionUtils() {
    }

    public static void setSessionAttributeAndSetNameInRequestAttribute( final Object objectToAddToSession,
                                                                              HttpServletRequest request,
                                                                              final String sessionAttributeNameRequestAttributeName ) {
        final String sessionAttributeName = ClassUtils.getShortClassName( objectToAddToSession.getClass() ) + "." + System.currentTimeMillis();
        request.getSession().setAttribute( sessionAttributeName, objectToAddToSession );
        request.setAttribute( sessionAttributeNameRequestAttributeName, sessionAttributeName );
    }

    public static Object getSessionAttributeWithNameInRequest( HttpServletRequest request,
                                                               String requestAttributeOrParameterName ) {
        String sessionAttributeName = getSessionAttributeNameFromRequest( request, requestAttributeOrParameterName );
        return request.getSession().getAttribute( sessionAttributeName );
    }

    public static void removeSessionAttributeWithNameInRequest( HttpServletRequest request,
                                                                 String requestAttributeOrParameterName ) {
        String sessionAttributeName = getSessionAttributeNameFromRequest( request, requestAttributeOrParameterName );
        request.getSession().removeAttribute( sessionAttributeName );
    }

    public static String getSessionAttributeNameFromRequest( HttpServletRequest request,
                                                             String requestAttributeOrParameterName ) {
        String sessionAttributeName = (String)request.getAttribute( requestAttributeOrParameterName );
        if ( null == sessionAttributeName ) {
            sessionAttributeName = request.getParameter( requestAttributeOrParameterName );
        }
        return sessionAttributeName;
    }
}
