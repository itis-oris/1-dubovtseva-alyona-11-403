package boardgames.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.kpfu.itis.boardgames.model.Game;
import ru.kpfu.itis.boardgames.model.User;
import ru.kpfu.itis.boardgames.service.GameService;

import java.io.IOException;

@WebServlet("/gamesdeleteedit")
public class GameDeleteEditServlet extends HttpServlet {

    private GameService gameService;

    @Override
    public void init() {
        this.gameService = (GameService) getServletContext().getAttribute("gameService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long id = Long.parseLong(req.getParameter("id"));
        Game game = gameService.findById(id);

        if (game == null) {
            resp.sendRedirect(req.getContextPath() + "/games?error=Игра не найдена");
            return;
        }

        req.setAttribute("game", game);
        req.getRequestDispatcher("game_edit.ftlh").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        Long id = Long.parseLong(req.getParameter("id"));
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            try {
                Game deletedGame = gameService.findById(id);
                String title = deletedGame.getTitle();

                gameService.deleteGame(id, currentUser.getId());

                resp.sendRedirect(req.getContextPath()
                        + "/gamesdeletesuccess?title=" + java.net.URLEncoder.encode(title, "UTF-8"));
            } catch (IllegalArgumentException e) {
                resp.sendRedirect(req.getContextPath() + "/games?error=" + e.getMessage());
            }
            return;
        }



        Game game = gameService.findById(id);

        game.setTitle(req.getParameter("title"));
        game.setAddress(req.getParameter("address"));
        game.setDescription(req.getParameter("description"));
        game.setMinPlayers(Integer.parseInt(req.getParameter("minPlayers")));
        game.setMaxPlayers(Integer.parseInt(req.getParameter("maxPlayers")));

        String date = req.getParameter("eventDate");
        String time = req.getParameter("eventTime");

        if (date != null && time != null && !date.isEmpty() && !time.isEmpty()) {
            game.setEventTime(date + " " + time);
        }

        try {
            gameService.updateGame(game);
            resp.sendRedirect(req.getContextPath() + "/gamesview?id=" + id + "&success=Изменения сохранены");

        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("game", game);
            req.getRequestDispatcher("game_edit.ftlh").forward(req, resp);
        }
    }
}
