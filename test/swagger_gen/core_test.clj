(ns swagger-gen.core-test
  (:require [clojure.test :refer :all]
            [swagger-gen.fixtures :refer :all]
            [swagger-gen.core :refer :all]))

(def petstore-yaml "resources/swagger/petstore.yaml")
(def uber-yaml     "resources/swagger/uber.yaml")

(def petstore (parse-swagger petstore-yaml))
(def uber     (parse-swagger uber-yaml))

(deftest load-swagger-file-test []
  (testing "should load a swagger yaml file"
    (is (map? (load-swagger-file petstore-yaml)))))

(deftest parse-swagger-test []
  (testing "it should parse a swagger spec"
    (is (= (keys petstore)
      '(:schemes :definitions :produces :paths :consumes :host :info :swagger :basePath)))))

(deftest check-paths-test []
  (testing "paths should be a vector of paths containing method and path"
    (let [path (last (:paths petstore))]
      (is (= (:path path) "/pets/{id}"))
      (is (= (:method path) "delete"))
      (is (= (:operationId path) "deletePet")))))

(deftest check-definitions-test []
  (testing "should add name to definition and flatten into vector"
    (let [definitions (:definitions petstore)]
      )))
