(ns rpg-combat-kata.prop-test
  (:require [clojure.test :refer :all]
            [rpg-combat-kata.character :as character]
            [rpg-combat-kata.prop :as prop]))

(deftest props-test
  (testing "default prop"
    (is (= {:prop/type :rock
            :character/health 1
            :character/position {:x 0}}
           (-> (prop/create)
               (dissoc :character/id)))))

  (testing "customized prop"
    (is (= {:prop/type :house
            :character/health 2000
            :character/position {:x 0}}
           (-> (prop/create {:prop/type :house
                             :character/health 2000})
               (dissoc :character/id)))))

  (testing "props cannot heal themselves"
    (let [healer (prop/create)]
      (is (= healer (character/heal healer healer)))))

  (testing "props cannot heal others"
    (let [healer (prop/create)
          target (character/create)]
      (is (= target (character/heal target healer)))))

  (testing "props cannot be healed"
    (let [healer (character/create)
          target (prop/create)]
      (is (= target (character/heal target healer)))))

  (testing "props cannot attack themselves")
  (testing "props cannot attack others")
  (testing "props can be attacked")

  (testing "props cannot join factions")
  (testing "props cannot leave factions"))
