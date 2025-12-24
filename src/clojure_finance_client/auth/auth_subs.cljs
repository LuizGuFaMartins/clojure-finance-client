(ns clojure-finance-client.auth.auth-subs
  (:require
   [clojure-finance-client.shared.db :as db]
   [re-frame.core :as rf]))

(rf/reg-sub
 :session-loaded?
 (fn [db _]
   (get db :session-loaded? false)))