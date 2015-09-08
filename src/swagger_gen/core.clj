(ns swagger-gen.core
  (:require [clojure.walk :refer [keywordize-keys]]
            [cheshire.core :as json]
            [swagger-gen.util :refer [normalize-def]]
            [yaml.core :as yml]))

(defn file-extension
  "Extract the file extension from a swagger spec or file"
  [spec]
  (->> (clojure.string/split spec #"\.") last keyword))

(defmulti load-swagger-file file-extension)

(defmethod load-swagger-file :json [spec]
  (->> spec slurp json/parse-string))

(defmethod load-swagger-file :yaml [spec]
  (yml/from-file spec false))

(defn- get-section
  "Util function for extracting swagger spec sections
    e.g (get-section :paths)"
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
           (let [[class-name attributes] definition]
             {:name (name class-name)
              :attributes attributes})) data)))

(defn swagger-defs
  "Extract all swagger definitions/models from a spec"
  [spec]
  (when-let [definitions (get-definitions spec)]
    (map normalize-definition definitions)))

(defn params-of-type 
  "Extract swagger params of a given type i.e :body or :path"
  [swagger-route param-type]
  (->> swagger-route 
       :parameters 
       (filter #(= (:in %) param-type))
       (into [])))

(defn body-params 
  "Extract one or more body params from a sswagger path"
  [swagger-route]
  (params-of-type swagger-route "body"))

(defn query-params
  "Extract one or more query params from a swagger path"
  [swagger-route]
  (params-of-type swagger-route "query"))

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

(defn swagger-info [spec] (get-section spec :info))
(defn swagger-apis [spec] (get-section spec :apis))

(defn parse-swagger
  "Load a swagger specification from file path and convert it into
   a sane/traversable format making it easier to work with"
  [path-to-swagger]
  (let [data (load-swagger-file path-to-swagger)]
    {:info  (swagger-info data)
     :paths (swagger-paths data)
     :apis (swagger-apis data)
     :definitions (swagger-defs data)}))
