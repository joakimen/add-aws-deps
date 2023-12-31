(ns add-aws-deps
  (:require [babashka.http-client :as http]
            [fzf.core :refer [fzf]]
            [zprint.core :refer [zprint-str]]
            [clojure.java.io :as io]))

(def api-version-url "https://raw.githubusercontent.com/cognitect-labs/aws-api/main/latest-releases.edn")

(defn query-cognitect-versions
  "fetch latest api versions from cognitect repo"
  []
  (let [{:keys [status body]} (http/get api-version-url)]
    (when-not (= 200 status)
      (throw (ex-info (str "non-200 response from api version url: " api-version-url) {:babashka/exit 1})))
    body))

(defn parse-versions
  "read api versions into edn and keep relevant keys"
  [body]
  (-> body read-string
      (update-vals #(dissoc % :aws/serviceFullName))))

(defn select-deps
  "select one or more deps from the list"
  [deps]
  (->> (keys deps)
       (fzf {:multi true})
       (map read-string)
       (select-keys deps)))

(defn add-deps
  "write `deps` to the `:deps`-key of `outfile`, merging if deps already exists"
  [file deps]
  (let [content (try (-> file slurp read-string)
                     (catch java.io.FileNotFoundException _ {}))
        updated-content (update content :deps merge deps)]
    (spit file (zprint-str updated-content {:map {:sort? false
                                                  :comma? false}}))))
(defn get-all-versions []
  (let [cognitect-versions (parse-versions (query-cognitect-versions))
        awyeah-versions (-> (io/resource "awyeah-api.edn") slurp read-string)]
    (merge cognitect-versions awyeah-versions)))

(let [outfile (or (first *command-line-args*) "deps.edn")
      selected-versions (select-deps (get-all-versions))]
  (when-not (empty? selected-versions)
    (println "adding" (-> selected-versions keys count) "dependencies to:" outfile)
    (doseq [[k v] selected-versions]
      (println "+" k v))
    (add-deps outfile selected-versions)))
