(ns clojure-finance-client.core
  (:require
   [clojure-finance-client.auth.auth-events]
   [clojure-finance-client.auth.auth-subs]
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
   [clojure-finance-client.shared.components.confirmation-modal.confirmation-modal-events]
   [clojure-finance-client.shared.components.confirmation-modal.confirmation-modal-subs]
   [clojure-finance-client.shared.components.confirmation-modal.confirmation-modal-view]
   [clojure-finance-client.shared.db :as db]
   [clojure-finance-client.views :as views]
   [re-frame.core :as rf]
   [reagent.dom.client :as rdom]))

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   db/default-db))

(defn init []
  (rf/dispatch-sync [:initialize-db])

  (let [has-session? (.getItem js/localStorage "finance-app/has-session")]
    (if has-session?
      (rf/dispatch [:initialize-session])
      (rf/dispatch [:session-skipped])))

  (routes/init-routes)

  (let [root (rdom/create-root (.getElementById js/document "app"))]
    (rdom/render root [views/root])))