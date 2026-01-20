package boardgames.repository.impl;

import ru.kpfu.itis.boardgames.model.User;
import ru.kpfu.itis.boardgames.model.enums.UserRole;
import ru.kpfu.itis.boardgames.repository.Dbconnection;
import ru.kpfu.itis.boardgames.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToUser(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by username", e);
        }
        return null;
    }

    private User mapRowToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setName(resultSet.getString("name"));
        user.setAge(resultSet.getInt("age"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("passwordhash"));
        user.setRole(UserRole.valueOf(resultSet.getString("role")));
        user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        user.setProfileImage(resultSet.getString("profile_image")); // 🔽 ДОБАВИЛ
        return user;
    }
    private User mapRowToUserWithoutPassword(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setName(resultSet.getString("name"));
        user.setAge(resultSet.getInt("age"));
        user.setEmail(resultSet.getString("email"));
        user.setRole(UserRole.valueOf(resultSet.getString("role")));
        user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        return user;
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = Dbconnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by email", e);
        }
        return null;
    }

    @Override
    public List<User> findByRole(UserRole role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ?";
        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, role.name());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(mapRowToUser(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding users by role", e);
        }
        return users;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, name, age, email, role, created_at FROM users";
        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapRowToUserWithoutPassword(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving users", e);
        }
        return users;
    }

    @Override
    public User findById(Long userId) {

        String sql = "SELECT id, username, name, age, email, role, created_at FROM users WHERE id = ?";
        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToUserWithoutPassword(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by ID", e);
        }
        return null;
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (username, name, age, email, passwordhash, role, profile_image) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at";
        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setInt(3, user.getAge());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setString(6, user.getRole().name());
            preparedStatement.setString(7, user.getProfileImage());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getLong("id"));
                    user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving user", e);
        }
        return user;
    }


    @Override
    public void delete(User user) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, user.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    @Override
    public User update(User user) {
        String sql = """
                UPDATE users
                SET username = ?, name = ?, age = ?, email = ?, passwordhash = ?, role = ?
                WHERE id = ?
                """;
        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setInt(3, user.getAge());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setString(6, user.getRole().name());
            preparedStatement.setLong(7, user.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating user", e);
        }
        return user;
    }
    public void updateProfile(User user) {
        String sql = "UPDATE users SET name = ?, age = ?, email = ? WHERE id = ?";
        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getAge());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setLong(4, user.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating user profile.ftlh", e);
        }
    }
    public void updateProfileImage(Long userId, String profileImage) {
        String sql = "UPDATE users SET profile_image = ? WHERE id = ?";
        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, profileImage);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating profile.ftlh image", e);
        }
    }

    @Override
    public void updatePassword(Long userId, String hashedPassword) {
        String sql = "UPDATE users SET passwordhash = ? WHERE id = ?";

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, hashedPassword);
            stmt.setLong(2, userId);

            int updatedRows = stmt.executeUpdate();
            if (updatedRows == 0) {
                throw new RuntimeException("User not found with id: " + userId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating password", e);
        }
    }

}
