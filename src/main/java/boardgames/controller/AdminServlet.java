package boardgames.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.kpfu.itis.boardgames.model.Category;
import ru.kpfu.itis.boardgames.model.User;
import ru.kpfu.itis.boardgames.model.enums.UserRole;
import ru.kpfu.itis.boardgames.service.CategoryService;
import ru.kpfu.itis.boardgames.service.UserService;

import java.io.IOException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private UserService userService;
    private CategoryService categoryService;

    @Override
    public void init() {
        this.userService = (UserService) getServletContext().getAttribute("userService");
        this.categoryService = (CategoryService) getServletContext().getAttribute("categoryService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRole() != UserRole.ADMIN) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        req.setAttribute("users", userService.findAll());
        req.setAttribute("categories", categoryService.findAll());

        req.getRequestDispatcher("admin.ftlh").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String action = req.getParameter("action");

        if ("createCategory".equals(action)) {
            String name = req.getParameter("name");
            Category c = new Category();
            c.setName(name);
            categoryService.save(c);
        } else if ("deleteUser".equals(action)) {
            Long userId = Long.parseLong(req.getParameter("userId"));
            userService.deleteUser(userId);
        } else if ("deleteCategory".equals(action)) {
            Long catId = Long.parseLong(req.getParameter("categoryId"));
            categoryService.deleteById(catId);
        }

        resp.sendRedirect(req.getContextPath() + "/admin");
    }
}
