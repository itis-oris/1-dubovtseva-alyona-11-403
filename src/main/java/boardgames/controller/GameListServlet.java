package boardgames.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kpfu.itis.boardgames.service.GameService;

import java.io.IOException;

@WebServlet("/games")
public class GameListServlet extends HttpServlet {

    private GameService gameService;

    @Override
    public void init() {
        this.gameService = (GameService) getServletContext().getAttribute("gameService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("games", gameService.findAvailableGames());

        req.getRequestDispatcher("games.ftlh").forward(req, resp);
    }
}

