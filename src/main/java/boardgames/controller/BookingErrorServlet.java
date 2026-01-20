package boardgames.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/bookingerror")
public class BookingErrorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String message = req.getParameter("message");

        if (message != null) {
            message = URLDecoder.decode(message, StandardCharsets.UTF_8);
        }

        req.setAttribute("message", message);

        req.getRequestDispatcher("bookingerror.ftlh").forward(req, resp);
    }
}
