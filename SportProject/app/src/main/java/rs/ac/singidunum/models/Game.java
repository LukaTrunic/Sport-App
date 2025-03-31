package rs.ac.singidunum.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {

    private String title;
    private String description;
    private String sport;
    private String startTime;
    private String endTime;
    private String date;
    private String location;
    private int playersNeeded;
    private int maxPlayers;
    private double price;
    private String ageRange;
    private String additionalInfo;
    private String creatorUsername;
    private List<String> joinedUsers;

    // Constructor with all fields
    public Game(String title, String description, String sport, String startTime, String endTime, String date,
                String location, int playersNeeded, int maxPlayers, double price, String ageRange, String additionalInfo, String creatorUsername) {
        this.title = title;
        this.description = description;
        this.sport = sport;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.location = location;
        this.playersNeeded = playersNeeded;
        this.maxPlayers = maxPlayers;
        this.price = price;
        this.ageRange = ageRange;
        this.additionalInfo = additionalInfo;
        this.creatorUsername = creatorUsername;
        this.joinedUsers = new ArrayList<>(); // Initialize the list
    }

    // Getters and Setters for all fields
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSport() { return sport; }
    public void setSport(String sport) { this.sport = sport; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getPlayersNeeded() { return playersNeeded; }
    public void setPlayersNeeded(int playersNeeded) { this.playersNeeded = playersNeeded; }

    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getAgeRange() { return ageRange; }
    public void setAgeRange(String ageRange) { this.ageRange = ageRange; }

    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }

    public String getCreatorUsername() { return creatorUsername; }
    public void setCreatorUsername(String creatorUsername) { this.creatorUsername = creatorUsername; }

    public List<String> getJoinedUsers() { return joinedUsers; }
    public void setJoinedUsers(List<String> joinedUsers) { this.joinedUsers = joinedUsers; }
}
