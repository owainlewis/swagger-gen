(ns swagger-gen.fixtures)

(def error-definition
  {:required ["code" "message"],
   :properties {:code    {:type "integer"},
                :message {:type "string" :enum ["Foo", "Bar"]}},
   :name "Error"})

(def error-definition-with-optional-params
  {:required [],
   :properties {:code    {:type "integer"},
                :message {:type "string"}},
   :name "Error"})

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
