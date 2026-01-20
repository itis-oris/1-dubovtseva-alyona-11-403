package boardgames.service;

import jakarta.servlet.http.Part;
import ru.kpfu.itis.boardgames.model.Category;
import ru.kpfu.itis.boardgames.model.Game;

import java.sql.SQLException;
import java.util.List;

public interface GameService {
    Game createGame(Game game);
    List<Game> findAvailableGames();
    List<Game> findByCategory(Long categoryId);
    List<Game> findByCreator(Long creatorId);
    Game findById(Long id);
    Long createCategory(String name);
    Game updateGame(Game game);
    void deleteGame(Long id, Long currentUserId);
    void addCategoryToGame(Long gameId, Long categoryId);
    void removeCategoryFromGame(Long gameId, Long categoryId);
    List<Category> findCategoriesByGameId(Long gameId);
    void addCategoryToGameByName(Long gameId, String categoryName);
    void removeCategoryFromGameByName(Long gameId, String categoryName);
    void saveGameImage(Long gameId, Part filePart);
    List<Category> findAllCategories() throws SQLException;
    List<Category> getCategoriesByGameId(Long gameId);

}
