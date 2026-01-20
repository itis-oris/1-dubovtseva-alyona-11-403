package boardgames.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.kpfu.itis.boardgames.model.User;
import ru.kpfu.itis.boardgames.service.BookingService;
import ru.kpfu.itis.boardgames.service.GameService;

import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private GameService gameService;
    private BookingService bookingService;

    @Override
    public void init() {
        this.gameService = (GameService) getServletContext().getAttribute("gameService");
        this.bookingService = (BookingService) getServletContext().getAttribute("bookingService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        req.setAttribute("user", currentUser);
        req.setAttribute("createdGames", gameService.findByCreator(currentUser.getId()));
        var bookings = bookingService.findBookingsByUser(currentUser.getId());

        for (var b : bookings) {
            b.setGame(gameService.findById(b.getGameId()));
        }

        req.setAttribute("bookings", bookings);


        req.getRequestDispatcher("profile.ftlh").forward(req, resp);
    }
}
