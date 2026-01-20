package boardgames.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.kpfu.itis.boardgames.model.Game;
import ru.kpfu.itis.boardgames.model.User;
import ru.kpfu.itis.boardgames.service.GameService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/creategame")
@MultipartConfig
public class GameCreateServlet extends HttpServlet {

    private GameService gameService;

    @Override
    public void init() {
        this.gameService = (GameService) getServletContext().getAttribute("gameService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        try {
            req.setAttribute("categories", gameService.findAllCategories());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        req.getRequestDispatcher("game_create.ftlh").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        Game game = new Game();
        game.setTitle(req.getParameter("title"));
        game.setAddress(req.getParameter("address"));
        game.setDescription(req.getParameter("description"));
        game.setMinPlayers(Integer.parseInt(req.getParameter("minPlayers")));
        game.setMaxPlayers(Integer.parseInt(req.getParameter("maxPlayers")));
        game.setCreatorId(currentUser.getId());

        String date = req.getParameter("eventDate");
        String time = req.getParameter("eventTime");

        if (date != null && time != null && !date.isEmpty() && !time.isEmpty()) {
            game.setEventTime(date + " " + time);
        }

        try {

            Game saved = gameService.createGame(game);

            String[] catIds = req.getParameterValues("categories");
            if (catIds != null) {
                for (String c : catIds) {
                    gameService.addCategoryToGame(saved.getId(), Long.parseLong(c));
                }
            }

            Part imagePart = req.getPart("gameImage");
            if (imagePart != null && imagePart.getSize() > 0) {
                gameService.saveGameImage(saved.getId(), imagePart);
            }

            resp.sendRedirect(req.getContextPath() + "/gamesview?id=" + saved.getId());

        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("formGame", game);
            req.getRequestDispatcher("game_create.ftlh").forward(req, resp);
        }
    }
}
