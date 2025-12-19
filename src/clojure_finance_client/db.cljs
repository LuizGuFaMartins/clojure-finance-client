(ns clojure-finance-client.db)

(def default-db
  {
   :login-form {:email "" :password ""} ; Adicione isso aqui
   :current-user nil
   
   :admin/users []
   :admin/loading? false
   :admin/error nil

   :user/profile nil
   :user/bank-data nil
   :user/loading? false
   :user/error nil})