(ns swagger-gen.core-test
  (:require [clojure.test :refer :all]
            [swagger-gen.fixtures :refer :all]
            [swagger-gen.core :refer :all]))

(def petstore-yaml "resources/swagger/petstore.yaml")

(deftest loads-swagger-yaml []
  (testing "should load a swagger yaml file"
    (is (map? (load-swagger-file petstore-yaml)))))

(deftest parse-swagger-test
  (testing "should parse swagger into sections"
    (is (= (count (keys (parse-swagger petstore-yaml))) 10))))

(deftest normalized-paths-test
  (testing "should extract all paths into a flattened list"
    (let [extracted (->> (parse-swagger petstore-yaml) :paths)]
      (is (= (count extracted) 4)))))

(deftest normalize-definition-test
  (testing "should normalize a swagger model definition"
    (let [def [:Pet {:allOf [{:$ref "#/definitions/NewPet"}
                             {:required ["id"], :properties {:id {:type "integer", :format "int64"}}}]}]]
    (is (= (normalize-swagger-definition def)
      {:allOf [{:$ref "#/definitions/NewPet"}
                 {:required ["id"], :properties
       {:id {:type "integer", :format "int64"}}}], :name "Pet", :args []})))))

(deftest normalise-path-test
  (testing "should normalize a swagger path"
    (is (= (normalize-swagger-path path)
           {:path "/echo", :method "get", :description "Returns the 'message' to the caller",
            :operationId "echo",
            :parameters [{:name "headerParam", :in "header", :type "string", :required false}
                         {:name "message", :in "query", :type "string", :required true}],
            :responses {200 {:description "Success", :schema {:$ref "EchoResponse"}},
                        :default {:description "Error", :schema {:$ref "Error"}}}}))))
