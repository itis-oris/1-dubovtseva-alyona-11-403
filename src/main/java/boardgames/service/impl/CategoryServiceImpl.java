package boardgames.service.impl;


import ru.kpfu.itis.boardgames.model.Category;
import ru.kpfu.itis.boardgames.repository.CategoryRepository;
import ru.kpfu.itis.boardgames.repository.impl.CategoryRepositoryImpl;
import ru.kpfu.itis.boardgames.service.CategoryService;

import java.sql.SQLException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {


    private CategoryRepository categoryRepository = new CategoryRepositoryImpl();

    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        try {
            return categoryRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Category findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название категории не может быть пустым");
        }

        Category category = categoryRepository.findByName(name.trim());
        if (category == null) {
            throw new IllegalArgumentException("Категория '" + name + "' не найдена");
        }

        return category;
    }

    @Override
    public Category save(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Название категории не может быть пустым");
        }

        Category existingCategory = categoryRepository.findByName(category.getName().trim());
        if (existingCategory != null) {
            throw new IllegalArgumentException("Категория с названием '" + category.getName() + "' уже существует");
        }

        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        if(category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Название категории не может быть пустым");
        }
        Category existingCategory = categoryRepository.findByName(category.getName().trim());
        if (existingCategory == null) {
            throw new IllegalArgumentException("Категория не найдена");
        }
        Category sameNameCategory = categoryRepository.findByName(category.getName());
        if (sameNameCategory != null&& !sameNameCategory.getId().equals(category.getId())) {
            throw new IllegalArgumentException("Категория с названием '" + category.getName() + "' уже существует");
        }

        return categoryRepository.save(category);
    }

    @Override
    public void delete(Category category) {
        Category existingCategory = categoryRepository.findById(category.getId());
        if (existingCategory == null) {
            throw new IllegalArgumentException("Категория не найдена");
        }

        categoryRepository.delete(category);
    }

    @Override
    public void deleteById(Long categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("ID категории не может быть null");
        }

        Category category = new Category();
        category.setId(categoryId);
        delete(category);
    }
}
