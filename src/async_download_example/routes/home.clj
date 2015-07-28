(ns async-download-example.routes.home
  (:require [async-download-example.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :refer [ok]]
            [ring.util.response :refer [content-type resource-response header]]
            [clojure.java.io :as io])
  (:import [java.util UUID]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (POST "/request-download" []
    {:body {:id (.toString (UUID/randomUUID)) :delay (inc (rand-int 20))}})
  (GET "/request-download/:id" [id]
    (if (< (rand-int 10) 5)
      {:body {:status "done" :url (str "/download/" id)}}
      {:body {:status "working" :id id :delay (inc (rand-int 5))}}))
  (GET "/download/:id" [id]
    (-> (resource-response "example.pdf")
        (content-type "application/pdf")
        (header "Content-disposition" (str "attachment; filename=\"" id ".pdf\"") ))))

