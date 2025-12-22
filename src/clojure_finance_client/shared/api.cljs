(ns clojure-finance-client.shared.api
  (:require
   [ajax.core :as ajax]))

(def base-url "http://localhost:8080")

(defn- get-token []
  (.getItem js/localStorage "token"))

;; Auxiliar para GET
(defn- auth-get [uri on-success on-error]
  {:method          :get
   :uri             (str base-url uri)
   :headers         {"Authorization" (str "Bearer " (get-token))}
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success      on-success
   :on-failure      on-error})

;; Auxiliar para POST/PUT/PATCH/DELETE com JSON
(defn- auth-send [method uri params on-success on-error]
  {:method          method
   :uri             (str base-url uri)
   :params          params
   :headers         {"Authorization" (str "Bearer " (get-token))}
   :format          (ajax/json-request-format)
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success      on-success
   :on-failure      on-error})

;; --- Autenticação & Recuperação (Públicos) ---

(defn login [credentials on-success on-error]
  ;; Login não usa token no header, mas envia JSON
  (auth-send :post "/login" credentials on-success on-error))

(defn request-password-code [params on-success on-error]
  (auth-send :post "/forgot-password/request" params on-success on-error))

(defn verify-reset-code [params on-success on-error]
  (auth-send :post "/forgot-password/verify" params on-success on-error))

(defn reset-password [params on-success on-error]
  (auth-send :post "/forgot-password/reset" params on-success on-error))

;; --- Perfil & Dados (Autenticados) ---

(defn fetch-user [user-id on-success on-error]
  (auth-get (str "/users/" user-id) on-success on-error))

(defn fetch-user-bank-data [user-id on-success on-error]
  (auth-get (str "/bank-data/user/" user-id) on-success on-error))

;; --- Admin (Autenticados) ---

(defn fetch-users [on-success on-error]
  (auth-get "/users" on-success on-error))

(defn create-user [user-data on-success on-error]
  (auth-send :post "/users" user-data on-success on-error))

(defn update-user [user-id user-data on-success on-error]
  (auth-send :put (str "/users/" user-id) user-data on-success on-error))

(defn delete-user [user-id on-success on-error]
  (auth-send :delete (str "/users/" user-id) nil on-success on-error))

;; --- Bank data ---
(defn create-bank-data [bank-data on-success on-error]
  (auth-send :post "/bank-data" bank-data on-success on-error))

(defn update-bank-data [bank-data-id bank-data on-success on-error]
  (auth-send :put (str "/bank-data/" bank-data-id) bank-data on-success on-error))

(defn delete-bank-data [bank-data-id on-success on-error]
  (auth-send :delete (str "/bank-data/" bank-data-id) nil on-success on-error))