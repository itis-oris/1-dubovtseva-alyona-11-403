package boardgames.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.kpfu.itis.boardgames.model.User;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        HttpSession session = req.getSession(false);
        String path = req.getRequestURI();
        String context = req.getContextPath();
        String method = req.getMethod();

        User currentUser = (session != null)
                ? (User) session.getAttribute("currentUser")
                : null;

        boolean loggedIn = (currentUser != null);

        if (loggedIn) {
            req.setAttribute("currentUser", currentUser);
        }

        boolean isAuthPage =
                path.equals(context + "/login") ||
                        path.equals(context + "/register");

        boolean isPublic =
                path.equals(context + "/") ||
                        path.startsWith(context + "/static/") ||
                        path.startsWith(context + "/uploads/");

        boolean isPostOnlyPath =
                path.equals(context + "/bookingscreate") ||
                        path.equals(context + "/categoriesadd");

        if (!loggedIn) {

            if (isAuthPage || isPublic) {
                filterChain.doFilter(req, resp);
                return;
            }

            if (isPostOnlyPath && !method.equals("POST")) {
                resp.sendRedirect(context + "/login");
                return;
            }

            if (isPostOnlyPath && method.equals("POST")) {
                filterChain.doFilter(req, resp);
                return;
            }

            resp.sendRedirect(context + "/login");
            return;
        }
        if (isPostOnlyPath && !method.equals("POST")) {
            resp.sendRedirect(context + "/games");
            return;
        }

        filterChain.doFilter(req, resp);
    }
}
