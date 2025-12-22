(ns clojure-finance-client.shared.components.confirmation-modal.confirmation-modal-events
  (:require
   [day8.re-frame.http-fx]
   [re-frame.core :as rf]))


(rf/reg-event-db
 :modal/show-confirm
 (fn [db [_ {:keys [title message on-confirm]}]]
   (assoc db :confirmation-modal {:show? true
                                  :title title
                                  :message message
                                  :on-confirm on-confirm})))

(rf/reg-event-db
 :modal/close-confirm
 (fn [db _]
   (assoc-in db [:confirmation-modal :show?] false)))

(rf/reg-event-fx
 :modal/confirm-action
 (fn [{:keys [db]} _]
   (let [action (get-in db [:confirmation-modal :on-confirm])]
     {:db (assoc-in db [:confirmation-modal :show?] false)
      :dispatch action})))