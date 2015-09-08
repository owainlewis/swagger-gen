(ns swagger-gen.spray
  (:require [clojure.string :refer [blank? replace split]]
            [swagger-gen.util :refer [camelize normalize-def]]))

;; Model generation code

(defn resolve-array-type
  [items]
  (let [type (first items)]
    (format "Seq[%s]" (normalize-def type))))

(defn format-type [type items]
  (condp = type
    "string"    "String"
    "array"     (resolve-array-type items)
    "integer"   "Int"
    type))

(defn render-arg [arg]
  (let [[name type items] ((juxt :name :type :items) arg)]
    (format "%s: %s"
            (camelize name)
            (format-type type items))))

(defn render-case-class
  "Take a swagger model definition and turns it into a Scala case class 
   or case object depending on arity"
  [definition]
  (let [[name args] ((juxt :name :args) definition)]
    (if (zero? (count args))
    (format "case object %s" name)
             (let [arguments (->> (map render-arg args) (interpose ", ")
                                  (apply str))]
               (format "case class %s(%s)" name  arguments)))))

;; Route generation

(defn starts-and-ends-with? [input starts ends]
  (and (.startsWith input starts)
       (.endsWith input ends)))

(defn route-arg? [segment]
  (starts-and-ends-with? segment "{" "}"))
  
(defn route-parts [path]
  (filter (complement blank?) (split path #"\/")))

(defn normalize-route-arg [arg]
  (replace arg #"[{}]" ""))

(defn to-spray-route
  "Generate the full spray route for spray i.e 
   foo / bar / Segment"
  [path]
  (apply str
    (interpose " / "
      (reduce (fn [acc v]
                (if (route-arg? v)
                  (conj acc "Segment")
                  (conj acc v))) [] (route-parts path)))))

(defn deconstruct-spray-path [path]
  (let [route (to-spray-route path)
        args (filter route-arg? (route-parts path))]
    {:route route :args (mapv normalize-route-arg args)}))

