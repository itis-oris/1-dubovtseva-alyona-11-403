package boardgames.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.kpfu.itis.boardgames.model.User;
import ru.kpfu.itis.boardgames.service.BookingService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@WebServlet("/bookingscreate")
public class BookingCreateServlet extends HttpServlet {

    private BookingService bookingService;

    @Override
    public void init() {
        this.bookingService = (BookingService) getServletContext().getAttribute("bookingService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String gameId = req.getParameter("gameId");
        if (gameId != null) {
            resp.sendRedirect(req.getContextPath()
                    + "/bookingsuccess?gameId=" + gameId);
        } else {
            resp.sendRedirect(req.getContextPath() + "/games");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        Long gameId = Long.parseLong(req.getParameter("gameId"));

        try {
            bookingService.bookGame(
                    currentUser.getId(),
                    gameId,
                    LocalDateTime.now(),
                    1
            );

            resp.sendRedirect(req.getContextPath()
                    + "/bookingsuccess?gameId=" + gameId);


        } catch (IllegalArgumentException e) {

            String encoded = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);

            resp.sendRedirect(req.getContextPath()
                    + "/bookingerror?gameId=" + gameId + "&message=" + encoded);
        }
    }
}
