(ns porteiro.diplomatic.http-server
  (:require [porteiro.interceptors.user :as interceptors.user]
            [porteiro.interceptors.password-reset :as interceptors.password-reset]
            [porteiro.interceptors.user-identity :as interceptors.user-identity]
            [porteiro.diplomatic.http-server.healthy :as diplomatic.http-server.healthy]
            [porteiro.diplomatic.http-server.password :as diplomatic.http-server.password]
            [porteiro.diplomatic.http-server.user :as diplomatic.http-server.user]
            [porteiro.diplomatic.http-server.auth :as diplomatic.http-server.auth]
            [porteiro.diplomatic.http-server.contact :as diplomatic.http-server.contact]))


(def routes [["/health" :get diplomatic.http-server.healthy/healthy-check :route-name :health-check]
             ["/users" :post [interceptors.user/username-already-in-use-interceptor
                              diplomatic.http-server.user/create-user!] :route-name :create-user]
             ["/users/contacts" :get [interceptors.user-identity/user-identity-interceptor
                                      diplomatic.http-server.contact/fetch-contacts] :route-name :fetch-contacts]
             ["/users/:id/roles" :post [interceptors.user-identity/user-identity-interceptor
                                        (interceptors.user-identity/user-required-roles-interceptor [:admin])
                                        diplomatic.http-server.user/add-role!] :route-name :add-role-to-user]
             ["/users/auth" :post diplomatic.http-server.auth/authenticate-user! :route-name :user-authentication]
             ["/users/password" :put [interceptors.user-identity/user-identity-interceptor
                                      diplomatic.http-server.password/update-password!] :route-name :password-update]
             ["/users/password-reset" :post diplomatic.http-server.password/reset-password! :route-name :request-password-reset]
             ["/users/password-reset" :put [interceptors.password-reset/valid-password-reset-execution-token
                                            diplomatic.http-server.password/execute-reset-password!] :route-name :execute-password-reset]])
