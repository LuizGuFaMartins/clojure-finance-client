(ns clojure-finance-client.events
  (:require
   [re-frame.core :as rf]
   [clojure-finance-client.db :as db]
   [day8.re-frame.http-fx]
   [day8.re-frame.http-fx]
   [clojure-finance-client.api :as api]))

(rf/reg-event-db
 :navigate
 (fn [db [_ route]]
   (assoc db :current-route route)))

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   db/default-db))

;; User data
(rf/reg-event-fx
 :user/load
 (fn [{:keys [db]} [_ user-id]]
   {:db (assoc db :user/loading? true)
    :http-xhrio
    (api/fetch-user
     user-id
     [:user/load-success]
     [:user/load-failure])}))

(rf/reg-event-db
 :user/load-success
 (fn [db [_ user]]
   (-> db
       (assoc :user/profile user)
       (assoc :user/loading? false))))

(rf/reg-event-db
 :user/load-failure
 (fn [db [_ error]]
   (-> db
       (assoc :user/error error)
       (assoc :user/loading? false))))

;; Bank data
(rf/reg-event-fx
 :bank-data/load
 (fn [{:keys [db]} [_ user-id]]
   {:db (assoc db :bank-data/loading? true)
    :http-xhrio
    (api/fetch-user
     user-id
     [:bank-data/load-success]
     [:bank-data/load-failure])}))

(rf/reg-event-db
 :bank-data/load-success
 (fn [db [_ bank-data]]
   (-> db
       (assoc :user/bank-data bank-data)
       (assoc :user/loading? false))))

(rf/reg-event-db
 :bank-data/load-failure
 (fn [db [_ error]]
   (-> db
       (assoc :user/error error)
       (assoc :user/loading? false))))