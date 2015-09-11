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

(defn params-of-type
  "Extract swagger params of a given type i.e :body or :path"
  [swagger-route param-type]
  (->> swagger-route
       :parameters
       (filter #(= (:in %) param-type))
       (into [])))

(defn body-params
  "Extract one or more body params from a swagger path"
  [swagger-route]
  (params-of-type swagger-route "body"))

(defn query-params
  "Extract one or more query params from a swagger path"
  [swagger-route]
  (params-of-type swagger-route "query"))
