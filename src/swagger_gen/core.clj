(ns swagger-gen.core
  (:require [clojure.walk :refer [keywordize-keys]]
            [cheshire.core :as json]
            [stencil.core :refer [render-string]]
            [swagger-gen.util :refer [normalize-def]]
            [yaml.core :as yml]))

(defn file-extension
  "Extract the file extension from a swagger spec or file"
  [spec]
  (-> (clojure.string/split spec #"\.") last keyword))

(defmulti load-swagger-file file-extension)

(defmethod load-swagger-file :json [spec]
  (->> spec slurp json/parse-string))

(defmethod load-swagger-file :yaml [spec]
  (yml/from-file spec false))

(defn extract-args
  [attributes]
  (let [properties (-> attributes :properties)
        required-attributes
        (set (->> attributes :required (map keyword)))]
  (into []
    (for [[property attrs] properties]
      {:name (name property)
       :type (normalize-def (or (:type attrs) (:$ref attrs)))
       :items (or (vals (:items attrs)) [])
       :required (contains? required-attributes property)}))))

(defn normalize-swagger-definition
  "Take a swagger definition and re-arrange the structure
   to make it more easily traversable in templates.
   We add a :name and :args property to each definition"
  [definition]
  (let [[class-name attributes] definition
         args (extract-args attributes)]
    (assoc attributes :name (name class-name)
                      :args args)))

(defn normalize-swagger-path [path]
  (let [[path args] ((juxt first rest) path)]
    (into {}
      (for [[method attributes] (first args)]
        (merge {:path path :method method} (keywordize-keys attributes))))))

(defn normalize-swagger-paths
  "Extract all HTTP request paths from a swagger spec
   and normalize them into a flat sequence for easier traversal"
  [paths]
  (into [] (flatten
             (for [[k v] paths]
               (for [[method attributes] v]
                 (merge {:method method :path k}
                        (keywordize-keys attributes)))))))

(defn keywordize-all-but-paths
  "Paths prevent an exeptional case where they may be in the form
   :/path as a keyword which won't parse correctly using Clojure's internal
   AST rules. This is a fundamental issue with YAML and Clojure keywords. 
   By treating it as a string we can avoid the issue by mapping it into a 
   nicer form {:path \"foo\"}"
  [m]
  (into {}
    (for [[k v] m]
      (if (= k "paths")
        {k v}
        (keywordize-keys {k v})))))

(defn normalize-swagger-spec
  "Attach normalized data that is easier to work with to the spec"
  [spec]
  (let [adjusted-spec (keywordize-all-but-paths spec)
        normalized-paths (normalize-swagger-paths (get spec "paths"))
        normalized-defs (map normalize-swagger-definition
                             (:definitions adjusted-spec))
        normalized-fields (assoc adjusted-spec
                            :adjusted-paths normalized-paths
                            :adjusted-definitions normalized-defs)]

    (dissoc normalized-fields "paths")))

(defn parse-swagger
  "Load a swagger specification from file path and convert it into
   a sane/traversable format making it easier to work with"
  [path-to-swagger]
    (-> (load-swagger-file path-to-swagger)
         (normalize-swagger-spec)))

(defn render-swagger
  "Render a swagger spec to a given template. 
   
   We can optionally pass in a transfomer function that takes a swagger spec
   and enriches or alters it with a custome function. This could be used
   to *merge* additional arguments 
   
   (fn [spec] (merge additional-params spec))
   
   (fn [spec] (assoc spec :foo "bar"))
   
   or transform a swagger spec by restructuring it
   in a way that is more acceptable for template rendering.
   
   (fn [spec] (transform spec))

   Examples:
   *************************************************************************
     1. Render direct (render-swagger spec template)

     2. Render with a function that transforms a swagger spec

       (render-swagger spec template 
         (fn [spec]
           (assoc spec :namespace \"foobar\")))"
  ([swagger-spec path-to-template transformer]
      (render-string (slurp path-to-template)
        (transformer
          (parse-swagger swagger-spec))))
  ([swagger-spec path-to-template]
    (render-swagger swagger-spec path-to-template identity)))
