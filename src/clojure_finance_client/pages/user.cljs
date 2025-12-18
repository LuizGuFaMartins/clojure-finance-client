(ns clojure-finance-client.pages.user
  (:require
   [re-frame.core :as rf]
   [reitit.frontend.easy :as rfe]))

(defn page [{:keys [user-id]}]
  (reagent.core/create-class
   {:component-did-mount
    #(do
       (rf/dispatch [:user/load "5ff75e1c-366e-46f3-97ca-0f562a0acdaa"])
       (rf/dispatch [:bank-data/load "5ff75e1c-366e-46f3-97ca-0f562a0acdaa"]))

    :reagent-render
    (fn [{:keys [user-id]}]
      (let [user     @(rf/subscribe [:user/profile])
            bank     @(rf/subscribe [:user/bank-data])
            loading? @(rf/subscribe [:user/loading?])]

        [:div {:class "min-h-screen bg-slate-900 flex items-center justify-center p-4"}
         [:div {:class "w-full max-w-xl bg-slate-950 border border-slate-800 rounded-2xl shadow-2xl p-8"}

          [:h1 {:class "text-2xl font-bold text-white mb-8 border-b border-slate-800 pb-4"} 
           "Área do Usuário"]

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
                [:p {:class "text-slate-600 text-sm"} "Nenhum método de pagamento cadastrado."]])])]]))}))