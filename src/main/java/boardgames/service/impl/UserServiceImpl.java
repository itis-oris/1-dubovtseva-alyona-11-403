package boardgames.service.impl;

import jakarta.servlet.http.Part;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kpfu.itis.boardgames.model.User;
import ru.kpfu.itis.boardgames.model.enums.UserRole;
import ru.kpfu.itis.boardgames.repository.UserRepository;
import ru.kpfu.itis.boardgames.repository.impl.UserRepositoryImpl;
import ru.kpfu.itis.boardgames.service.FileService;
import ru.kpfu.itis.boardgames.service.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class UserServiceImpl implements UserService {

    private UserRepository userRepository = new UserRepositoryImpl();

    private FileService fileService;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public User register(User user) {
        if(user.getUsername() == null || user.getUsername().trim().isEmpty())  {
            throw new IllegalArgumentException("Логин не может быть пустым");
        }

        if(user.getName() == null || user.getName().trim().isEmpty())  {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }

        if(user.getAge() ==  null || user.getAge().intValue() < 0 ||  user.getAge() >  100)  {
            throw new IllegalArgumentException("Введите свой возраст");
        }

        if(user.getEmail() == null || user.getEmail().trim().isEmpty())  {
            throw new IllegalArgumentException("Почта не может быть пустой");
        }

        if(user.getPassword() == null || user.getPassword().trim().isEmpty())  {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }

        if(user.getPassword().length() < 8)  {
            throw new IllegalArgumentException("Пароль должен содержать более 8 символов");
        }

        User existingUser = userRepository.findByUsername(user.getUsername());
        if(existingUser != null)  {
            throw new IllegalArgumentException("Пользователь с логином " + existingUser.getUsername() + " уже существует");
        }
        User existingUser2 = userRepository.findByEmail(user.getEmail());
        if(existingUser2 != null)  {
            throw new IllegalArgumentException("Пользователь с почтой " + existingUser.getEmail() + " уже зарегистрирован");
        }


        String hashPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(hashPassword);

        if(user.getRole() == null)  {
            user.setRole(UserRole.USER);
        }

        return userRepository.save(user);
    }

    @Override
    public void saveProfileImage(Long userId, Part filePart) {
        try {
            if (fileService != null && filePart != null && filePart.getSize() > 0) {
                User existingUser = userRepository.findById(userId);
                if (existingUser == null) {
                    throw new IllegalArgumentException("Пользователь не найден");
                }


                if (existingUser.getProfileImage() != null) {
                    fileService.deleteImage(existingUser.getProfileImage());
                }

                String profileImage = fileService.saveProfileImage(filePart);
                if (profileImage != null) {
                    userRepository.updateProfileImage(userId, profileImage);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении фото профиля: " + e.getMessage(), e);
        }
    }



    @Override
    public User login(String username, String password) {
        if(username == null || username.trim().isEmpty())  {
            throw new IllegalArgumentException("Логин не может быть пустым");
        }

        if(password == null || password.trim().isEmpty())  {
            throw new IllegalArgumentException("Почта не может быть пустой");
        }

        User user = userRepository.findByUsername(username);
        if(user == null)  {
            throw new IllegalArgumentException("Пользователя с таким логином не существует");
        }

        if(!BCrypt.checkpw(password, user.getPassword()))  {
            throw new IllegalArgumentException("Неверный пароль");
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {
        if(username == null || username.trim().isEmpty())  {
            throw new IllegalArgumentException("Логин не может быть пустым");
        }
        User user = userRepository.findByUsername(username);
        if(user == null)  {
            throw new IllegalArgumentException("Пользователя с таким логином не существует");
        }
        return user;
    }

    @Override
    public User findById(Long id) {
        if(id == null)  {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }
        User user = userRepository.findById(id);
        if(user == null)  {
            throw new IllegalArgumentException("Пользователя не найден");
        }
        return user;
    }

    @Override
    public void updateProfile(User user, Part filePart) throws IOException {
        if (user.getName() != null && user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }

        User existingUser = userRepository.findById(user.getId());
        if (existingUser == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        if (!user.getEmail().equals(existingUser.getEmail())) {
            User userWithSameEmail = userRepository.findByEmail(user.getEmail());
            if (userWithSameEmail != null) {
                throw new IllegalArgumentException("Пользователь с email '" + user.getEmail() + "' уже существует");
            }
        }

        String profileImagePath = existingUser.getProfileImage();

        if (filePart != null && filePart.getSize() > 0) {
            if (profileImagePath != null) {
                fileService.deleteImage(profileImagePath);
            }
            profileImagePath = fileService.saveProfileImage(filePart);
        }

        User userToUpdate = new User();
        userToUpdate.setId(existingUser.getId());
        userToUpdate.setName(user.getName());
        userToUpdate.setAge(user.getAge());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setProfileImage(profileImagePath);

        userRepository.updateProfile(userToUpdate);
    }



    @Override
    public void updatePassword(Long userId, String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("Пароль должен содержать минимум 6 символов");
        }

        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        userRepository.updatePassword(userId, hashedPassword);
    }

    @Override
    public List<User> findAll() {
        try {
            return userRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        userRepository.delete(user);
    }

    public boolean isAdmin(User user) {
        return user != null && user.getRole() == UserRole.ADMIN;
    }

    public boolean isOrganizer(User user) {
        return user != null && (user.getRole() == UserRole.ORGANIZER || user.getRole() == UserRole.ADMIN);
    }


}
