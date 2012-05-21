(ns echonest-api.core
  (:require [http.async.client :as client])
  (:use [clojure.data.json :only [read-json]])
  (:import [java.io File]))

(def url-base "http://developer.echonest.com/api/v4/")

(def #^{:dynamic true} *api-key* (atom "N6E4NIOVYMTHNDM8J"))

(defn set-api-key! [new-api]
  (reset! *api-key* new-api))

(def #^{:dynamic true} *wait-response* (atom true))

(defn set-wait-policy! [bool]
  (reset! *wait-response* bool))

(defn analyze-response
  "The function simply analyze the response by the echo-nest server throwing simply exception in case of 404, or in case of bad request.
If the exception is a 404 usually means that there is a spell error in the parameters passed to the function that genereated the request.
In the other case is simply a bad request, check again the echonest documentation and read the error message should be more than enough to understand the problem."
  ;;TODO throw different exception for the two different case
  [response]
  (let [code-http (-> response :status deref :code)
        ;; The code that the server response, if everything is fine
        ;; the code will be 200, in case of problem something different
      body (try (-> response client/string read-json)
                (catch Exception e (throw (Exception. (str "Connection Error #: " code-http)))))
        ;; The server will always try to responde with json, in case
        ;; of bad request (400) the server will reply with json
        ;; explaing why it was a bad request, I have experiment that
        ;; only 404 (Not Found) error doesn't reply with json, but
        ;; since i could be wrong the exception will at least define
        ;; what kind of problem the server or the request had.
        code-echo (-> body :response :status :code)
        ;;The code of echonest, zero (0) means that everything is
        ;;fine, for the other code please consult the API doc.
        ]
    (if (and (= code-http 200) (= 0 code-echo))
      ;;If everything is fine return...
      body
      ;;the json that the server responde like a clojure map, if
      ;;something is wrong throw an exception explaing what went wrong.
      (throw (Exception. (str "Error #: " code-echo ": " (-> body :response :status :message)))))))

(defn basic-query ;;TODO maybe i don't want to wait for the response...
  "Basic function for all the GET request, it takes two string and one (is not really an option) map, the first string rappresent the category you are looking for, for instance, \"artist\" or \"song\", the second string rappresent what in that determinate category what you are looking for, for instance \"blogs\" or \"images\", the map get all the other parameter, like the name of the artist or the title of the song. If so i want to find the twitter account of rihanna i will do something like this (search \"artist\" \"twitter\" {:name \"rihanna\"})
This function return a map of promises, not very usefull, suggest to simply call (analyze-response (search \"artist\" \"images\" {:name \"shakira\"})) to get a map of interesting link..."
  [category paramaters & {:keys [query wait-response] :or {wait-response @*wait-response*}}]
    (let [url (str url-base category "/" paramaters "?") ;;Put
          ;;together the url in a very easy way...
          query (conj {"api_key" @*api-key*} query) ;;Make a map of the
          ;;various paramater for the request including the api_key
          ]
      (with-open [client (client/create-client)] ;;Make the request
        (let [response (client/GET client url :query query)]
          (if @*wait-response*
            (client/await response)
            response)))))

(defn upload-song [path-to-song & {queri :query}]
  (let [url (str url-base "track/upload?")
        queri (conj {"api_key" *api-key*} queri)]
    (with-open [client (client/create-client)]
      (let [response (client/POST client url :query queri :body (File. path-to-song) :headers {"Content-Type" "application/octet-stream"})]
        (client/await response)
         response))))

(defn analyze-song [queri]
  (let [url (str url-base "track/analyze?")
        queri (conj {"api_key" @*api-key*} queri)]
    (with-open [client (client/create-client)]
      (let [response (client/POST client url :query queri :headers {"Content-Type" "multipart/form-data"})]
        (client/await response)
        response))))

(defn- POST-REQUEST
  "Utility to send a generic POST request DO NOT wait for the realization of the response"
  [url & {:keys [query body headers wait-response] :or {wait-response @*wait-response*}}]
  (with-open [client (client/create-client)]
    (let [response (client/POST client url :query query :body body :headers headers)]
      (if wait-response
        (client/await response)
        response))))

(defn create-catalog [name type & {:keys [query wait-response]}]
  (POST-REQUEST (str url-base "catalog/create") :query (conj query {:name name :type type :api_key @*api-key*}) :headers {"Content-Type" "multipart/form-data"} :wait-response wait-response))

(defn update-catalog [id data & {:keys [query wait-response]}]
  "Easiest way data = (slurp \"path/of/the/json.json\") "
  (POST-REQUEST (str url-base "catalog/update") :body {:id id :api_key @*api-key* :data data} :query query :wait-response wait-response))

(defn delete-catalog [id & {:keys [query wait-response]}]
  (POST-REQUEST (str url-base "catalog/delete") :body {:id id :api_key @*api-key*} :query query :wait-response wait-response))

