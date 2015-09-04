(ns swagger-gen.rendering.scala-test
  (:require [clojure.test :refer :all]
            [swagger-gen.rendering.scala :refer :all]))

(def model1
  {:name "ExpiryDate",
   :args [{:name "expiry_month", :type "string", :required true}
          {:name "expiry_year", :type "string", :required true}]})

(def model1-expected
  "case class ExpiryDate(expiryMonth: String, expiryYear: String)")

(def model2
  {:name "Error",
   :args [{:name "code", :type "integer", :items nil, :required true}
          {:name "message", :type "string", :items nil, :required true}]})

(def model2-expected
  "case class Error(code: Int, message: String)")

(deftest test-render-case-class
  (testing "should render a Scala case class"
    (is (= model1-expected (render-case-class model1)))
    (is (= model2-expected (render-case-class model2)))
    ))
