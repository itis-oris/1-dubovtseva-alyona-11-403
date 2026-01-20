package boardgames.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);

        boolean loggedIn = session != null && session.getAttribute("currentUser") != null;

        if (loggedIn) {
            resp.sendRedirect(req.getContextPath() + "/games");
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}

