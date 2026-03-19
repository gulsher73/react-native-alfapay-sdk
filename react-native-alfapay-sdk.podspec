require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-alfapay-sdk"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = "https://github.com/gulsher73/react-native-alfapay-sdk"
  s.license      = package["license"]
  s.author       = package["author"]
  s.platforms    = { :ios => "13.0" }
  s.source       = { :git => "https://github.com/gulsher73/react-native-alfapay-sdk.git", :tag => s.version }
  # iOS bridge files must be added to the host app target manually
  # (they import AlfaPaySDK via SPM which CocoaPods can't resolve)
  s.source_files = "ios-pod/**/*.{swift,h,m}"

  s.dependency "React-Core"
end
