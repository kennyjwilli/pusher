(ns pusher.core
  (:require [cljsjs.pusher]
            [beicon.core :as s]))

(defn pusher
  ([app-key opts] (js/Pusher. app-key (clj->js (or opts {}))))
  ([app-key] (pusher app-key nil)))

(defn socket-id [p] (aget p "connection" "socket_id"))

(defn on-pusher-event
  [p event handler-fn]
  (.bind (aget p "connection") event handler-fn))

(defn on-connected
  [p handler-fn]
  (on-pusher-event p "connected" handler-fn))

(defn on-message
  [p handler-fn]
  (on-pusher-event p "message" handler-fn))

(defn on-disconnected
  [p handler-fn]
  (on-pusher-event p "disconnected" handler-fn))

(defn on-connecting-in
  [p handler-fn]
  (on-pusher-event p "connecting_in" handler-fn))

(defn on-state-change
  [p handler-fn]
  (on-pusher-event p "state_change" (fn [states] (-> states js->clj handler-fn))))

(defn on-error
  [p handler-fn]
  (on-pusher-event p "error" handler-fn))

(defn connection-state [p] (aget p "connection" "state"))

(defn connected? [p] (= (connection-state p) "connected"))

(defn channel
  [p channel]
  (.subscribe p channel))

(defn disconnect
  [p]
  (.disconnect p))

(defn on-channel-event
  ([ch event on-value-fn {:keys [parse-fn]
                          :or   {parse-fn identity}}]
   (let []
     (.bind ch event (fn [v] (-> v parse-fn on-value-fn)))))
  ([ch event on-value-fn] (on-channel-event ch event on-value-fn {})))

(defn subscribe
  "Subscribes to a pusher event stream and returns a stream of events. Closing
  the bus will have no effect."
  ([ch event opts]
   (let [bus (s/bus)]
     (on-channel-event ch event (fn [v] (s/push! bus v)) opts)
     bus))
  ([ch event] (subscribe ch event {})))