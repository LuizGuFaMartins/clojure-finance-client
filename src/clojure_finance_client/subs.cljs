(ns clojure-finance-client.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 :current-route
 (fn [db _]
   (:current-route db)))
