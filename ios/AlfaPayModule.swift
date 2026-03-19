import Foundation
import UIKit
import AlfaPaySDK

@objc(AlfaPaySDK)
class AlfaPayModule: NSObject {

  @objc
  func launch(_ config: NSDictionary) {
    DispatchQueue.main.async {
      guard let rootVC = UIApplication.shared.keyWindow?.rootViewController else { return }

      let engine = FlutterEngine(name: "alfapay_sdk")
      engine.run()
      GeneratedPluginRegistrant.register(with: engine)

      let flutterVC = FlutterViewController(engine: engine, nibName: nil, bundle: nil)
      flutterVC.modalPresentationStyle = .fullScreen

      let channel = FlutterMethodChannel(
        name: "alfapay_sdk",
        binaryMessenger: flutterVC.binaryMessenger
      )

      channel.invokeMethod("sdk_initialize", arguments: [
        "partnerApiKey": config["partnerApiKey"] as? String ?? "",
        "partnerSecret": config["partnerSecret"] as? String ?? "",
        "environment": config["environment"] as? String ?? "sandbox",
        "partnerUserId": config["partnerUserId"] as? String ?? "",
        "countryCode": config["countryCode"] as? String ?? "",
        "primaryColor": config["primaryColor"] as? String ?? "#13A538",
        "logoUrl": config["logoUrl"] as? String ?? "",
        "locale": config["locale"] as? String ?? "en",
      ])

      channel.setMethodCallHandler { call, result in
        let args = call.arguments as? [String: Any]
        switch call.method {
        case "onUserRegistered":
          print("[AlfaPay] User registered: \(args?["customerId"] ?? "")")
        case "onAuthSuccess":
          print("[AlfaPay] Auth success: \(args?["customerId"] ?? "")")
        case "onOnboardingComplete":
          print("[AlfaPay] Onboarding complete: \(args?["customerId"] ?? "")")
        case "onTransactionComplete":
          print("[AlfaPay] Transaction: \(args ?? [:])")
        case "onError":
          print("[AlfaPay] Error: \(args ?? [:])")
        case "onSDKClosed":
          print("[AlfaPay] SDK closed: \(args?["reason"] ?? "")")
          rootVC.dismiss(animated: true)
        default:
          result(FlutterMethodNotImplemented)
        }
      }

      rootVC.present(flutterVC, animated: true)
    }
  }

  @objc
  static func requiresMainQueueSetup() -> Bool {
    return false
  }
}
