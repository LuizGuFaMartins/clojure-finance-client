(ns clojure-finance-client.pages.forgot-password.forgot-password-events
  (:require
   [clojure-finance-client.api :as api]
   [re-frame.core :as rf]))

(rf/reg-event-db
 :forgot-password/set-field
 (fn [db [_ field value]]
   (assoc-in db [:forgot-password/form field] value)))

(rf/reg-event-fx
 :forgot-password/send-email
 (fn [{:keys [db]} _]
   {:db (assoc db :forgot-password/loading? true)
    :http-xhrio (api/request-password-code
                 {:email (get-in db [:forgot-password/form :email])}
                 [:forgot-password/send-email-success]
                 [:api/handle-failure])}))

(rf/reg-event-db
 :forgot-password/send-email-success
 (fn [db _]
   (assoc db :forgot-password/loading? false
          :forgot-password/step :code)))

(rf/reg-event-fx
 :forgot-password/verify-code
 (fn [{:keys [db]} _]
   {:db (assoc db :forgot-password/loading? true)
    :http-xhrio (api/verify-reset-code
                 {:email (get-in db [:forgot-password/form :email])
                  :code  (get-in db [:forgot-password/form :code])}
                 [:forgot-password/verify-code-success]
                 [:api/handle-failure])}))

(rf/reg-event-db
 :forgot-password/verify-code-success
 (fn [db _]
   (assoc db :forgot-password/loading? false
          :forgot-password/step :password)))

(rf/reg-event-fx
 :forgot-password/reset-password
 (fn [{:keys [db]} _]
   (let [form (:forgot-password/form db)]
     {:db (assoc db :forgot-password/loading? true)
      :http-xhrio (api/reset-password
                   {:email (:email form) :code (:code form) :password (:password form)}
                   [:forgot-password/reset-success]
                   [:api/handle-failure])})))

(rf/reg-event-fx
 :forgot-password/reset-success
 (fn [{:keys [db]} _]
   (js/alert "Senha alterada com sucesso!")
   {:db (dissoc db :forgot-password/form :forgot-password/step :forgot-password/loading?)
    :dispatch [:navigate-to :login]}))