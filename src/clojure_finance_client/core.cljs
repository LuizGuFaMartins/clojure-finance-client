(ns clojure-finance-client.core
  (:require
   [clojure-finance-client.events]
   [clojure-finance-client.pages.admin.users-list.users-list-events]
   [clojure-finance-client.pages.admin.users-list.users-list-subs]
   [clojure-finance-client.pages.admin.users-list.users-list-view]
   [clojure-finance-client.pages.client.profile.profile-events]
   [clojure-finance-client.pages.client.profile.profile-subs]
   [clojure-finance-client.pages.client.profile.profile-view]
   [clojure-finance-client.pages.forgot-password.forgot-password-events]
   [clojure-finance-client.pages.forgot-password.forgot-password-subs]
   [clojure-finance-client.pages.forgot-password.forgot-password-view]
   [clojure-finance-client.pages.login.login-events]
   [clojure-finance-client.pages.login.login-subs]
   [clojure-finance-client.pages.login.login-view]
   [clojure-finance-client.routes :as routes]
   [clojure-finance-client.subs]
   [clojure-finance-client.views :as views]
   [re-frame.core :as rf]
   [reagent.dom.client :as rdom]))

(defn init []
  (rf/dispatch-sync [:initialize-db])

  (rf/dispatch-sync [:auth/check-session])

  (routes/init-routes)

  (let [root (rdom/create-root (.getElementById js/document "app"))]
    (rdom/render root [views/root])))
