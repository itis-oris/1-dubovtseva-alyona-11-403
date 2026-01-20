package boardgames.repository.impl;

import ru.kpfu.itis.boardgames.model.Booking;
import ru.kpfu.itis.boardgames.model.enums.BookingStatus;
import ru.kpfu.itis.boardgames.repository.BookingRepository;
import ru.kpfu.itis.boardgames.repository.Dbconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingRepositoryImpl implements BookingRepository {
    @Override
    //активные игры пользователя
    public List<Booking> findByUserId(Long userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.*, g.title as game_title 
            FROM bookings b
            JOIN games g ON b.game_id = g.id
            WHERE b.user_id = ? AND b.status = 'BOOKED'
            ORDER BY b.booking_date DESC
            """;
        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setLong(1, userId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(mapRowToBookingForUser(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bookings;
    }

    private Booking mapRowToBookingForUser(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getLong("id"));
        booking.setGameId(rs.getLong("game_id"));
        booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
        booking.setPlayersCount(rs.getInt("players_count"));
        booking.setStatus(BookingStatus.valueOf(rs.getString("status")));
        booking.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        booking.setGameTitle(rs.getString("game_title"));
        return booking;
    }

    @Override
    public List<Booking> findByGameId(Long gameId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.*, u.username as user_name, u.email as user_email
            FROM bookings b
            JOIN users u ON b.user_id = u.id
            WHERE b.game_id = ? AND b.status = 'BOOKED'
            ORDER BY b.booking_date DESC
            """;
        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setLong(1, gameId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(mapRowToBookingForCreator(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bookings;

    }

    private Booking mapRowToBookingForCreator(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getLong("id"));
        booking.setUserId(rs.getLong("user_id"));
        booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
        booking.setPlayersCount(rs.getInt("players_count"));
        booking.setStatus(BookingStatus.valueOf(rs.getString("status")));
        booking.setUserName(rs.getString("user_name"));
        booking.setUserEmail(rs.getString("user_email"));
        return booking;
    }

    @Override
    public void cancelBooking(Long id) {
        String sql = "UPDATE bookings SET status = 'CANCELLED' WHERE id = ? AND status = 'BOOKED'";

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int updatedRows = stmt.executeUpdate();
            if (updatedRows == 0) {
                throw new RuntimeException("Бронирование не найдено или уже отменено");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error cancelling booking", e);
        }
    }

    @Override
    public boolean isGameFull(Long gameId, Timestamp bookingDate) {
        String sql = """
            SELECT 
                g.max_players,
                COALESCE(SUM(b.players_count), 0) as current_players
            FROM games g
            LEFT JOIN bookings b ON g.id = b.game_id 
                AND b.booking_date = ? 
                AND b.status = 'BOOKED'
            WHERE g.id = ?
            GROUP BY g.id, g.max_players
            """;

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setTimestamp(1, bookingDate);
            stmt.setLong(2, gameId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int maxPlayers = rs.getInt("max_players");
                    int currentPlayers = rs.getInt("current_players");
                    return currentPlayers >= maxPlayers;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking if game is full", e);
        }
        return false;
    }

    @Override
    public int countBookingsForGame(Long gameId, Timestamp bookingDate) {
        String sql = """
            SELECT COALESCE(SUM(players_count), 0) as total_players 
            FROM bookings 
            WHERE game_id = ? AND booking_date = ? AND status = 'BOOKED'
            """;

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, gameId);
            stmt.setTimestamp(2, bookingDate);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_players");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error counting bookings for game", e);
        }
        return 0;
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.*, u.username, g.title as game_title
            FROM bookings b
            JOIN users u ON b.user_id = u.id
            JOIN games g ON b.game_id = g.id
            ORDER BY b.created_at DESC
            """;

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bookings.add(mapRowToBookingForAdmin(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all bookings", e);
        }
        return bookings;
    }

    private Booking mapRowToBookingForAdmin(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getLong("id"));
        booking.setUserId(rs.getLong("user_id"));
        booking.setGameId(rs.getLong("game_id"));
        booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
        booking.setPlayersCount(rs.getInt("players_count"));
        booking.setStatus(BookingStatus.valueOf(rs.getString("status")));
        booking.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        booking.setUserName(rs.getString("username"));
        booking.setGameTitle(rs.getString("game_title"));
        return booking;
    }

    @Override
    public Booking findById(Long id) {
        String sql = """
            SELECT b.*, u.username, g.title as game_title, g.max_players
            FROM bookings b
            JOIN users u ON b.user_id = u.id
            JOIN games g ON b.game_id = g.id
            WHERE b.id = ?
            """;

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToBooking(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding booking by ID", e);
        }
        return null;
    }

    private Booking mapRowToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getLong("id"));
        booking.setUserId(rs.getLong("user_id"));
        booking.setGameId(rs.getLong("game_id"));
        booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
        booking.setPlayersCount(rs.getInt("players_count"));
        booking.setStatus(BookingStatus.valueOf(rs.getString("status")));
        booking.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        booking.setUserName(rs.getString("username"));
        booking.setGameTitle(rs.getString("game_title"));
        booking.setMaxPlayers(rs.getInt("max_players"));
        return booking;
    }

    @Override
    public Booking save(Booking booking) {
        String sql = """
            INSERT INTO bookings (user_id, game_id, booking_date, players_count, status) 
            VALUES (?, ?, ?, ?, ?) RETURNING id, created_at
            """;

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, booking.getUserId());
            stmt.setLong(2, booking.getGameId());
            stmt.setTimestamp(3, Timestamp.valueOf(booking.getBookingDate()));
            stmt.setInt(4, booking.getPlayersCount());
            stmt.setString(5, booking.getStatus().name());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    booking.setId(rs.getLong("id"));
                    booking.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving booking", e);
        }
        return booking;
    }

    @Override
    public void delete(Booking booking) {
        String sql = "DELETE FROM bookings WHERE id = ?";

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, booking.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting booking", e);
        }
    }

    @Override
    public Booking update(Booking booking) {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, booking.getStatus().name());
            stmt.setLong(2, booking.getId());

            int updatedRows = stmt.executeUpdate();
            if (updatedRows == 0) {
                throw new RuntimeException("Booking not found with id: " + booking.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating booking", e);
        }
        return booking;
    }
}
