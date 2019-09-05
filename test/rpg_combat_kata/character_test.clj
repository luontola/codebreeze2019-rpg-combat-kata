(ns rpg-combat-kata.character-test
  (:require [clojure.test :refer :all]
            [rpg-combat-kata.character :as character]))

(deftest character-test
  (testing "new character"
    (testing "has health, starting at 1000"
      (is (= 1000 (:character/health character/new))))
    (testing "has a level, starting at 1"
      (is (= 1 (:character/level character/new))))
    (testing "is alive"
      (is (character/alive? character/new))))

  (testing "character with zero health is dead"
    (is (not (character/alive? (assoc character/new :character/health 0)))))

  (testing "attacking:"
    (testing "deals damage to the victim"
      (let [attacker character/new
            victim character/new
            damaged-victim (character/attack attacker victim)]
        (is (< (:character/health damaged-victim)
               (:character/health victim)))))

    (testing "health becomes 0 if damage is greater than health"
      (let [attacker character/new
            victim (assoc character/new :character/health 1)
            damaged-victim (->> victim
                                (character/attack attacker)
                                (character/attack attacker))]
        (is (= 0 (:character/health damaged-victim)))))))
