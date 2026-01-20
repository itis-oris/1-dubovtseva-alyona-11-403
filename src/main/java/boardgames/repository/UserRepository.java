package boardgames.repository;

import ru.kpfu.itis.boardgames.model.User;
import ru.kpfu.itis.boardgames.model.enums.UserRole;

import java.util.List;


public interface UserRepository extends CrudRepository<User> {
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findByRole(UserRole role);
    void updateProfile(User user);
    void updatePassword(Long userId, String hashedPassword);
    void updateProfileImage(Long userId, String profileImage);
}
