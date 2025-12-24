(ns clojure-finance-client.pages.login.login-events
  (:require
   [clojure-finance-client.shared.api :as api]
   [clojure.string :as str]
   [day8.re-frame.http-fx]
   [re-frame.core :as rf]))

(def session-flag "finance-app/has-session")

;; LOGIN

(rf/reg-event-db
 :login-view/set-login-field
 (fn [db [_ field value]]
   (assoc-in db [:login/login-form field] value)))

(rf/reg-event-fx
 :login/login-request
 (fn [{:keys [db]} _]
   (let [credentials (:login/login-form db)
         {:keys [email password]} credentials]
     (cond
       (or (str/blank? email) (str/blank? password))
       {:db (assoc db :login/loading? false)
        :dispatch [:api/handle-failure {:status 400 :response {:error "Preencha todos os campos"}}]}

       :else
       {:db (assoc db :login/loading? true :login/error nil)
        :http-xhrio (api/login
                     credentials
                     [:login/login-success]
                     [:api/handle-failure])}))))

(rf/reg-event-fx
 :login/login-success
 (fn [{:keys [db]} [_ response]]
   (let [user response
         role (keyword (:role user))]

     (.setItem js/localStorage session-flag "true")

     {:db (-> db
              (assoc :user/current-user user)
              (assoc :user/current-user-id (:id user))
              (assoc :login/loading? false)
              (assoc :session-loaded? true)
              (dissoc :login/login-form))
      :dispatch [:route/navigate-by-role role]})))

;; LOGOUT

(rf/reg-event-fx
 :login/logout
 (fn [{:keys [db]} _]
   {:http-xhrio (api/logout [:login/logout-success] [:login/logout-success])
    :db (assoc db :loading? true)}))

(rf/reg-event-fx
 :login/logout-success
 (fn [{:keys [db]} _]
   (.removeItem js/localStorage session-flag)

   {:db (-> db
            (dissoc :user/current-user)
            (dissoc :user/current-user-id)
            (assoc :loading? false)
            (assoc :session-loaded? true))
    :dispatch [:route/navigate-to-login]}))
