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
Find the twitter account of "shakira"
--------------------------------------
```clj
echonest-api.core> (analyze-response (basic-query "artist" "twitter" :query {:name "shakira"}))
{:response {:status {:version "4.2", :code 0, :message "Success"}, :artist {:twitter "shakira", :id "AR6PJ8R1187FB5AD70", :name "Shakira"}}}
```

Recognize song
--------------
```clj
echonest-api.core> (upload-song "/home/siscia/Music/Move Like Jagger - Maroon 5.mp3" :query {:filetype "mp3"})
echonest-api.core> (analyze-response *1)
{:response {:status {:version "4.2", :code 0, :message "Success"}, :track {:status "complete", :audio_md5 "a02d45a7d3d9b9e29343f9b642e4e7ec", :artist "Maroon 5", :samplerate 44100, :title "Moves Like Jagger (Sex Ray Vision Remix)", :analyzer_version "3.1.0_beta_5", :bitrate 320, :release "", :id "TRPIYYY1372839546F", :md5 "44cadacdae7d5331962fd9b2fd35b8ef"}}}
```

How it's work
=============

To use the library in the best possible way is neccesary know the [API](http://developer.echonest.com/docs/v4/index.html), basic-query take two string and a dictionary, the first string rappresent the category you are looking for ([artist](http://developer.echonest.com/docs/v4/artist.html), [song](http://developer.echonest.com/docs/v4/song.html), [track](http://developer.echonest.com/docs/v4/track.html), [playlist](http://developer.echonest.com/docs/v4/playlist.html), [catalog](http://developer.echonest.com/docs/v4/catalog.html), [sandbox](http://developer.echonest.com/docs/v4/sandbox.html) and [oauth](http://developer.echonest.com/docs/v4/oauth.html)) the second string rappresent what field you are asking (in the case of song you could ask search, [profile](http://developer.echonest.com/docs/v4/song.html#profile), or identify), the dictionary is the parameter you are passing (for song/profille valid paramaters are id, tack_id, format, bucket and limit).

Every request take the :query dictionary, is not necessary pass the api_key every single time.

The function ```analyze-response``` take the response returned by ```basic-query```, if everthing worked fine it return a nice map with the info we was looking for, in case of every problems the function will throw an exception.

For now the library works only in a **synchronous** way.