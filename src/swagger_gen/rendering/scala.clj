(ns swagger-gen.rendering.scala
  (:require [swagger-gen.util :refer [camelize normalize-def]]))

;; Rendering logic for Scala projects

(defn resolve-array-type
  [items]
  (let [type (first items)]
    (format "Seq[%s]" (normalize-def type))))

(defn format-type [type items]
  (condp = type
    "string" "String"
    "array"  (resolve-array-type items)
    "integer" "Int"
    type))

(defn render-arg [arg]
  (let [[name type items] ((juxt :name :type :items) arg)]
    (format "%s: %s"
            (camelize name)
            (format-type type items))))
  
(defn render-case-class
  [definition]
  (let [[name args] ((juxt :name :args) definition)]
    (if (zero? (count args))
      (format "case object %s" name)
      (let [arguments (->> (map render-arg args)
                           (interpose ", ")
                           (apply str))]
      (format "case class %s(%s)" name  arguments)))))

