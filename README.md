# swagger-gen

[![Build Status](https://travis-ci.org/owainlewis/swagger-gen.svg)](https://travis-ci.org/owainlewis/swagger-gen)

A library for easy swagger code generation

## Goals

1. Lightweight and simple DSL for traversing Swagger Specs
2. Clean and simple code gen based on templates
3. More complex code generation through Clojure functions

## Usage

There are two ways to use the swagger generator. If your use case is simple enough you can use 
the CLI and a single mustache template to generate code from a swagger spec.

This example generates HTML docs from the canonical petstore example.

There are three arguments to the generator

+ -t The path to your mustache template
+ -s The path to your swagger spec
+ -d The destination to dump the results to

```
lein run -m swagger-gen.generator -t src/swagger_gen/examples/html/template.mustache -s resources/swagger/petstore.yaml -d service-docs.html
```

For more complex code generation tasks we can write custom functions to assist with the generation.

The only real "logic" here is adding some additional data to the swagger structure to make rendering in the templates
easier. For example in the example below we have a custom function that renders a swagger path as a scala
case class string.

```clojure
(ns swagger-gen.examples.scala.generator
  (:require [swagger-gen.spray :as spray]
            [swagger-gen.generator :refer [render-swagger]]))

(defn expand-model
  "Add some additional data here so we don't have to do any 
   tricky logic in the template"
  [model]
  (assoc model :class   (spray/render-case-class model)
               :arglen  (count (:args model))))
           
(defn -main
  "An example using custom rendering logic to generate model
   code in Scala for a standard Spray application"
  []
  (let [spec "resources/swagger/petstore.yaml"
        template "src/swagger_gen/examples/scala/template.mustache"
        additional-params {
          :namespace "com.google.service.models"
                           }]
    (print
    (render-swagger spec template
                    (fn [spec]
                      (merge additional-params
                             (assoc spec :definitions
                                    (map expand-model (:definitions spec)))))))))
```

## Running the example

```
➜  swagger-gen git:(master) lein run -m swagger-gen.examples.scala.generator
```

Generates the following Scala code and dumps it in the terminal

```scala
package com.google.service.models

import spray.json.DefaultJsonProtocol

case object Pet

object Pet extends DefaultJsonProtocol {
  implicit val format = jsonFormat0(Pet.apply)
}

case class NewPet(name: String, tag: String)

object NewPet extends DefaultJsonProtocol {
  implicit val format = jsonFormat2(NewPet.apply)
}

case class Error(code: Int, message: String)

object Error extends DefaultJsonProtocol {
  implicit val format = jsonFormat2(Error.apply)
}

```

## License

Copyright © 2015 Owain Lewis

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
