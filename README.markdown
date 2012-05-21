What it is ? 
=============

A library to access the echonest API by clojure.

The library is based on [http.async.client](https://github.com/neotyk/http.async.client)

Installation
============

By using lein just add at your dependencies:

```clj
(defproject my-project "1.0.0"
  :dependencies [[org.clojure/clojure "1.2.1"]
				 [echonest-api "0.0.1"]])
```

Code Example
============

Before to make any operation must be set an API key, the key can be request [here](http://developer.echonest.com/)

And set your own API key

```clj
(set-api-key! "YOUR API KEY")
```

Find news about "The Beatles"
-----------------------------

```clj
echonest-api.core> (analyze-response (basic-query "artist" "news" :query {:name "The Beatles"}))
{:response {:status {:version "4.2", :code 0, :message "Success"}, :start 0, :total 4121, :news [{:name "\"Ruby Tuesday,\" The Rolling Stones", :url "http://www.americansongwriter.com/2012/05/ruby-tuesday-the-rolling-stones/", :summary "always enjoy singing it.\" Keith Richards actually did...
```
Find the twitter accounnt of "shakira"
--------------------------------------
```clj
echonest-api.core> (analyze-response (basic-query "artist" "twitter" :query {:name "shakira"}))
{:response {:status {:version "4.2", :code 0, :message "Success"}, :artist {:twitter "shakira", :id "AR6PJ8R1187FB5AD70", :name "Shakira"}}}
```

It is possible to upload a song, to let a recognizion of the same song:
```clj
echonest-api.core> (upload-song "/home/siscia/Music/Move Like Jagger - Maroon 5.mp3" :query {:filetype "mp3"})
echonest-api.core> (analyze-response *1)
{:response {:status {:version "4.2", :code 0, :message "Success"}, :track {:status "complete", :audio_md5 "a02d45a7d3d9b9e29343f9b642e4e7ec", :artist "Maroon 5", :samplerate 44100, :title "Moves Like Jagger (Sex Ray Vision Remix)", :analyzer_version "3.1.0_beta_5", :bitrate 320, :release "", :id "TRPIYYY1372839546F", :md5 "44cadacdae7d5331962fd9b2fd35b8ef"}}}
```