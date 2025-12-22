(ns clojure-finance-client.shared.components.confirmation-modal.confirmation-modal-subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 :modal/confirmation-state
 (fn [db _]
   (:confirmation-modal db)))
