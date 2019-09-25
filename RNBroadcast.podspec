require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = "RNBroadcast"
  s.version      = package['version']
  s.summary      = package['description']
  s.license      = package['license']

  s.authors      = package['author']
  s.homepage     = package['homepage']
  s.platform     = :ios, "9.0"

  s.source       = { :git => "https://github.com/BehaviorCloud/react-native-broadcast.git", :tag => "#{s.version}" }
  s.source_files  = "ios/RNBroadcast/**/*.{c,h,m,mm}"

  s.dependency 'React'
end
