(ns hook.core
  (:require [clojure.string :as string]
            [clj-http.client :as client]
            [cheshire.core :as json])
  (:gen-class))

(defn base_url "https://henry-test.firebaseio.com")

(defn request [method base_url path data [options]]
  (let
    [request-options
      (reduce recursive-merge
        [{:query-params {:pretty-print true}}
          (when (not (nil? data)) {:body (json/generate-string data)})
          options])
      url base_url]
    (-> (method url request-options {:as :json}) :body json/decode)))

(defn write! [path data & [options]]
  (request client/put base_url path data options))

(defn update! [path data & [options]]
  (request client/patch base_url path data options))

(defn push! [path data & [options]]
  (request client/post base_url path data options))

(defn remove! [path & [options]]
  (request client/delete base_url path nil options))

(defn read [path & [query-params options]]
  (request client/get base_url path nil (merge {:query-params (or query-params {})} options)))

(defn -main
  [& args]
  (println "This should be used as a library, not a main class."))
