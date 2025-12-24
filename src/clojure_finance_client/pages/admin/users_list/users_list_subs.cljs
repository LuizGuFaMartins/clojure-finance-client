(ns clojure-finance-client.pages.admin.users-list.users-list-subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 :admin/users
 (fn [db _]
   (:admin/users db)))

(rf/reg-sub :admin/modal (fn [db _] (:admin/modal db)))

(rf/reg-sub :admin/user-form (fn [db _] (:admin/user-form db)))