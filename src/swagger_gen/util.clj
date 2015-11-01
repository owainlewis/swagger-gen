(ns swagger-gen.util
  (:refer-clojure :exclude [replace])
  (:require
    [clojure.string :refer
      [split replace replace-first lower-case capitalize join]]))

(defn interpose-map
  [f sep xs]
  (->> (map f xs) (interpose sep) (apply str)))

(defn unescape-html
    "Unescape HTML special entities in mustache templates"
    [text]
    (.. ^String text
      (replace "&amp;" "&")
      (replace "&lt;" "<")
      (replace "&gt;" ">")
      (replace "&quot;" "\"")))

(defn camelize
  "Convert snake_case string into CamelCase"
  [input-string]
  (let [words (split input-string #"[\s_-]+")]
    (join ""
      (cons (lower-case (first words))
        (map capitalize (rest words))))))

(defn capitalize-first-letter [s]
  (if (> (count s) 0)
    (str (Character/toUpperCase (.charAt s 0))
      (subs s 1))
    s))

(def upcase-camelize (comp capitalize-first-letter camelize))

(defn normalize-def
  "Normalize a definition like #/definitions/Card into Card"
  [type-ref]
  (replace-first type-ref #"#/definitions/" ""))

(defn seq-to-string
  ([xs]           (apply str xs))
  ([xs separator] (apply str (interpose separator xs))))

(defn quote-string [s] 
  (format "\"%s\"" s))
