#/bin/bash
java -jar test/JsTestDriver.jar --port 9876 --browser /usr/bin/firefox --tests all
java -jar test/JsTestDriver.jar --port 9876 --browser /opt/google/chrome/google-chrome --tests all
# TODO run tests for ie and safari
