(ns clojure-finance-client.pages.admin
  (:require
   [re-frame.core :as rf]
   [reagent.core :as reagent]
   [reitit.frontend.easy :as rfe]))

(defn users-table [users]
  [:div {:class "overflow-x-auto"}
   [:table {:class "min-w-full text-sm text-left text-zinc-300"}
    [:thead {:class "text-xs uppercase bg-slate-800 text-zinc-400"}
     [:tr
      [:th {:class "px-6 py-3"} "ID"]
      [:th {:class "px-6 py-3"} "Nome"]
      [:th {:class "px-6 py-3"} "Email"]
      [:th {:class "px-6 py-3"} "Saldo"]]]
    [:tbody
     (for [{:keys [id name email balance]} users]
       ^{:key id}
       [:tr {:class "border-b border-zinc-800 hover:bg-slate-800"}
        [:td {:class "px-6 py-4"} id]
        [:td {:class "px-6 py-4 font-medium text-white"} name]
        [:td {:class "px-6 py-4"} email]
        [:td {:class "px-6 py-4 text-emerald-400"}
         (str "R$ " balance)]])]]])

(defn page [_]
  (reagent/create-class
   {:component-did-mount
    #(do
       (rf/dispatch [:users/load]))

    :reagent-render
    (fn [_]
      (let [users    @(rf/subscribe [:admin/users])
            loading? @(rf/subscribe [:user/loading?])]

        (cond
          loading?
          [:div {:class "flex flex-col items-center py-10"}
           [:div {:class "animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-500 mb-4"}]
           [:p {:class "text-zinc-500"} "Carregando dados..."]]

          (nil? users)
          [:p {:class "text-zinc-500 text-center py-10"} "Usuário não encontrado"]

          :else
          [:div {:class "min-h-screen bg-slate-900 flex items-center justify-center"}
           [:div {:class "w-full max-w-5xl bg-slate-950 rounded-2xl shadow-xl p-8"}
            [:div {:class "flex justify-between items-center mb-6"}
             [:h1 {:class "text-2xl font-bold text-white"} "Admin"]
             [:button
              {:class "bg-slate-800 text-zinc-200 px-4 py-2 rounded-lg hover:bg-slate-700 transition"
               :on-click #(rfe/push-state :user)}
              "Área do usuário"]]

            [:p {:class "text-zinc-400 mb-4"} "Lista de usuários cadastrados"]

            (if (seq users)
              [users-table users]
              [:p {:class "text-zinc-500"} "Carregando usuários..."])]])))}))

