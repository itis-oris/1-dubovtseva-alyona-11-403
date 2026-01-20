package boardgames.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.kpfu.itis.boardgames.model.Game;
import ru.kpfu.itis.boardgames.service.GameService;

import java.io.IOException;

@WebServlet("/bookingsuccess")
public class BookingSuccessServlet extends HttpServlet {

    private GameService gameService;

    @Override
    public void init() {
        this.gameService = (GameService) getServletContext().getAttribute("gameService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long gameId = Long.parseLong(req.getParameter("gameId"));
        Game game = gameService.findById(gameId);

        req.setAttribute("gameTitle", game.getTitle());
        req.setAttribute("eventTime", game.getEventTime());

        req.getRequestDispatcher("bookingsuccess.ftlh").forward(req, resp);
    }

}
