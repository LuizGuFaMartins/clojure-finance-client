(ns clojure-finance-client.pages.login.login-events
  (:require
   [clojure-finance-client.api :as api]
   [clojure.string :as str]
   [day8.re-frame.http-fx]
   [re-frame.core :as rf]
   [reitit.frontend.easy :as rfe]))

(rf/reg-event-db
 :set-login-field
 (fn [db [_ field value]]
   (assoc-in db [:login/login-form field] value)))

(rf/reg-event-fx
 :login-request
 (fn [{:keys [db]} _]
   (let [credentials (:login/login-form db)
         {:keys [email password]} credentials]
     (cond
       (or (str/blank? email) (str/blank? password))
       {:db (assoc db :login/loading? false)
        :dispatch [:api/handle-failure {:status 400 :response {:error "Preencha todos os campos"}}]}

       :else
       {:db (assoc db :login/loading? true :login/error nil)
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
              (assoc :login/loading? false)
              (dissoc :login/login-form))
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