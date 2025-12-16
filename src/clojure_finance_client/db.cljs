(ns clojure-finance-client.db)

(def default-db
  {:admin/users []
   :admin/loading? false
   :admin/error nil

   :user/profile nil
   :user/bank-data nil
   :user/loading? false
   :user/error nil})