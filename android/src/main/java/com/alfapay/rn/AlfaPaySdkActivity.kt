package com.alfapay.rn

import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class AlfaPaySdkActivity : FlutterActivity() {

    companion object {
        private const val TAG = "AlfaPaySDK"
        private const val CHANNEL = "alfapay_sdk"
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        val channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)

        channel.setMethodCallHandler { call, result ->
            val args = call.arguments as? Map<*, *> ?: emptyMap<String, Any?>()
            when (call.method) {
                "onUserRegistered" -> Log.d(TAG, "Registered: ${args["customerId"]}")
                "onAuthSuccess" -> Log.d(TAG, "Auth: ${args["customerId"]}")
                "onOnboardingComplete" -> Log.d(TAG, "Onboarded: ${args["customerId"]}")
                "onTransactionComplete" -> Log.d(TAG, "Transaction: $args")
                "onError" -> Log.e(TAG, "Error: $args")
                "onSDKClosed" -> {
                    Log.d(TAG, "Closed: ${args["reason"]}")
                    finish()
                }
                else -> { result.notImplemented(); return@setMethodCallHandler }
            }
            result.success(null)
        }

        val config = HashMap<String, Any?>()
        config["partnerApiKey"] = intent.getStringExtra("partnerApiKey") ?: ""
        config["partnerSecret"] = intent.getStringExtra("partnerSecret") ?: ""
        config["environment"] = intent.getStringExtra("environment") ?: "sandbox"
        config["partnerUserId"] = intent.getStringExtra("partnerUserId") ?: ""
        config["countryCode"] = intent.getStringExtra("countryCode") ?: ""
        config["primaryColor"] = intent.getStringExtra("primaryColor") ?: "#13A538"
        config["logoUrl"] = intent.getStringExtra("logoUrl") ?: ""
        config["locale"] = intent.getStringExtra("locale") ?: "en"

        channel.invokeMethod("sdk_initialize", config)
    }
}
