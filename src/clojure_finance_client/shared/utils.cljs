(ns clojure-finance-client.shared.utils)

(defn validate-password [password]
  (if (empty? password)
    {:valid? false
     :errors ["Mínimo de 8 caracteres"
              "Uma letra maiúscula"
              "Uma letra minúscula"
              "Um número"
              "Um caractere especial"]}
    (let [has-min-length? (>= (count password) 8)
          has-special?    (boolean (re-find #"[!@#$%^&*(),.?\":{}|<>]" password))
          has-number?     (boolean (re-find #"\d" password))
          has-upper?      (boolean (re-find #"[A-Z]" password))
          has-lower?      (boolean (re-find #"[a-z]" password))]
      {:valid? (and has-min-length?
                    has-special?
                    has-number?
                    has-upper?
                    has-lower?)
       :errors (cond-> []
                 (not has-min-length?) (conj "Mínimo de 8 caracteres")
                 (not has-upper?)      (conj "Uma letra maiúscula")
                 (not has-lower?)      (conj "Uma letra minúscula")
                 (not has-number?)     (conj "Um número")
                 (not has-special?)    (conj "Um caractere especial"))})))
