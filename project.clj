(defproject io.forward/swagger-gen "1.0.2"
  :description "Swagger code generation toolkit"
  :url "http://github.com/owainlewis"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main swagger-gen.generator
  :aot [swagger-gen.generator]
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [stencil "0.5.0"]
                 [yaml "1.0.0"]])
