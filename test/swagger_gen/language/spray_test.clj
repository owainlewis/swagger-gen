(ns swagger-gen.language.spray-test
  (:require [clojure.test :refer :all]
            [swagger-gen.fixtures :refer :all]
            [swagger-gen.language.spray :refer :all]))

(def petstore-yaml "resources/swagger/petstore.yaml")

(deftest generate-case-object-test []
  (testing "should generate a Scala case object when arity is zero"
    (let [route {:name "Foo" :args []}]
      (is (= "case object Foo" (render-case-class route))))))

(deftest generate-case-class-test []
  (testing "should generate a Scala case class"
    (is (= "case class ExpiryDate(expiryMonth: String, expiryYear: String)"
           (render-case-class expiry-date)))
    (is (= "case class Error(code: Int, message: String)"
           (render-case-class error-model)))))

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
