# swagger-gen

[![Build Status](https://travis-ci.org/owainlewis/swagger-gen.svg)](https://travis-ci.org/owainlewis/swagger-gen)

A library for easy swagger code generation

## Goals

1. Lightweight and simple DSL for traversing Swagger Specs
2. Clean and simple code gen based on templates
3. More complex code generation through Clojure functions

## Usage

### Parsing specs

```clojure

(ns example
  (:require [swagger-gen.core :refer :all]))

(def petstore-routes
  ((juxt :method :path)
    (first (swagger-paths
             (load-swagger "resources/swagger/petstore.yaml")))))
	     
;; => ["get" "/pets"]

```

### Code Generation


```clojure
(print (code-generate spec))
```

Generatesthe following Scala code

```scala
package com.google.myproject

import spray.json.DefaultJsonProtocol
  
case object Pet

object Pet extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat0(Pet.apply)
}
  
case class NewPet(name: String, tag: String)

object NewPet extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat2(NewPet.apply)
}
  
case class Error(code: Int, message: String)

object Error extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat2(Error.apply)
}

```

## License

Copyright © 2015 Owain Lewis

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
