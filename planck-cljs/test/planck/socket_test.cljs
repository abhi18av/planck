(ns planck.socket-test
  (:require
   [clojure.string :as string]
   [clojure.test :refer [deftest is async]]
   [planck.socket :as socket]))

(deftest socket-connect-test
  (async done
    (let [host  "http-test.planck-repl.org"
          port  80
          accum (atom [])
          s     (socket/connect host port
                  (fn [_ data]
                    (prn data)
                    (if data
                      (swap! accum conj data)
                      (let [all (string/join @accum)]
                        (is (string/starts-with? all "HTTP/1.1 200 OK"))
                        (done)))))]
      (socket/write s "GET / HTTP/1.1")
      (socket/write s \newline)
      (socket/write s (str "Host: " host ":" port))
      (socket/write s \newline)
      (socket/write s \newline))))
