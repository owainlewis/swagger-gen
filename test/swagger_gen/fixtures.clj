(ns swagger-gen.fixtures)

(def expiry-date
  {:name "ExpiryDate",
   :args [{:name "expiry_month", :type "string", :required true}
          {:name "expiry_year", :type "string", :required true}]})

(def error-model
  {:name "Error",
   :args [{:name "code", :type "integer", :items nil, :required true}
          {:name "message", :type "string", :items nil, :required true}]})

(def simple-route
  {:method "get",
   :path "/api/cards",
   :summary "Gets a list of cards",
   :operationId "getCards",
   :roles ["api.service.CONSUMER"]})

(def body-param
  {:in "body",
   :name "body",
   :required true,
   :schema {:$ref "#/definitions/CardEdit"}})

(def path-param
  {:in "path",
   :name "card_id",
   :description "Card id of the card to edit",
   :required true,
   :type "string"})

(def route-with-body
  {:method "put",
   :path "/consumers/cards/{card_id}",
   :operationId "editCard",
   :roles ["api.service.CONSUMER"],
   :parameters [body-param path-param]})

(def path
  ["/echo"
    {"get" {"description" "Returns the 'message' to the caller",
     "operationId" "echo",
     "parameters"  [{"name" "headerParam", "in" "header", "type" "string", "required" false} {"name" "message", "in" "query", "type" "string", "required" true}], "responses" {200 {"description" "Success", "schema" {"$ref" "EchoResponse"}}, "default" {"description" "Error", "schema" {"$ref" "Error"}}}}}])
