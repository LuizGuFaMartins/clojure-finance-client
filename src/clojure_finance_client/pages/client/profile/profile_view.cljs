(ns clojure-finance-client.pages.client.profile.profile-view
  (:require
   [clojure-finance-client.shared.components.confirmation-modal.confirmation-modal-view :refer [confirmation-modal]]
   [re-frame.core :as rf]
   [reagent.core :as r]))

(defn bank-data-modal []
  (let [modal    @(rf/subscribe [:bank-data/modal])
        form     @(rf/subscribe [:bank-data/form])
        loading? @(rf/subscribe [:user/loading?])
        user     @(rf/subscribe [:user/profile])]
    (when (:show? modal)
      [:div {:class "fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm"}
       [:div {:class "w-full max-w-md bg-slate-900 border border-slate-800 rounded-2xl shadow-2xl overflow-hidden"}
        [:div {:class "p-6 border-b border-slate-800"}
         [:h3 {:class "text-xl font-bold text-white"}
          (if (= (:mode modal) :create) "Cadastrar Cartão" "Editar Cartão")]]

        [:div {:class "p-6 space-y-4"}
         [:div
          [:label {:class "block text-xs font-medium text-slate-400 mb-1"} "Titular do Cartão"]
          [:input {:class "w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-white outline-none focus:ring-2 focus:ring-indigo-500"
                   :value (:card-holder form)
                   :on-change #(rf/dispatch [:bank-data/set-form-field :card-holder (-> % .-target .-value)])}]]

         [:div {:class "grid grid-cols-2 gap-4"}
          [:div
           [:label {:class "block text-xs font-medium text-slate-400 mb-1"} "Bandeira"]
           [:input {:class "w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-white outline-none"
                    :value (:card-brand form)
                    :on-change #(rf/dispatch [:bank-data/set-form-field :card-brand (-> % .-target .-value)])}]]
          [:div
           [:label {:class "block text-xs font-medium text-slate-400 mb-1"} "Últimos 4 dígitos"]
           [:input {:max-length 4
                    :class "w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-white outline-none"
                    :value (:card-last-4 form)
                    :on-change #(rf/dispatch [:bank-data/set-form-field :card-last-4 (-> % .-target .-value)])}]]]

         [:div {:class "grid grid-cols-2 gap-4"}
          [:div
           [:label {:class "block text-xs font-medium text-slate-400 mb-1"} "Mês Expiração"]
           [:input {:type "number" :min 1 :max 12
                    :class "w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-white outline-none"
                    :value (:expires-month form)
                    :on-change #(rf/dispatch [:bank-data/set-form-field :expires-month (js/parseInt (-> % .-target .-value))])}]]
          [:div
           [:label {:class "block text-xs font-medium text-slate-400 mb-1"} "Ano Expiração"]
           [:input {:type "number"
                    :class "w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-white outline-none"
                    :value (:expires-year form)
                    :on-change #(rf/dispatch [:bank-data/set-form-field :expires-year (js/parseInt (-> % .-target .-value))])}]]]]

        [:div {:class "p-6 bg-slate-900/50 flex justify-end gap-3"}
         [:button {:class "px-4 py-2 text-slate-400 hover:text-white transition"
                   :on-click #(rf/dispatch [:bank-data/close-modal])} "Cancelar"]
         [:button {:class "bg-indigo-600 hover:bg-indigo-500 text-white px-6 py-2 rounded-lg font-bold transition shadow-lg shadow-indigo-900/20"
                   :on-click #(rf/dispatch [:bank-data/save (:id user)])
                   :disabled loading?}
          (if loading? "Salvando..." "Confirmar")]]]])))

(defn profile-info []
  (let [user     @(rf/subscribe [:user/profile])
        bank     @(rf/subscribe [:user/bank-data])]

    [:div {:class "space-y-8"}

     [:div {:class "grid grid-cols-1 md:grid-cols-2 gap-4"}
      [:div {:class "bg-slate-900/50 border border-slate-800 p-4 rounded-xl"}
       [:p {:class "text-slate-500 text-xs uppercase tracking-wider mb-1"} "Saldo em Conta"]
       [:p {:class "text-2xl font-mono text-emerald-400"} (str "R$ " (:balance user))]]

      [:div {:class "bg-slate-900/50 border border-slate-800 p-4 rounded-xl flex items-center justify-between"}
       [:div
        [:p {:class "text-slate-500 text-xs uppercase tracking-wider mb-1"} "Status da Conta"]
        [:p {:class "text-sm font-medium text-slate-200"} (if (:active user) "Ativa" "Inativa")]]
       [:div {:class (str "h-3 w-3 rounded-full " (if (:active user) "bg-emerald-500 shadow-[0_0_10px_rgba(16,185,129,0.5)]" "bg-red-500"))}]]]

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
        [:p {:class "text-slate-400 text-sm"} (subs (:created-at user) 0 10)]]

       [:div
        [:p {:class "text-slate-500 text-[10px] uppercase"} "Tipo de Perfil"]
        [:span {:class "text-[10px] bg-indigo-500/10 text-indigo-400 px-2 py-0.5 rounded border border-indigo-500/20 uppercase"}
         (:role user)]]]]

     [:div {:class "space-y-8"}
      [:section
       [:div {:class "flex justify-between items-end mb-4"}
        [:h2 {:class "text-xs font-uppercase tracking-widest text-slate-500 uppercase"} "Dados do cartão"]
        (when bank
          [:button {:class "text-[10px] text-red-500 hover:text-red-400 font-bold uppercase transition"
                    :on-click #(rf/dispatch [:modal/show-confirm
                                             {:title "Excluir dados do cartão"
                                              :message "Tem certeza? Esta ação não pode ser desfeita."
                                              :on-confirm [:bank-data/delete (:id bank)]}])}
           "Excluir Cartão"])]

       (if bank
         [:div {:class "bg-gradient-to-br from-slate-800 to-slate-900 p-6 rounded-2xl border border-slate-700 relative overflow-hidden"}
          [:div {:class "flex justify-between items-start mb-8"}
           [:p {:class "text-slate-300 font-mono italic"} (:card-brand bank)]
           [:div {:class "h-10 w-14 bg-yellow-500/20 rounded-md border border-yellow-500/20"}]]
          [:p {:class "text-2xl text-white tracking-[0.25em] mb-6 font-mono"} (str "•••• •••• •••• " (:card-last-4 bank))]
          [:div {:class "flex justify-between items-end"}
           [:div [:p {:class "text-[10px] text-slate-500 uppercase"} "Titular"] [:p {:class "text-sm text-slate-200 uppercase"} (:card-holder bank)]]
           [:div {:class "text-right"} [:p {:class "text-[10px] text-slate-500 uppercase"} "Validade"] [:p {:class "text-sm text-slate-200 font-mono"} (str (:expires-month bank) "/" (:expires-year bank))]]]]
         [:div {:class "border-2 border-dashed border-slate-800 rounded-xl p-8 text-center text-slate-600 text-sm"}
          "Nenhum cartão cadastrado."])]

      [:button
       {:class "w-full flex items-center justify-center gap-2 bg-indigo-600 text-white px-5 py-3 rounded-xl font-semibold hover:bg-indigo-500 transition active:scale-95"
        :on-click #(rf/dispatch [:bank-data/open-modal (if bank :edit :create) bank])}
       [:svg {:class "w-5 h-5" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
        [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M12 4v16m8-8H4"}]]
       (if bank "Alterar Cartão" "Adicionar Cartão")]]]))

(defn transactions-modal []
  (let [modal    @(rf/subscribe [:transactions/modal])
        form     @(rf/subscribe [:transactions/form])
        loading? @(rf/subscribe [:transactions/loading?])]
    (when (:show? modal)
      [:div {:class "fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md animate-in fade-in duration-200"}
       [:div {:class "w-500 bg-slate-900 border border-slate-800 rounded-3xl shadow-2xl overflow-hidden"}

        [:div {:class "p-6 text-center border-b border-slate-800/50"}
         [:div {:class "w-12 h-12 bg-emerald-500/10 text-emerald-500 rounded-full flex items-center justify-center mx-auto mb-3"}
          [:svg {:class "w-6 h-6" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
           [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2"
                   :d "M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"}]]]
         [:h3 {:class "text-xl font-bold text-white"} "Realizar Transação"]
         [:p {:class "text-slate-500 text-[10px] uppercase tracking-widest mt-1"} "Transferência entre contas"]]

        [:div {:class "p-6 space-y-6"}

         [:div
          [:label {:class "block text-[10px] uppercase font-bold text-slate-500 mb-2 ml-1"} "ID do Destinatário"]
          [:input {:type "text"
                   :placeholder "Ex: 550e8400-e29b..."
                   :class "w-full bg-slate-950 border border-slate-800 rounded-2xl px-4 py-3 text-sm text-white font-mono outline-none focus:ring-2 focus:ring-indigo-500/50 transition placeholder:text-slate-800"
                   :value (or (:to_user form) "")
                   :on-change #(rf/dispatch [:transactions/set-form-field :to_user (-> % .-target .-value)])}]]

         [:div
          [:label {:class "block text-[10px] uppercase font-bold text-slate-500 mb-2 ml-1"} "Valor"]
          [:div {:class "relative"}
           [:span {:class "absolute left-5 top-1/2 -translate-y-1/2 text-slate-600 font-bold text-xl"} "R$"]
           [:input {:type "number" :step "0.01" :placeholder "0,00"
                    :class "w-full bg-slate-950 border border-slate-800 rounded-2xl pl-14 pr-4 py-5 text-3xl font-mono text-emerald-400 outline-none focus:ring-2 focus:ring-emerald-500/50 transition shadow-inner"
                    :value (or (:amount form) nil)
                    :on-change #(rf/dispatch [:transactions/set-form-field :amount (-> % .-target .-value)])}]]]

         [:div {:class "p-6 pt-0 space-y-3"}
          [:button {:class (str "w-full py-4 rounded-2xl font-bold text-sm transition-all active:scale-[0.98] shadow-lg "
                                (if (or loading? (empty? (str (:to_user form))) (empty? (str (:amount form))))
                                  "bg-slate-800 text-slate-600 cursor-not-allowed"
                                  "bg-indigo-600 hover:bg-indigo-500 text-white shadow-indigo-900/20"))
                    :on-click #(rf/dispatch [:transactions/save])
                    :disabled (or loading? (empty? (str (:to_user form))) (empty? (str (:amount form))))}
           (if loading?
             [:div {:class "flex items-center justify-center gap-2"}
              [:div {:class "w-4 h-4 border-2 border-white/20 border-t-white rounded-full animate-spin"}]
              "Processando..."]
             "Confirmar Transação")]

          [:button {:class "w-full py-2 text-sm font-medium text-slate-500 hover:text-slate-300 transition"
                    :on-click #(rf/dispatch [:transactions/close-modal])}
           "Cancelar"]]]]])))

(defn transactions-list []
  (let [transactions @(rf/subscribe [:transactions/list])
        loading?     @(rf/subscribe [:transactions/loading?])
        filters      @(rf/subscribe [:transactions/filters])
        user-id      @(rf/subscribe [:user/current-user-id])]
    [:div {:class "space-y-6 animate-in fade-in duration-500"}
     [:div {:class "flex justify-between items-start"}
      [:div {:class "flex justify-center items-center h-10"}
       [:h1 {:class "text-[15px] font-uppercase tracking-widest text-slate-500 uppercase"} "Histórico Financeiro"]]

      [:div {:class "flex items-center gap-2"}
       [:button
        {:class "p-2 text-slate-400 hover:text-emerald-400 bg-slate-900 border border-slate-800 rounded-lg transition-all group"
         :on-click #(rf/dispatch [:transactions/load])}
        [:svg {:class (str "w-4 h-4 " (when loading? "animate-spin")) :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
         [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2"
                 :d "M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"}]]]

       [:button
        {:class "flex items-center gap-2 bg-emerald-600 hover:bg-emerald-500 text-white text-[11px] font-bold py-2 px-4 rounded-lg transition-all shadow-lg shadow-emerald-900/20"
         :on-click #(rf/dispatch [:transactions/open-modal :create])}
        [:svg {:class "w-3.5 h-3.5" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
         [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M12 4v16m8-8H4"}]]
        "Nova transferência"]]]

     [:div {:class "flex flex-wrap items-center justify-between gap-4 bg-slate-900/40 p-3 rounded-2xl border border-slate-800/50"}
      [:div {:class "flex items-center gap-1 bg-slate-950 p-1 rounded-xl border border-slate-800"}
       (for [[id label] [["all" "Tudo"] ["debit" "Saídas"] ["credit" "Entradas"]]]
         ^{:key id}
         [:button
          {:class (str "px-4 py-1.5 text-[10px] font-bold uppercase tracking-wider rounded-lg transition-all "
                       (if (= (:type filters) id)
                         "bg-indigo-500/20 text-indigo-400 border border-indigo-500/30"
                         "text-slate-500 hover:text-slate-300 border border-transparent"))
           :on-click #(rf/dispatch [:transactions/set-filter :type id])}
          label])]

      [:div {:class "flex items-center gap-3"}
       [:label {:class "text-[10px] text-slate-500 uppercase font-bold"} "Período"]
       [:select
        {:class "bg-slate-950 border border-slate-800 text-slate-300 text-[11px] rounded-lg px-3 py-1.5 outline-none focus:border-indigo-500"
         :value (or (:days filters) "30")
         :on-change #(rf/dispatch [:transactions/set-filter :days (-> % .-target .-value)])}
        [:option {:value "7"} "Últimos 7 dias"]
        [:option {:value "30"} "Últimos 30 dias"]
        [:option {:value "90"} "Últimos 90 dias"]]]]

     (cond
       loading? [:div {:class "py-20 text-center"} [:div {:class "animate-spin inline-block w-6 h-6 border-2 border-indigo-500 border-t-transparent rounded-full"}]]

       (empty? transactions) [:div {:class "py-20 text-center border-2 border-dashed border-slate-900 rounded-3xl"}
                              [:p {:class "text-slate-600"} "Nenhuma transação encontrada para este período."]]

       :else [:div {:class "space-y-3"}
              (for [tx transactions]
                (let [from-id    (get-in tx [:from_user :id])
                      is-debit?  (= (str from-id) (str user-id))

                      date-raw   (:created_at tx)
                      date-str   (if date-raw (subs date-raw 0 10) "---")]

                  ^{:key (:id tx)}
                  [:div {:class "bg-slate-900/30 hover:bg-slate-900/60 border border-slate-800/40 p-4 rounded-2xl flex justify-between items-center transition-all group"}
                   [:div {:class "flex items-center gap-4"}
                    [:div {:class (str "p-2 rounded-full " (if is-debit? "bg-red-500/10 text-red-500" "bg-emerald-500/10 text-emerald-500"))}
                     [:svg {:class "w-4 h-4" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
                      (if is-debit?
                        [:path {:stroke-width "2" :d "M16 17l-4 4m0 0l-4-4m4 4V3"}]
                        [:path {:stroke-width "2" :d "M8 7l4-4m0 0l4 4m-4-4v18"}])]]

                    [:div
                     [:p {:class "text-slate-200 text-sm font-medium"}
                      (if is-debit?
                        (str "Para: " (get-in tx [:to_user :name]))
                        (str "De: "   (get-in tx [:from_user :name])))]
                     [:p {:class "text-slate-600 text-[10px]"} date-str]]]

                   [:div {:class "text-right"}
                    [:p {:class (str "font-mono font-bold " (if is-debit? "text-slate-200" "text-emerald-400"))}
                     (str (if is-debit? "- " "+ ") "R$ " (:amount tx))]
                    [:p {:class "text-[9px] uppercase tracking-tighter text-slate-500"} (:status tx)]]]))])]))

(defn page [_]
  (let [user-id-sub (rf/subscribe [:user/current-user-id])]
    (r/create-class
     {:component-did-mount
      (fn [_]
        (let [id @user-id-sub]
          (when id
            (rf/dispatch [:user/load id])
            (rf/dispatch [:bank-data/load id])
            (rf/dispatch [:transactions/load]))))

      :reagent-render
      (fn [_]
        (let [user     @(rf/subscribe [:user/profile])
              loading? @(rf/subscribe [:user/loading?])
              current-tab @(rf/subscribe [:user/active-tab])]

          [:div {:class "min-h-screen bg-slate-900 flex justify-center p-4 pt-12"}
           [:div {:class "w-full max-w-2xl bg-slate-950 border border-slate-800 rounded-2xl shadow-2xl p-8 h-fit"}

            [:div {:class "flex justify-between items-center mb-8 border-b border-slate-800 pb-4"}
             [:div
              [:h1 {:class "text-2xl font-bold text-white"} "Área do Usuário"]
              [:p {:class "text-slate-500 text-xs mt-1"} (str "ID: " (:id user))]]

             [:button
              {:class "flex items-center gap-2 px-3 py-1.5 text-sm font-medium text-slate-400 
                       hover:text-red-400 hover:bg-red-400/10 rounded-lg transition-all"
               :on-click #(rf/dispatch [:login/logout])}
              [:svg {:class "w-4 h-4" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
               [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2"
                       :d "M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"}]]
              "Sair"]]

            [:div {:class "flex gap-4 border-b border-slate-800 mb-8"}
             (for [[id label] [[:personal-info "Perfil"] [:transactions "Transações"]]]
               ^{:key id}
               [:button
                {:class (str "pb-3 text-sm font-medium transition-all relative "
                             (if (= current-tab id) "text-indigo-400" "text-slate-500 hover:text-slate-300"))
                 :on-click #(rf/dispatch [:user/set-active-tab id])}
                label
                (when (= current-tab id)
                  [:div {:class "absolute bottom-0 left-0 w-full h-0.5 bg-indigo-500 animate-in slide-in-from-left-full"}])])]

            (cond
              loading?
              [:div {:class "flex flex-col items-center py-20"}
               [:div {:class "animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-500 mb-4"}]
               [:p {:class "text-slate-500"} "Carregando dados..."]]

              (nil? user)
              [:p {:class "text-slate-500 text-center py-10"} "Usuário não encontrado"]

              :else

              [:div
               (case current-tab
                 :personal-info
                 [:div {:class "space-y-8 animate-in fade-in slide-in-from-bottom-2 duration-300"}
                  [profile-info]]

                 :transactions
                 [transactions-list])])

            [transactions-modal]
            [bank-data-modal]
            [confirmation-modal]]]))})))