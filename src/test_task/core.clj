(ns test-task.core)

(require '[clojure.string :as str])
(require '[org.httpkit.client :as http])

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

(defn get-full-address
  [query]
  (str "https://www.bing.com/search?q=" query "&format=rss&count=10"))



(defn get-xml
  [address]
  (let [{:keys [status headers body error] :as resp} @(http/get address)]
    (if error
      (println "Failed, exception: " error)
      (println "HTTP GET success: " status))))

(defn get-links
  "Получает ссылки из поисковых страниц"
  [query]
  (->
    ;; Собираем полный адрес запроса
    (get-full-address query)
    ;; Синхронно идем на сервер
    (get-xml)
    ;; Парсим XML-страницу
    ))

(defn fetch-query
  "Возвращает спискок ссылок"
  [queries]
  (map get-links queries))
