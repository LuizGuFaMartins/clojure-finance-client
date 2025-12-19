(ns clojure-finance-client.pages.client.profile.profile-subs
  (:require
   [re-frame.core :as rf]))

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

(rf/reg-sub
 :current-user-id
 (fn [db _]
   (get-in db [:current-user :id])))
