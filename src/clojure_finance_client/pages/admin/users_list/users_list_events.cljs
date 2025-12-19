(ns clojure-finance-client.pages.admin.users-list.users-list-events
    (:require
    [re-frame.core :as rf]
    [clojure-finance-client.db :as db]
    [day8.re-frame.http-fx]
    [day8.re-frame.http-fx]
    [clojure-finance-client.api :as api]))

;; User data
(rf/reg-event-fx
 :users/load
 (fn [{:keys [db]} [_]]
   {:db (assoc db :user/loading? true)
    :http-xhrio
    (api/fetch-users
     [:users/load-success]
     [:user/load-failure])}))