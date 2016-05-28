(ns pusher.core
  (:require [cheshire.core :refer [parse-string generate-string]])
  (:import [com.pusher.rest Pusher]
           [com.pusher.rest.data Result]))

(defn pusher
  "Creates a Pusher object."
  (^Pusher [creds] (pusher creds true))
  (^Pusher [creds encrypted?]
   (doto (Pusher. (:app-id creds) (:api-key creds) (:api-secret creds))
     (.setEncrypted encrypted?))))

(defn result->map
  [^Result result]
  {:status  (.getHttpStatus result)
   :message (-> result (.getMessage) parse-string)})

(defn push!
  "Sends an event to one or more channels. You can trigger an event to at most 10 channels at once.
  Passing more than 10 channels will cause an exception to be thrown.

  In order to avoid the client that triggered the event from also receiving it, the trigger function takes an
  optional socketId parameter. For more information see:
  http://pusher.com/docs/publisher_api_guide/publisher_excluding_recipients."
  ([p channel-or-channels event msg] (push! p channel-or-channels event msg nil))
  ([p channel-or-channels event msg socket-id]
   (-> p (.trigger channel-or-channels event msg socket-id) result->map)))

(defn authenticate
  [p socket-id channel]
  (.authenticate p socket-id channel))

(comment
  (def creds {:app-id     (System/getenv "APP_ID")
              :api-key    (System/getenv "API_KEY")
              :api-secret (System/getenv "API_SECRET")})
  )