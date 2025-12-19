(ns clojure-finance-client.pages.admin.users-list.users-list-subs
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

