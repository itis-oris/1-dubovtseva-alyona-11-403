package boardgames.repository.impl;

import ru.kpfu.itis.boardgames.model.Category;
import ru.kpfu.itis.boardgames.repository.CategoryRepository;
import ru.kpfu.itis.boardgames.repository.Dbconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {

    @Override
    public Category findByName(String name) {
        String sql = "SELECT * FROM categories WHERE name = ?";

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToCategory(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding category by name", e);
        }
        return null;
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String query = "select * from categories order by name";
        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                categories.add(mapRowToCategory(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all categories", e);
        }
        return categories;
    }

    private Category mapRowToCategory(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setId(resultSet.getLong("id"));
        category.setName(resultSet.getString("name"));
        return category;
    }

    @Override
    public Category findById(Long id) {
        String sql = "SELECT * FROM categories WHERE id = ?";

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToCategory(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding category by ID", e);
        }
        return null;
    }


    @Override
    public Category save(Category category) {

        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Название категории не может быть пустым");
        }

        if (existByName(category.getName().trim())) {
            throw new IllegalArgumentException("Категория уже существует");
        }

        String sql = "INSERT INTO categories (name) VALUES (?) returning id";

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, category.getName().trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    category.setId(rs.getLong("id"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL при сохранении категории: " + e.getMessage(), e);
        }

        return category;
    }

    private boolean existByName(String name) {
        String sql = "SELECT COUNT(*) FROM categories WHERE LOWER(name) = LOWER(?)";

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, name.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка SQL в existByName(): " + e.getMessage(), e);
        }
    }


    @Override
    public void delete(Category category) {

        String sql = "DELETE FROM categories WHERE id = ?";

        try (Connection connection = Dbconnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, category.getId());
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Category update(Category category) {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";

        try (Connection connection = Dbconnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            stmt.setLong(2, category.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            if (existByName(category.getName().trim())) {
                throw new IllegalArgumentException("Категория уже существует");
            }
            throw new RuntimeException("Error updating category", e);
        }
        return category;
    }
}
