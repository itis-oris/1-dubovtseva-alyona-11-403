package boardgames.repository;

import ru.kpfu.itis.boardgames.model.Game;
import ru.kpfu.itis.boardgames.model.Category;

import java.util.List;

public interface GameRepository extends CrudRepository<Game> {
    List<Game> findAvailableGame();
    List<Game> findByCategoryID(Long categoryId);
    List<Game> findByCreaotr(Long creatorID);
    void addCategoryToGame(Long gameId, Long categoryId);
    void removeCategoryFromGame(Long gameId, Long categoryId);
    List<Category> findCategoriesByGameId(Long gameId);
    void updateGameImage(Long gameId, String gameImage);
    Long createCategory(String name);
}
