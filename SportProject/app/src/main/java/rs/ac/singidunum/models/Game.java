package rs.ac.singidunum.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Game implements Serializable {
    private String id;
    private String title;
    private String description;
    private String sport;
    private String startTime;
    private String endTime;
    private String date;
    private String location;
    private int    playersNeeded;
    private int    maxPlayers;
    private double price;
    private String ageRange;
    private String additionalInfo;
    private String creatorUsername;
    private String creatorUid;
    private List<String> joinedUsers;
    private Date startTimestamp;
    private Date endTimestamp;
// AND generate public getters/setters for both


    /** Firestore needs this no-arg constructor */
    public Game() {
        this.joinedUsers = new ArrayList<>();
    }

    /**
     * Full constructor
     */
    public Game(String title,
                String description,
                String sport,
                String startTime,
                String endTime,
                String date,
                String location,
                int    playersNeeded,
                int    maxPlayers,
                double price,
                String ageRange,
                String additionalInfo,
                String creatorUsername,
                String creatorUid) {
        this.title           = title;
        this.description     = description;
        this.sport           = sport;
        this.startTime       = startTime;
        this.endTime         = endTime;
        this.date            = date;
        this.location        = location;
        this.playersNeeded   = playersNeeded;
        this.maxPlayers      = maxPlayers;
        this.price           = price;
        this.ageRange        = ageRange;
        this.additionalInfo  = additionalInfo;
        this.creatorUsername = creatorUsername;
        this.creatorUid      = creatorUid;
        this.joinedUsers     = new ArrayList<>();
    }

    // --- Getters & Setters ---

    public String getId()               { return id; }
    public void   setId(String id)      { this.id = id; }

    public String getTitle()            { return title; }
    public void   setTitle(String t)    { this.title = t; }

    public String getDescription()      { return description; }
    public void   setDescription(String d) { this.description = d; }

    public String getSport()            { return sport; }
    public void   setSport(String s)    { this.sport = s; }

    public String getStartTime()        { return startTime; }
    public void   setStartTime(String s) { this.startTime = s; }

    public String getEndTime()          { return endTime; }
    public void   setEndTime(String e)   { this.endTime = e; }

    public String getDate()             { return date; }
    public void   setDate(String d)      { this.date = d; }

    public String getLocation()         { return location; }
    public void   setLocation(String l)  { this.location = l; }

    public int    getPlayersNeeded()    { return playersNeeded; }
    public void   setPlayersNeeded(int p) { this.playersNeeded = p; }

    public int    getMaxPlayers()       { return maxPlayers; }
    public void   setMaxPlayers(int m)  { this.maxPlayers = m; }

    public double getPrice()            { return price; }
    public void   setPrice(double p)    { this.price = p; }

    public String getAgeRange()         { return ageRange; }
    public void   setAgeRange(String a) { this.ageRange = a; }

    public String getAdditionalInfo()   { return additionalInfo; }
    public void   setAdditionalInfo(String a) { this.additionalInfo = a; }

    public String getCreatorUsername()  { return creatorUsername; }
    public void   setCreatorUsername(String c) { this.creatorUsername = c; }

    public String getCreatorUid()       { return creatorUid; }
    public void   setCreatorUid(String u) { this.creatorUid = u; }

    public List<String> getJoinedUsers()      { return joinedUsers; }
    public void        setJoinedUsers(List<String> list) { this.joinedUsers = list; }
}
