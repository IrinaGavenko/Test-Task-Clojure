(ns test-task.core)

(require '[clojure.string :as str])
(require '[org.httpkit.client :as http])

(require '[clojure.xml :as xml]
         '[clojure.zip :as zip])

(defn get-domain
  [link]
  (nth (str/split link #"/") 2))

(defn my-frequencies
  [coll]
  (reduce
    #(update %1 %2 (fnil inc 0))
    {}
    coll))

(defn get-statistics
  [coll]
  (->>
    coll
    (set)
    (map get-domain)
    frequencies))


;; ИСПРАВИТЬ: ВХОДНЫЕ ПАРАМЕТРЫ -- МАССИВ (отдельной функцией)
(defn get-full-address
  [query]
  (str "https://www.bing.com/search?q=" query "&format=rss&count=1"))


;; ИСПРАВИТЬ -- НЕ НА ВСЕХ XML ОТРАБОТАЕТ КОРРЕКТНО
(defn links-in-xml
  [s]
  (str/starts-with? s "http"))

(defn parse-xml
  "Достает ссылки из переданной xml в формате String"
  [xml]
  (str/split xml #"<link>|</link>")
  (print xml)
  (filter links-in-xml xml))

(defn get-xml
  [address]
  (let [{:keys [status headers body error] :as resp} @(http/get address)]
    (if error
      (println "Failed, exception: " error)
      (parse-xml body))))

(defn get-links
  "Получает ссылки из поисковых страниц"
  [query]
  (->>
    ;; Собираем полный адрес запроса
    (get-full-address query)
    ;; Синхронно идем на сервер и парсим xml
    (get-xml)
    ))

(defn fetch-query
  "Возвращает спискок ссылок"
  [queries]
  (map get-links queries))
