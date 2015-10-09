(ns swagger-gen.language.scala
  (:require
   [clojure.string   :refer [capitalize]]
   [swagger-gen.util :refer [camelize normalize-def quote-string]]))

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

(defn type-as-scala [swagger-type]
  (condp = swagger-type
    "boolean"   "Boolean"
    "string"    "String"
    "integer"   "Int"
    "number"    "Double"
    swagger-type))

(defn gen-seq
  [attributes]
  (let [seq-type (normalize-def (or (:$ref attributes)
                                    (type-as-scala (get-in attributes [:items :type]))
                                    (get-in attributes [:items :$ref])))]
    (format "Seq[%s]" seq-type)))

(defn optional? [is-required scala-type]
  (if is-required
    (identity scala-type)
    (format "Option[%s] = None" scala-type)))

(defn raw-type
  "Mapping of swagger types to Scala types"
  [attributes]
  (condp = (:type attributes)
    "boolean"   "Boolean"
    "string"    "String"
    "array"     (gen-seq attributes)
    "integer"   "Int"
    "number"    "Double"
    (or (:type attributes)
        (normalize-def (:$ref attributes)))))

(defn enum-type-string [definition-name property-name]
  (format "%s%sEnum.Type"
          (capitalize (name definition-name))
          (capitalize (name property-name))))

(defn scala-type
  "Convert to a Scala type"
  [definition-name is-required property-name attributes]
  (if (not (nil? (:enum attributes)))
    (optional? is-required (enum-type-string definition-name property-name))
    (optional? is-required (raw-type attributes))))

(defn render-property
  [definition-name required-properties prop]
  (let [[property-name attributes] prop
        required (contains? required-properties (name property-name))]
    (format "%s: %s"
            (camelize (name property-name))
            (scala-type definition-name required property-name attributes))))

(defn render-case-class
  [definition use-case-objects]
  (let [[klass props required] ((juxt :name :properties :required) definition)
        required-properties (set required)
        definition-name (:name definition)
        args (->> (map #(render-property definition-name required-properties %) props)
                  (interpose ", ")
                  (apply str))]
    (if (and use-case-objects (zero? (count args)))
      (format "case object %s" klass)
      (format "case class %s(%s)" klass args))))
