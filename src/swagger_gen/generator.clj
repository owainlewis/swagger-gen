(ns swagger-gen.generator
  (:require 
    [stencil.core :refer [render-string]]
    [swagger-gen.core :as core]))

(defn render-swagger
  "The generator takes a spec and a template and turns it into real code
   
   We can optionally pass in a transfomer function that takes a swagger spec
   and enriches or alters it with a customer function

   For example

   (render-swagger spec template 
     (fn [spec]
       (assoc :spec :foo :bar)))

   Any template being rendered will have access to the new :foo :bar key value pair
   "
  ([swagger-spec path-to-template transformer]
      (render-string (slurp path-to-template)
        (transformer
          (core/parse-swagger swagger-spec))))
  ([swagger-spec path-to-template]
   (render-swagger swagger-spec path-to-template identity)))

