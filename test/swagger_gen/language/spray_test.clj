(ns swagger-gen.language.spray-test
  (:require [clojure.test :refer :all]
            [swagger-gen.fixtures :refer :all]
            [swagger-gen.language.spray :refer :all]))

;; Routes

(deftest starts-and-ends-with-test []
  (testing "should return true for spray route part"
    (is (= true (starts-and-ends-with? "{foo}" "{" "}")))))

(deftest route-parts-test []
  (testing "should split spray route into parts"
    (is (= ["api" "users" "{id}"]
           (route-parts "/api/users/{id}")))))

(deftest to-spray-route-test []
  (testing "should return a spray route string"
    (is (= "api / users / Segment"
           (to-spray-route "/api/users/{id}")))))

(deftest parenthesize-test []
  (testing "should handle no args"
    (is (= "" (parenthesize [])))
    (is (= "foo" (parenthesize ["foo"])))
    (is (= "(foo, bar") (parenthesize ["foo", "bar"]))))
