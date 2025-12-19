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

          [:div {:class "min-h-screen bg-slate-900 flex justify-center p-4 pt-12"} ;; pt-12 alinha ao topo
           [:div {:class "w-full max-w-2xl bg-slate-950 border border-slate-800 rounded-2xl shadow-2xl p-8 h-fit"}

            ;; HEADER
            [:div {:class "flex justify-between items-center mb-8 border-b border-slate-800 pb-4"}
             [:div
              [:h1 {:class "text-2xl font-bold text-white"} "Área do Usuário"]
              [:p {:class "text-slate-500 text-xs mt-1"} (str "ID: " (:id user))]]

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
              [:div {:class "flex flex-col items-center py-20"}
               [:div {:class "animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-500 mb-4"}]
               [:p {:class "text-slate-500"} "Carregando dados..."]]

              (nil? user)
              [:p {:class "text-slate-500 text-center py-10"} "Usuário não encontrado"]

              :else
              [:div {:class "space-y-8"}

               ;; SEÇÃO: STATUS E SALDO (Cards rápidos)
               [:div {:class "grid grid-cols-1 md:grid-cols-2 gap-4"}
                [:div {:class "bg-slate-900/50 border border-slate-800 p-4 rounded-xl"}
                 [:p {:class "text-slate-500 text-xs uppercase tracking-wider mb-1"} "Saldo em Conta"]
                 [:p {:class "text-2xl font-mono text-emerald-400"} (str "R$ " (:balance user))]]

                [:div {:class "bg-slate-900/50 border border-slate-800 p-4 rounded-xl flex items-center justify-between"}
                 [:div
                  [:p {:class "text-slate-500 text-xs uppercase tracking-wider mb-1"} "Status da Conta"]
                  [:p {:class "text-sm font-medium text-slate-200"} (if (:active user) "Ativa" "Inativa")]]
                 [:div {:class (str "h-3 w-3 rounded-full " (if (:active user) "bg-emerald-500 shadow-[0_0_10px_rgba(16,185,129,0.5)]" "bg-red-500"))}]]]

               ;; SEÇÃO: INFORMAÇÕES PESSOAIS (Grid 2 colunas)
               [:section
                [:h2 {:class "text-xs font-uppercase tracking-widest text-slate-500 mb-4 uppercase"} "Informações Pessoais"]
                [:div {:class "grid grid-cols-1 md:grid-cols-2 gap-y-6 gap-x-4 bg-slate-900/30 p-6 rounded-xl border border-slate-800/50"}
                 [:div
                  [:p {:class "text-slate-500 text-[10px] uppercase"} "Nome Completo"]
                  [:p {:class "text-slate-200 font-medium"} (:name user)]]

                 [:div
                  [:p {:class "text-slate-500 text-[10px] uppercase"} "CPF"]
                  [:p {:class "text-slate-200 font-medium"} (:cpf user)]]

                 [:div
                  [:p {:class "text-slate-500 text-[10px] uppercase"} "E-mail"]
                  [:p {:class "text-slate-200 font-medium"} (:email user)]]

                 [:div
                  [:p {:class "text-slate-500 text-[10px] uppercase"} "Telefone"]
                  [:p {:class "text-slate-200 font-medium"} (:phone user)]]

                 [:div
                  [:p {:class "text-slate-500 text-[10px] uppercase"} "Membro desde"]
                  [:p {:class "text-slate-400 text-sm"} (subs (:created-at user) 0 10)]] ;; Pega apenas a data YYYY-MM-DD

                 [:div
                  [:p {:class "text-slate-500 text-[10px] uppercase"} "Tipo de Perfil"]
                  [:span {:class "text-[10px] bg-indigo-500/10 text-indigo-400 px-2 py-0.5 rounded border border-indigo-500/20 uppercase"}
                   (:role user)]]]]

               ;; SEÇÃO: DADOS BANCÁRIOS
               (if bank
                 [:section
                  [:h2 {:class "text-xs font-uppercase tracking-widest text-slate-500 mb-4 uppercase"} "Cartão de Crédito Ativo"]
                  [:div {:class "bg-gradient-to-br from-slate-800 to-slate-900 p-6 rounded-2xl border border-slate-700 shadow-inner relative overflow-hidden"}
                   ;; Detalhe visual do card
                   [:div {:class "absolute -right-4 -top-4 w-24 h-24 bg-white/5 rounded-full"}]

                   [:div {:class "flex justify-between items-start mb-8"}
                    [:p {:class "text-slate-300 font-mono italic tracking-wider"} (:card-brand bank)]
                    [:div {:class "h-10 w-14 bg-gradient-to-r from-yellow-500/20 to-yellow-600/40 rounded-md border border-yellow-500/20"}]]

                   [:p {:class "text-2xl text-white tracking-[0.25em] mb-6 font-mono"}
                    (str "•••• •••• •••• " (:card-last-4 bank))]

                   [:div {:class "flex justify-between items-end"}
                    [:div
                     [:p {:class "text-[10px] text-slate-500 uppercase mb-1"} "Titular do Cartão"]
                     [:p {:class "text-sm text-slate-200 uppercase font-medium tracking-wide"} (:card-holder bank)]]
                    [:div {:class "text-right"}
                     [:p {:class "text-[10px] text-slate-500 uppercase mb-1"} "Validade"]
                     [:p {:class "text-sm text-slate-200 font-mono"}
                      (str (:expires-month bank) "/" (:expires-year bank))]]]]]

                 [:div {:class "border-2 border-dashed border-slate-800 rounded-xl p-8 text-center"}
                  [:p {:class "text-slate-600 text-sm"} "Nenhum método de pagamento cadastrado."]])])]]))})))