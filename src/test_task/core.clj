(ns test-task.core)

(require '[clojure.string :as str])
(require '[org.httpkit.client :as http])

(require '[clojure.xml :as xml]
         '[clojure.zip :as zip])

(defn get-domain
  "Получает домен второго уровня из переданной ссылки"
  [link]
  (nth (str/split link #"/") 2))

(defn my-frequencies
  "Функция для создания частотного словаря"
  [coll]
  (reduce
    #(update %1 %2 (fnil inc 0))
    {}
    coll))

(defn get-statistics
  "Подсчет статистики по доменам второго уровня"
  [coll]
  (->>
    coll
    (set)
    (map get-domain)
    frequencies))

(defn concat-query
  "Вставляет слово в форму запроса"
  [s]
  (str "q=" s "&"))

(defn get-full-query
  "Для запроса по нескольким словам"
  [queries]
  (reduce str (map concat-query queries)))

(defn get-full-address
  "Собирается адрес запроса"
  [query]
  (str "https://www.bing.com/search?" (get-full-query query) "format=rss&count=10"))

(defn links-in-xml
  "Костыльная замена функции Parse"
  [s]
  (str/starts-with? s "http"))

(defn parse-xml
  "Достает ссылки из переданной xml в формате String"
  [xml]
  (filter links-in-xml (str/split xml #"<link>|</link>")))

(defn get-xml
  [address]
  (let [{:keys [status headers body error] :as resp} @(http/get address)]
    (if error
      (println "Failed, exception: " error)
      (parse-xml body))))

(get-xml "https://www.bing.com/search?q=scala&format=rss&count=1")

(defn get-links
  "Получает ссылки из поисковых страниц"
  [query]
  (->>
    ;; Собираем полный адрес запроса
    (get-full-address query)
    ;; Синхронно идем на сервер и парсим xml
    (get-xml)
    ))

;;(get-links ["scala"])

(defn fetch-query
  "Возвращает спискок ссылок"
  [queries]
  (map get-links queries))