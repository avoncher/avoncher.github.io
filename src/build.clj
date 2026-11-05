(ns build
  (:require [hiccup2.core :as h]
            [clojure.java.io :as io]
            [clojure.tools.build.api :as b]
            [pages.index]
            [pages.weekly]
            [pages.posts]))

(def output-location "output/")
(def static-location "static/")

(def stylesheets
  ["/css/normalize.css"
   "/css/sf-grid.css"
   "/css/style.css"])

(defn page-structure [{:keys [title append-head body]}]
  (str
   (h/html
    (h/raw "<!doctype html>")
    [:html {:lang "en"}
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport"
              :content "width=device-width, initial-scale=1.0"}]
      [:title title]
      (for [stylesheet stylesheets]
        [:link {:rel "stylesheet" :href stylesheet}])
      (h/raw "<script data-goatcounter=\"https://avoncher.goatcounter.com/count\" async src=\"//gc.zgo.at/count.js\"></script>")
      append-head]
     [:body body]])))

(defn create-page [page]
  (io/make-parents (str output-location (:location page)))
  (spit (str output-location (:location page))
        (page-structure (:contents page))))

(defn delete-directory-recursive
  [^java.io.File file]
  (when (.isDirectory file)
    (run! delete-directory-recursive (.listFiles file)))
  (io/delete-file file))

(defn clean-folder-name [name]
  (if (= (last name) "/")
    (drop-last name)
    name))

(defn -main []
  (println "Building...")

  (println "Deleting old build directory.")
  (delete-directory-recursive (io/file output-location))
  (println "Copying static resources.")
  (b/copy-dir {:src-dirs   [(clean-folder-name static-location)]
               :target-dir (clean-folder-name output-location)})

  (println "Creating pages...")
  (create-page pages.index/get-page)

  (create-page pages.posts/post-index)
  (mapv create-page pages.posts/posts)

  (create-page pages.weekly/get-page)
  ;; (pages.weekly/create-weekly-pages!)

  (println "Built!"))
