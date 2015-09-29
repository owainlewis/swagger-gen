(ns swagger-gen.language.scala
  (:require
   [swagger-gen.core :as core]
   [swagger-gen.util :refer [camelize normalize-def]]
   [swagger-gen.util :refer [quote-string]]))

(defn to-cons-list
  "Generate a scala list from a sequence of values"
  [xs]
  (let [quoted-strings (mapv quote-string xs)]
        (->> (conj quoted-strings "Nil")
             (interpose " :: ")
             (apply str))))

(defn to-seq
  "Genrate a scala sequence such as List(foo, bar) or Vector(1,2)"
  [seq-type xs]
  (format "%s(%s)"
    seq-type           
    (->> xs (interpose ", ") (apply str))))

;; ***********************************************************
;; Case class generation
;; ***********************************************************

(defn gen-seq
  [attributes]
  (format "Seq[%s]"
    (normalize-def       
      (or (:$ref attributes) (get-in attributes [:items :$ref])))))

(defn scala-type [attributes]
  (condp = (:type attributes)
    "boolean"   "Boolean"
    "string"    "String"
    "array"     (gen-seq attributes)
    "integer"   "Int"
    "number"    "Double"
    (:type attributes)))

(defn render-property
  [prop]
  (let [[property-name attributes] prop]
    (format "%s: %s" (swagger-gen.util/camelize (name property-name))
                     (scala-type attributes))))

(defn render-case-class
  "Take a swagger model definition and turns it into a Scala case class 
   or case object depending on arity"
  [definition]
  (let [[klass props] ((juxt :name :properties) definition)]
    (if ((comp zero? count) props)
      (format "case object %s" klass)
      (let [args (->> (map render-property props) (interpose ", ") (apply str))]
        (format "case class %s(%s)" klass args)))))
