(ns swagger-gen.examples.scala.generator
  (:require [swagger-gen.language.spray :as spray]
            [swagger-gen.core :refer [render-swagger]]))

(defn expand-model
  "Add some additional data here so we don't have to do any 
   tricky logic in the template"
  [model]
  {:class (spray/render-case-class model)
   :args (count (:args model))
   :name (:name model)})
           
(defn -main
  "An example using custom rendering logic to generate model
   code in Scala for a standard Spray application"
  []
  (let [spec "resources/swagger/petstore.yaml"
        template "src/swagger_gen/examples/scala/template.mustache"
        additional-params { :namespace "com.google.service.models" }]
    (render-swagger spec template
      (fn [spec]
        (merge additional-params
          {:models   
            (map expand-model (:normalized-definitions spec))})))))
