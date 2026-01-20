package boardgames.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.kpfu.itis.boardgames.model.User;
import ru.kpfu.itis.boardgames.service.UserService;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        this.userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("register.ftlh").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = new User();
        user.setUsername(req.getParameter("username"));
        user.setName(req.getParameter("name"));
        user.setAge(Integer.parseInt(req.getParameter("age")));
        user.setEmail(req.getParameter("email"));
        user.setPassword(req.getParameter("password"));

        try {
            User saved = userService.register(user);
            HttpSession session = req.getSession();
            session.setAttribute("currentUser", saved);
            resp.sendRedirect(req.getContextPath() + "/games");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("formUser", user);
            req.getRequestDispatcher("register.ftlh").forward(req, resp);
        }
    }
}

