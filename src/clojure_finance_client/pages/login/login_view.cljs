(ns clojure-finance-client.pages.login.login-view
  (:require
   [clojure.string :as str]
   [re-frame.core :as rf]
   [reagent.core :as r]
   [reitit.frontend.easy :as rfe]))

(defn page []
  (r/create-class
   {:reagent-render
    (fn [_]
      (let [form-data (rf/subscribe [:login/login-form])
            loading?  (rf/subscribe [:login/loading?])
            error     (rf/subscribe [:login/error])
            fields-empty? (or (str/blank? (:email @form-data))
                              (str/blank? (:password @form-data)))]

        [:div {:class "min-h-screen flex items-center justify-center bg-slate-900 px-4"}

         [:div {:class "w-full max-w-sm bg-slate-800 p-8 rounded-2xl shadow-xl border border-slate-700"}

          [:h1 {:class "text-2xl font-bold text-white mb-2 text-center"}
           "Clojure Finance"]

          [:p {:class "text-slate-400 text-sm text-center mb-6"}
           "Acesse sua conta para continuar"]

          (when @error
            [:div {:class "mb-4 p-3 rounded-lg bg-red-500/10 border border-red-500/50 text-red-400 text-sm animate-pulse"}
             @error])

          [:div {:class "space-y-4"}
           [:div
            [:label {:class "text-xs font-medium text-slate-400 ml-1"} "E-mail"]
            [:input
             {:type "email"
              :placeholder "seu@email.com"
              :value (:email @form-data)
              :on-change #(rf/dispatch [:login-view/set-login-field :email (-> % .-target .-value)])
              :disabled @loading?
              :class (str "w-full mt-1 rounded-lg bg-slate-700 text-white placeholder-slate-500 "
                          "border border-slate-600 px-4 py-2 focus:outline-none "
                          "focus:ring-2 focus:ring-blue-500 transition-all "
                          (when @loading? "opacity-50 cursor-not-allowed"))}]]

           [:div
            [:label {:class "text-xs font-medium text-slate-400 ml-1"} "Senha"]
            [:input
             {:type "password"
              :placeholder "••••••••"
              :value (:password @form-data)
              :on-change #(rf/dispatch [:login-view/set-login-field :password (-> % .-target .-value)])
              :disabled @loading?
              :class (str "w-full mt-1 rounded-lg bg-slate-700 text-white placeholder-slate-500 "
                          "border border-slate-600 px-4 py-2 focus:outline-none "
                          "focus:ring-2 focus:ring-blue-500 transition-all "
                          (when @loading? "opacity-50 cursor-not-allowed"))}]]

           [:div {:class "space-y-4"}
            [:button
             {:class (str "mt-8 w-full rounded-lg py-2 font-semibold text-white transition-all flex items-center justify-center "
                          (if (or fields-empty? @loading?)
                            "bg-slate-600 cursor-not-allowed opacity-70"
                            "bg-blue-600 hover:bg-blue-700 active:transform active:scale-95 shadow-lg shadow-blue-900/20"))
              :disabled (or fields-empty? @loading?)
              :on-click #(rf/dispatch [:login/login-request])}

             (if @loading?
               [:span {:class "flex items-center"}
                [:svg {:class "animate-spin -ml-1 mr-3 h-5 w-5 text-white" :fill "none" :viewBox "0 0 24 24"}
                 [:circle {:class "opacity-25" :cx "12" :cy "12" :r "10" :stroke "currentColor" :stroke-width "4"}]
                 [:path {:class "opacity-75" :fill "currentColor" :d "M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"}]]
                "Autenticando..."]
               "Entrar")]]]

          [:p {:class "mt-6 text-slate-500 text-xs text-center"}
           "Esqueceu sua senha? " [:a {:href "#"
                                       :class "text-blue-400 hover:underline"
                                       :on-click (fn [e]
                                                   (.preventDefault e)
                                                   (rfe/push-state :forgot-password))}
                                   "Recuperar"]]]]))}))