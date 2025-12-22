(ns clojure-finance-client.shared.components.confirmation-modal.confirmation-modal-view
  (:require
   [re-frame.core :as rf]))

(defn confirmation-modal []
  (let [{:keys [show? title message]} @(rf/subscribe [:modal/confirmation-state])]
    (when show?
      [:div {:class "fixed inset-0 z-[100] flex items-center justify-center p-4 bg-slate-950/80 backdrop-blur-sm"}
       [:div {:class "w-full max-w-sm bg-slate-900 border border-slate-800 rounded-2xl shadow-2xl overflow-hidden animate-in fade-in zoom-in duration-200"}
        [:div {:class "p-6 text-center"}
         [:div {:class "w-12 h-12 rounded-full bg-red-500/10 text-red-500 flex items-center justify-center mx-auto mb-4"}
          [:svg {:class "w-6 h-6" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
           [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"}]]]

         [:h3 {:class "text-lg font-bold text-white mb-2"} title]
         [:p {:class "text-slate-400 text-sm"} message]]

        [:div {:class "p-4 bg-slate-800/50 flex gap-3"}
         [:button {:class "flex-1 px-4 py-2 rounded-xl text-slate-400 hover:text-white hover:bg-slate-700 transition"
                   :on-click #(rf/dispatch [:modal/close-confirm])}
          "Cancelar"]
         [:button {:class "flex-1 px-4 py-2 rounded-xl bg-red-600 hover:bg-red-500 text-white font-bold transition shadow-lg shadow-red-900/20"
                   :on-click #(rf/dispatch [:modal/confirm-action])}
          "Excluir"]]]])))