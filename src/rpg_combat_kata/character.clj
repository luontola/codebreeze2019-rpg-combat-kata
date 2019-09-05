(ns rpg-combat-kata.character
  (:require [rpg-combat-kata.coordinate :as coordinate])
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
  (<= (coordinate/distance (:character/position target)
                           (:character/position attacker))
      (:character/attack-range attacker)))

(defn allies? [character-1 character-2]
  ;; FIXME
  (and (not (empty? (:character/factions character-1)))
       (= (:character/factions character-1)
          (:character/factions character-2))))

(defn attack [target attacker]
  (if (and (in-range? target attacker)
           (not (same? target attacker))
           (not (allies? target attacker)))
    (let [level-diff (- (:character/level attacker)
                        (:character/level target))
          multiplier (cond
                       (<= 5 level-diff) 1.5
                       (<= level-diff -5) 0.5
                       :else 1)
          dps (int (* multiplier (:character/dps attacker)))]
      (change-health target (- dps)))
    target))

(defn heal [target healer]
  (if (and (alive? target)
           (same? target healer))
    (change-health target (:character/healing-power healer))
    target))

(defn join-faction [character faction]
  (update character :character/factions conj faction))

(defn leave-faction [character faction]
  (update character :character/factions disj faction))
