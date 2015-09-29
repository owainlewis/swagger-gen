(ns swagger-gen.language.spray
  (:refer-clojure :exclude [replace])
  (:require
    [clojure.string :refer [blank? replace split]]
    [swagger-gen.language.scala :as lang-scala]
    [swagger-gen.util :refer [camelize normalize-def quote-string]]))

;; Spray.io code

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

(defn parenthesize [args]
  (condp = (count args)
    0 ""
    1 (first args)
    (str "(" (apply str (interpose ", " args)) ")")))

(defn deconstruct-spray-path [path]
  (let [route (to-spray-route path)
        args (filter route-arg? (route-parts path))]
    {:route route
     :args (parenthesize (mapv normalize-route-arg args))}))
