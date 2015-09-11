(ns swagger-gen.generator
  (:require 
    [stencil.core :refer [render-string]]
    [swagger-gen.core :as core]
    [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(defn render-swagger
  "The generator takes a spec and a template and turns it into real code
   
   We can optionally pass in a transfomer function that takes a swagger spec
   and enriches or alters it with a customer function

   For example

   (render-swagger spec template 
     (fn [spec]
       (assoc spec :paths {:foo 1})))

   Any template being rendered will have access to the new :foo :bar key value pair"
  ([swagger-spec path-to-template transformer]
      (render-string (slurp path-to-template)
        (transformer
          (core/parse-swagger swagger-spec))))
  ([swagger-spec path-to-template]
   (render-swagger swagger-spec path-to-template identity)))

;; CLI
;; ****************************************************************************

(def cli-options
  ;; An option with a required argument
  [["-s" "--spec SPEC" "The spec path"]
   ["-t" "--template TEMPLATE" "The template path"]
   ["-d" "--destination DESTINATION" "The destination path"]])

(defn dispatch [spec template destination]
  (println (format "Rending template spec %s to %s" spec template))
  (spit destination (render-swagger spec template)))

(defn -main [& args]
  (let [args (parse-opts args cli-options)]
    (let [spec        (get-in args [:options :spec])
          template    (get-in args [:options :template])
          destination (get-in args [:options :destination])]
    (dispatch spec template destination))))
