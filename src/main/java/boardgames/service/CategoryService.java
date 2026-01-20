package boardgames.service;

import ru.kpfu.itis.boardgames.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findByName(String name);
    Category save(Category category) ;
    Category update(Category category);
    void delete(Category category);
    void deleteById(Long categoryId);
}
