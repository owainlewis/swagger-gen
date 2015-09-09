(ns swagger-gen.language.scala
  (:require
   [swagger-gen.util :refer [quote-string]]))

(defn to-cons-list
  "Generate a scala list from a sequence of values"
  [xs]
  (let [quoted-strings (mapv quote-string xs)]
        (->> (conj quoted-strings "Nil")
             (interpose " :: ")
             (apply str))))

(defn to-seq-type
  "Genrate a scala sequence such as List(foo, bar) or Vector(1,2)"
  [seq-type xs]
  (format "%s(%s)"
    seq-type           
    (->> xs (interpose ", ") (apply str))))

(def scala-array  (partial to-seq-type "Array"))
(def scala-vector (partial to-seq-type "Vector"))
(def scala-list   (partial to-seq-type "List"))

(defn to-sequence-type
  [seq-kind t]
  (format "%s[%s]" seq-kind t))

(def as-list   (partial to-sequence-type "List"))
(def as-seq    (partial to-sequence-type "Seq"))
(def as-vector (partial to-sequence-type "Vector"))
