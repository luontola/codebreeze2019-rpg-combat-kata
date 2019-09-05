(ns rpg-combat-kata.character
  (:require [clojure.set :as set]
            [rpg-combat-kata.coordinate :as coordinate])
  (:import (java.util UUID)))

(def max-health 1000)

(defn create
  ([]
   (create nil))
  ([initial-values]
   (merge {:character/id (UUID/randomUUID)
           :character/health max-health
           :character/level 1
           :character/dps 10
           :character/healing-power 10
           :character/attack-range (case (get initial-values :character/fighter-type :melee)
                                     :melee 2
                                     :ranged 20)
           :character/position {:x 0}
           :character/factions #{}}
          initial-values)))

(defn alive? [character]
  (pos? (:character/health character)))

(defn same? [character-1 character-2]
  (= (:character/id character-1)
     (:character/id character-2)))

(defn- change-health [character change]
  (update character :character/health (fn [health]
                                        (max 0 (min max-health (+ health change))))))

(defn- in-range? [target attacker]
  (and (some? (:character/attack-range attacker))
       (<= (coordinate/distance (:character/position target)
                                (:character/position attacker))
           (:character/attack-range attacker))))

(defn allies? [character-1 character-2]
  (not (empty? (set/intersection
                (:character/factions character-1)
                (:character/factions character-2)))))

(defn attack [target attacker]
  (if (and (in-range? target attacker)
           (not (same? target attacker))
           (not (allies? target attacker))
           (some? (:character/dps attacker)))
    (let [level-diff (if (some? (:character/level target))
                       (- (:character/level attacker)
                          (:character/level target))
                       0)
          multiplier (cond
                       (<= 5 level-diff) 1.5
                       (<= level-diff -5) 0.5
                       :else 1)
          dps (int (* multiplier (:character/dps attacker)))]
      (change-health target (- dps)))
    target))

(defn heal [target healer]
  (if (and (alive? target)
           (or (same? target healer)
               (allies? target healer))
           (some? (:character/healing-power healer)))
    (change-health target (:character/healing-power healer))
    target))

(defn join-faction [character faction]
  (if (:character/factions character)
    (update character :character/factions conj faction)
    character))

(defn leave-faction [character faction]
  (if (:character/factions character)
    (update character :character/factions disj faction)
    character))
