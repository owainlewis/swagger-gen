(ns swagger-gen.core-test
  (:require [clojure.test :refer :all]
            [swagger-gen.core :refer :all]))

(def petstore-yaml "resources/swagger/petstore.yaml")

(deftest file-exension-test []
  (testing "should return correct extension"
    (is (= :yaml (file-extension petstore-yaml)))))

(deftest loads-swagger-yaml []
  (testing "should load a swagger yaml file"
    (is (map? (load-swagger-file petstore-yaml)))))

(deftest parse-swagger-test
  (testing "should parse swagger into sections"
    (is (= (count (keys (parse-swagger petstore-yaml))) 3))))

(deftest paths-test
  (testing "should extract all paths into a flattened list"
    (let [extracted (->> (parse-swagger petstore-yaml) :paths)]
      (is (= (count extracted) 4)))))
