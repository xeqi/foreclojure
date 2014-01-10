(ns leiningen.new.foreclojure
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]
            [clj-http.client :as client]
            [clojure.edn :as edn]
            [clojure.pprint :as pp]))

(def render (renderer "foreclojure"))

(def url "http://www.4clojure.com/api/problem/")

(defn fetch-data [number]
  (try
    (:body (client/get
            (str url number)
            {:as :json}))
    (catch Exception e nil)))

(defn foreclojure
  "EX: `lein new foreclojure download 4`"
  [_ number]
  (if-let [data (fetch-data number)]
    (let [name (str "foreclojure" number)
          data (merge data
                      {:name name})]
      (main/info (str "Generating fresh 'lein new' 4clojure project from 4clojure problem #" number))
      (->files data
               ["project.clj" (render "project.clj" data)]
               ["README.md" (render "README.md" data)]
               ["test/{{name}}.clj" (render "test.clj" data)]))
    (main/info (str "Could not find 4clojure problem #" number))))
