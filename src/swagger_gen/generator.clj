(ns swagger-gen.generator
  (:require 
    [swagger-gen.core :as core]
    [clojure.tools.cli :refer [cli]])
  (:gen-class))

(defn dispatch [spec template destination]
  (println (format "Rending template spec %s to %s" spec template))
  (spit destination (core/render-swagger spec template)))

(defn -main [& args]
  (let [[opts args banner]
        (cli args
          ["-s" "--spec"        "The spec path"]
          ["-t" "--template"    "The template path"]
          ["-d" "--destination" "The destination path"])]
    (println "Swagger Gen")
    (println opts)))
