package boardgames.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kpfu.itis.boardgames.service.GameService;

import java.io.IOException;

@WebServlet("/categoriesadd")
public class CategoryAddServlet extends HttpServlet {

    private GameService gameService;

    @Override
    public void init() {
        gameService = (GameService) getServletContext().getAttribute("gameService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String name = req.getParameter("name");

        if (name == null || name.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Название категории пустое");
            return;
        }

        try {
            Long id = gameService.createCategory(name);

            resp.setContentType("application/json");
            resp.getWriter().write("{\"id\": %d, \"name\": \"%s\"}".formatted(id, name));

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("Категория уже существует");

        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Ошибка сервера: " + e.getMessage());
        }
    }
}
