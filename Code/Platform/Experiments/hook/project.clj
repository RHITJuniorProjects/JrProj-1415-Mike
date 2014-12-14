(defproject hook "0.1.0-SNAPSHOT"
  :description "Firebase-Clojure wrapper for use with Henry"
  :url "https://github.com/RHITJuniorProjects"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot hook.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
