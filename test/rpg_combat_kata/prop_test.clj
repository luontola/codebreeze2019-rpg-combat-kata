(ns rpg-combat-kata.prop-test
  (:require [clojure.test :refer :all]
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
               (dissoc :character/id))))))
