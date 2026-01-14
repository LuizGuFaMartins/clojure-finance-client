(ns clojure-finance-client.pages.client.profile.profile-subs
  (:require
   [re-frame.core :as rf]))

;; Change tab
(rf/reg-sub
 :user/active-tab
 (fn [db _]
   (get db :profile-active-tab :personal-info)))

;; User data
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
 :user/current-user-id
 (fn [db _]
   (get-in db [:user/current-user :id])))

;; Bank data
(rf/reg-sub
 :bank-data/users
 (fn [db _]
   (:bank-data/users db)))

(rf/reg-sub :bank-data/modal (fn [db _] (:bank-data/modal db)))

(rf/reg-sub :bank-data/form (fn [db _] (:bank-data/form db)))

;; Transactions
(rf/reg-sub
 :transactions/list
 (fn [db _]
   (:transactions/list db)))

(rf/reg-sub
 :transactions/loading?
 (fn [db _]
   (:transactions/loading? db)))

(rf/reg-sub
 :transactions/filters
 (fn [db _]
   (:transactions/filters db)))

(rf/reg-sub 
 :transactions/modal 
 (fn [db _] (:transactions/modal db)))

(rf/reg-sub 
 :transactions/form 
 (fn [db _] (:transactions/form db)))