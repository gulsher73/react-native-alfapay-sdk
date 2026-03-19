package com.alfapay.rn

import android.content.Intent
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import io.flutter.embedding.android.FlutterActivity

class AlfaPayModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String = "AlfaPaySDK"

    @ReactMethod
    fun launch(config: ReadableMap) {
        val activity = reactApplicationContext.currentActivity ?: return

        val intent = Intent(activity, AlfaPaySdkActivity::class.java).apply {
            putExtra("partnerApiKey", config.getString("partnerApiKey") ?: "")
            putExtra("partnerSecret", config.getString("partnerSecret") ?: "")
            putExtra("environment", config.getString("environment") ?: "sandbox")
            putExtra("partnerUserId", config.getString("partnerUserId") ?: "")
            putExtra("countryCode", config.getString("countryCode") ?: "")
            putExtra("primaryColor", config.getString("primaryColor") ?: "#13A538")
            putExtra("logoUrl", config.getString("logoUrl") ?: "")
            putExtra("locale", config.getString("locale") ?: "en")
        }
        activity.startActivity(intent)
    }
}
