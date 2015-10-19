(ns swagger-gen.util-test
  (:require [clojure.test :refer :all]
            [swagger-gen.util :refer :all]))

(deftest interpose-map-test
  (testing "should concat values and comma separate"
    (let [actual (interpose-map identity ", " ["foo", "bar", "baz"])]
      (is (= actual "foo, bar, baz")))))
    
(deftest test-camelize
  (testing "should convert snake_case to camelCase"
    (is (= "fooBar" (camelize "foo_bar")))))

(deftest test-upcase-camelize
  (testing "should convert snake_case to CamelCase"
    (is (= "FooBar" (upcase-camelize "foo_bar")))))

(deftest test-normalize-def
  (testing "should strip the definition prefix"
    (is (= "Card" (normalize-def "#/definitions/Card")))))
