package boardgames.repository;

import ru.kpfu.itis.boardgames.model.User;

import java.sql.SQLException;
import java.util.List;

public interface CrudRepository<T> {
     List<T> findAll() throws SQLException;
     T findById(Long id);
     T save(T t);
     void delete(T t);
     T update(T t);
}
