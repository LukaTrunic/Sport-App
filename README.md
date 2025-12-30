# Sportiko – Sports Matchmaking and Community App

Sportiko is an Android application designed to make organizing casual sports games easier. Many people want to play football, basketball, tennis, or padel, but finding enough players or coordinating time and place is often difficult. Sportiko solves this by allowing users to create games, browse available games, join games created by others, and stay updated with the latest sports news. The application is built in Java using Android Studio, and it uses SQLite for storing data locally.

When the app starts, the user is greeted with a splash screen and then authentication screens where they can register and log in. After logging in, they access the main part of the app. Users can create new sports games by entering information such as game name, location, date, time, and number of players needed. Other users can browse available terms, view game details, and join games they are interested in. In addition to browsing public games, users also have a “My Terms” section showing games they created.

The app also includes a Profile section where the user’s data is stored and displayed. This helps personalize the experience and keeps important account information in one place. All application data such as users and games are stored using SQLite. This means the app works without needing a remote server or internet backend, which simplifies usage and improves reliability.

Sportiko also features a dedicated Sports News section. This feature is implemented using Retrofit and integrates with the News API to fetch real sports headlines. News is displayed using RecyclerView alongside SwipeRefreshLayout to allow pull-to-refresh functionality. For security reasons, the News API key is not included in the repository. To enable the news feature, the user must create a free key at newsapi.org and then insert it into the project in the appropriate place.

The project follows a clean and organized structure. Activities represent major screens such as LoginActivity, RegisterActivity, MainActivity, CreateGameActivity, GameDetailsActivity, ProfileActivity, SportsNewsActivity, and SplashActivity. Fragments (AvailableTermsFragment and MyTermsFragment) are used to display different sections of available and personal games. The data package includes DatabaseHelper, UserDao, and GameDao which handle SQLite database logic. The adapters package contains GameAdapter, NewsAdapter, and ViewPagerAdapter to handle data display in RecyclerViews and tab navigation. The network package includes RetrofitClient and NewsApiService for networking functionality, and models such as User, Game, and NewsResponse define app data structures.

This project closely reflects what I learned in CS50. Concepts such as problem solving, logical structuring, algorithms, data handling, and designing user-focused software all contributed to building this app. Even though this project is implemented in Java, the programming mindset, debugging ability, and structured thinking come directly from CS50. The project required careful planning, code organization, and testing to ensure everything worked together smoothly.

To run Sportiko, the project simply needs to be opened in Android Studio. After ensuring that the Android SDK is installed, the project can be built and run on either a virtual emulator or a real Android device. If the user wants to enable the news feature, they only need to add a News API key in SportsNewsActivity at the spot "Your_Api_Key". No additional server setup is necessary thanks to SQLite storage.

Building Sportiko was both challenging and rewarding. It is a complete and functional Android application that demonstrates database management, UI design, news API integration, app structure planning, and real problem solving. I am proud of this project and how it provides a simple but meaningful way for people to organize sports activities and stay connected to the sports world.

### Files Overview

Below is a brief overview of the most important files that I wrote for this project and what they do:

### app/src/main/java/rs/ac/singidunum/activities/
- `SplashActivity.java`  
  Shows the initial splash screen before the user is taken to the login or main part of the app if already logged in.

- `LoginActivity.java`  
  Handles user login, validates the entered credentials against the SQLite database and navigates to `MainActivity` on success.

- `RegisterActivity.java`  
  Allows new users to create an account, saving their information into the SQLite database via `UserDao`.

- `MainActivity.java`  
  Serves as the main container after login. It sets up navigation, hosts the fragments for available games and the user’s own games, and provides access to profile and news.

- `CreateGameActivity.java`  
  Screen where a user can create a new sports game by entering details such as name, location, date, time, and number of players. Uses `GameDao` to insert the game into the database.

- `GameDetailsActivity.java`  
  Shows detailed information about a selected game and lets the user join it. Updates the database accordingly.

- `ProfileActivity.java`  
  Displays information about the currently logged-in user and reads it from the database.

- `SportsNewsActivity.java`  
  Fetches and displays sports news using Retrofit and the News API, and shows articles in a RecyclerView with pull-to-refresh.

### app/src/main/java/rs/ac/singidunum/fragments/
- `AvailableTermsFragment.java`  
  Lists all available games that users can join, using `GameAdapter` to bind game data to the RecyclerView.

- `MyTermsFragment.java`  
  Lists only the games created by the currently logged-in user.

- `GameAdapter.java`  
  RecyclerView adapter responsible for displaying game items in the lists.

- `NewsAdapter.java`  
  RecyclerView adapter that binds news article data to the news list in `SportsNewsActivity`.

- `ViewPagerAdapter.java`  
  Connects fragments (Available Terms and My Terms) to the ViewPager / tab layout in the main screen.

### app/src/main/java/rs/ac/singidunum/data/
- `DatabaseHelper.java`  
  Creates and upgrades the SQLite database and defines the tables for users and games.

- `UserDao.java`  
  Provides methods for inserting, querying and validating users in the database.

- `GameDao.java`  
  Provides methods for inserting, listing, filtering and updating games in the database.

### app/src/main/java/rs/ac/singidunum/models/
- `User.java`, `Game.java`, `NewsResponse.java`  
  Model classes that define the structure of user, game and news data used throughout the app.

Together, these files implement the full flow of the application: authentication, game creation and discovery, profile management, SQLite persistence, and sports news retrieval.

