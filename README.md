# SmartPantryApp

## Project Description

SmartPantryApp is an Android application that helps users manage pantry ingredients and discover recipes based on the ingredients they have available.

Users can add, view, edit and delete pantry ingredients. Each ingredient can have a name, quantity, measurement unit and expiry date. The application also provides recipe suggestions and recipe details.

## Technologies Used

- Java
- XML
- SQLite
- SQLiteOpenHelper
- Android Studio

## Database Choice

SmartPantryApp uses SQLite for local data storage. SQLite was chosen because the application stores pantry ingredients and recipe information directly on the Android device. It does not require a separate database server or an internet connection for local database operations.

## Main Features

- Add pantry ingredients.
- View saved pantry ingredients.
- Edit ingredient information.
- Delete ingredients.
- Store ingredient quantities, units and expiry dates.
- View suggested recipes based on available ingredients.
- View recipe details and required ingredients.

## Requirements

- Android Studio
- Android SDK compatible with the project
- An Android emulator or compatible Android device

## Setup and Running the Application

1. Clone or download this repository from GitHub.
2. Open Android Studio.
3. Select **Open** and choose the project folder.
4. Allow Gradle to sync.
5. Start an Android emulator or connect an Android device.
6. Click **Run** to launch the application.

## Data Persistence

The application uses a local SQLite database to store pantry data. Saved data should remain available after the application is closed and reopened.

## Repository

GitHub: https://github.com/Busi23/SmartPantryApp-C