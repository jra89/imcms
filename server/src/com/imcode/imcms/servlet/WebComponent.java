package com.imcode.imcms.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebComponent {

    private DispatchCommand cancelCommand;

    public void cancel( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException {
        cancelCommand.dispatch(request,response) ;
    }

    public boolean isCancelable() {
        return null != cancelCommand;
    }

    public void setCancelCommand( DispatchCommand cancelCommand ) {
        this.cancelCommand = cancelCommand;
    }

    public interface DispatchCommand {
        public void dispatch( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException;
    }

}
