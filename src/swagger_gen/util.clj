(ns swagger-gen.util
  (:require 
    [clojure.string :refer [split lower-case capitalize join]]))

(defn camelize
  "Convert snake_case string into CamelCase"
  [input-string]
  (let [words (split input-string #"[\s_-]+")]
    (join ""
          (cons (lower-case (first words))
                (map capitalize (rest words)))))) 

(defn normalize-def
  "Normalize a definition like #/definitions/Card into Card"
  [type-ref]
  (clojure.string/replace-first type-ref #"#/definitions/" ""))
