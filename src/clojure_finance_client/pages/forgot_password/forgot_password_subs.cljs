(ns clojure-finance-client.pages.forgot-password.forgot-password-subs
  (:require
   [clojure.string :as str]
   [re-frame.core :as rf]))

(rf/reg-sub
 :forgot-password/step
 (fn [db _] (:forgot-password/step db :email)))

(rf/reg-sub
 :forgot-password/form
 (fn [db _] (:forgot-password/form db {})))

(rf/reg-sub
 :forgot-password/loading?
 (fn [db _] (:forgot-password/loading? db false)))

(rf/reg-sub
 :forgot-password/error
 (fn [db _] (:forgot-password/error db)))

(rf/reg-sub
 :forgot-password/password-validation-error
 (fn [db _]
   (let [{:keys [password confirm-password]} (:forgot-password/form db)
         sequential? (re-find #"(.)\1\1" (str password))]
     (cond
       (str/blank? password) nil
       (not (<= 8 (count password) 64)) :length
       sequential? :sequential
       (and (not (str/blank? confirm-password)) (not= password confirm-password)) :unmatched
       :else nil))))