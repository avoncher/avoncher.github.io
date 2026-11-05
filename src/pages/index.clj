(ns pages.index
  (:require [hiccup2.core :as h]
            [nextjournal.markdown :as md]
            [clojure.java.io :as io])
  (:import (java.time LocalDate)
           (java.time.format DateTimeFormatter)
           (java.util Locale)))

(def message-of-the-day "Good morning! Scroll down for a new article!")

(def spezi
  [:div {:style {:display "flex"
                 :justify-content "center"
                 :align-items "center"}}
   [:a {:href "https://mishka.gay/webrings/speziring?action=previous"
        :title "previous"}
    [:img {:src "https://mishka.gay/webrings/speziring/images/left.png"
           :alt "previous"}]]

   [:div {:style {:display "flex"
                  :flex-direction "column"
                  :align-items "center"}}
    [:a {:href "https://mishka.gay/webrings/speziring"}
     [:img {:src "https://mishka.gay/webrings/speziring/images/spezi-bottle-pixel.png"
            :alt "Spezi Ring"
            :height 130}]]
    [:a {:href "https://mishka.gay/webrings/speziring?action=random"
         :title "random"}
     "random"]]

   [:a {:href "https://mishka.gay/webrings/speziring?action=next"
        :title "next"}
    [:img {:src "https://mishka.gay/webrings/speziring/images/right.png"
           :alt "next"}]]])

(defn get-post-content
  ([post-name] (-> (slurp (io/resource (str "pages/posts/" post-name ".md")))
                   md/parse
                   md/->hiccup))
  ([post-name paragraph-count]
   (let [[tag & children] (get-post-content post-name)]
     (into [tag] (take paragraph-count children)))))

(defn get-post-content-with-link
  ([title post-name]
   (h/html [:h2 [:a {:href (str "/posts/" post-name)} title]]
           (get-post-content post-name)))
  ([title post-name paragraph-count]
   (h/html [:h2 [:a {:href (str "/posts/" post-name)} title]]
           (get-post-content post-name paragraph-count)
           [:p [:a {:href (str "/posts/" post-name)} "Full article on page " (+ (rand-int 50) 1) "."]])))

(def date-fmt (DateTimeFormatter/ofPattern "EEEE, MMMM d, yyyy" Locale/US))

(def -title
  [:div#title
   [:div#title-intro "Proudly Presenting The Latest"]
   [:h1 "Avoncher"]
   [:div#title-outro (.format (LocalDate/now) date-fmt)]])

(def -body
  (h/html
   [:div#paper
    -title
    [:div#motd message-of-the-day]
    [:div#columns-container
     [:div#columns.sf-grid
      [:div.col-4.column
       [:h2 [:a {:href "/diary"} "The Wondrous Weekly Diary Now Revealed"]]
       [:p "Every week now in review — Is this finally a solution to faux accountability?"]
       [:p "Starting with the 44th week of 2026, I plan on documenting how each week progresses. Generally you are on your best behaviour if you know that other people are watching!"]
       [:p "Imagine a YouTube video, but instead of watching it at the end with all the clips spliced in, you're watching it in real time, and maybe it never gets finished lol."]
       [:p [:a {:href "/diary"} "More on page 2."]]
       [:hr]

       (get-post-content-with-link "Put Your Phone Away!"
                                   "put-your-phone-away")
       [:hr]
       [:h2 "Artifcial Intelligence Usage"]
       [:p "The vast majority of this site was hand coded by yours truly. The images on this site are not AI generated, but may have minor modifications from an AI, including but not limited to changing the background colour. The prose on the website is handwritten in totality."]]
      [:div.col-8.column
       [:img {:src "/img/ship.png"}]
       [:p "Look at how cool this ship is looking! Quite distinguished. Let's hope it is royalty free."]
       [:hr]
       [:div.sf-grid
        [:div.col-6.column
         [:h2 [:a {:href "/posts/lessons"} "Lessons Learnt: A 21 Year Reflection"]]
         [:p "As my twenty first birthday rolled around, it got me thinking about the things I have learnt, and what I wish I could have told my younger self. After pondering for a while, and having things float around my brain, twelve items feature in this Avoncher exclusive compilation."]
         [:p [:a {:href "/posts/lessons"} "Full article on page 10."]]
         [:hr]
         [:div.ad
          spezi]
         [:p "When I was in Germany, I discovered Spezi, and it is one of my favourite drinks! I highly recommend trying some, especially with some ice and a slice of orange."]
         [:hr]
         (get-post-content-with-link "My Goals"
                                     "my-goals")
         [:hr]
         (get-post-content-with-link  "Almost Getting Hit By Cars"
                                      "almost-getting-hit-by-cars")]
        [:div.col-6.column
         (get-post-content-with-link "New Years Resolutions"
                                     "new-years-resolutions"
                                     3)

         [:hr]
         (get-post-content-with-link "You're Doing Long Term Goals Wrong!"
                                     "long-term-goals"
                                     1)

         [:hr]
         [:h2 "Contact"]
         [:p "Go to 39°52'59.2\"N 25°13'23.8\"E. You'll know what to do when you get there."]
         [:hr]
         (get-post-content-with-link "The Count of Monte Cristo"
                                     "the-count-of-monte-cristo")]]]]]]))

(def get-page
  {:location "index.html"
   :contents {:title "Avoncher"
              :append-head (h/html [:link {:rel "stylesheet" :href "/css/pages/index.css"}])
              :body -body}})
