(ns clojure-finance-client.pages.login.login-subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 :login-form
 (fn [db _]
   (:login-form db {:email "" :password ""})))

(rf/reg-sub
 :current-user
 (fn [db _]
   (:current-user db)))

(rf/reg-sub
 :loading?
 (fn [db _]
   (:loading? db false)))

(rf/reg-sub
 :login-error
 (fn [db _]
   (:login-error db)))