(ns swagger-gen.core
  (:require [clojure.walk :refer [keywordize-keys]]
            [cheshire.core :as json]
            [swagger-gen.util :refer [normalize-def]]
            [yaml.core :as yml]))
          
;; Code for processing swagger specs and normalizing into more workable forms
;;
;; By default it will reorder the paths data into a single array of paths rather than the nested structure 
;; imposed by swagger specs
;;
;; This makes processing the swagger path data much easier
;;
;; TODO decide whether we need to "mess" with the spec at this point or whether we just pass through

(defn file-extension
  "Extract the file extension from a swagger spec or file"
  [spec]
  (->> (clojure.string/split spec #"\.")
       last
       keyword))

(defmulti load-swagger-file file-extension)

(defmethod load-swagger-file :json [spec]
  (->> spec slurp json/parse-string))

(defmethod load-swagger-file :yaml [spec]
  (yml/from-file spec false))

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

;; **************************************************************

(defn normalize-definition
  "Take a raw YAML definition from swagger and normalize it
   to a form that is easier to work with and parse"
  [definition]
  (let [properties (->> definition :attributes :properties)
        required-attributes
          (set (map keyword
                    (->> definition :attributes :required)))]
    (merge definition 
      {:name (:name definition)
       :args (into []
               (for [[property attrs] properties]
                 {:name (name property)
                  :type (normalize-def (or (:type attrs) (:$ref attrs)))
                  :items (or (vals (:items attrs)) [])
                  :required (contains? required-attributes property)}))})))

(defn get-definitions [data]
  (map (fn [definition]
         (let [[class-name attributes] definition]
           {:name (name class-name)
            :attributes attributes})) data))

(defn normalize-swagger-definitions
  "Extract all swagger definitions/models from a spec"
  [spec]
  (->> spec (get-definitions) (map normalize-definition)))

(defn normalize-swagger-paths
  "Extract all HTTP request paths from a swagger spec
   and normalize them into a flat sequence for easier traversal"
  [data]
  (into [] (flatten
             (for [[k v] data]
               (for [[method attributes] v]
                 (merge {:method method :path k}
                        (keywordize-keys attributes)))))))

(defn keywordize-all-but-paths
  "Paths prevent an exeptional case where they may be in the form
   :/path as a keyword which won't parse correctly using Clojure's internal
   AST rules"
  [m]
  (into {}
    (for [[k v] m]
      (if (= k "paths")
        {k v}
        (keywordize-keys {k v})))))

;; **************************************************************

(defn normalize-swagger-spec
  "Attach normalized data that is easier to work with to the spec"
  [spec]
  (let [adjusted-spec (keywordize-all-but-paths spec)
        with-normalized-fields
          (assoc adjusted-spec  
            :normalized-paths (normalize-swagger-paths (get spec "paths"))
            :normalized-definitions (normalize-swagger-definitions (:definitions adjusted-spec)))]
    (dissoc with-normalized-fields "paths")))

(defn parse-swagger
  "Load a swagger specification from file path and convert it into
   a sane/traversable format making it easier to work with"
  [path-to-swagger]
    (->> (load-swagger-file path-to-swagger)
         (normalize-swagger-spec)))
