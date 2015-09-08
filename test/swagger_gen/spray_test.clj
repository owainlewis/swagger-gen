(ns swagger-gen.spray-test
    (:require [clojure.test :refer :all]
              [swagger-gen.spray :refer :all]))

(def petstore-yaml "resources/swagger/petstore.yaml")

(deftest starts-and-ends-with-test []
  (testing "should return true for spray route part"
    (is (= true (starts-and-ends-with? "{foo}" "{" "}")))))
