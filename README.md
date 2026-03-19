# react-native-alfapay-sdk

React Native bridge for AlfaPay SDK. This package provides the JS-to-native bridging — the native SDKs must be added separately.

## Requirements

- React Native 0.70+
- iOS 13.0+ / Xcode 15.0+
- Android API 24+ / Java 17+ / Kotlin 2.0.0+

## Step 1 — Install the bridge package

```bash
npm install git+ssh://git@github.com:gulsher73/react-native-alfapay-sdk.git
```

> This is a **private** repo — ensure your GitHub account has been granted access by AlfaPay.

---

## Step 2 — Android Setup

### 2.1 Clone the Android SDK

```bash
cd your-rn-project/
git submodule add git@github.com:Code-Brew-AI/alfapay-android-sdk.git alfapay-android-sdk
git submodule update --init
```

### 2.2 Add repositories

`android/build.gradle` (root level) — add an `allprojects` block after `buildscript`:

```groovy
allprojects {
    repositories {
        google()
        mavenCentral()

        // Flutter engine
        maven { url "https://storage.googleapis.com/download.flutter.io" }

        // AlfaPay SDK
        maven { url "${rootProject.projectDir}/../alfapay-android-sdk/repo" }

        // Vendor AARs
        flatDir { dirs "${rootProject.projectDir}/../alfapay-android-sdk/libs" }

        // PureLive (identity verification) — credentials provided by AlfaPay
        maven {
            url "https://pureliveefr.jfrog.io/artifactory/main"
            credentials {
                username "your_username"
                password "your_password"
            }
        }
    }
}
```

> **Important:** These repositories must be in the **root** `build.gradle`, not in `app/build.gradle`. The SDK module needs access to these repos at compile time.

### 2.3 Add dependencies

`android/app/build.gradle` — add inside `dependencies`:

```groovy
// AlfaPay SDK
implementation("ae.alfapay.sdk.module:flutter_release:1.0")

// Vendor AARs
implementation(name: 'mdr-6.5.2', ext: 'aar')
implementation(name: 'mfl-5.1.0', ext: 'aar')
```

### 2.4 Build config

`android/app/build.gradle` — add inside `android` block:

```groovy
compileOptions {
    sourceCompatibility JavaVersion.VERSION_17
    targetCompatibility JavaVersion.VERSION_17
}
kotlinOptions {
    jvmTarget = "17"
}
packaging {
    jniLibs {
        pickFirsts += ["**/libc++_shared.so"]
        useLegacyPackaging = true
    }
    resources {
        excludes += ["META-INF/DEPENDENCIES", "META-INF/LICENSE", "META-INF/LICENSE.txt", "META-INF/NOTICE", "META-INF/NOTICE.txt"]
    }
}
```

### 2.5 Gradle properties

`android/gradle.properties` — add:

```properties
android.enableJetifier=true
android.nonTransitiveRClass=false
android.nonFinalResIds=false
android.stripNativeDebugSymbols=false
```

### 2.6 Permissions

`android/app/src/main/AndroidManifest.xml` — add inside `<manifest>`:

```xml
<uses-feature android:name="android.hardware.camera" android:required="false" />

<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.USE_BIOMETRIC" />
```

---

## Step 3 — iOS Setup

### 3.1 Clone the iOS SDK

```bash
cd your-rn-project/
git submodule add git@github.com:Code-Brew-AI/alfapay-ios-sdk.git alfapay-ios-sdk
git submodule update --init
```

### 3.2 Add AlfaPaySDK via SPM

Open your `.xcworkspace` in Xcode:

1. **File > Add Package Dependencies...**
2. Click **Add Local** and select the `alfapay-ios-sdk` folder
3. Select **AlfaPaySDK** library and add it to your app target

**Paste the GitHub URL and sign in to your GitHub account:**

<p align="center">
  <img width="800" alt="Paste GitHub URL and sign in" src="https://github.com/user-attachments/assets/15633e2a-3232-4671-a573-44c0bfced6f0" />
</p>

**After sign-in, you will see the distribution package — select it:**

<p align="center">
  <img width="800" alt="Select distribution package" src="https://github.com/user-attachments/assets/4926e8f6-23d7-4694-99f2-e355d093076a" />
</p>

**AlfaPaySDK is now added to your project:**

<p align="center">
  <img width="800" alt="AlfaPaySDK added to project" src="https://github.com/user-attachments/assets/d9331ed5-1a34-4cda-be3e-807c4595f1a5" />
</p>

### 3.3 Add iOS bridge files

