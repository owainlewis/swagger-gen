(ns swagger-gen.generator
  (:require 
    [swagger-gen.core :as core]
    [clojure.tools.cli :refer [cli]])
  (:gen-class))

(defn dispatch
  [spec template destination]
  (println (format "Rending template spec %s to %s" spec template))
  (try 
    (spit destination (core/render-swagger spec template))
    (catch Exception e
      (print (str "Error: "
                  (.getMessage e))))))

(defn -main [& args]
  (let [[opts args banner]
        (cli args
          ["-s" "--spec"        "The spec path"]
          ["-t" "--template"    "The template path"]
          ["-d" "--destination" "The destination path"])]
    (println "Swagger Gen")
    (let [[spec template dest] ((juxt :spec :template :destination) opts)
          args [spec template dest]]
      (if (every? (complement nil?) args)
        (apply dispatch args)
        (println "Missing required arguments")))))
