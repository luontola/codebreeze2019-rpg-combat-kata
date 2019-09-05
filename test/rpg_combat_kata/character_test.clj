(ns rpg-combat-kata.character-test
  (:require [clojure.test :refer :all]
            [rpg-combat-kata.character :as character]))

(deftest new-character-test
  (testing "starts at max health"
    (is (= character/max-health (:character/health (character/new)))))
  (testing "starts at level 1"
    (is (= 1 (:character/level (character/new)))))
  (testing "is alive"
    (is (character/alive? (character/new)))))

(deftest attacking-test
  (testing "deals damage to the victim"
    (let [attacker (character/new)
          victim (character/new)
          damaged-victim (character/attack victim attacker)]
      (is (< (:character/health damaged-victim)
             (:character/health victim)))))

  (testing "cannot self-inflict damage"
    (let [character (character/new)]
      (is (= character (character/attack character character)))))

  (testing "health becomes 0 if damage is greater than health"
    (let [attacker (character/new)
          target (-> (character/new)
                     (assoc :character/health 1))
          damaged-target (-> target
                             (character/attack attacker)
                             (character/attack attacker))]
      (is (= 0 (:character/health damaged-target)))))

  (testing "dies when health is 0"
    (let [attacker (character/new)
          victim (-> (character/new)
                     (assoc :character/health 1))
          damaged-victim (character/attack victim attacker)]
      (is (not (character/alive? damaged-victim))))))

(deftest healing-test
  (testing "increases health"
    (let [damaged (-> (character/new)
                      (assoc :character/health 500))
          healed (character/heal damaged damaged)]
      (is (> (:character/health healed)
             (:character/health damaged)))))

  (testing "enemies cannot be healed"
    (let [healer (character/new)
          enemy (-> (character/new)
                    (assoc :character/health 500))]
      (is (= enemy (character/heal enemy healer)))))

  (testing "the dead cannot be healed"
    (let [damaged (-> (character/new)
                      (assoc :character/health 0))
          healed (character/heal damaged damaged)]
      (is (= healed damaged))))

  (testing "cannot be healed over max health"
    (let [damaged (-> (character/new)
                      (assoc :character/health (dec character/max-health)))
          healed (-> damaged
                     (character/heal damaged)
                     (character/heal damaged))]
      (is (= character/max-health (:character/health healed))))))
