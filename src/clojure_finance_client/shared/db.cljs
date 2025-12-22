(ns clojure-finance-client.shared.db)

(def default-db
  {:login/login-form {:email "" :password ""}
   :login/loading? false
   :login/error nil

   :password-reset/password-reset-form {:password "" :confirm-password ""}
   :password-reset/loading? false
   :password-reset/error nil

   :admin/users []
   :admin/loading? false

   :user/current-user nil
   :user/profile nil
   :user/bank-data nil
   :user/loading? false

   :confirmation-modal {:show? false
                        :title ""
                        :message ""
                        :on-confirm nil}})
