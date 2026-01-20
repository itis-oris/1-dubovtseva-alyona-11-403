package boardgames.model;

import ru.kpfu.itis.boardgames.model.enums.BookingStatus;

import java.time.LocalDateTime;

public class Booking {
    private Long id;
    private Long userId;
    private Long gameId;
    private LocalDateTime bookingDate;
    private Integer playersCount;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private String userName;
    private String userEmail;
    private String gameTitle;
    private Integer maxPlayers;
    private Game game;

    public Booking() {}
    public Booking(Long id, Long userId, Long gameId, LocalDateTime bookingDate, Integer playersCount, BookingStatus status, LocalDateTime createdAt, String userName, String userEmail, String gameTitle, Integer maxPlayers) {
        this.id = id;
        this.userId = userId;
        this.gameId = gameId;
        this.bookingDate = bookingDate;
        this.playersCount = playersCount;
        this.status = status;
        this.createdAt = createdAt;
        this.userName = userName;
        this.userEmail = userEmail;
        this.gameTitle = gameTitle;
        this.maxPlayers = maxPlayers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Integer getPlayersCount() {
        return playersCount;
    }

    public void setPlayersCount(Integer playersCount) {
        this.playersCount = playersCount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
}
