(ns pages.weekly
  (:require [hiccup2.core :as h]))

(def -body
  (h/html
   [:h1 "Weekly updates!"]
   [:p "Coming soon!"]))

(def get-page
  {:location "diary/index.html"
   :contents {:title "Weekly updates!"
              :body (h/html -body)}})
