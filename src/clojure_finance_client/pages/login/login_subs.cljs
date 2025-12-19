(ns clojure-finance-client.pages.login.login-subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 :login/login-form
 (fn [db _]
   (:login/login-form db {:email "" :password ""})))

(rf/reg-sub
 :user/current-user
 (fn [db _]
   (:current-user db)))

(rf/reg-sub
 :login/loading?
 (fn [db _]
   (:login/loading? db false)))

(rf/reg-sub
 :login/error
 (fn [db _]
   (:login/error db)))
