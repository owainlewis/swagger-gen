(ns swagger-gen.language.scala-test
  (:require [clojure.test :refer :all]
            [swagger-gen.fixtures :refer :all]
            [swagger-gen.language.scala :refer :all]))

(def petstore-yaml "resources/swagger/petstore.yaml")

(deftest generate-case-object-test []
  (testing "should generate a Scala case object when arity is zero"
    (let [route {:name "Foo" :args []}]
      (is (= "case object Foo" (render-case-class route true))))))

(deftest optional-params-case-class-test []
  (testing "should handle optional params"
    (let [expected "case class Error(code: Option[Int] = None, message: Option[String] = None)"
          actual (render-case-class error-definition-with-optional-params true)]
      (is (= expected actual)))))

(deftest generate-case-class-test []
  (testing "should generate a Scala case class"
    (is (= "case class Error(code: Int, message: String)"
           (render-case-class error-definition true)))))
