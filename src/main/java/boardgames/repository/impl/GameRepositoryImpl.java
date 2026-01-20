package boardgames.repository.impl;

import ru.kpfu.itis.boardgames.model.Category;
import ru.kpfu.itis.boardgames.model.Game;
import ru.kpfu.itis.boardgames.repository.Dbconnection;
import ru.kpfu.itis.boardgames.repository.GameRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GameRepositoryImpl implements GameRepository {

    private Game mapRowToGame(ResultSet resultSet) throws SQLException {
        Game game = new Game();
        game.setId(resultSet.getLong("id"));
        game.setTitle(resultSet.getString("title"));
        game.setAddress(resultSet.getString("address"));
        game.setDescription(resultSet.getString("description"));
        game.setMinPlayers(resultSet.getInt("min_players"));
        game.setMaxPlayers(resultSet.getInt("max_players"));
        game.setAvailable(resultSet.getBoolean("available"));
        game.setCreatorId(resultSet.getLong("creator_id"));
        game.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        game.setGameImage(resultSet.getString("game_image"));
        String event = resultSet.getString("event_time");
        if (event != null && !event.isEmpty()) {
            game.setEventTime(event);
        }

        return game;
    }

    private Game mapRowToGameForUser(ResultSet rs) throws SQLException {
        Game game = new Game();
        game.setId(rs.getLong("id"));
        game.setTitle(rs.getString("title"));
        game.setDescription(rs.getString("description"));
        game.setMinPlayers(rs.getInt("min_players"));
        game.setMaxPlayers(rs.getInt("max_players"));
        game.setAvailable(rs.getBoolean("available"));
        game.setAddress(rs.getString("address"));
        game.setGameImage(rs.getString("game_image"));

        String event = rs.getString("event_time");
        if (event != null && !event.isEmpty()) {
            game.setEventTime(event);
        }


        return game;
    }

    public void updateGameImage(Long gameId, String gameImage) {
        String sql = "UPDATE games SET game_image = ? WHERE id = ?";

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, gameImage);
            stmt.setLong(2, gameId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating game image", e);
        }
    }

    @Override
    public List<Game> findAvailableGame() {
        List<Game> gameList = new ArrayList<>();
        String query = """
        SELECT id, title, description, min_players, max_players,
               available, event_time, game_image, address
        FROM games
        WHERE available = true
        ORDER BY created_at ASC
        
    """;

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                gameList.add(mapRowToGameForUser(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return gameList;
    }


    @Override
    public List<Game> findByCategoryID(Long categoryId) {
        List<Game> gameList = new ArrayList<>();
        String query = """
                SELECT g.id, g.title, g.description, g.min_players, g.max_players
                        FROM games g
                        JOIN game_categories gc ON g.id = gc.game_id
                        WHERE gc.category_id = ? AND g.available = true
                        ORDER BY g.created_at DESC
                """;
        try (Connection connection = Dbconnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setLong(1, categoryId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    gameList.add(mapRowToGameForUser(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return gameList;
    }

    @Override
    public List<Game> findByCreaotr(Long creatorID) {
        List<Game> games = new ArrayList<>();
        String query = "SELECT * FROM GAMES WHERE creator_id = ? ORDER BY created_at DESC";
        try (Connection connection = Dbconnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setLong(1, creatorID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    games.add(mapRowToGame(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error find game by creator id", e);
        }
        return games;
    }

    @Override
    public List<Game> findAll() throws SQLException {
        List<Game> games = new ArrayList<>();
        String query = "SELECT * FROM GAMES";
        try (Connection connection = Dbconnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                games.add(mapRowToGame(resultSet));
            }
        }
        return games;
    }

    @Override
    public Game findById(Long id) {
        String query = "SELECT * FROM GAMES WHERE id = ?";
        try (Connection connection = Dbconnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToGame(resultSet);
                }
            }
        }  catch (SQLException e) {
            throw new RuntimeException("Error finding game by ID", e);
        }
        return null;
    }

    @Override
    public Game save(Game game) {
        String sql = """
            
                INSERT INTO games
            (title, address, description, min_players, max_players, available, creator_id, game_image, event_time)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at
            
            """;

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, game.getTitle());
            stmt.setString(2, game.getAddress());
            stmt.setString(3, game.getDescription());
            stmt.setInt(4, game.getMinPlayers());
            stmt.setInt(5, game.getMaxPlayers());
            stmt.setBoolean(6, game.getAvailable());
            stmt.setLong(7, game.getCreatorId());
            stmt.setString(8, game.getGameImage());
            stmt.setString(9, game.getEventTime());



            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    game.setId(rs.getLong("id"));
                    game.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving game", e);
        }
        return game;
    }

    @Override
    public void delete(Game game) {
        String sql = "DELETE FROM games WHERE id = ?";

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, game.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting game", e);
        }
    }

    @Override
    public Game update(Game game) {
        String sql = """
            UPDATE games
                            SET title=?, description=?, min_players=?, max_players=?, available=?, event_time=?
            WHERE id = ?
            """;

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, game.getTitle());
            stmt.setString(2, game.getDescription());
            stmt.setInt(3, game.getMinPlayers());
            stmt.setInt(4, game.getMaxPlayers());
            stmt.setBoolean(5, game.getAvailable());
            stmt.setString(6, game.getEventTime());

            stmt.setLong(7, game.getId());

            int updatedRows = stmt.executeUpdate();
            if (updatedRows == 0) {
                throw new RuntimeException("Game not found with id: " + game.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating game", e);
        }
        return game;
    }

    public void addCategoryToGame(Long gameId, Long categoryId) {
        String sql = "INSERT INTO game_categories (game_id, category_id) VALUES (?, ?)";

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, gameId);
            stmt.setLong(2, categoryId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error adding category to game", e);
        }
    }

    @Override
    public void removeCategoryFromGame(Long gameId, Long categoryId) {
        String sql = "DELETE FROM game_categories WHERE game_id = ? AND category_id = ?";

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, gameId);
            stmt.setLong(2, categoryId);
            int deletedRows = stmt.executeUpdate();

            if (deletedRows == 0) {
                throw new RuntimeException("Связь между игрой и категорией не найдена");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error removing category from game", e);
        }
    }
    @Override
    public List<Category> findCategoriesByGameId(Long gameId) {
        List<Category> categories = new ArrayList<>();
        String sql = """
            SELECT c.* FROM categories c
            JOIN game_categories gc ON c.id = gc.category_id
            WHERE gc.game_id = ?
            """;

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, gameId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category();
                    category.setId(rs.getLong("id"));
                    category.setName(rs.getString("name"));
                    categories.add(category);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding categories for game", e);
        }
        return categories;
    }
    @Override
    public Long createCategory(String name) {
        String sql = "INSERT INTO categories (name) VALUES (?) RETURNING id";

        try (Connection conn = Dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании категории", e);
        }

        return null;
    }

}
