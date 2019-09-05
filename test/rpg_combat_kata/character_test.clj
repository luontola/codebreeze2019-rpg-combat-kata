(ns rpg-combat-kata.character-test
  (:require [clojure.test :refer :all]
            [rpg-combat-kata.character :as character]))

(deftest new-character-test
  (let [new-character (character/create)]
    (testing "starts at max health"
      (is (= character/max-health (:character/health new-character))))
    (testing "starts at level 1"
      (is (= 1 (:character/level new-character))))
    (testing "is alive"
      (is (character/alive? new-character)))
    (testing "has no factions"
      (is (= #{} (:character/factions new-character)))))

  (testing "melee fighter"
    (is (= {:character/attack-range 2}
           (-> (character/create {:character/fighter-type :melee})
               (select-keys [:character/attack-range])))))

  (testing "ranged fighter"
    (is (= {:character/attack-range 20}
           (-> (character/create {:character/fighter-type :ranged})
               (select-keys [:character/attack-range]))))))

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
    (testing "attacker and target are within 4 levels"
      (let [attacker (character/create {:character/level 14
                                        :character/dps 10})
            target (character/create {:character/level 10
                                      :character/health 100})]
        (is (= 90 (:character/health (character/attack target attacker)))))
      (let [attacker (character/create {:character/level 6
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

    (testing "attacker is 5 levels below target, damage is reduced 50%"
      (let [attacker (character/create {:character/level 5
                                        :character/dps 10})
            target (character/create {:character/level 10
                                      :character/health 100})]
        (is (= 95 (:character/health (character/attack target attacker)))))))

  (testing "attack range:"
    (testing "in range"
      (let [attacker (character/create {:character/attack-range 10
                                        :character/position {:x 10}})
            target (character/create {:character/health 100
                                      :character/position {:x 20}})]
        (is (> 100 (:character/health (character/attack target attacker))))))
    (testing "out of range"
      (let [attacker (character/create {:character/attack-range 10
                                        :character/position {:x 10}})
            target (character/create {:character/health 100
                                      :character/position {:x 21}})]
        (is (= 100 (:character/health (character/attack target attacker))))))))

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

(deftest factions-test
  (testing "character may join a faction"
    (let [character (-> (character/create)
                        (character/join-faction :elves))]
      (is (= #{:elves} (:character/factions character)))))

  (testing "character may join many factions"
    (let [character (-> (character/create {:character/factions #{:elves}})
                        (character/join-faction :orks))]
      (is (= #{:elves :orks} (:character/factions character)))))

  (testing "character may leave a faction"
    (let [character (-> (character/create {:character/factions #{:elves :orks}})
                        (character/leave-faction :elves))]
      (is (= #{:orks} (:character/factions character)))))

  (testing "characters in the same faction are allies"
    (is (true? (character/allies? (character/create {:character/factions #{:elves}})
                                  (character/create {:character/factions #{:elves}}))))
    (is (true? (character/allies? (character/create {:character/factions #{:elves :humans}})
                                  (character/create {:character/factions #{:elves :hobbits}}))))
    (is (false? (character/allies? (character/create {:character/factions #{}})
                                   (character/create {:character/factions #{}}))))
    (is (false? (character/allies? (character/create {:character/factions #{:humans}})
                                   (character/create {:character/factions #{:hobbits}})))))

  (testing "allies cannot attack each other"
    (let [attacker (character/create {:character/factions #{:elves}})
          target (character/create {:character/factions #{:elves}})]
      (is (= target (character/attack target attacker)))))

  (testing "allies can heal each other"
    (let [healer (character/create {:character/factions #{:elves}
                                    :character/healing-power 100})
          target (character/create {:character/factions #{:elves}
                                    :character/health 100})]
      (is (= 200 (:character/health (character/heal target healer)))))))
