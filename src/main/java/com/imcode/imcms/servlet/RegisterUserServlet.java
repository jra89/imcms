package com.imcode.imcms.servlet;

import imcode.server.Imcms;
import imcode.server.user.RoleId;
import imcode.server.user.UserAlreadyExistsException;
import imcode.server.user.UserDomainObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Shadowgun on 19.02.2015.
 */
public class RegisterUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String login = req.getParameter("login");
            String name = req.getParameter("name");
            String surname = req.getParameter("surname");
            String password = req.getParameter("password1");
            String email = req.getParameter("email");
            UserDomainObject user = new UserDomainObject();
            user.setActive(true);
            user.setLoginName(login);
            user.setFirstName(name);
            user.setLastName(surname);
            user.setEmailAddress(email);
            user.setLanguageIso639_2(Imcms.getUser().getLanguageIso639_2());
            user.setPassword(password);
            user.addRoleId(RoleId.USERS);
            Imcms.getServices().getImcmsAuthenticatorAndUserAndRoleMapper().addUser(user);
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
        }
        req.getRequestDispatcher("/login/").forward(req, resp);
    }
}
