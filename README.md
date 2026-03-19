# react-native-alfapay-sdk

React Native bridge for AlfaPay SDK. This package provides the JS-to-native bridging — the native SDKs must be added separately.

## Install

```bash
npm install git+ssh://git@github.com:Code-Brew-AI/react-native-alfapay-sdk.git
```

## Native SDK Setup (required)

This package only provides the bridge. You must add the native SDKs manually:

### Android

1. Clone `alfapay-android-sdk` alongside your project
2. Add repositories and dependencies — see [Android SDK README](https://github.com/Code-Brew-AI/alfapay-android-sdk)
3. Add to `android/gradle.properties`:
   ```properties
   android.enableJetifier=true
   android.nonTransitiveRClass=false
   android.nonFinalResIds=false
   android.stripNativeDebugSymbols=false
   ```
4. Add permissions to `AndroidManifest.xml`:
   ```xml
   <uses-feature android:name="android.hardware.camera" android:required="false" />
   <uses-permission android:name="android.permission.CAMERA" />
   <uses-permission android:name="android.permission.USE_BIOMETRIC" />
   ```

### iOS

1. Clone `alfapay-ios-sdk` alongside your project
2. Open `.xcworkspace` in Xcode → **File > Add Package Dependencies** → add local `alfapay-ios-sdk`
3. Add to `Info.plist`:
   ```xml
   <key>NSCameraUsageDescription</key>
   <string>Camera is required for identity verification</string>
   <key>NSFaceIDUsageDescription</key>
   <string>Face ID is used for secure authentication</string>
   <key>NSPhotoLibraryUsageDescription</key>
   <string>Photo library access is needed for document upload</string>
   ```
4. Run `cd ios && pod install`

## Usage

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

## Configuration

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

## Support

Contact your AlfaPay integration manager for API keys and technical support.
