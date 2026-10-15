# SlopeWatch NER — Landslide Early Warning & Slope Telemetry System

**SlopeWatch NER** is a production-ready Android application built with **Kotlin**, **Jetpack Compose (Material 3)**, **Room Database**, and **Koin** dependency injection. It monitors geological slope stability, precipitation thresholds, pore-water pressure, and seismic telemetry across 12 high-risk corridors in North-East India (Sikkim, Meghalaya, Assam, Arunachal Pradesh, Nagaland, Manipur, Mizoram, Tripura).

---

## 🔗 Live Application Links

- **Shared App URL (Direct Web Streaming Link):**  
  [https://ais-pre-cgeub47j3a65fqplbplxkj-2134830940.asia-east1.run.app](https://ais-pre-cgeub47j3a65fqplbplxkj-2134830940.asia-east1.run.app)
- **Development App URL:**  
  [https://ais-dev-cgeub47j3a65fqplbplxkj-2134830940.asia-east1.run.app](https://ais-dev-cgeub47j3a65fqplbplxkj-2134830940.asia-east1.run.app)

Anyone with the **Shared App URL** can open the app in a web browser and interact directly with the running Android application via the cloud-streamed emulator.

---

## 📱 How to Download & Share

### Option 1: Share the Live Web App (No Installation Needed)
Share the **Shared App URL** above. Recipients can run and test the complete app in their browser without downloading anything.

### Option 2: Download the APK onto Your Phone
1. In the AI Studio top navigation bar or settings menu (⚙️), select **Generate APK / Download APK**.
2. Or use the built APK generated at:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```
3. Transfer the `.apk` file to your Android phone via USB, Google Drive, WhatsApp, or Telegram.
4. On your phone, tap the file and choose **Install** (allow *Install from Unknown Sources* if prompted).

### Option 3: Export as Full Project (ZIP or GitHub)
1. In AI Studio, click the project menu in the top bar.
2. Click **Export as ZIP** or **Push to GitHub**.
3. Open the unzipped folder in **Android Studio** (Ladybug or newer) and click **Run ▶**.

---

## 🛠 Features & Capabilities

- **12 Real Monitoring Stations**: Includes Gangtok Ridge (NH10), Mangan-Chungthang (MCT), Cherrapunji Escarpment, Dima Hasao Rail Corridor, Kamakhya Hills, Noney Tupul Railway Yard, Kohima Zubza, Sela Pass Highway, Aizawl Crest, and more.
- **Mohr-Coulomb Factor of Safety (FoS) Calculator**: Computes active failure risk based on slope angle, effective normal stress, pore water pressure, and inclinometer tilt.
- **Interactive Geotechnical Failure Simulator**: Live slider simulating cloudburst pore-pressure surges to project emergency slope slip triggers.
- **Native 60/120 FPS Canvas Sparklines**: Smooth 12-hour trends for tilt angle, pore water pressure, 24h accumulated rainfall, and seismic PGA.
- **Customizable Safety Thresholds**: Calibrate pore pressure, slope tilt, and rainfall alert boundaries with Room SQLite persistence.
- **Live Sensor Telemetry Stream**: Auto-emulates sensor packet broadcasts every 4 seconds.

---

## 🏛 Technical Architecture

- **UI Framework:** Jetpack Compose with Material Design 3 (Edge-to-Edge `enableEdgeToEdge()`).
- **State Management:** MVVM with lifecycle-aware `StateFlow` and `collectAsStateWithLifecycle()`.
- **Dependency Injection:** Koin (`koin-android`, `koin-androidx-compose`).
- **Local Persistence:** Room Database 2.7.0 with indexed composite keys (`station_id`, `timestamp`).
- **Rendering Performance:** Zero-allocation Canvas draw scopes, unkeyed lazy column avoidance with stable keys.
