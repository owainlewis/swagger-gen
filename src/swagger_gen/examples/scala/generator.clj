(ns swagger-gen.examples.scala.generator
  (:require [swagger-gen.spray :as spray]
            [swagger-gen.generator :refer [render-swagger]]))

(defn expand-model
  "Add some additional data here so we don't have to do any 
   tricky logic in the template"
  [model]
  (assoc model :class   (spray/render-case-class model)
               :arglen  (count (:args model))))
           
(defn -main
  "An example using custom rendering logic to generate model
   code in Scala for a standard Spray application"
  []
  (let [spec "resources/swagger/petstore.yaml"
        template "src/swagger_gen/examples/scala/template.mustache"
        additional-params { :namespace "com.google.service.models" }]
    (print
      (render-swagger spec template
                    (fn [spec]
                      (merge additional-params
                             (assoc spec :normalized-definitions
                                    (map expand-model (:normalized-definitions spec)))))))))
  
