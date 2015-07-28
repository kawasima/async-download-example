(ns async-download-example.core
  (:require [async-download-example.handler :refer [app init destroy parse-port]]
            [immutant.web :as immutant]
            [taoensso.timbre :as timbre]
            [environ.core :refer [env]])
  (:gen-class))

(defn http-port [port]
  (parse-port (or port (env :port) 3000)))

(defonce server (atom nil))

(defn start-server [port]
  (init)
  (reset! server (immutant/run app :port port)))

(defn stop-server []
  (when @server
    (destroy)
    (immutant/stop @server)
    (reset! server nil)))

(defn start-app [[port]]
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-server))
  (start-server (http-port port))
  (timbre/info "server started on port:" (:port @server)))

(defn -main [& args]
  (start-app args))
