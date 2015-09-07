(ns swagger-gen.generator
  (:require 
    [stencil.core :refer [render-string]]
    [swagger-gen.core :as core]))

;; The generator takes a spec and a template and turns it into
;; real code

(def spec "resources/swagger/petstore.yaml")

(def template "resources/templates/scala/models.mustache")

(defn render-swagger
  ([swagger-spec path-to-template transformer]
      (render-string (slurp path-to-template)
        (transformer
          (core/parse-swagger swagger-spec))))
  ([swagger-spec path-to-template]
   (render-swagger swagger-spec path-to-template identity)))

