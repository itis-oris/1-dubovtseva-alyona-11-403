package boardgames.service.impl;

import jakarta.servlet.http.Part;
import ru.kpfu.itis.boardgames.model.Category;
import ru.kpfu.itis.boardgames.model.Game;
import ru.kpfu.itis.boardgames.repository.CategoryRepository;
import ru.kpfu.itis.boardgames.repository.GameRepository;
import ru.kpfu.itis.boardgames.repository.impl.CategoryRepositoryImpl;
import ru.kpfu.itis.boardgames.repository.impl.GameRepositoryImpl;
import ru.kpfu.itis.boardgames.service.FileService;
import ru.kpfu.itis.boardgames.service.GameService;

import java.sql.SQLException;
import java.util.List;

public class GameServiceImpl implements GameService {
    private GameRepository gameRepository =  new GameRepositoryImpl();
    private CategoryRepository categoryRepository =  new CategoryRepositoryImpl();
    private FileService fileService;

    public void setGameRepository(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public Game createGame(Game game) {

        if (game.getTitle() == null || game.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Название игры не может быть пустым");
        }

        if (game.getAddress() == null || game.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Адрес проведения не может быть пустым");
        }

        if (game.getDescription() == null || game.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Описание игры не может быть пустым");
        }

        if (game.getMinPlayers() == null || game.getMinPlayers() < 1) {
            throw new IllegalArgumentException("Минимальное количество игроков должно быть не менее 1");
        }

        if (game.getMaxPlayers() == null || game.getMaxPlayers() < game.getMinPlayers()) {
            throw new IllegalArgumentException("Максимальное количество игроков должно быть больше или равно минимальному");
        }

        if (game.getCreatorId() == null) {
            throw new IllegalArgumentException("ID создателя не может быть null");
        }

        if (game.getAvailable() == null) {
            game.setAvailable(true);
        }

        return gameRepository.save(game);
    }

    public void saveGameImage(Long gameId, Part filePart) {
        try {
            if (fileService != null && filePart != null && filePart.getSize() > 0) {

                Game existingGame = gameRepository.findById(gameId);
                if (existingGame == null) {
                    throw new IllegalArgumentException("Игра не найдена");
                }

                if (existingGame.getGameImage() != null) {
                    fileService.deleteImage(existingGame.getGameImage());
                }

                String gameImage = fileService.saveGameImage(filePart);
                if (gameImage != null) {
                    gameRepository.updateGameImage(gameId, gameImage);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении фото игры: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Game> findAvailableGames() {
        return gameRepository.findAvailableGame();
    }

    @Override
    public List<Game> findByCategory(Long categoryId) {
        if (categoryRepository.findById(categoryId) == null) {
            throw new IllegalArgumentException("Категория не найдена");
        }

        return gameRepository.findByCategoryID(categoryId);
    }

    @Override
    public List<Game> findByCreator(Long creatorId) {
        if (creatorId == null) {
            throw new IllegalArgumentException("ID создателя не может быть null");
        }

        return gameRepository.findByCreaotr(                    creatorId);
    }

    @Override
    public Game findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID игры не может быть null");
        }

        Game game = gameRepository.findById(id);
        if (game == null) {
            throw new IllegalArgumentException("Игра не найдена");
        }

        return game;
    }

    @Override
    public Game updateGame(Game game) {
        Game existingGame = gameRepository.findById(game.getId());
        if (existingGame == null) {
            throw new IllegalArgumentException("Игра не найдена");
        }

        if (game.getTitle() != null && game.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Название игры не может быть пустым");
        }
        if (game.getMinPlayers() != null && game.getMinPlayers() < 1) {
            throw new IllegalArgumentException("Минимальное количество игроков должно быть не менее 1");
        }
        if (game.getMaxPlayers() != null && game.getMinPlayers() != null
                && game.getMaxPlayers() < game.getMinPlayers()) {
            throw new IllegalArgumentException("Максимальное количество игроков должно быть больше или равно минимальному");
        }

        return gameRepository.update(game);
    }

    public List<Category> findAllCategories() throws SQLException {
        return categoryRepository.findAll();
    }
    public Long createCategory(String name) {
        return gameRepository.createCategory(name);
    }

    public List<Category> getCategoriesByGameId(Long gameId) {
        return gameRepository.findCategoriesByGameId(gameId);
    }


    @Override
    public void deleteGame(Long id, Long currentUserId) {
        if (id == null) {
            throw new IllegalArgumentException("ID игры не может быть null");
        }
        if (currentUserId == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }

        Game game = gameRepository.findById(id);
        if (game == null) {
            throw new IllegalArgumentException("Игра не найдена");
        }

        if (!game.getCreatorId().equals(currentUserId)) {
            throw new IllegalArgumentException("Вы можете удалять только свои игры");
        }

        gameRepository.delete(game);
    }

    @Override
    public void addCategoryToGame(Long gameId, Long categoryId) {
        if (gameId == null) {
            throw new IllegalArgumentException("ID игры не может быть null");
        }
        if (categoryId == null) {
            throw new IllegalArgumentException("ID категории не может быть null");
        }

        Game game = gameRepository.findById(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Игра не найдена");
        }

        if (categoryRepository.findById(categoryId) == null) {
            throw new IllegalArgumentException("Категория не найдена");
        }

        gameRepository.addCategoryToGame(gameId, categoryId);
    }
    public void removeCategoryFromGame(Long gameId, Long categoryId) {

        Game game = gameRepository.findById(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Игра не найдена");
        }

        if (categoryRepository.findById(categoryId) == null) {
            throw new IllegalArgumentException("Категория не найдена");
        }

        gameRepository.removeCategoryFromGame(gameId, categoryId);
    }

    public List<Category> findCategoriesByGameId(Long gameId) {
        if (gameId == null) {
            throw new IllegalArgumentException("ID игры не может быть null");
        }

        return gameRepository.findCategoriesByGameId(gameId);
    }

    public void addCategoryToGameByName(Long gameId, String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Название категории не может быть пустым");
        }

        Category category = categoryRepository.findByName(categoryName.trim());
        if (category == null) {
            throw new IllegalArgumentException("Категория '" + categoryName + "' не найдена");
        }

        addCategoryToGame(gameId, category.getId());
    }

    public void removeCategoryFromGameByName(Long gameId, String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Название категории не может быть пустым");
        }

        Category category = categoryRepository.findByName(categoryName.trim());
        if (category == null) {
            throw new IllegalArgumentException("Категория '" + categoryName + "' не найдена");
        }

        removeCategoryFromGame(gameId, category.getId());
    }

}
