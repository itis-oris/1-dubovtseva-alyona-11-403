package boardgames.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.kpfu.itis.boardgames.model.User;
import ru.kpfu.itis.boardgames.service.BookingService;

import java.io.IOException;


@WebServlet("/bookingscancel")
public class BookingCancelServlet extends HttpServlet {

    private BookingService bookingService;

    @Override
    public void init() {
        this.bookingService = (BookingService) getServletContext().getAttribute("bookingService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idStr = req.getParameter("bookingId");

        if (idStr == null) {
            resp.sendRedirect(req.getContextPath() + "/bookingerror?message=Отсутствует+ID+бронирования");
            return;
        }

        Long bookingId = Long.parseLong(idStr);

        Long userId = ((User) req.getSession().getAttribute("currentUser")).getId();

        try {
            Long gameId = bookingService.cancelBookingAndReturnGameId(bookingId, userId);

            resp.sendRedirect(req.getContextPath() + "/bookingcancelsuccess?gameId=" + gameId);

        } catch (IllegalArgumentException e) {
            resp.sendRedirect(req.getContextPath()
                    + "/bookingerror?message=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }
}

