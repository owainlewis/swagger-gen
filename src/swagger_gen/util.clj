(ns swagger-gen.util
  (:refer-clojure :exclude [replace])
  (:require
    [clojure.string :refer
      [split replace replace-first lower-case capitalize join]]))

(defn interpose-map [f sep xs] (->> (map f xs) (interpose sep) (apply str)))

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

(defn params-of-type
  "Extract swagger params of a given type i.e :body or :path"
  [swagger-route param-type]
  (->> (:parameters swagger-route)
       (filter #(= (:in %) param-type))
       (into [])))

(defn body-params
  "Extract one or more body params from a swagger path"
  [swagger-route]
  (params-of-type swagger-route "body"))

(defn path-params
  "Extract one or more path params from a swagger path"
  [swagger-route]
  (params-of-type swagger-route "path"))

(defn query-params
  "Extract one or more query params from a swagger path"
  [swagger-route]
  (params-of-type swagger-route "query"))
