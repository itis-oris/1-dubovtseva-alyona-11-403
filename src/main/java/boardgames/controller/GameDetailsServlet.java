package boardgames.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kpfu.itis.boardgames.model.Game;
import ru.kpfu.itis.boardgames.service.BookingService;
import ru.kpfu.itis.boardgames.service.GameService;

import java.io.IOException;

@WebServlet("/gamesview")
public class GameDetailsServlet extends HttpServlet {

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

        Long gameId = Long.parseLong(req.getParameter("id"));

        Game game = gameService.findById(gameId);
        int bookedCount = bookingService.getBookedPlayersCount(gameId);

        req.setAttribute("categories", gameService.getCategoriesByGameId(gameId));

        req.setAttribute("game", game);
        req.setAttribute("bookedCount", bookedCount);

        String error = req.getParameter("error");
        if (error != null && !error.isEmpty()) {
            req.setAttribute("error", error);
        }

        String success = req.getParameter("success");
        if (success != null && !success.isEmpty()) {
            req.setAttribute("success", success);
        }

        req.getRequestDispatcher("game_details.ftlh").forward(req, resp);
    }
}

