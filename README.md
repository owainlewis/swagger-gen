# swagger-gen

[![Build Status](https://travis-ci.org/owainlewis/swagger-gen.svg)](https://travis-ci.org/owainlewis/swagger-gen)

A library for easy swagger code generation

## Goals

1. Lightweight and simple DSL for traversing Swagger Specs
2. Clean and simple code gen based on templates
3. More complex code generation through Clojure functions

## Usage

```clojure

(ns example.core
  (:require [swagger-gen.generator :refer :all]))




```

And an example template

```mustache
package com.monitise.mcp.service.models

import spray.json.DefaultJsonProtocol

{{#definitions}}

{{repr}}

object {{name}} extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat1({{name}}.apply)
}
{{/definitions}}

```

### Parsing specs

```clojure

(ns example
  (:require [swagger-gen.core :refer :all]))

(def petstore-routes
  ((juxt :method :path)
    (first
      (swagger-paths
        (load-swagger-file "resources/swagger/petstore.yaml")))))
	     
;; => ["get" "/pets"]

```

### Custom rendering

An example renderer for Scala case classes used with Spray json

```clojure
(ns swagger-gen.rendering.scala
  (:require [swagger-gen.util :refer [camelize normalize-def]]))

;; Rendering logic for Scala projects using Spray json

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
             (let [arguments (->> (map render-arg args) (interpose ", ")
                                  (apply str))]
    (format "case class %s(%s)" name  arguments)))))

```

### Code Generation

Code generation is done through templates.


```clojure
(ns swagger-gen.main
  (:require [swagger-gen.rendering.scala :as scala]
            [swagger-gen.generator :refer [render-swagger]])) 

(def petstore-yaml "resources/swagger/petstore.yaml")

(defn scala-models-from-swagger
  "Generates scala models with a custom case class generation function" 
  []
  (render-swagger petstore-yaml "templates/scala/models.mustache"
    (fn [spec]
      (assoc spec :definitions 
             (map #(assoc % :repr (scala/render-case-class %))
                  (:definitions spec))))))

(defn -main []
  (println (scala-models-from-swagger)))

```

## License

Copyright © 2015 Owain Lewis

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
