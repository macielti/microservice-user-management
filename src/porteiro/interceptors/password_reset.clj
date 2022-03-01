(ns porteiro.interceptors.password-reset
  (:require [porteiro.db.datomic.password-reset :as datomic.password-reset]
            [porteiro.adapters.password-reset :as adapters.password-reset]
            [common-clj.error.core :as common-error]))

(def valid-password-reset-execution-token
  {:name  ::valid-password-reset-execution-token
   :enter (fn [{{password-reset    :json-params
                 {:keys [datomic]} :components} :request :as context}]
            (let [reset-password (datomic.password-reset/valid-password-reset-by-token
                                   (-> (adapters.password-reset/wire->password-reset-execution-internal password-reset)
                                       :password-reset-execution/token)
                                   (:connection datomic))]
              (when (empty? reset-password)
                (common-error/http-friendly-exception 401
                                                      "invalid-expired-token"
                                                      "Invalid/Expired password reset token"
                                                      "Invalid/Expired password reset token")))
            context)})
