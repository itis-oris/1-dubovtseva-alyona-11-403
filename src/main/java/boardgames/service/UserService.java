package boardgames.service;
import jakarta.servlet.http.Part;
import ru.kpfu.itis.boardgames.model.User;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User register(User user);
    void saveProfileImage(Long userId, Part filePart);
    User login(String username, String password) ;
    User findByUsername(String username);
    User findById(Long id);
    void updateProfile(User user,Part filePart) throws IOException;
    void updatePassword(Long userId, String newPassword);
    List<User> findAll();
    void deleteUser(Long userId);
    boolean isAdmin(User user);
    boolean isOrganizer(User user);


}
