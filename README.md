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
  
(render-swagger spec template)

;; Or add custom attributes to a swagger spec for easy rendering

(render-swagger spec template
  (fn [spec]
    (assoc spec :key value)))
```

And an example template

```mustache
package com.google.service.models

import spray.json.DefaultJsonProtocol

{{#definitions}}

{{class}}

object {{name}} extends DefaultJsonProtocol with SnakeCaseProductFormats {
  implicit val format = jsonFormat{{arglen}}({{name}}.apply)
}
{{/definitions}}
```

## License

Copyright © 2015 Owain Lewis

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
