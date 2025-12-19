(ns clojure-finance-client.pages.forgot-password.forgot-password-view
  (:require [clojure.string :as str]
            [re-frame.core :as rf]
            [reitit.frontend.easy :as rfe]))

(defn- input-field [{:keys [label type placeholder value on-change disabled]}]
  [:div
   [:label {:class "text-xs font-medium text-slate-400 ml-1"} label]
   [:input {:type type :placeholder placeholder :value value :disabled disabled
            :on-change #(on-change (-> % .-target .-value))
            :class "w-full mt-1 rounded-lg bg-slate-700 text-white border border-slate-600 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 transition-all"}]])

(defn- step-email [form loading?]
  [:div {:class "space-y-6"}
   [input-field {:label "E-mail de Recuperação" :type "email" :placeholder "seu@email.com"
                 :value (:email form) :disabled loading?
                 :on-change #(rf/dispatch [:forgot-password/set-field :email %])}]
   [:button {:class "w-full bg-blue-600 py-2.5 rounded-lg font-semibold text-white hover:bg-blue-700"
             :on-click #(rf/dispatch [:forgot-password/send-email]) :disabled loading?}
    (if loading? "Enviando..." "Enviar Código")]])

(defn- step-code [form loading?]
  [:div {:class "space-y-6"}
   [:p {:class "text-xs text-blue-400 text-center"} (str "Código enviado para " (:email form))]
   [input-field {:label "Código de Verificação" :type "text" :placeholder "000000"
                 :value (:code form) :disabled loading?
                 :on-change #(rf/dispatch [:forgot-password/set-field :code %])}]
   [:button {:class "w-full bg-blue-600 py-2.5 rounded-lg font-semibold text-white hover:bg-blue-700"
             :on-click #(rf/dispatch [:forgot-password/verify-code]) :disabled loading?}
    (if loading? "Verificando..." "Validar Código")]])

(defn- step-password [form loading? pass-err]
  [:div {:class "space-y-6"}
   [input-field {:label "Nova Senia" :type "password" :placeholder "••••••••"
                 :value (:password form) :disabled loading?
                 :on-change #(rf/dispatch [:forgot-password/set-field :password %])}]
   [input-field {:label "Confirmar Senha" :type "password" :placeholder "••••••••"
                 :value (:confirm-password form) :disabled loading?
                 :on-change #(rf/dispatch [:forgot-password/set-field :confirm-password %])}]
   (when pass-err [:p {:class "text-xs text-red-400"} "Verifique os requisitos da senha."])
   [:button {:class (str "w-full py-2.5 rounded-lg font-semibold text-white "
                         (if (or pass-err (str/blank? (:password form))) "bg-slate-600" "bg-blue-600"))
             :on-click #(rf/dispatch [:forgot-password/reset-password])
             :disabled (or loading? pass-err)}
    (if loading? "Redefinindo..." "Confirmar Nova Senha")]])

(defn page []
  (let [step     (rf/subscribe [:forgot-password/step])
        form     (rf/subscribe [:forgot-password/form])
        loading? (rf/subscribe [:forgot-password/loading?])
        pass-err (rf/subscribe [:forgot-password/password-validation-error])]
    (fn []
      [:div {:class "min-h-screen flex items-center justify-center bg-slate-900 px-4"}
       [:div {:class "w-full max-w-sm bg-slate-800 p-8 rounded-2xl shadow-xl border border-slate-700"}
        [:button {:on-click #(rfe/push-state :login) :class "text-slate-500 text-xs mb-6"} "← Voltar"]
        [:h1 {:class "text-2xl font-bold text-white mb-2"} "Recuperar Conta"]

        (case @step
          :email    [step-email @form @loading?]
          :code     [step-code @form @loading?]
          :password [step-password @form @loading? @pass-err])]])))