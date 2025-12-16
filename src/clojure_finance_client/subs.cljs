(ns clojure-finance-client.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 :current-route
 (fn [db _]
   (:current-route db)))

(rf/reg-sub
 :admin/users
 (fn [db _]
   (:admin/users db)))

(rf/reg-sub
 :user/profile
 (fn [db _]
  (:user/profile db)))

(rf/reg-sub
 :user/bank-data
 (fn [db _]
  (:user/bank-data db)))

(rf/reg-sub
 :user/loading?
 (fn [db _]
  (:user/loading? db)))

