(ns clojure-finance-client.shared.api
  (:require-macros [clojure-finance-client.config :refer [get-api-url get-auth-api-url]])
  (:require
   [ajax.core :as ajax]))

(def base-url (get-api-url))
(def base-auth-url (get-auth-api-url))

(defn- auth-get [base uri on-success on-error]
  {:method          :get
   :uri             (str base uri)
   :with-credentials true
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success      on-success
   :on-failure      on-error})

(defn- auth-send [method base uri params on-success on-error]
  {:method          method
   :uri             (str base uri)
   :params          params
   :with-credentials true
   :format          (ajax/json-request-format)
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success      on-success
   :on-failure      on-error})

;; --- Autenticação & Recuperação (Públicos) ---

(defn login [credentials on-success on-error]
  (auth-send :post base-auth-url "/login" credentials on-success on-error))

(defn get-self [on-success on-error]
  (auth-get base-auth-url "/auth/me" on-success on-error))

(defn logout [on-success on-error]
  (auth-send :post base-auth-url "/logout" nil on-success on-error))

(defn request-password-code [params on-success on-error]
  (auth-send :post base-url "/forgot-password/request" params on-success on-error))

(defn verify-reset-code [params on-success on-error]
  (auth-send :post base-url "/forgot-password/verify" params on-success on-error))

(defn reset-password [params on-success on-error]
  (auth-send :post base-url "/forgot-password/reset" params on-success on-error))

;; --- Perfil & Dados (Autenticados) ---

(defn fetch-user [user-id on-success on-error]
  (auth-get base-url (str "/users/" user-id) on-success on-error))

(defn fetch-user-bank-data [user-id on-success on-error]
  (auth-get base-url (str "/bank-data/user/" user-id) on-success on-error))

;; --- Admin (Autenticados) ---

(defn fetch-users [on-success on-error]
  (auth-get base-url "/users" on-success on-error))

(defn create-user [user-data on-success on-error]
  (auth-send :post base-url "/users" user-data on-success on-error))

(defn update-user [user-id user-data on-success on-error]
  (auth-send :put  base-url(str "/users/" user-id) user-data on-success on-error))

(defn delete-user [user-id on-success on-error]
  (auth-send :dele base-url (str "/users/" user-id) nil on-success on-error))

;; --- Bank data ---

(defn create-bank-data [bank-data on-success on-error]
  (auth-send :post base-url "/bank-data" bank-data on-success on-error))

(defn update-bank-data [bank-data-id bank-data on-success on-error]
  (auth-send :put  base-url(str "/bank-data/" bank-data-id) bank-data on-success on-error))

(defn delete-bank-data [bank-data-id on-success on-error]
  (auth-send :dele base-url (str "/bank-data/" bank-data-id) nil on-success on-error))