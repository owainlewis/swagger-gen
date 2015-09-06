(ns swagger-gen.main
  (:require [swagger-gen.rendering.scala :as scala]
            [clostache.parser :refer [render-resource]]
            [swagger-gen.core :as core]))

(def spec "resources/swagger/petstore.yaml")

;; TODO use functions here from clostache

(defn generate-scala-models [spec]
  (let [swagger-definitions (:definitions (core/parse-swagger spec))]
    (map #(assoc % :repr (scala/render-case-class %)
                   :count (count (:args %)))
         swagger-definitions)))

(defn generate-scala-routes [spec]
  (render-resource "templates/scala/routes.mustache"
    (core/parse-swagger spec)))

(defn code-generate [spec]
  (let [models (generate-scala-models spec)]
    (render-resource "templates/scala/models.mustache"
      (core/parse-swagger spec))))

(defn -main []
  (println "Generating code...")
  (spit "Models.scala" (code-generate spec))
  )
