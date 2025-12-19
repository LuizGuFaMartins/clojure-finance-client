(ns clojure-finance-client.shared.utils)

(defn validate-password [password]
  (cond
    (nil? password) :length
    (not (<= 8 (count password) 64)) :length
    (re-find #"(.)\1\1" password) :sequential ;; Detecta 3 caracteres iguais seguidos (ex: 111)
    :else nil))