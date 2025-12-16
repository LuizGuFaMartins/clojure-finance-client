(ns clojure-finance-client.routes
  (:require
   [reitit.frontend :as rf]
   [reitit.frontend.easy :as rfe]
   [re-frame.core :as re-frame]
   [clojure-finance-client.pages.login :as login]
   [clojure-finance-client.pages.admin :as admin]
   [clojure-finance-client.pages.user :as user]))

(def routes
  (rf/router
   [["/" {:name :login :view login/page}]
    ["/admin" {:name :admin :view admin/page}]
    ["/user" {:name :user :view user/page}]]))

(defn on-navigate [new-match]
  (when new-match
    (re-frame/dispatch [:navigate new-match])))

(defn init-routes []
  (rfe/start!
   routes
   on-navigate
   {:use-fragment false}))
