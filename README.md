# HackHunt - Hackathon Discovery Platform

<div align="center">

| Login | Register | App View |
|-------|----------|----------|
| <img src="https://github.com/user-attachments/assets/0ca9e0d9-5767-4eff-af54-ca5c814caff9" width="250"/> | <img src="https://github.com/user-attachments/assets/c950331d-9b14-46fa-bda9-29972372d8b8" width="250"/> | <img src="https://github.com/user-attachments/assets/585cd96a-b5d2-44e6-9158-a7eb98c9dd65" width="250"/> |


*A modern Android application for discovering and exploring hackathon events*

[Features](#features) • [Installation](#installation) • [Usage](#usage) • [Architecture](#architecture) • [Authors](#authors)

</div>

---

## Overview

**HackHunt** is a native Android application designed to help developers, designers, and tech enthusiasts discover upcoming hackathon events. Built with modern Android development practices, the app provides a seamless experience for browsing, filtering, and managing hackathon registrations.

### Project Information

- **Course:** Mobile Computing (CS 3101)
- **Institution:** [Your University Name]
- **Academic Year:** 2024-2025
- **Development Period:** [Start Date] - [End Date]

---

## Features

### Authentication
- **Email/Password Authentication** - Secure user registration and login
- **Google Sign-In Integration** - One-tap authentication with Google accounts
- **Remember Me Functionality** - Persistent login sessions for convenience
- **Password Visibility Toggle** - Enhanced user experience for password input

### Hackathon Discovery
- **Browse Hackathons** - View a curated list of upcoming hackathon events
- **Advanced Filtering** - Filter events by:
  - Month
  - Location
- **Detailed Event Information** - Access comprehensive details including:
  - Event title and description
  - Date and time
  - Location
  - Registration details

### User Interface
- **Modern Material Design 3** - Clean and intuitive interface
- **Coral Theme** - Eye-catching color scheme with coral accents (#FF7F66)
- **Dark Gradient Background** - Aesthetic dark theme for reduced eye strain
- **Smooth Animations** - Fluid transitions and interactions
- **Responsive Layouts** - Optimized for various screen sizes

### Backend Integration
- **Firebase Authentication** - Secure user authentication backend
- **Cloud Firestore** - Real-time database for hackathon data
- **Firebase Analytics** - Track user engagement and app performance

---

## 🎥 Demo Video

Click the image below to watch the full explanation:

[![Watch the video](https://github.com/user-attachments/assets/585cd96a-b5d2-44e6-9158-a7eb98c9dd65)](https://github.com/user-attachments/assets/02d1a749-ef8f-4c72-a047-a8dc011a397b)


## Getting Started

### Prerequisites

- **Android Studio** - Arctic Fox (2020.3.1) or later
- **JDK** - Version 11 or higher
- **Android SDK** - API Level 24 (Android 7.0) minimum, API Level 36 target
- **Google Account** - For Firebase configuration

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/BunquinTheodore/MobileComputing_FinalProject.git
   cd MobileComputing_FinalProject
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned repository

3. **Configure Firebase**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Add an Android app with package name: `com.example.vibecoders_finalprojectmobilecomputing`
   - Download `google-services.json` and place it in the `app/` directory
   - Enable Authentication (Email/Password and Google Sign-In)
   - Enable Cloud Firestore

4. **Add SHA-1 Certificate**
   ```bash
   # Get your debug SHA-1
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
   ```
   - Add the SHA-1 fingerprint to your Firebase project settings

5. **Sync and Build**
   - Click "Sync Now" when prompted by Android Studio
   - Build the project: `Build > Make Project`

6. **Run the App**
   - Connect an Android device or start an emulator
   - Click "Run" or press `Shift + F10`

---

## Usage

### User Registration
1. Launch the app
2. Tap "Sign Up" on the login screen
3. Fill in your details:
   - Full Name
   - Email Address
   - Password (minimum 6 characters)
   - Confirm Password
4. Tap "Register" to create your account

### Login
1. Enter your email and password
2. (Optional) Check "Remember Me" for persistent login
3. Tap "Login" or use "Sign in with Google"

### Browse Hackathons
1. After successful login, you'll see the hackathon list
2. Use the filter dropdowns to:
   - Select a specific month
   - Choose a location
3. Scroll through the list to explore events
4. Tap on any hackathon card to view more details

### Logout
1. Tap the logout button (circular icon) in the top-right corner
2. Confirm logout to return to the login screen

---

## Architecture

### Tech Stack

| Component | Technology |
|-----------|-----------|
| **Language** | Kotlin |
| **UI Framework** | Android Views + Material Design 3 |
| **Architecture Pattern** | MVVM (Model-View-ViewModel) |
| **Authentication** | Firebase Authentication |
| **Database** | Cloud Firestore |
| **Analytics** | Firebase Analytics |
| **Image Loading** | Native Android |
| **Build System** | Gradle (Kotlin DSL) |

### Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/vibecoders_finalprojectmobilecomputing/
│   │   │   ├── MainActivity.kt              # Login screen
│   │   │   ├── RegisterActivity.kt          # Registration screen
│   │   │   ├── HackathonListActivity.kt     # Main hackathon list
│   │   │   ├── HackathonAdapter.kt          # RecyclerView adapter
│   │   │   └── Hackathon.kt                 # Data model
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml        # Login UI
│   │   │   │   ├── activity_register.xml    # Registration UI
│   │   │   │   ├── activity_hackathon_list.xml
│   │   │   │   └── item_hackathon.xml       # Hackathon card layout
│   │   │   ├── values/
│   │   │   │   ├── colors.xml               # Color definitions
│   │   │   │   ├── strings.xml              # String resources
│   │   │   │   └── themes.xml               # App themes
│   │   │   └── drawable/
│   │   │       └── gradient_background.xml  # Background gradient
│   │   └── AndroidManifest.xml
│   └── google-services.json                 # Firebase configuration
├── build.gradle.kts                         # App-level build config
└── gradle/
```

### Key Dependencies

```kotlin
// Firebase
implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
implementation("com.google.firebase:firebase-auth")
implementation("com.google.firebase:firebase-analytics")
implementation("com.google.firebase:firebase-firestore")

// Google Sign-In
implementation("com.google.android.gms:play-services-auth:21.2.0")

// Material Design
implementation("com.google.android.material:material:1.9.0")

// AndroidX
implementation("androidx.appcompat:appcompat:1.7.0")
implementation("androidx.constraintlayout:constraintlayout:2.1.4")
implementation("androidx.recyclerview:recyclerview:1.3.2")
```

---

## Design System

### Color Palette

| Color | Hex Code | Usage |
|-------|----------|-------|
| Coral Primary | `#FF7F66` | Primary buttons, accents |
| Coral Light | `#FFB199` | Hover states, highlights |
| Background Dark | `#1A1A1A` | Main background |
| Card White | `#FFFFFF` | Card backgrounds |
| Text Gray | `#BDBDBD` | Secondary text |
| Text Dark | `#1A1A1A` | Primary text on light backgrounds |

### Typography

- **Primary Font:** Roboto
- **Headings:** Bold, 24sp - 32sp
- **Body Text:** Regular, 14sp - 16sp
- **Captions:** Light, 12sp

---

## Security Features

- **Password Encryption** - Firebase handles secure password storage
- **HTTPS Communication** - All Firebase requests use encrypted connections
- **Input Validation** - Client-side validation for email and password formats
- **Session Management** - Secure token-based authentication
- **SHA-1 Certificate Pinning** - Firebase app verification

---

## Testing

### Manual Testing Checklist

- [x] User registration with valid credentials
- [x] User registration with invalid credentials (error handling)
- [x] Login with email/password
- [x] Login with Google Sign-In
- [x] Remember Me functionality
- [x] Password visibility toggle
- [x] Hackathon list display
- [x] Filter by month
- [x] Filter by location
- [x] Logout functionality

### Test Devices

- **Emulator:** Android API 36 (flutter_emulator)
- **Physical Device:** [Add your test device if applicable]

---

## Authors

<table>
  <tr>
    <td align="center">
      <br />
      <sub><b>Theodore Von Joshua M. Bunquin</b></sub>
      <br />
      <sub>Lead Developer</sub>
      <br />
      <a href="https://github.com/BunquinTheodore">GitHub</a>
    </td>
    <td align="center">
      <br />
      <sub><b>Paul C. Alcaraz</b></sub>
      <br />
      <sub>Co-Lead Developer</sub>
      <br />
      <a href="https://github.com/PaulAlcaraz">GitHub</a>
    </td>
    <td align="center">
      <br />
      <sub><b>John Richnell C. Catibog</b></sub>
      <br />
      <sub>QA Tester</sub>
      <br />
      <a href="https://github.com/JohnCatibog">GitHub</a>
    </td>
  </tr>
</table>

### Contributions

- **Theodore Von Joshua M. Bunquin** - Project architecture, Firebase integration, UI/UX design, authentication system
- **Paul C. Alcaraz** - Feature implementation, code review, backend integration
- **John Richnell C. Catibog** - Quality assurance, testing, bug reports, user experience testing

---

## 📄 License

This project is developed as an academic requirement for CS 3101 - Mobile Computing.

**Copyright © 2024 VibeCoders Team. All rights reserved.**

For educational purposes only. Not licensed for commercial use.

---

## 🙏 Acknowledgments

- **Instructor:** Joshua Fronda - CS 3101 Mobile Computing
- **Firebase** - For providing excellent backend services
- **Material Design** - For comprehensive design guidelines
- **Stack Overflow Community** - For troubleshooting assistance
- **Android Developers Documentation** - For comprehensive API references

---

## 📞 Contact & Support

For questions, suggestions, or issues related to this project:
- **Repository Issues:** [GitHub Issues](https://github.com/BunquinTheodore/MobileComputing_FinalProject/issues)

---

## 🔗 Useful Links

- [Android Developers Guide](https://developer.android.com)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Material Design Guidelines](https://material.io/design)
- [Kotlin Programming Language](https://kotlinlang.org/)
- [Android Architecture Components](https://developer.android.com/topic/architecture)

---

<div align="center">

**Made with ❤️ by VibeCoders Team**

*CS 3101 - Mobile Computing Final Project*

</div>
