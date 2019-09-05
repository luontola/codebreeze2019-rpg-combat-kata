(ns rpg-combat-kata.character-test
  (:require [clojure.test :refer :all]
            [rpg-combat-kata.character :as character]))

(deftest new-character-test
  (let [character (character/create)]
    (testing "starts at max health"
      (is (= character/max-health (:character/health character))))
    (testing "starts at level 1"
      (is (= 1 (:character/level character))))
    (testing "is alive"
      (is (character/alive? character)))))

(deftest attacking-test
  (testing "deals damage to the target"
    (let [attacker (character/create)
          target (character/create)
          damaged-target (character/attack target attacker)]
      (is (< (:character/health damaged-target)
             (:character/health target)))))

  (testing "cannot self-inflict damage"
    (let [character (character/create)]
      (is (= character (character/attack character character)))))

  (testing "health becomes 0 if damage is greater than health"
    (let [attacker (character/create)
          target (character/create {:character/health 1})
          damaged-target (-> target
                             (character/attack attacker)
                             (character/attack attacker))]
      (is (= 0 (:character/health damaged-target)))))

  (testing "dies when health is 0"
    (let [attacker (character/create)
          target (character/create {:character/health 1})
          damaged-target (character/attack target attacker)]
      (is (not (character/alive? damaged-target)))))

  (testing "adjust damage to level:"
    (testing "attacker and target are about same level"
      (let [attacker (character/create {:character/level 14
                                        :character/dps 10})
            target (character/create {:character/level 10
                                      :character/health 100})]
        (is (= 90 (:character/health (character/attack target attacker))))))

    (testing "attacker is 5 levels above target, damage is boosted 50%"
      (let [attacker (character/create {:character/level 15
                                        :character/dps 10})
            target (character/create {:character/level 10
                                      :character/health 100})]
        (is (= 85 (:character/health (character/attack target attacker))))))

    #_(testing "attacker is 5 levels below target, damage is reduced 50%")))


(deftest healing-test
  (testing "increases health"
    (let [damaged (character/create {:character/health 500})
          healed (character/heal damaged damaged)]
      (is (> (:character/health healed)
             (:character/health damaged)))))

  (testing "enemies cannot be healed"
    (let [healer (character/create)
          enemy (character/create {:character/health 500})]
      (is (= enemy (character/heal enemy healer)))))

  (testing "the dead cannot be healed"
    (let [damaged (character/create {:character/health 0})
          healed (character/heal damaged damaged)]
      (is (= healed damaged))))

  (testing "cannot be healed over max health"
    (let [damaged (character/create {:character/health (dec character/max-health)})
          healed (-> damaged
                     (character/heal damaged)
                     (character/heal damaged))]
      (is (= character/max-health (:character/health healed))))))
