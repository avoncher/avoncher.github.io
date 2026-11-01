(ns dev
  (:require [babashka.http-server :as http-server]
            [build]))

(defn start-server
  [port] (http-server/exec {:dir build/output-location :port port}))
