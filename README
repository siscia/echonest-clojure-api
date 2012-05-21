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
```
Find the twitter accounnt of "shakira"
--------------------------------------
```clj
echonest-api.core> (analyze-response (basic-query "artist" "twitter" :query {:name "shakira"}))
{:response {:status {:version "4.2", :code 0, :message "Success"}, :artist {:twitter "shakira", :id "AR6PJ8R1187FB5AD70", :name "Shakira"}}}
```
And so on, for every request made by GET the patern is always the same, for request made by POST I wrote different function, simply read the source code.