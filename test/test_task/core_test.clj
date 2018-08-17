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

(deftest links-in-xml
  (are [result query]
    (= result (core/links-in-xml))
    false "aaaa"
    true "https://aaaa"))

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

(comment (def sent ["aaaaaaa" "bbbbbbb" "http://aaaaa.aaaaa" "https://aaaa.bbbb"])
         (filter links-in-xml sent))

(comment
  (def help-xml "<?xml version=\"1.0\" encoding=\"utf-8\" ?><rss version=\"2.0\"><channel><title>Bing: scala</title><link>http://www.bing.com:80/search?q=scala</link><description>Результаты поиска</description><image><url>http://www.bing.com:80/s/a/rsslogo.gif</url><title>scala</title><link>http://www.bing.com:80/search?q=scala</link></image><copyright>© 2018, Microsoft. Все права защищены. Не допускается использовать, воспроизводить или передавать любым образом эти результаты в виде XML в каких-либо целях, кроме обработки результатов Bing в агрегаторе RSS для личного некоммерческого использования. Для любого другого использования данных результатов необходимо явное письменное разрешение корпорации Майкрософт. Получая доступ к этой веб-странице или любым образом используя данные результаты, вы соглашаетесь соблюдать упомянутые выше ограничения.</copyright><item><title>The Scala Programming Language</title><link>https://scala-lang.org/</link><description>Scala began life in 2003, created by Martin Odersky and his research group at EPFL, next to Lake Geneva and the Alps, in Lausanne, Switzerland.</description><pubDate>Пт, 10 авг 2018 23:43:00 GMT</pubDate></item></channel></rss>")
  (parse-xml help-xml)
  (def new_help_xml (str/split help-xml #"<link>|</link>"))
  (filter links-in-xml new_help_xml))