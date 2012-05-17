(ns echonest-api.test.core
  (:use [echonest-api.core])
  (:use [clojure.test]
        [midje.sweet])
  (:require [http.async.client :as client])
  (:use [clojure.data.json :only [read-json]]))

(fact ;;Just make sure everything is working
 (+ 1 1) => 2)


;; (fact
;;  (with-open [client (client/create-client)]
;;    (let [resp (upload-song "/home/simo/Music/London Calling - The Clash.mp3" :query {:filetype "mp3"})]
;;      (read-json (client/string resp)))) => (read-json "{\"response\": {\"status\": {\"version\": \"4.2\", \"code\": 0, \"message\": \"Success\"}, \"track\": {\"status\": \"complete\", \"song_id\": \"SOZITHT12812D029DA\", \"audio_md5\": \"fa78064e502aa893429ffcd79470f822\", \"artist\": \"The Clash\", \"samplerate\": 44100, \"title\": \"London Calling\", \"analyzer_version\": \"3.1.0_beta_5\", \"release\": \"Best Of Rock\", \"artist_id\": \"AR13CXU1187B9AD30A\", \"bitrate\": 128, \"id\": \"TRUPOEL134CE9FEF25\", \"md5\": \"edf887ed75efa69d610ba621bb6483c0\"}}}"))

(fact
 (:url (search "artist" "biographies" :query {:name "rihanna"})) =>
 (str "http://developer.echonest.com/api/v4/artist/biographies?name=rihanna&api_key=" @*api-key*))

(fact
 (-> (analyze-response (search "artist" "profile" :query {:name "The beatles"})) :response :artist :id) => "AR6XZ861187FB4CECD")

(fact
 (-> (analyze-response (search "artist" "twitter" :query {:name "The beatles"})) :response :artist :twitter) => "thebeatles")

(fact
 (let [urls (-> (analyze-response (search "artist" "urls" :query {:name "The beatles"})) :response :urls)]
   (:itunes_url urls) => "http://itunes.com/TheBeatles"
   (:wikipedia_url urls) => "http://en.wikipedia.org/wiki/The_Beatles"))

(fact
 (let [song (-> (analyze-response (search "song" "search" :query {:title "I love the way you lie" :artist "rihanna"})) :response :songs (nth 0))]
   (:artist_id song) => "ARYGAUS13442810319"
   (:id song) => "SOGGYLD13521E6CAAA"
   (:artist_name song) => "emminem & rihanna"
   (:title song) => "I Love the Way You Lie"))











