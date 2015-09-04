(ns swagger-gen.main
  (:require [swagger-gen.rendering.scala :as scala]
            [clostache.parser :refer [render-resource]]
            [swagger-gen.core :as core]))

(def spec "resources/swagger/petstore.yaml")

(defn generate-scala-models [spec]
  (let [swagger-definitions (:definitions (core/parse-swagger spec))]
    (map #(assoc % :repr (scala/render-case-class %)
                   :count (count (:args %)))
         swagger-definitions)))

(defn code-generate [spec]
  (let [models (generate-scala-models spec)]
    (render-resource "templates/scala/models.mustache"
      {:models models})))

(defn -main []
  (println "Generating code...")
  (spit "Models.scala" (code-generate spec))
  )
