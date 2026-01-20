package boardgames.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/gamesdeletesuccess")
public class GameDeleteSuccessServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String title = req.getParameter("title");

        req.setAttribute("title", title);
        req.getRequestDispatcher("game_delete_success.ftlh").forward(req, resp);
    }
}
