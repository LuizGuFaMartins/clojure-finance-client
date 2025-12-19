
(ns clojure-finance-client.api
  (:require
   [ajax.core :as ajax]))

(def base-url "http://localhost:8080")

(defn- get-token []
  (.getItem js/localStorage "token"))

(defn- authenticated-request [method uri on-success on-error]
  {:method          method
   :uri             (str base-url uri)
   :headers         {"Authorization" (str "Bearer " (get-token))}
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success      on-success
   :on-failure      on-error})

(defn login
  [credentials on-success on-error]
  {:method          :post
   :uri             (str base-url "/login")
   :params          credentials
   :format          (ajax/json-request-format)
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success      on-success
   :on-failure      on-error})

(defn request-password-code
  [credentials on-success on-error]
  {:method          :post
   :uri             (str base-url "/login")
   :params          credentials
   :format          (ajax/json-request-format)
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success      on-success
   :on-failure      on-error})

(defn verify-reset-code
  [credentials on-success on-error]
  {:method          :post
   :uri             (str base-url "/login")
   :params          credentials
   :format          (ajax/json-request-format)
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success      on-success
   :on-failure      on-error})

(defn reset-password
  [credentials on-success on-error]
  {:method          :post
   :uri             (str base-url "/login")
   :params          credentials
   :format          (ajax/json-request-format)
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success      on-success
   :on-failure      on-error})

(defn fetch-user [user-id on-success on-error]
  (authenticated-request :get (str "/users/" user-id) on-success on-error))

(defn fetch-user-bank-data [user-id on-success on-error]
  (authenticated-request :get (str "/bank-data/user/" user-id) on-success on-error))

(defn fetch-users [on-success on-error]
  (authenticated-request :get "/users" on-success on-error))