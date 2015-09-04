(ns swagger-gen.core
  (:require [clojure.walk :refer [keywordize-keys]]
            [cheshire.core :as json]
            [swagger-gen.util :refer [normalize-def]]
            [yaml.core :as yml]))

(defn- get-section
  [spec section]
  (get spec (name section)))

(defn normalize-definition
  "Take a raw YAML definition from swagger and normalize it
   to a form that is easier to work with and parse"
  [definition]
  (let [properties (->> definition :attributes :properties)
        required-attributes
          (set (map keyword
                 (->> definition :attributes :required)))]
    {:name (:name definition)
     :args (into []
             (for [[property attrs] properties]
               {:name (name property)
                :type (normalize-def (or (:type attrs) (:$ref attrs)))
                :items (or (vals (:items attrs)) [])
                :required (contains? required-attributes property)}))}))
  
(defn get-definitions [spec]
  (when-let [data (keywordize-keys (get-section spec :definitions))]
    (map (fn [definition]
           (let [[name attributes] definition]
             {:name name
              :attributes attributes})) data)))
    
(defn swagger-defs
  "Extract all swagger definitions/models from a spec"
  [spec]
  (when-let [definitions (get-definitions spec)]
    (map normalize-definition definitions)))

(defn swagger-paths
  "Extract all HTTP request paths from a swagger spec"
  [spec]
  (when-let [data (get-section spec :paths)]
    (into []
      (flatten
        (for [[k v] data]
          (for [[method attributes] v]
            (merge {:method method :path k}
                   (keywordize-keys attributes))))))))

(defn load-swagger [path-to-file]
  (yml/from-file path-to-file false))

(defn parse-swagger
  "Load a swagger specification from file path and convert it into 
   a sane/traversable format making it easier to work with"
  [path-to-swagger]
  (let [data (load-swagger path-to-swagger)]
    {:paths (swagger-paths data)
     :definitions (swagger-defs data)}))

