# Timeless Parking - Client Mobile Application 🚗📱

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/studio)
[![Language](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.java.com/)
[![Backend](https://img.shields.io/badge/Backend-Firebase-yellow.svg)](https://firebase.google.com/)
[![Hardware](https://img.shields.io/badge/Hardware-Arduino-blue.svg)](https://www.arduino.cc/)

**Timeless Parking (User App)** is the client-side Android mobile application for the Timeless Parking ecosystem. Designed to eliminate the stress of searching for parking, this app connects directly to IoT-enabled parking lots via Bluetooth and coordinates with a central Firebase cloud backend. It empowers drivers to find vacant spaces, track active parking durations, handle frictionless checkouts using physical RFID technology, and claim promotional offers.

Developed as a Senior Project by **Nader Slim & Jad El Sayegh** at MUBS.

---

## 📌 User Features

* **Real-Time Slot Discovery:** View live availability grids before reaching your destination to secure an open slot immediately.
* **Frictionless RFID Entry/Exit:** Seamlessly pair with on-site hardware to register when your vehicle occupies or leaves a spot via physical RFID scans.
* **Automated Cost Tracking:** Built-in millisecond-precise timers calculate exact stay durations and instantly compute fair financial billing based on lot rates.
* **Comprehensive Session History:** Access a clean historical log of all previous parking sessions, including unique spot IDs, date/time timestamps, and exact payments.
* **Discounts & Promotions Hub:** Discover and claim active promo codes and dynamic offers rolled out instantly by lot administrators.

---

## 🏗️ System Architecture

The client application plays a vital role in processing on-site physical interaction loops and syncing them to the cloud:
1. **Hardware Interaction:** Receives real-time state changes and entry logs from the Arduino Uno (equipped with IR and RFID sensors) via an HC-05 Bluetooth module.
2. **Local Processing:** Formats the local incoming serial data streams inside the MVVM architecture framework.
3. **Cloud Synchronization:** Updates and fetches real-time parameters from Cloud Firestore, ensuring accurate globally synchronized billing states.

---

## ⚙️ Tech Stack & Dependencies

* **Development Environment:** Android Studio (Java, SDK 26+)
* **UI Design Pattern:** Google Material Design with MVVM architecture
* **Authentication:** Firebase Auth (Email/Password & Google Ecosystem Login)
* **Database & Storage:** Cloud Firestore NoSQL & Firebase Cloud Storage
* **Hardware Interfacing:** Android Bluetooth Core APIs (`BluetoothSocket`, `InputStream` listeners)

---

## 💾 App Package Structure

```text
app/src/main/java/com/example/timelessparking/user/
│
├── adapter/        # Custom RecyclerView Adapters for Slot Layouts, Offers, and History
├── bluetooth/      # Bluetooth Service handlers managing HC-05 data streams 
├── fragments/      # UI Fragments (Home, Map/Slots, History, Profile, Offers)
├── model/          # Local POJO Data models mapping Firestore documents (User, Receipt, Slot)
└── utils/          # Shared time converters, text formatters, and field validators
