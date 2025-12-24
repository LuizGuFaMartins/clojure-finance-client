(ns clojure-finance-client.auth.auth-events
  (:require
   [clojure-finance-client.shared.api :as api]
   [re-frame.core :as rf]))

(def session-flag "finance-app/has-session")

;; --- Sessão (Inicialização) ---

(rf/reg-event-db
 :session-skipped
 (fn [db _]
   (-> db
       (assoc :session-loaded? true)
       (assoc :user/current-user nil))))

(rf/reg-event-fx
 :initialize-session
 (fn [{:keys [db]} _]
   {:db (assoc db :session-loaded? false)
    :http-xhrio (api/get-self [:load-session-success] [:load-session-failed])}))

(rf/reg-event-fx
 :load-session-success
 (fn [{:keys [db]} [_ user-data]]
   (let [user-id (:id user-data)]
     {:db (-> db
              (assoc :user/current-user user-data)
              (assoc :user/current-user-id user-id)
              (assoc :session-loaded? true))
      ;; Após carregar os dados, pedimos para re-avaliar a rota atual
      :dispatch [:route/re-evaluate-current-route]})))

(rf/reg-event-fx
 :load-session-failed
 (fn [{:keys [db]} _]
   {:db (assoc db :session-loaded? true :user/current-user nil)
    :dispatch [:route/re-evaluate-current-route]}))

(rf/reg-event-fx
 :route/re-evaluate-current-route
 (fn [{:keys [db]} _]
   (let [current-match (:current-route db)]
     (if current-match
       {:dispatch [:route/handle-navigation current-match]}
       {}))))

;; --- Erros Globais ---

(rf/reg-event-fx
 :api/handle-failure
 (fn [{:keys [db]} [_ {:keys [status] :as error}]]
   (js/console.error "Erro na API:" error)
   (let [base-db (assoc db :loading? false
                        :login/error "Usuário ou senha inválidos")]
     (if (= status 401)
       (do
         (.removeItem js/localStorage session-flag)
         {:db base-db
          :dispatch [:login/logout]})
       {:db base-db}))))