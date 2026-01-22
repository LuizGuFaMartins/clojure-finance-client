(ns clojure-finance-client.config
  (:require [aero.core :as aero]
            [clojure.java.io :as io]))

(defmacro get-api-url []
  (let [config (aero/read-config (io/resource "config.edn"))]
    (get-in config [:api :base-url])))

(defmacro get-auth-api-url []
  (let [config (aero/read-config (io/resource "config.edn"))]
    (get-in config [:auth-api :base-url])))