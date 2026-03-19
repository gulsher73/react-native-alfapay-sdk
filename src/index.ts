import { NativeModules } from 'react-native';

const { AlfaPaySDK } = NativeModules;

export interface AlfaPayConfig {
  partnerApiKey: string;
  partnerSecret: string;
  environment: 'sandbox' | 'production';
  partnerUserId: string;
  countryCode?: string;
  primaryColor?: string;
  logoUrl?: string;
  locale?: string;
}

export function launchAlfaPay(config: AlfaPayConfig): void {
  if (!AlfaPaySDK) {
    throw new Error(
      'AlfaPaySDK native module not found. Make sure you have completed the native setup: ' +
      'Android (alfapay-android-sdk) and iOS (alfapay-ios-sdk).'
    );
  }
  AlfaPaySDK.launch(config);
}

export default AlfaPaySDK;
