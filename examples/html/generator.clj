(ns swagger-gen.examples.html.generator
  (:require [swagger-gen.core :refer [render-swagger]]))

(def template "src/swagger_gen/examples/html/template.mustache")

(def spec "resources/swagger/petstore.yaml")
           
(defn -main [] (print (render-swagger spec template)))
