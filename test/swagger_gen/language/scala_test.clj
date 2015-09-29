(ns swagger-gen.language.scala-test
  (:require [clojure.test :refer :all]
            [swagger-gen.fixtures :refer :all]
            [swagger-gen.language.scala :refer :all]))

(def petstore-yaml "resources/swagger/petstore.yaml")

(deftest generate-case-object-test []
  (testing "should generate a Scala case object when arity is zero"
    (let [route {:name "Foo" :args []}]
      (is (= "case object Foo" (render-case-class route))))))

(deftest generate-case-class-test []
  (testing "should generate a Scala case class"
    (is (= "case class Error(code: Int, message: String)"
           (render-case-class error-definition)))))
