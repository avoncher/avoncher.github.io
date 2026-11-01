(ns pages.posts
  (:require [clojure.java.io :as io]
            [nextjournal.markdown :as md]
            [clojure.string :as string]
            [hiccup2.core :as h])
  (:import (java.time Instant ZoneId)
           (java.time.format DateTimeFormatter)
           (java.util Locale)))

(def css-post
  [:link {:rel "stylesheet" :href "/css/pages/posts.css"}])

(def post-files
  (->> (.listFiles (io/file "src/pages/posts"))
       (filter #(.isFile %))
       (map #(.getName %))
       (filter #(string/ends-with? % ".md"))))

(def date-formatter
  (-> (DateTimeFormatter/ofPattern "d MMMM yyyy" Locale/ENGLISH)
      (.withZone (ZoneId/systemDefault))))

(defn last-edited [file-name]
  (->> (io/file "src/pages/posts" file-name)
       .lastModified
       Instant/ofEpochMilli
       (.format date-formatter)))

(def post-index
  {:location  "posts/index.html"
   :contents
   {:title "Posts"
    :append-head css-post
    :body (h/html
           [:h1 "All posts"]
           [:p [:a {:href "/"} "Home"] "."]
           [:ul
            (for [post-name post-files] [:li [:a {:href (str "posts/"
                                                             (string/replace post-name #".md$" ""))} post-name]])])}})

(def posts
  (map (fn [name]
         (let [real-name (string/replace name #".md$" "")]
           {:location (str "posts/" real-name "/index.html")
            :contents
            {:title name
             :append-head css-post
             :body
             (h/html [:h1 name]
                     [:p {:style "color: #777"} "Last edited " (last-edited name) "."]
                     (md/->hiccup (slurp (str "src/pages/posts/" name)))
                     [:p [:a {:href "/"} "Home"] "."])}}))
       post-files))
