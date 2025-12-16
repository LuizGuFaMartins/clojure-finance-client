
(ns clojure-finance-client.api
  (:require
   [ajax.core :as ajax]))

(defn fetch-user
  [user-id on-success on-error]
  {:method          :get
   :uri             (str "http://localhost:8080/users/" user-id)
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success      on-success
   :on-failure      on-error})

