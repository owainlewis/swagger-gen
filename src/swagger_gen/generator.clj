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
       (assoc :spec :foo :bar)))

   Any template being rendered will have access to the new :foo :bar key value pair
   "
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
  [["-t" "--template TEMPLATE" "The template path"]
   ["-s" "--spec SPEC" "The spec path"]
   ["-d" "--destination DESTINATION" "The destination path"]])

(defn -main [& args]
  (let [args (parse-opts args cli-options)]
    (let [template (get-in args [:options :template])
          spec (get-in args [:options :spec])
          destination (get-in args [:options :destination])]
      ;; TODO error handling on missing args
      (do
        (println "Generating swagger output from spec")
        (spit destination (render-swagger spec template))))))

