(ns clojure-finance-client.shared.views
  (:require
   [re-frame.core :as rf]))

(defn root []
  (let [route @(rf/subscribe [:current-route])]
    (when route
      (let [view (get-in route [:data :view])]
        [view]))))
