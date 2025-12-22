(ns clojure-finance-client.pages.client.profile.profile-events
  (:require
   [clojure-finance-client.shared.api :as api]
   [re-frame.core :as rf]))

(rf/reg-event-fx
 :user/load
 (fn [{:keys [db]} [_ user-id]]
   {:db (assoc db :user/loading? true)
    :http-xhrio
    (api/fetch-user
     user-id
     [:user/load-success]
     [:api/handle-failure])}))

(rf/reg-event-db
 :user/load-success
 (fn [db [_ user]]
   (-> db
       (assoc :user/profile user)
       (assoc :user/loading? false))))

;; Bank data
(rf/reg-event-fx
 :bank-data/load
 (fn [{:keys [db]} [_ user-id]]
   {:db (assoc db :bank-data/loading? true)
    :http-xhrio
    (api/fetch-user-bank-data
     user-id
     [:bank-data/load-success]
     [:api/handle-failure])}))

(rf/reg-event-db
 :bank-data/load-success
 (fn [db [_ bank-data]]
   (-> db
       (assoc :user/bank-data bank-data)
       (assoc :user/loading? false))))

(rf/reg-event-db
 :bank-data/open-modal
 (fn [db [_ mode user-data]]
   (assoc db :bank-data/modal {:show? true :mode mode :user-id (:id user-data)}
          :bank-data/form (or user-data {:name "" :email "" :password "" :cpf "" :phone ""}))))

(rf/reg-event-db
 :bank-data/close-modal
 (fn [db _]
   (assoc-in db [:bank-data/modal :show?] false)))

(rf/reg-event-db
 :bank-data/set-form-field
 (fn [db [_ field value]]
   (assoc-in db [:bank-data/form field] value)))

(rf/reg-event-fx
 :bank-data/save
 (fn [{:keys [db]} [_ user-id]]
   (let [mode      (get-in db [:bank-data/modal :mode])
         form-data (:bank-data/form db)
         payload   (assoc form-data :user-id user-id)]
     {:db (assoc db :user/loading? true)
      :http-xhrio (if (= mode :create)
                    (api/create-bank-data payload [:bank-data/save-success] [:api/handle-failure])
                    (api/update-bank-data (:id form-data) payload [:bank-data/save-success] [:api/handle-failure]))})))

(rf/reg-event-fx
 :bank-data/save-success
 (fn [{:keys [db]} _]
   (let [user-id (get-in db [:user/profile :id])]
     {:db (assoc db :user/loading? false)
      :dispatch-n [[:bank-data/close-modal]
                   [:bank-data/load user-id]]})))

(rf/reg-event-fx
 :bank-data/delete
 (fn [{:keys [db]} [_ id]]
   {:db (assoc db :user/loading? true)
    :http-xhrio (api/delete-bank-data id [:bank-data/save-success] [:api/handle-failure])}))