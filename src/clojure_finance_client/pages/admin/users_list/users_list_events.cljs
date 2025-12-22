(ns clojure-finance-client.pages.admin.users-list.users-list-events
  (:require
   [clojure-finance-client.shared.api :as api]
   [day8.re-frame.http-fx]
   [re-frame.core :as rf]))

;; User data
(rf/reg-event-fx
 :users/load
 (fn [{:keys [db]} [_]]
   {:db (assoc db :user/loading? true)
    :http-xhrio
    (api/fetch-users
     [:users/load-success]
     [:user/load-failure])}))

(rf/reg-event-db
 :users/load-success
 (fn [db [_ users]]
   (-> db
       (assoc :admin/users users)
       (assoc :user/loading? false))))

(rf/reg-event-db
 :admin/open-modal
 (fn [db [_ mode user-data]]
   (assoc db :admin/modal {:show? true :mode mode :user-id (:id user-data)}
          :admin/user-form (or user-data {:name "" :email "" :password "" :cpf "" :phone ""}))))

(rf/reg-event-db
 :admin/close-modal
 (fn [db _]
   (assoc-in db [:admin/modal :show?] false)))

(rf/reg-event-db
 :admin/set-form-field
 (fn [db [_ field value]]
   (assoc-in db [:admin/user-form field] value)))

(rf/reg-event-fx
 :admin/save-user
 (fn [{:keys [db]} _]
   (let [{:keys [mode user-id]} (:admin/modal db)
         user-data (:admin/user-form db)]
     {:db (assoc db :user/loading? true)
      :http-xhrio (if (= mode :create)
                    (api/create-user user-data [:admin/save-success] [:api/handle-failure])
                    (api/update-user user-id user-data [:admin/save-success] [:api/handle-failure]))})))

(rf/reg-event-fx
 :admin/save-success
 (fn [{:keys [db]} _]
   {:db (assoc db :user/loading? false)
    :dispatch-n [[:admin/close-modal] [:users/load]]}))

(rf/reg-event-fx
 :admin/delete-user
 (fn [{:keys [db]} [_ id]]
   {:db (assoc db :user/loading? true)
    :http-xhrio (api/delete-user id [:admin/save-success] [:api/handle-failure])}))

