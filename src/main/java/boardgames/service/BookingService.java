package boardgames.service;

import ru.kpfu.itis.boardgames.model.Booking;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    Booking bookGame(Long userId, Long gameId, LocalDateTime bookingDate, int playersCount);
    void cancelBooking(Long bookingId, Long currentUserId);
    List<Booking> findBookingsByUser(Long userId);
    List<Booking> findBookingsByGame(Long gameId);
    int countBookings(Long gameId, LocalDateTime date);
    boolean isGameFull(Long gameId, LocalDateTime date);
    void completeBooking(Long bookingId, Long organizerId);
    List<Booking> findAll() throws SQLException;
    int getBookedPlayersCount(Long gameId);
    Long cancelBookingAndReturnGameId(Long bookingId, Long currentUserId);

}
