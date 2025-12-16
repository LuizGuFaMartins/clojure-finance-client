(ns clojure-finance-client.core
  (:require
   [re-frame.core :as rf]
   [reagent.dom.client :as rdom]
   [clojure-finance-client.routes :as routes]
   [clojure-finance-client.views :as views]
   [clojure-finance-client.db :as db]
   [clojure-finance-client.events]
   [clojure-finance-client.subs]))

(defn init []
  (rf/dispatch-sync [:initialize-db])
  (routes/init-routes)
  (let [root (rdom/create-root (.getElementById js/document "app"))]
    (rdom/render root [views/root])))
