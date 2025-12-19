(ns clojure-finance-client.pages.client.profile.profile-events
  (:require
   [clojure-finance-client.api :as api]
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
