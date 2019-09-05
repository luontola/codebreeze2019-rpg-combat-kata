(ns rpg-combat-kata.character-test
  (:require [clojure.test :refer :all]
            [rpg-combat-kata.character :as character]))

(deftest character-test
  (testing "has health, starting at 1000"
    (is (= 1000 (:character/health character/new)))))
