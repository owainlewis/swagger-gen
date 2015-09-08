(ns swagger-gen.util
  (:require 
    [clojure.string :refer [split replace-first lower-case capitalize join]]))

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
  (replace-first type-ref #"#/definitions/" ""))

(defn seq-to-string
  ([xs]           (apply str xs))
  ([xs separator] (apply str (interpose separator xs))))

(defn quote-string
  [s]
  (format "\"%s\"" s))
