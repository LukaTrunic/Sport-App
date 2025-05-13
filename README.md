# 🏀 SportProject – Android App for Sports Game Organization

**SportProject** is an Android application developed to help users organize and join sports matches. Users can browse upcoming games, create new ones, register/login, view details, and keep track of sports news — all within a single mobile interface.

---

## 📱 Key Features

- 👥 **User Registration/Login** – Create and manage user accounts
- 📅 **Game Creation** – Add new sports games with location, time, price, and required players
- 🗂 **Game Listings** – Browse a list of available games with filtering
- 📄 **Game Details View** – See all info related to a particular game
- 📰 **Sports News Integration** – Fetch sports news from an external API
- 🌐 **Profile Page** – View personal profile and posted games

---

## 🛠 Technology Stack

- **Language**: Java
- **Platform**: Android Studio
- **Database**: SQLite (local storage)
- **Networking**: Retrofit for API calls
- **Image Loading**: Glide
- **Architecture**: Activity-based, using adapters and intent-driven navigation

---

## 📁 Project Structure

```
SportProject/
├── app/
│   └── src/
│       ├── main/
│       │   ├── java/rs/ac/singidunum/activities/
│       │   ├── java/rs/ac/singidunum/adapters/
│       │   ├── java/rs/ac/singidunum/api/
│       │   ├── java/rs/ac/singidunum/db/
│       │   ├── java/rs/ac/singidunum/models/
│       │   └── res/ (layouts, values, drawables)
│       └── AndroidManifest.xml
```

---

## ▶️ How to Run

1. Open the project in **Android Studio**.
2. Ensure required dependencies are installed (`Retrofit`, `Glide`, etc.).
3. Run the app on an emulator or physical device (API level 33+ recommended).

---

## 🚀 External APIs

- **News API** – Sports news integration using Retrofit and image loading with Glide

---

## 🔐 Permissions Required

- Internet access
- (Optionally) Location if game filtering by distance is added

---

## 📬 Author

**Luka Trunić**  
Student ID: 2021230020  
Faculty of Technical Sciences, Novi Sad  
Course: Android Development

---
