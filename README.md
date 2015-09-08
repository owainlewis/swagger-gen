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
lein run -m swagger-gen.generator -t examples/html/template.mustache -s resources/swagger/petstore.yaml -d service-docs.html
```

For more complex code generation tasks we can write custom functions to assist with the generation.

```clojure

(ns example.core
  (:require [swagger-gen.generator :refer :all]))
  
(render-swagger spec template)

;; Or add custom attributes to a swagger spec for easy rendering

(render-swagger spec template
  (fn [spec]
    (assoc spec :key value)))
```

## License

Copyright © 2015 Owain Lewis

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
