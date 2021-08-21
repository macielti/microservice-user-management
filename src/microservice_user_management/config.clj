(ns microservice-user-management.config
  (:require [microservice-user-management.adapters.common :as adapters.common]
            [com.stuartsierra.component :as component]
            [cheshire.core :as json]))

(defrecord Config []
  component/Lifecycle
  (start [this]
    (let [config (json/parse-string (slurp "resources/config.json")
                                    adapters.common/str->keyword-kebab-case)]
      (assoc this :config config)))

  (stop [this]
    (println "Stop Config")
    (assoc this :config nil)))

(defn new-config []
  (->Config))
