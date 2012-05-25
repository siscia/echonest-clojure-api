(ns echonest-api.test.core
  (:use [echonest-api.core])
  (:use [clojure.test]
        [midje.sweet])
  (:require [http.async.client :as client])
  (:use [clojure.data.json :only [read-json]]))

(fact ;;Just make sure everything is working
 (+ 1 1) => 2)

(fact
 *api-key* => "N6E4NIOVYMTHNDM8J"
 (binding [*api-key* 3] *api-key*) => 3
 (do (set-api-key! "api-key") *api-key*) => "api-key"
 (do (set-api-key! "N6E4NIOVYMTHNDM8J"))
 *api-key* => "N6E4NIOVYMTHNDM8J")


(fact
 *wait-response* => true
 (binding [*wait-response* 3] *wait-response*) => 3
 (do (set-wait-policy! "wait-policy") *wait-response*) => "wait-policy"
 (do (set-wait-policy! true))
 *wait-response* => true)

(fact
 (:url (basic-query "artist" "biographies" :query {:name "rihanna"})) =>
 (str "http://developer.echonest.com/api/v4/artist/biographies?name=rihanna&api_key=" *api-key*))

(fact
 (-> (analyze-response (basic-query "artist" "profile" :query {:name "The beatles"})) :response :artist :id) => "AR6XZ861187FB4CECD")

(fact
 (-> (analyze-response (basic-query "artist" "twitter" :query {:name "The beatles"})) :response :artist :twitter) => "thebeatles")

(fact
 (let [urls (-> (analyze-response (basic-query "artist" "urls" :query {:name "The beatles"})) :response :urls)]
   (:itunes_url urls) => "http://itunes.com/TheBeatles"
   (:wikipedia_url urls) => "http://en.wikipedia.org/wiki/The_Beatles"))

(fact
 (let [song (-> (analyze-response (basic-query "song" "search" :query {:title "Hate that I love you" :artist "rihanna"})) :response :songs (nth 0))]
   (:artist_id song) => "ARKU3Z61187FB51DCA"
   (:id song) => "SOYLDAZ13773AA9C4A"
   (:artist_name song) => "Rihanna"
   (:title song) => "Hate That I Love You"))











