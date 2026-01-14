(ns clojure-finance-client.shared.graphql.api-gql
  (:require
   [ajax.core :as ajax]
   [clojure-finance-client.shared.api :refer [base-url]]
   [clojure-finance-client.shared.graphql.queries :as queries]))

(defn- auth-gql [query variables on-success on-error]
  {:method          :post
   :uri             (str base-url "/graphql")
   :params          {:query query :variables variables}
   :with-credentials true
   :format          (ajax/json-request-format)
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success      on-success
   :on-failure      on-error})

;; --- Transações ---
(defn fetch-transactions [variables on-success on-error]
  (auth-gql
   queries/list-my-transactions
   variables
   on-success
   on-error))

(defn send-transaction [transaction-data on-success on-error]
  (auth-gql
   queries/create-transaction
   {:input transaction-data}
   on-success
   on-error))