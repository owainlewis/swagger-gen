(ns swagger-gen.examples.spray.spray
  (:require [swagger-gen.generator :refer :all]
            [swagger-gen.util :refer [camelize normalize-def]]))

;; Rendering logic for Scala projects using Spray json
;;
;; *******************************************************************

(defn resolve-array-type
  [items]
  (let [type (first items)]
    (format "Seq[%s]" (normalize-def type))))

(defn format-type [type items]
  (condp = type
    "string"    "String"
    "array"     (resolve-array-type items)
    "integer"   "Int"
    "integer32" "Int"
    type))

(defn render-arg [arg]
  (let [[name type items] ((juxt :name :type :items) arg)]
    (format "%s: %s"
            (camelize name)
            (format-type type items))))

(defn render-case-class
  "Take a swagger model definition and turns it into a Scala case class or case object 
   depending on arity"
  [definition]
  (let [[name args] ((juxt :name :args) definition)]
    (if (zero? (count args))
    (format "case object %s" name)
             (let [arguments (->> (map render-arg args) (interpose ", ")
                                  (apply str))]
               (format "case class %s(%s)" name  arguments)))))

(defn to-spray-route [path]
  "")

;; Rendering
;; *******************************************************************

(def spec "resources/swagger/petstore.yaml")
(def cardstorage "/home/vagrant/workspace/cardstorage/cardstorage-sync-acceptor/cardstorage-sync-acceptor-spec/src/main/resources/cardstorage-v2.yaml")

(def model-template "resources/templates/spray/models.mustache")
(def routes-template "resources/templates/spray/routes.mustache")

(defn models []
  (render-swagger cardstorage model-template
    (fn [spec]                  
      (assoc spec :definitions 
             (map #(assoc % :class  (render-case-class %)
                            :arglen (count (:args %)))
                  (:definitions spec))))))

(defn routes []
  (render-swagger cardstorage routes-template
                  (fn [spec]
                    (assoc spec :paths
                           (map identity (:paths spec))))))

                   
