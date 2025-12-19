(ns clojure-finance-client.routes
  (:require
   [clojure-finance-client.pages.admin.users-list.users-list-view :as users-list]
   [clojure-finance-client.pages.client.profile.profile-view :as profile]
   [clojure-finance-client.pages.forgot-password.forgot-password-view :as forgot-password]
   [clojure-finance-client.pages.login.login-view :as login]
   [re-frame.core :as rf]
   [re-frame.db :refer [app-db]]
   [reitit.frontend :as rt]
   [reitit.frontend.easy :as rfe]))

(def routes
  (rt/router
   [""
    ["/" {:name :login
          :view login/page
          :public? true}]

    ["/forgot-password" {:name :forgot-password
                         :view forgot-password/page
                         :public? true}]

    ["/admin" {:name :admin
               :view users-list/page
               :roles #{:admin}}]

    ["/profile" {:name :customer
                 :view profile/page
                 :roles #{:customer}}]]))

(defn on-navigate [new-match]
  (let [user           (:current-user @app-db)
        user-role      (some-> user :role keyword)
        data           (:data new-match)
        route-name     (:name data)
        required-roles (:roles data)
        public?        (:public? data)]

    (cond
      (not new-match)
      (rfe/replace-state :login)

      (and user (= route-name :login))
      (if (= user-role :admin)
        (rfe/replace-state :admin)
        (rfe/replace-state :customer))

      public?
      (rf/dispatch [:navigate new-match])

      (not user)
      (rfe/replace-state :login)

      (and required-roles (not (contains? required-roles user-role)))
      (do
        (js/console.warn "Sem permissão para" route-name)
        (if (= user-role :admin)
          (rfe/replace-state :admin)
          (rfe/replace-state :customer)))

      :else
      (rf/dispatch [:navigate new-match]))))

(defn init-routes []
  (rfe/start! routes on-navigate {:use-fragment false}))