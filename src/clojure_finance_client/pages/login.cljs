(ns clojure-finance-client.pages.login
  (:require
   [reitit.frontend.easy :as rfe]))

(defn page []
  [:div {:class "min-h-screen flex items-center justify-center bg-slate-900"}
   [:div {:class "w-full max-w-sm bg-slate-800 p-8 rounded-2xl shadow-xl"}

    [:h1 {:class "text-2xl font-bold text-white mb-6 text-center"}
     "Login"]

    [:div {:class "space-y-4"}
     [:input
      {:type "email"
       :placeholder "Email"
       :class "w-full rounded-lg bg-slate-700 text-white placeholder-slate-400
               border border-slate-600 px-4 py-2 focus:outline-none
               focus:ring-2 focus:ring-blue-500"}]

     [:input
      {:type "password"
       :placeholder "Senha"
       :class "w-full rounded-lg bg-slate-700 text-white placeholder-slate-400
               border border-slate-600 px-4 py-2 focus:outline-none
               focus:ring-2 focus:ring-blue-500"}]]

    [:button
     {:class "mt-6 w-full rounded-lg bg-blue-600 py-2 font-semibold text-white
              hover:bg-blue-700 transition-colors"
      :on-click #(rfe/push-state :admin)}
     "Entrar"]]])