Copy the bridge files from `node_modules/react-native-alfapay-sdk/ios/` into your Xcode project:

1. In Xcode, right-click your app folder (e.g. `YourApp/`) → **Add Files to "YourApp"**
2. Navigate to `node_modules/react-native-alfapay-sdk/ios/`
3. Select both files:
   - `AlfaPayModule.swift`
   - `AlfaPayModule.m`
4. Ensure **"Copy items if needed"** is checked
5. Ensure your **app target** is selected
6. Click **Add**

### 3.4 Info.plist permissions

Add to `ios/<YourApp>/Info.plist`:

```xml
<key>NSCameraUsageDescription</key>
<string>Camera is required for identity verification</string>

<key>NSFaceIDUsageDescription</key>
<string>Face ID is used for secure authentication</string>

<key>NSPhotoLibraryUsageDescription</key>
<string>Photo library access is needed for document upload</string>
```

### 3.5 Install pods

```bash
cd ios && pod install && cd ..
```

---

## Step 4 — Usage

```typescript
import { launchAlfaPay } from 'react-native-alfapay-sdk';

launchAlfaPay({
  partnerApiKey: 'PTR-XXXXX-001',
  partnerSecret: 'sk_sand_xxxxx',
  environment: 'sandbox',
  partnerUserId: 'your-user-id',
  countryCode: 'AE',
  primaryColor: '#2563EB',
  logoUrl: 'https://yourapp.com/logo.png',
  locale: 'en',
});
```

## SDK Configuration

| Parameter       | Required | Description                       |
|-----------------|----------|-----------------------------------|
| `partnerApiKey` | Yes      | API key from AlfaPay              |
| `partnerSecret` | Yes      | Secret key from AlfaPay           |
| `environment`   | Yes      | `"sandbox"` or `"production"`     |
| `partnerUserId` | Yes      | Your app's user identifier        |
| `countryCode`   | No       | ISO country code (e.g. `"AE"`)    |
| `primaryColor`  | No       | Brand hex color (e.g. `"#2563EB"`)|
| `logoUrl`       | No       | URL to your logo                  |
| `locale`        | No       | `"en"` or `"ar"`                  |

## SDK Callbacks

| Method                  | Description                    |
|-------------------------|--------------------------------|
| `onUserRegistered`      | Signup complete                |
| `onAuthSuccess`         | MPIN login success             |
| `onOnboardingComplete`  | KYC complete                   |
| `onTransactionComplete` | Payment/remittance done        |
| `onError`               | SDK error                      |
| `onSDKClosed`           | SDK dismissed                  |

## Troubleshooting

### Android: `Failed to resolve: ae.alfapay.sdk.module:flutter_release:1.0`

SDK repositories must be in the **root** `android/build.gradle` inside an `allprojects` block — not in `app/build.gradle` or `settings.gradle`. The SDK bridge module needs these repos to compile against the Flutter embedding.

### iOS: `No such module 'AlfaPaySDK'`

The bridge files (`AlfaPayModule.swift` + `.m`) must be in your **main app target**, not in a Pod. Make sure you followed Step 3.3 — add the files directly to your Xcode project.

### iOS: App crashes on camera / Face ID

Missing Info.plist permissions. Add all three keys from Step 3.4.

### Android: `Cannot run program "node"` in Android Studio

This happens when Android Studio can't find `node` in its environment — common with **nvm** or **fnm** users since GUI apps don't inherit shell PATH.

**Fix (pick one):**

1. **Symlink node globally** (recommended):
   ```bash
   sudo ln -sf $(which node) /usr/local/bin/node
   ```

2. **Launch Android Studio from terminal** so it inherits your PATH:
   ```bash
   open -a "Android Studio"
   ```

3. **Add `node.dir` to `android/local.properties`:**
   ```properties
   node.dir=/path/to/your/node/bin
   ```

> After applying any fix, fully quit Android Studio (Cmd+Q) and reopen it.

### Android: `llvm-strip` error on `liboz_native.so`

Harmless warning. Already handled by `android.stripNativeDebugSymbols=false` in Step 2.5.

## Updating the SDK

```bash
# Update bridge package
npm install git+ssh://git@github.com:gulsher73/react-native-alfapay-sdk.git

# Update Android SDK
cd alfapay-android-sdk && git pull origin main && cd ..

# Update iOS SDK
cd alfapay-ios-sdk && git pull origin main && cd ..
```

## Support

Contact your AlfaPay integration manager for API keys, PureLive credentials, and technical support.
