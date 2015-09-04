(ns swagger-gen.util-test
  (:require [clojure.test :refer :all]
            [swagger-gen.util :refer :all]))

(deftest test-camelize
  (testing "should convert snake_case to CamelCase"
    (is (= "fooBar" (camelize "foo_bar")))))

(deftest test-normalize-def
  (testing "should strip the definition prefix"
    (is (= "Card" (normalize-def "#/definitions/Card")))))
