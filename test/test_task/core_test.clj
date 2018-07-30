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