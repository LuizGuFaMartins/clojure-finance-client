(ns clojure-finance-client.pages.admin.users-list.users-list-events
  (:require
   [clojure-finance-client.api :as api]
   [day8.re-frame.http-fx]
   [day8.re-frame.http-fx]
   [re-frame.core :as rf]))

;; User data
(rf/reg-event-fx
 :users/load
 (fn [{:keys [db]} [_]]
   {:db (assoc db :user/loading? true)
    :http-xhrio
    (api/fetch-users
     [:users/load-success]
     [:user/load-failure])}))

(rf/reg-event-db
 :users/load-success
 (fn [db [_ users]]
   (-> db
       (assoc :admin/users users)
       (assoc :user/loading? false))))
