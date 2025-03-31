package rs.ac.singidunum.models;

// This class represents a User with various attributes
public class User {
    private int id; // Unique identifier for the user
    private String username; // Username of the user
    private String password; // Password of the user
    private int age; // Age of the user
    private String gender; // Gender of the user
    private String biography; // Biography of the user
    private int basketballLevel; // Skill level in basketball
    private int footballLevel; // Skill level in football
    private int volleyballLevel; // Skill level in volleyball
    private int padelLevel; // Skill level in padel

    // Constructor to initialize a new User with given attributes
    public User(String username, String password, int age, String gender, String biography, int basketballLevel, int footballLevel, int volleyballLevel, int padelLevel) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.biography = biography;
        this.basketballLevel = basketballLevel;
        this.footballLevel = footballLevel;
        this.volleyballLevel = volleyballLevel;
        this.padelLevel = padelLevel;
    }

    // Getters and Setters for all fields
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }

    public int getBasketballLevel() { return basketballLevel; }
    public void setBasketballLevel(int basketballLevel) { this.basketballLevel = basketballLevel; }

    public int getFootballLevel() { return footballLevel; }
    public void setFootballLevel(int footballLevel) { this.footballLevel = footballLevel; }

    public int getVolleyballLevel() { return volleyballLevel; }
    public void setVolleyballLevel(int volleyballLevel) { this.volleyballLevel = volleyballLevel; }

    public int getPadelLevel() { return padelLevel; }
    public void setPadelLevel(int padelLevel) { this.padelLevel = padelLevel; }
}
