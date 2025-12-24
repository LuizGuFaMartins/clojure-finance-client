(ns clojure-finance-client.routes
  (:require
   [clojure-finance-client.pages.admin.users-list.users-list-view :as users-list]
   [clojure-finance-client.pages.client.profile.profile-view :as profile]
   [clojure-finance-client.pages.forgot-password.forgot-password-view :as forgot-password]
   [clojure-finance-client.pages.login.login-view :as login]
   [re-frame.core :as rf]
   [reitit.frontend :as rt]
   [reitit.frontend.easy :as rfe]))

(rf/reg-event-db
 :route/navigate
 (fn [db [_ route]]
   (assoc db :current-route route)))

(rf/reg-sub
 :current-route
 (fn [db _]
   (:current-route db)))

(rf/reg-event-fx
 :route/handle-navigation
 (fn [{:keys [db]} [_ new-match]]
   (let [user            (:user/current-user db)
         session-loaded? (:session-loaded? db)
         user-role       (some-> user :role keyword)
         data            (:data new-match)
         required-roles  (:roles data)
         public?         (:public? data)]

     (cond
       ;; AQUI ESTÁ O SEGREDO: Se a sessão ainda não carregou (F5), 
       ;; guardamos a rota mas NÃO validamos nada ainda.
       (not session-loaded?)
       {:db (assoc db :current-route new-match)}

       ;; Se já carregou e é pública (Login), deixa passar
       public?
       {:db (assoc db :current-route new-match)}

       ;; Se já carregou e não tem usuário, chuta pro Login
       (not user)
       {:dispatch [:login/navigate-to-login]}

       ;; Se já carregou e a role não bate, redireciona pelo cargo
       (and required-roles (not (contains? required-roles user-role)))
       {:dispatch [:route/redirect-by-role user-role]}

       ;; Caso contrário, tudo OK
       :else
       {:db (assoc db :current-route new-match)}))))

(rf/reg-event-fx
 :route/redirect-by-role
 (fn [_ [_ role]]
   (let [target (if (= role :admin) :admin :profile)]
     (rfe/replace-state target)
     {})))

(rf/reg-event-fx
 :route/navigate-to-login
 (fn [_ _]
   (rfe/replace-state :login)
   {}))

(rf/reg-event-fx
 :route/navigate-by-role
 (fn [_ [_ role]]
   (let [route (cond
                 (= role :admin)    :admin
                 (= role :customer) :profile
                 :else              :login)]
     (rfe/push-state route)
     {})))

(def routes
  (rt/router
   [""
    ["/" {:name :login :view login/page :public? true}]
    ["/forgot-password" {:name :forgot-password :view forgot-password/page :public? true}]
    ["/admin" {:name :admin :view users-list/page :roles #{:admin}}]
    ["/profile" {:name :profile :view profile/page :roles #{:customer}}]]))

(defn on-navigate [new-match]
  (when new-match
    ;; Não decidimos nada aqui. Enviamos para o Re-frame decidir.
    (rf/dispatch [:route/handle-navigation new-match])))

(defn init-routes []
  (rfe/start! routes on-navigate {:use-fragment false}))