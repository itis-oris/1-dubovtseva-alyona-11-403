package boardgames.model;

import java.time.LocalDateTime;

public class Game {
    private Long id;
    private Long creatorId;
    private String title;
    private String address;
    private String description;
    private Integer minPlayers;
    private Integer maxPlayers;
    private  Boolean available;
    private LocalDateTime createdAt;
    private String gameImage;
    private String eventTime;

    public Game(Long id, String title, String address, String description, Integer minPlayers, Integer maxPlayers, Boolean available, String gameImage, String eventTime) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.description = description;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.available = available;
        this.gameImage = gameImage;
        this.eventTime = eventTime;
    }

    public Game(Long id, String title, String description, Integer minPlayers, Integer maxPlayers) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public Game(String title, String address, String description, Integer minPlayers,
                Integer maxPlayers, Long creatorId) {
        this.title = title;
        this.address = address;
        this.description = description;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.creatorId = creatorId;
        this.available = true;
    }

    public Game(String title, String address, String description, Integer minPlayers) {
        this.title = title;
        this.address = address;
        this.description = description;
    }

    public Game(String title, String address, String description, Integer minPlayers, Integer maxPlayers) {
        this.title = title;
        this.address = address;
        this.description = description;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public Game(Long id, String title, String address, String description, Integer minPlayers, Integer maxPlayers) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.description = description;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public Game() {}

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(Integer minPlayers) {
        this.minPlayers = minPlayers;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getGameImage() {
        return gameImage;
    }

    public void setGameImage(String gameImage) {
        this.gameImage = gameImage;
    }
}
