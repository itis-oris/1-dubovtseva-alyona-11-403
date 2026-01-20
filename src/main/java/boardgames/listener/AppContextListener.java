package boardgames.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import ru.kpfu.itis.boardgames.repository.*;
import ru.kpfu.itis.boardgames.repository.impl.*;
import ru.kpfu.itis.boardgames.service.*;
import ru.kpfu.itis.boardgames.service.impl.*;
import ru.kpfu.itis.boardgames.repository.Dbconnection;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {

            Dbconnection.init();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Ошибка инициализации базы данных", e);
        }

        ServletContext context = sce.getServletContext();

        String uploadPath = context.getRealPath("/uploads");
        FileService fileService = new FileService(uploadPath);

        UserRepository userRepository = new UserRepositoryImpl();
        GameRepository gameRepository = new GameRepositoryImpl();
        BookingRepository bookingRepository = new BookingRepositoryImpl();
        CategoryRepository categoryRepository = new CategoryRepositoryImpl();

        UserServiceImpl userService = new UserServiceImpl();
        GameServiceImpl gameService = new GameServiceImpl();
        BookingServiceImpl bookingService = new BookingServiceImpl();
        CategoryServiceImpl categoryService = new CategoryServiceImpl();

        userService.setFileService(fileService);
        gameService.setFileService(fileService);
        userService.setUserRepository(userRepository);
        gameService.setGameRepository(gameRepository);
        bookingService.setBookingRepository(bookingRepository);
        categoryService.setCategoryRepository(categoryRepository);
        context.setAttribute("fileService", fileService);
        context.setAttribute("userService", userService);
        context.setAttribute("gameService", gameService);
        context.setAttribute("bookingService", bookingService);
        context.setAttribute("categoryService", categoryService);
        context.setAttribute("userRepository", userRepository);
        context.setAttribute("gameRepository", gameRepository);
        context.setAttribute("bookingRepository", bookingRepository);
        context.setAttribute("categoryRepository", categoryRepository);

        System.out.println("Контекст успешно инициализирован 🎉");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Контекст уничтожен");
        Dbconnection.destroy();
    }
}
