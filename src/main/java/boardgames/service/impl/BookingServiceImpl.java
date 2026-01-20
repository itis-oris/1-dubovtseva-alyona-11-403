package boardgames.service.impl;


import ru.kpfu.itis.boardgames.model.Booking;
import ru.kpfu.itis.boardgames.model.Game;
import ru.kpfu.itis.boardgames.model.User;
import ru.kpfu.itis.boardgames.model.enums.BookingStatus;
import ru.kpfu.itis.boardgames.repository.BookingRepository;
import ru.kpfu.itis.boardgames.repository.CategoryRepository;
import ru.kpfu.itis.boardgames.repository.GameRepository;
import ru.kpfu.itis.boardgames.repository.UserRepository;
import ru.kpfu.itis.boardgames.repository.impl.BookingRepositoryImpl;

import ru.kpfu.itis.boardgames.repository.impl.GameRepositoryImpl;
import ru.kpfu.itis.boardgames.repository.impl.UserRepositoryImpl;
import ru.kpfu.itis.boardgames.service.BookingService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository = new BookingRepositoryImpl();
    private UserRepository userRepository = new UserRepositoryImpl();
    private GameRepository gameRepository = new GameRepositoryImpl();

    public void setGameRepository(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void setBookingRepository(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking bookGame(Long userId, Long gameId, LocalDateTime bookingDate, int playersCount) {

        if (userId == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }
        if (gameId == null) {
            throw new IllegalArgumentException("ID игры не может быть null");
        }

        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        Game game = gameRepository.findById(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Игра не найдена");
        }

        if (!game.getAvailable()) {
            throw new IllegalArgumentException("Игра недоступна для бронирования");
        }

        if (hasUserActiveBookingForGame(userId, gameId)) {
            throw new IllegalArgumentException("Вы уже записаны на эту игру");
        }

        int booked = getBookedPlayersCount(gameId);

        if (booked >= game.getMaxPlayers()) {
            throw new IllegalArgumentException("Игра уже заполнена");
        }

        if (booked + 1 > game.getMaxPlayers()) {
            throw new IllegalArgumentException("Недостаточно свободных мест");
        }

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setGameId(gameId);
        booking.setBookingDate(LocalDateTime.now());
        booking.setPlayersCount(1);
        booking.setStatus(BookingStatus.BOOKED);

        return bookingRepository.save(booking);
    }
    @Override
    public int getBookedPlayersCount(Long gameId) {
        List<Booking> bookings = bookingRepository.findByGameId(gameId);
        return bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.BOOKED)
                .mapToInt(Booking::getPlayersCount)
                .sum();
    }

    @Override
    public void cancelBooking(Long bookingId, Long currentUserId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Бронирование не найдено");
        }

        if (!booking.getUserId().equals(currentUserId)) {
            throw new IllegalArgumentException("Вы можете отменять только свои бронирования");
        }
        if (booking.getStatus() != BookingStatus.BOOKED) {
            throw new IllegalArgumentException("Бронирование уже отменено или завершено");
        }
        bookingRepository.cancelBooking(bookingId);
    }
    public Long cancelBookingAndReturnGameId(Long bookingId, Long currentUserId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Бронирование не найдено");
        }

        if (!booking.getUserId().equals(currentUserId)) {
            throw new IllegalArgumentException("Вы можете отменять только свои бронирования");
        }

        if (booking.getStatus() != BookingStatus.BOOKED) {
            throw new IllegalArgumentException("Бронирование уже отменено или завершено");
        }

        bookingRepository.cancelBooking(bookingId);
        return booking.getGameId();
    }


    @Override
    public List<Booking> findBookingsByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<Booking> findBookingsByGame(Long gameId) {
        if (gameId == null) {
            throw new IllegalArgumentException("ID игры не может быть null");
        }
        return bookingRepository.findByGameId(gameId);
    }

    @Override
    public int countBookings(Long gameId, LocalDateTime date) {

        if (gameId == null) {
            throw new IllegalArgumentException("ID игры не может быть null");
        }

        if (date == null) {
            throw new IllegalArgumentException("Дата бронирования не может быть null");
        }
        return bookingRepository.countBookingsForGame(gameId, Timestamp.valueOf(date));
    }

    @Override
    public boolean isGameFull(Long gameId, LocalDateTime date) {

        if (gameId == null) {
            throw new IllegalArgumentException("ID игры не может быть null");
        }

        if (date == null) {
            throw new IllegalArgumentException("Дата бронирования не может быть null");
        }
        return bookingRepository.isGameFull(gameId, Timestamp.valueOf(date));
    }
    private boolean hasUserActiveBookingForGame(Long userId, Long gameId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);

        return bookings.stream()
                .anyMatch(b ->
                        b.getGameId().equals(gameId)
                                && b.getStatus() == BookingStatus.BOOKED
                );
    }
    public void completeBooking(Long bookingId, Long organizerId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("ID бронирования не может быть null");
        }

        Booking booking = bookingRepository.findById(bookingId);

        if (booking == null) {
            throw new IllegalArgumentException("Бронирование не найдено");
        }

        Game game = gameRepository.findById(booking.getGameId());

        if (!game.getCreatorId().equals(organizerId)) {
            throw new IllegalArgumentException("Вы можете завершать только свои игры");
        }
        booking.setStatus(BookingStatus.COMPLETED);
        bookingRepository.update(booking);
    }
    public List<Booking> findAll() throws SQLException {

        return bookingRepository.findAll();
    }

}
