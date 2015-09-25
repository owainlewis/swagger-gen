(ns swagger-gen.examples.java.generator
  (:require [swagger-gen.core :refer [render-template-string
                                      parse-swagger render-swagger]]))

(defn java-type [t]
  (condp = t
    "string" "String"
    "int32"  "Integer"
    t))

(defn to-java [model]
  (let [props (:properties model)]
   {:name (:name model)
    :properties
    (into []
    (map (fn [[k v]]
      {:name (name k)
       :type (java-type (:type v))}) props))}))

(defn -main
  "An example using custom rendering logic to generate model
   code in Scala for a standard Spray application"
  []
  (let [spec "resources/swagger/petstore.yaml"
        template (slurp "src/swagger_gen/examples/java/template.mustache")
        additional-params { :namespace "com.google.service.models" }
        data (parse-swagger spec)]
    (doseq [model (:models data)]
      (spit (str "src/swagger_gen/examples/java/models/" (:name model) ".java")
        (render-template-string template (to-java model))))))
