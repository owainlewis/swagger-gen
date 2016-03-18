(ns swagger-gen.language.scala-test
  (:require [clojure.test :refer :all]
            [swagger-gen.fixtures :refer :all]
            [swagger-gen.language.scala :refer :all]))

(def petstore-yaml "resources/swagger/petstore.yaml")

(deftest transform-rate-test []
  (testing "should transform rate definition into Scala case class"
    (let [actual (render-case-class rate-definition true)]
      (is (= actual "case class Rates(rates: Seq[Rate])")))))

(deftest transform-definition-array-string-test []
  (testing "should transform array of string definitions"
    (let [actual (render-case-class definition-array-string true)]
      (is (= actual "case class Rates(rates: Seq[String])")))))

(deftest generate-case-object-test []
  (testing "should generate a Scala case object when arity is zero"
    (let [route {:name "Foo" :args []}]
      (is (= "case object Foo" (render-case-class route true))))))

(deftest enum-type-string-test []
  (testing "should generate enum string"
    (is (= "FooBarEnum.Value" (enum-type-string :Foo :bar)))))

(deftest enum-type-string-camel-test []
  (testing "should generate enum string"
    (is (= "FooBarBazEnum.Value" (enum-type-string :Foo "bar_baz")))))

(deftest optional?-test []
  (testing "should convert to an option type"
    (is (= "Option[Foo] = None" (optional? false "Foo")))
    (is (= "Foo"                (optional? true  "Foo")))))

(deftest optional-enum-type-string-test []
  (testing "should generate optional enum string"
    (is (= "Option[FooBarEnum.Value] = None"
           (optional? false (enum-type-string :Foo :bar))))))

(deftest enum-case-class-test []
  (testing "should render Scala enum types"
    (let [expected "case class Error(code: Long, message: ErrorMessageEnum.Value)"
          actual (render-case-class error-definition true)]
      (is (= expected actual)))))

(deftest optional-params-case-class-test []
  (testing "should handle optional params"
    (let [expected "case class Error(code: Option[Int] = None, message: Option[String] = None)"
          actual (render-case-class error-definition-with-optional-params true)]
      (is (= expected actual)))))

(deftest model-with-array-string-test []
  (testing "should convert data to model"
    (let [expected "case class Foo(roles: Seq[String])"
          actual (render-case-class definition-with-array false)]
      (is (= expected actual)))))
