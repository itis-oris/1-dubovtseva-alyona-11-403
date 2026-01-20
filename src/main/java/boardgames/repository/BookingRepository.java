package boardgames.repository;

import ru.kpfu.itis.boardgames.model.Booking;

import java.sql.Timestamp;
import java.util.List;

public interface BookingRepository extends CrudRepository<Booking> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByGameId(Long gameId);
    void cancelBooking(Long id);
    boolean isGameFull(Long gameId, Timestamp bookingDate);
    int countBookingsForGame(Long gameId, Timestamp bookingDate);
}
