(ns clojure-finance-client.pages.admin.users-list.users-list-view
  (:require
   [clojure-finance-client.shared.components.confirmation-modal.confirmation-modal-view :refer [confirmation-modal]]
   [re-frame.core :as rf]
   [reagent.core :as reagent]))

(defn user-modal []
  (let [modal @(rf/subscribe [:admin/modal])
        form  @(rf/subscribe [:admin/user-form])
        loading? @(rf/subscribe [:user/loading?])]
    (when (:show? modal)
      [:div {:class "fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm"}
       [:div {:class "w-full max-w-md bg-slate-900 border border-slate-800 rounded-2xl shadow-2xl overflow-hidden"}
        [:div {:class "p-6 border-b border-slate-800"}
         [:h3 {:class "text-xl font-bold text-white"}
          (if (= (:mode modal) :create) "Novo Usuário" "Editar Usuário")]]

        [:div {:class "p-6 space-y-4"}
         [:div
          [:label {:class "block text-xs font-medium text-slate-400 mb-1"} "Nome"]
          [:input {:class "w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-white focus:ring-2 focus:ring-indigo-500 outline-none"
                   :value (:name form) :on-change #(rf/dispatch [:admin/set-form-field :name (-> % .-target .-value)])}]]

         [:div {:class "grid grid-cols-2 gap-4"}
          [:div
           [:label {:class "block text-xs font-medium text-slate-400 mb-1"} "Email"]
           [:input {:class "w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-white outline-none"
                    :value (:email form) :on-change #(rf/dispatch [:admin/set-form-field :email (-> % .-target .-value)])}]]
          [:div
           [:label {:class "block text-xs font-medium text-slate-400 mb-1"} "Senha"]
           [:input {:type "password" :class "w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-white outline-none"
                    :value (:password form) :on-change #(rf/dispatch [:admin/set-form-field :password (-> % .-target .-value)])}]]]

         [:div {:class "grid grid-cols-2 gap-4"}
          [:div
           [:label {:class "block text-xs font-medium text-slate-400 mb-1"} "CPF"]
           [:input {:class "w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-white outline-none"
                    :value (:cpf form) :on-change #(rf/dispatch [:admin/set-form-field :cpf (-> % .-target .-value)])}]]
          [:div
           [:label {:class "block text-xs font-medium text-slate-400 mb-1"} "Telefone"]
           [:input {:class "w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-white outline-none"
                    :value (:phone form) :on-change #(rf/dispatch [:admin/set-form-field :phone (-> % .-target .-value)])}]]]]

        [:div {:class "p-6 bg-slate-900/50 flex justify-end gap-3"}
         [:button {:class "px-4 py-2 text-slate-400 hover:text-white transition"
                   :on-click #(rf/dispatch [:admin/close-modal])} "Cancelar"]
         [:button {:class "bg-indigo-600 hover:bg-indigo-500 text-white px-6 py-2 rounded-lg font-bold transition shadow-lg shadow-indigo-900/20"
                   :on-click #(rf/dispatch [:admin/save-user]) :disabled loading?}
          (if loading? "Salvando..." "Salvar Usuário")]]]])))

(defn users-table [users]
  [:div {:class "overflow-x-auto mt-4"}
   [:table {:class "min-w-full text-sm text-left text-zinc-300"}
    [:thead {:class "text-xs uppercase bg-slate-800/50 text-zinc-400"}
     [:tr
      [:th {:class "px-6 py-4"} "Nome"]
      [:th {:class "px-6 py-4"} "Email"]
      [:th {:class "px-6 py-4"} "Saldo"]
      [:th {:class "px-6 py-4 text-center"} "Ações"]]]
    [:tbody
     (for [{:keys [id name email balance] :as user} users]
       ^{:key id}
       [:tr {:class "border-b border-zinc-800/50 hover:bg-slate-800/30 transition-colors"}
        [:td {:class "px-6 py-4 font-medium text-white"} name]
        [:td {:class "px-6 py-4 text-zinc-400"} email]
        [:td {:class "px-6 py-4 text-emerald-400 font-mono"} (str "R$ " balance)]
        [:td {:class "px-6 py-4 text-center"}
         [:div {:class "flex justify-center gap-3"}
          [:button {:class "text-blue-400 hover:text-blue-300 p-1 transition"
                    :title "Editar"
                    :on-click #(rf/dispatch [:admin/open-modal :edit user])}
           [:svg {:class "w-5 h-5" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
            [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"}]]]

          [:button {:class "text-red-400 hover:text-red-300 p-1 transition"
                    :title "Excluir"
                    :on-click #(rf/dispatch [:modal/show-confirm
                                             {:title "Excluir Usuário"
                                              :message "Tem certeza? Esta ação não pode ser desfeita."
                                              :on-confirm [:admin/delete-user id]}])}
           [:svg {:class "w-5 h-5" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
            [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"}]]]]]])]]])

(defn page [_]
  (reagent/create-class
   {:component-did-mount
    #(rf/dispatch [:users/load])

    :reagent-render
    (fn [_]
      (let [users (:users @(rf/subscribe [:admin/users]))
            loading? @(rf/subscribe [:user/loading?])]

        [:div {:class "min-h-screen bg-slate-900 p-4 md:p-8"}
         [:div {:class "w-full max-w-6xl mx-auto bg-slate-950 rounded-2xl shadow-2xl border border-slate-800 overflow-hidden"}

          ;; Header
          [:div {:class "p-8 border-b border-slate-800 bg-slate-900/50"}
           [:div {:class "flex flex-col md:flex-row justify-between items-start md:items-center gap-4"}
            [:div
             [:h1 {:class "text-3xl font-bold text-white"} "Painel Admin"]
             [:p {:class "text-zinc-500 text-sm mt-1"} "Gerenciamento de usuários e saldos"]]

            [:div {:class "flex gap-3"}
             ;; Botão Logout
             [:button
              {:class "flex items-center gap-2 bg-slate-800 text-zinc-300 px-4 py-2 rounded-lg hover:bg-red-900/30 hover:text-red-400 transition"
               :on-click #(rf/dispatch [:login/logout])}
              [:svg {:class "w-4 h-4" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
               [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"}]]
              "Sair"]]]]

          ;; Conteúdo Central
          [:div {:class "p-8"}
           [:div {:class "flex justify-between items-center mb-6"}
            [:h2 {:class "text-xl font-semibold text-white"} "Usuários Cadastrados"]

            ;; Botão Novo Usuário
            [:button
             {:class "flex items-center gap-2 bg-indigo-600 text-white px-5 py-2.5 rounded-xl font-semibold hover:bg-indigo-500 shadow-lg shadow-indigo-900/20 transition active:scale-95"
              :on-click #(rf/dispatch [:admin/open-modal :create])}
             [:svg {:class "w-5 h-5" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
              [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M12 4v16m8-8H4"}]]
             "Cadastrar Usuário"]]

           (cond
             loading?
             [:div {:class "flex flex-col items-center py-20"}
              [:div {:class "animate-spin rounded-full h-10 w-10 border-b-2 border-indigo-500 mb-4"}]
              [:p {:class "text-zinc-500"} "Buscando base de dados..."]]

             (empty? users)
             [:div {:class "text-center py-20 border-2 border-dashed border-slate-800 rounded-2xl"}
              [:p {:class "text-zinc-500"} "Nenhum usuário encontrado no sistema."]]

             :else
             [users-table users])]
          [user-modal]
          [confirmation-modal]]]))}))