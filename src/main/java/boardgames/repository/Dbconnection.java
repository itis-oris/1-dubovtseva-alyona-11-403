package boardgames.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Dbconnection {

    private static HikariDataSource dataSource;

    private Dbconnection() {}

    public static synchronized void init() throws ClassNotFoundException {
        if (dataSource != null) return;

        Class.forName("org.postgresql.Driver");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/game_club");
        config.setUsername("postgres");
        config.setPassword("123456");
        config.setMaximumPoolSize(10);

        dataSource = new HikariDataSource(config);

    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null)
            throw new IllegalStateException("База данных не инициализирована");

        return dataSource.getConnection();
    }

    public static void destroy()  {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}

