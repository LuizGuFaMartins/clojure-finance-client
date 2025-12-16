(ns clojure-finance-client.pages.user
  (:require
   [re-frame.core :as rf]
   [reitit.frontend.easy :as rfe]))

(defn page
  [{:keys [user-id]}]

  ;; (rf/dispatch [:user/load user-id])

  (rf/dispatch [:user/load "5ff75e1c-366e-46f3-97ca-0f562a0acdaa"])

  (rf/dispatch [:bank-data/load "5ff75e1c-366e-46f3-97ca-0f562a0acdaa"])

  (let [user @(rf/subscribe [:user/profile])
        bank @(rf/subscribe [:user/bank-data])
        loading? @(rf/subscribe [:user/loading?])]

    [:div {:class "min-h-screen bg-zinc-900 flex items-center justify-center"}
     [:div {:class "w-full max-w-xl bg-zinc-950 rounded-2xl shadow-xl p-8"}

      [:h1 {:class "text-2xl font-bold text-white mb-6"} "Área do Usuário"]

      (cond
        loading?
        [:p {:class "text-zinc-500 text-center"} "Carregando dados..."]

        (nil? user)
        [:p {:class "text-zinc-500 text-center"} "Usuário não encontrado"]

        :else
        [:<>
         [:div {:class "mb-6"}
          [:h2 {:class "text-lg font-semibold text-white mb-2"} "Dados do usuário"]
          [:p {:class "text-zinc-300"} (:name user)]
          [:p {:class "text-zinc-400"} (:email user)]]

         (when bank
           [:div
            [:h2 {:class "text-lg font-semibold text-white mb-2"} "Dados bancários"]
            [:p {:class "text-zinc-300"}
             (str (:card-brand bank) " •••• " (:card-last4 bank))]])])]]))
