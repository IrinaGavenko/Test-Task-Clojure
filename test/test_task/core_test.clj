(ns test-task.core-test
  (:require [clojure.test :refer :all]
            [test-task.core :as core]))

(deftest my-frequencies
  (are [result coll]
    (= result (core/my-frequencies coll))
    {1 3, 2 3, 3 2, 4 1} [1 1 2 1 3 4 2 2 3]
    {1 3, 2 2, 3 2} '(1 1 2 3 1 2 3)))

(comment (core/my-frequencies [1 1 2 1 3 4 2 2 3])
         (core/my-frequencies '(1 1 2 3 1 2 3)))

(deftest get-domain
  (are [result link]
    (= result (core/get-domain link))
    "lenta.ru" "http://lenta.ru/photo/2012/11/28/lis/"
    "cursive-ide.com" "https://cursive-ide.com/userguide/testing.html"))

(deftest get-statistics
  (are [result coll]
    (= result (core/get-statistics coll))
    {"first" 1} ["https://first/first"
                 "https://first/first"
                 "https://first/first"]
    {"second" 2} ["https://second/first"
                  "https://second/first"
                  "https://second/second"]
    {"first" 1, "second" 2, "third" 3} ["https://first/first"
                                        "https://first/first"
                                        "https://first/first"
                                        "https://second/first"
                                        "https://second/first"
                                        "https://second/second"
                                        "https://third/first"
                                        "https://third/second"
                                        "https://third/third"]
    {"first" 1, "second" 2, "third" 3} ["https://first/first"
                                        "https://second/second"
                                        "https://third/second"
                                        "https://second/first"
                                        "https://third/third"
                                        "https://first/first"
                                        "https://third/first"
                                        "https://first/first"
                                        "https://second/first"]))

(deftest get-full-address
  (are [result query]
    (= result (core/get-full-address query))
    "https://www.bing.com/search?q=scala&format=rss&count=10" "scala"
    "https://www.bing.com/search?q=clojure&format=rss&count=10" "clojure"
    "https://www.bing.com/search?q=something&format=rss&count=10" "something"))

;; возращает success-status
(comment (defn get-xml-test
   [address]
   (let [{:keys [status headers body error] :as resp} @(http/get address)]
     (if error
       (println "Failed, exception: " error)
       (println "HTTP GET success: " status))))

  (get-xml-test "https://www.bing.com/search?q=scala&format=rss&count=10"))

(comment (core/get-links "scala"))
(comment (core/fetch-query ["scala" "clojure" "something"]))