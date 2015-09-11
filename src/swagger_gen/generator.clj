(ns swagger-gen.generator
  (:require 
    [swagger-gen.core :as core]
    [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

;; CLI
;; ****************************************************************************

(def cli-options
  ;; An option with a required argument
  [["-s" "--spec SPEC" "The spec path"]
   ["-t" "--template TEMPLATE" "The template path"]
   ["-d" "--destination DESTINATION" "The destination path"]])

(defn dispatch [spec template destination]
  (println (format "Rending template spec %s to %s" spec template))
  (spit destination (core/render-swagger spec template)))

(defn -main [& args]
  (let [args (parse-opts args cli-options)]
    (let [spec        (get-in args [:options :spec])
          template    (get-in args [:options :template])
          destination (get-in args [:options :destination])]
    (dispatch spec template destination))))
