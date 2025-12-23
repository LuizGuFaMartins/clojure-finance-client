(ns clojure-finance-client.shared.events
  (:require
   [clojure-finance-client.shared.db :as db]
   [clojure.string :as s]
   [re-frame.core :as rf]
   [reitit.frontend.easy :as rfe]))

(rf/reg-event-db
 :navigate
 (fn [db [_ route]]
   (assoc db :current-route route)))

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   db/default-db))