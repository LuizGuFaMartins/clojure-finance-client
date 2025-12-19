(ns clojure-finance-client.events
  (:require
   [clojure-finance-client.db :as db]
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

(defn decode-jwt [token]
  (try
    (let [payload (second (clojure.string/split token #"\."))
          json-str (js/window.atob payload)
          claims (js->clj (.parse js/JSON json-str) :keywordize-keys true)
          exp (:exp claims)
          now (/ (.now js/Date) 1000)]
      (if (and exp (< now exp))
        claims
        (do (js/console.warn "Token expirado") nil)))
    (catch js/Error e
      (js/console.error "Token inválido" e)
      nil)))

(rf/reg-event-db
 :auth/check-session
 (fn [db _]
   (let [token (.getItem js/localStorage "token")
         claims (when token (decode-jwt token))]
     (if claims
       (assoc db :current-user claims
              :current-user-id (:id claims))
       (do
         (.removeItem js/localStorage "token")
         (dissoc db :current-user :current-user-id))))))

(rf/reg-event-fx
 :auth/logout
 (fn [{:keys [db]} _]
   (.removeItem js/localStorage "token")
   {:db (-> db
            (dissoc :current-user)
            (dissoc :current-user-id))
    :dispatch [:navigate-to-login]}))

(rf/reg-event-fx
 :navigate-to-login
 (fn [_ _]
   (rfe/replace-state :login)
   {}))

(rf/reg-event-fx
 :api/handle-failure
 (fn [{:keys [db]} [_ {:keys [status] :as error}]]
   (js/console.error "Erro na API:" error)
   (let [base-db (assoc db :login/loading? false
                        :user/loading? false
                        :login/error "Usuário ou senha inválidos")]
     (if (= status 401)
       {:db base-db
        :dispatch [:auth/logout]}
       {:db base-db}))))


   

