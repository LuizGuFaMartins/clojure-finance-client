(ns clojure-finance-client.pages.login.login-events
  (:require
   [clojure-finance-client.api :as api]
   [clojure.string :as str]
   [day8.re-frame.http-fx]
   [reitit.frontend.easy :as rfe]
   [re-frame.core :as rf]))

(rf/reg-event-db
 :set-login-field
 (fn [db [_ field value]]
   (assoc-in db [:login-form field] value)))

(rf/reg-event-fx
 :login-request
 (fn [{:keys [db]} _]
   (let [credentials (:login-form db)
         {:keys [email password]} credentials]
     (cond
       (or (str/blank? email) (str/blank? password))
       {:dispatch [:api/handle-failure {:response {:error "Preencha todos os campos"}}]}

       :else
       {:db (assoc db :loading? true :login-error nil)
        :http-xhrio (api/login
                     credentials
                     [:login-success]
                     [:api/handle-failure])}))))

(rf/reg-event-fx
 :login-success
 (fn [{:keys [db]} [_ response]]
   (let [user (:user response)
         token (:access-token response)
         role (keyword (:role user))]

     (.setItem js/localStorage "token" token)

     {:db (-> db
              (assoc :current-user user)
              (assoc :loading? false)
              (dissoc :login-form))
      :dispatch [:navigate-by-role role]})))

(rf/reg-event-fx
 :navigate-by-role
 (fn [_ [_ role]]
   (let [route (cond
                 (= role :admin)    :admin
                 (= role :customer) :customer
                 :else              :login)]
     (rfe/push-state route)
     {})))