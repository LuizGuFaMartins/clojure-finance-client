(ns clojure-finance-client.pages.client.profile.profile-view
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]))

(defn page [_]
  (let [user-id-sub (rf/subscribe [:current-user-id])]
    (r/create-class
     {:component-did-mount
      (fn [_]
        (let [id @user-id-sub]
          (when id
            (rf/dispatch [:user/load id])
            (rf/dispatch [:bank-data/load id]))))

      :reagent-render
      (fn [_]
        (let [user     @(rf/subscribe [:user/profile])
              bank     @(rf/subscribe [:user/bank-data])
              loading? @(rf/subscribe [:user/loading?])]
          [:div {:class "min-h-screen bg-slate-900 flex items-center justify-center p-4"}
           [:div {:class "w-full max-w-xl bg-slate-950 border border-slate-800 rounded-2xl shadow-2xl p-8"}

            ;; HEADER COM BOTÃO DE LOGOUT
            [:div {:class "flex justify-between items-center mb-8 border-b border-slate-800 pb-4"}
             [:h1 {:class "text-2xl font-bold text-white"}
              "Área do Usuário"]
             [:button
              {:class "flex items-center gap-2 px-3 py-1.5 text-sm font-medium text-slate-400 
                       hover:text-red-400 hover:bg-red-400/10 rounded-lg transition-all"
               :on-click #(rf/dispatch [:auth/logout])}
              [:svg {:class "w-4 h-4" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
               [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2"
                       :d "M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"}]]
              "Sair"]]

            (cond
              loading?
              [:div {:class "flex flex-col items-center py-10"}
               [:div {:class "animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-500 mb-4"}]
               [:p {:class "text-slate-500"} "Carregando dados..."]]

              (nil? user)
              [:p {:class "text-slate-500 text-center py-10"} "Usuário não encontrado"]

              :else
              [:div {:class "space-y-8"}
               ;; Seção: Dados Pessoais
               [:section
                [:h2 {:class "text-xs font-uppercase tracking-widest text-slate-500 mb-3 uppercase"} "Informações Pessoais"]
                [:div {:class "bg-slate-900/50 p-4 rounded-lg"}
                 [:p {:class "text-lg font-medium text-slate-200"} (:name user)]
                 [:p {:class "text-slate-400 text-sm"} (:email user)]]]

               ;; Seção: Dados Bancários
               (if bank
                 [:section
                  [:h2 {:class "text-xs font-uppercase tracking-widest text-slate-500 mb-3 uppercase"} "Cartão de Crédito"]
                  [:div {:class "bg-gradient-to-br from-slate-800 to-slate-900 p-6 rounded-xl border border-slate-700 shadow-inner"}
                   [:div {:class "flex justify-between items-start mb-6"}
                    [:p {:class "text-slate-300 font-mono italic"} (:card-brand bank)]
                    [:div {:class "h-8 w-12 bg-slate-700/50 rounded-md"}]] ; Ícone placeholder

                   [:p {:class "text-xl text-white tracking-[0.2em] mb-4 font-mono"}
                    (str "•••• •••• •••• " (:card-last-4 bank))]

                   [:div {:class "flex justify-between"}
                    [:div
                     [:p {:class "text-[10px] text-slate-500 uppercase"} "Titular"]
                     [:p {:class "text-sm text-slate-300 uppercase"} (:card-holder bank)]]
                    [:div {:class "text-right"}
                     [:p {:class "text-[10px] text-slate-500 uppercase"} "Validade"]
                     [:p {:class "text-sm text-slate-300"}
                      (str (:expires-month bank) "/" (:expires-year bank))]]]]]

                 [:div {:class "border-2 border-dashed border-slate-800 rounded-xl p-6 text-center"}
                  [:p {:class "text-slate-600 text-sm"} "Nenhum método de pagamento cadastrado."]])])]]))})))