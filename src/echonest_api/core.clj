(ns echonest-api.core
  ^{:doc "Library to access the echonest API"
       :author "Simone Mosciatti"}
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
  ;;TODO throw different exception for the two different case
  [response]
  (let [code-http (-> response :status deref :code)
      body (try (-> response client/string read-json)
                (catch Exception e (throw (Exception. (str "Connection Error #: " code-http)))))
        code-echo (-> body :response :status :code)]
    (if (and (= code-http 200) (= 0 code-echo))
      body
      (throw (Exception. (str "Error #: " code-echo ": " (-> body :response :status :message)))))))

(defn basic-query ;;TODO maybe i don't want to wait for the response...
  [category paramaters & {:keys [query wait-response] :or {wait-response @*wait-response*}}]
  (letfn [(inner [_]
            (let [url (str url-base category "/" paramaters "?")
                  query (conj {"api_key" @*api-key*} query)]
              (with-open [client (client/create-client)]
                (let [response (client/GET client url :query query)]
                  (client/await response)))))]
    (if wait-response
      (inner nil)
      (send (agent nil) inner))))

(defn upload-song [path-to-song & {:keys [query wait-response] :or {wait-response @*wait-response*}}]
  (letfn [(inner [_]
            (let [url (str url-base "track/upload?")
                queri (conj {"api_key" @*api-key*} query)]
              (with-open [client (client/create-client)]
                (let [response (client/POST client url :query queri :body (File. path-to-song) :headers {"Content-Type" "application/octet-stream"} :timeout -1)]
                  (client/await response)))))]
    (if wait-response
      (inner nil)
      (send (agent nil) inner))))

(defn- POST-REQUEST
  "Utility to send a generic POST request DO NOT wait for the realization of the response"
  [url & {:keys [query body headers wait-response] :or {wait-response @*wait-response*}}]
  (letfn [(inner [_]
            (with-open [client (client/create-client)]
              (let [response (client/POST client url :query query :body body :headers headers)]
                (client/await response))))]
    (if wait-response
      (inner nil)
      (send (agent nil) inner))))

(defn analyze-track [{:keys [query wait-response] :or {wait-response @*wait-response*}}]
  (POST-REQUEST (str url-base "track/analyze?")
                :query (conj {"api_key" @*api-key*} query)
                :headers {"Content-Type" "multipart/form-data"}
                :wait-response wait-response))

(defn create-catalog [name type & {:keys [query wait-response]}]
  (POST-REQUEST (str url-base "catalog/create")
                :query (conj query {:name name :type type :api_key @*api-key*})
                :headers {"Content-Type" "multipart/form-data"} :wait-response wait-response))

(defn update-catalog [id data & {:keys [query wait-response]}]
  "Easiest way data = (slurp \"path/of/the/json.json\") "
  (POST-REQUEST (str url-base "catalog/update")
                :body {:id id :api_key @*api-key* :data data}
                :query query
                :wait-response wait-response))

(defn delete-catalog [id & {:keys [query wait-response]}]
  (POST-REQUEST (str url-base "catalog/delete")
                :body {:id id :api_key @*api-key*}
                :query query
                :wait-response wait-response))
