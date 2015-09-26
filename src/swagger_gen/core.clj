(ns swagger-gen.core
  (:require [clojure.walk :refer [keywordize-keys]]
            [stencil.core :as stencil]
            [swagger-gen.util :refer [normalize-def]]
            [yaml.core :as yml]))

(defn load-swagger-file
  "Reads a swagger.yaml file into a Clojure data structure with string keys"
  [path-to-spec]
  (yml/from-file path-to-spec false))

;; Do a minimal transformation on Swagger data structure to make it easier to traverse

(defn transform-paths
  "Flattens swagger paths into a more sensible data structure. Returns a vector of all swagger paths
   in a normalized form"
  [paths]
  (flatten (into []
    (for [[path info] paths]
      (for [[method attributes] info]
        (merge attributes {"path" path "method" method}))))))
      
(defn transform-definitions
  "Flattens swagger definitions into a more sensible data structure. Returns a vector of all 
   swagger definitions in normalized form"
  [definitions]
  (into []
    (for [[k v] definitions]
      (let [required (set (:required v))]
       (merge v {"name" k})))))

(defn params-of-type
  "Extract swagger params of a given type i.e :body or :path"
  [path param-type]
  (->> (:parameters path) (filter #(= (:in %) param-type)) (into [])))

(defn body-params
  "Extract one or more body params from a swagger path"
  [path]
  (params-of-type path "body"))

(defn path-params
  "Extract one or more path params from a swagger path"
  [path]
  (params-of-type path "path"))

(defn query-params
  "Extract one or more query params from a swagger path"
  [path]
  (params-of-type path "query"))

(defn normalize-swagger-spec
  "Attach normalized data that is easier to work with to the spec"
  [spec]
  (let [paths (get spec "paths")
        definitions (get spec "definitions")]
    (assoc spec "definitions" (transform-definitions definitions)
                "paths" (transform-paths paths))))

(defn parse-swagger
  "Load a swagger specification from file path and convert it into
   a sane/traversable format making it easier to work with"
  [path-to-swagger]
    (-> (load-swagger-file path-to-swagger)
        (normalize-swagger-spec)
        (keywordize-keys)))

(def render-template-string stencil/render-string)

(defn render-swagger
  "Render a swagger spec to a given template.

   We can optionally pass in a transfomer function that takes a swagger spec
   and enriches or alters it with a custom function. This could be used
   to *merge* additional arguments

   (fn [spec] (merge additional-params spec))

   (fn [spec] (assoc spec :foo \"bar\"))

   or *transform* a swagger spec by restructuring it
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
      (stencil/render-string (slurp path-to-template)
        (transformer
          (parse-swagger swagger-spec))))
  ([swagger-spec path-to-template]
    (render-swagger swagger-spec path-to-template identity)))
