(ns swagger-gen.examples.scala.generator
  (:require [swagger-gen.language.scala :as scala]
            [swagger-gen.core :refer [render-swagger parse-swagger]]))

(defn expand-def
  "Add some additional data here so we don't have to do any
   tricky logic in the template"
  [definition]
  {:klass (scala/render-case-class definition)
   :args (count (:properties definition))
   :name (:name definition)})

(defn -main
  "An example using custom rendering logic to generate model
   code in Scala for a standard Spray application"
  []
  (let [spec "resources/swagger/uber.yaml"
        template "src/swagger_gen/examples/scala/template.mustache"
        params { :package "com.google.service.models" }]
    (print
      (render-swagger spec template
        (fn [spec]
          (merge params
            {:definitions (->> (:definitions spec)
                          (map expand-def)
                          (sort-by :name))}))))))
