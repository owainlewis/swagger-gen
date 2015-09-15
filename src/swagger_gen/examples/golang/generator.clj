(ns swagger-gen.examples.golang.generator
  (:require [swagger-gen.core :refer [render-swagger]]))

(defn extract-route-param-names [normalized-path]
  (->> normalized-path
       :parameters
       (filter #(= (:in %) "path"))
       (map :name)))

(defn path-to-go-route [normalized-path]
  (let [extractor (juxt :path :operationId :method :summary)
        [path operation method doc] (extractor normalized-path)]
  {:path      path
   :method    (.toUpperCase method)
   :operation operation
   :params    (extract-route-param-names normalized-path)
   :doc       doc }))

(def spec "resources/swagger/petstore.yaml")

(defn generate []
  (let [template "src/swagger_gen/examples/golang/template.mustache"]
    (render-swagger spec template
      (fn [spec]
        {:routes (map path-to-go-route (:adjusted-paths spec))}))))

(defn -main [] (print (generate)))
