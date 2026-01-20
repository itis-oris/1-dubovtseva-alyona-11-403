package boardgames.repository;

import ru.kpfu.itis.boardgames.model.Category;

public interface CategoryRepository extends CrudRepository<Category> {
    Category findByName(String name);
}
