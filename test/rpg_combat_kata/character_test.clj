(ns rpg-combat-kata.character-test
  (:require [clojure.test :refer :all]
            [rpg-combat-kata.character :as character]))

(deftest character-test
  (testing "new character"
    (testing "starts at max health"
      (is (= character/max-health (:character/health character/new))))
    (testing "starts at level 1"
      (is (= 1 (:character/level character/new))))
    (testing "is alive"
      (is (character/alive? character/new))))

  (testing "attacking:"
    (testing "deals damage to the victim"
      (let [attacker character/new
            victim character/new
            damaged-victim (character/attack victim attacker)]
        (is (< (:character/health damaged-victim)
               (:character/health victim)))))

    (testing "health becomes 0 if damage is greater than health"
      (let [attacker character/new
            victim (assoc character/new :character/health 1)
            damaged-victim (-> victim
                               (character/attack attacker)
                               (character/attack attacker))]
        (is (= 0 (:character/health damaged-victim)))))

    (testing "dies when health is 0"
      (let [attacker character/new
            victim (assoc character/new :character/health 1)
            damaged-victim (character/attack victim attacker)]
        (is (not (character/alive? damaged-victim))))))

  (testing "healing:"
    (testing "increases health"
      (let [damaged (assoc character/new :character/health 500)
            healed (character/heal damaged 100)]
        (is (> (:character/health healed)
               (:character/health damaged)))))

    (testing "the dead cannot be healed"
      (let [damaged (assoc character/new :character/health 0)
            healed (character/heal damaged 100)]
        (is (= healed damaged))))

    (testing "cannot be healed over max health"
      (let [damaged (assoc character/new :character/health (dec character/max-health))
            healed (character/heal damaged 100)]
        (is (= character/max-health (:character/health healed)))))))
