(ns clojure-finance-client.views
  (:require
   [re-frame.core :as rf]))

(defn loading-screen []
  [:div {:class "flex flex-col items-center justify-center h-screen bg-slate-900"}
   [:div {:class "relative w-20 h-20"}
    [:div {:class "absolute top-0 left-0 w-full h-full border-4 border-blue-500/20 border-t-blue-500 rounded-full animate-spin"}]
    [:div {:class "absolute top-2 left-2 w-16 h-16 border-4 border-cyan-400/10 border-t-cyan-400 rounded-full animate-spin-slow"}]]

   [:div {:class "mt-8 text-center"}
    [:h2 {:class "text-xl font-semibold text-white tracking-wide"}
     "Clojure Finance"]
    [:p {:class "mt-2 text-slate-400 text-sm animate-pulse"}
     "Sincronizando sua conta..."]]

   [:div {:class "mt-6 w-48 h-1 bg-slate-800 rounded-full overflow-hidden"}
    [:div {:class "w-full h-full bg-gradient-to-r from-blue-500 to-cyan-400 animate-progress-indefinite"}]]])

(defn not-found-screen []
  [:div {:class "flex flex-col items-center justify-center h-screen bg-slate-900 px-4 text-center"}
   [:div {:class "relative mb-8"}
    [:div {:class "text-9xl font-black text-slate-800 select-none"} "404"]
    [:div {:class "absolute inset-0 flex items-center justify-center"}
     [:div {:class "w-24 h-24 bg-blue-500/10 rounded-full blur-2xl animate-pulse"}]
     [:div {:class "text-2xl font-bold text-blue-400"} "Ops!"]]]

   [:h2 {:class "text-2xl font-bold text-white mb-2"}
    "Página não encontrada"]
   [:p {:class "text-slate-400 max-w-md mb-8 leading-relaxed"}
    "O link que você tentou acessar pode estar quebrado ou a página foi movida para outro endereço."]

   [:button
    {:on-click #(rf/dispatch [:route/handle-navigation {:data {:name :login}}]) ;
     :class "group relative flex items-center gap-2 px-6 py-3 bg-slate-800 hover:bg-slate-700 
             text-white rounded-xl transition-all duration-300 border border-slate-700"}
    [:svg {:class "w-5 h-5 text-blue-400 group-hover:-translate-x-1 transition-transform"
           :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
     [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M10 19l-7-7m0 0l7-7m-7 7h18"}]]
    "Voltar para o início"]])

(defn root []
  (let [route           @(rf/subscribe [:current-route])
        session-loaded? @(rf/subscribe [:session-loaded?])]
    [:div {:class "transition-opacity duration-500"}
     (if-not session-loaded?
       [loading-screen]
       (if route
         (let [view (get-in route [:data :view])]
           [view])
         [not-found-screen]))]))