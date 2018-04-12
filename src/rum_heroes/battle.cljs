(ns rum-heroes.battle
  (:require 
    [rum-heroes.config.actors :as actors]
    [rum-heroes.spawn :as spawn]
    [rum-heroes.grid :as grid]))

;; find actor by ID from actors-state
(defn find-actor [id actors]
  (->> (map-indexed vector actors)
       (filter #(= (get-in % [1 :id]) id))
       (first)))

(defn actors-swap-turn [actors teamId]
  (doseq [[a i] (map vector @actors (range))]
    (swap! actors assoc-in [ i :actions ]
          (spawn/get-actions (= (get a :teamId) teamId)
                             (actors/get-template a)))))

(defn hover-actor? [x y actor] 
  (and (= { :x x :y y } (get actor :pos))
       (> (get actor :hp) 0)))

;; check if cell busy by actor or incorrect
(defn cannot-move? [cell actors] 
  (or (not (grid/correct-cell-vec? cell))
      (grid/busy-cell? cell actors)))

;; pos - { :x :y }, actors - actos-state atom
(defn get-neighbors-move [pos actors]
  (let [ x (get pos :x)
         y (get pos :y)
         dirs [[1 0] [0 1] [-1 0] [0 -1]]]
    (->> (map #(apply grid/add-cell-dir [x y %]) dirs)
         (remove #(apply cannot-move? [% actors]))
         (into []))))

(defn enemy-team? [a1 a2]
  (not (= (get a1 :teamId)
          (get a2 :teamId))))

(defn attack-dist? [a1 a2 dist]
  (<= (grid/cheb-distance (get a1 :pos) (get a2 :pos))
       dist))

(defn can-attack? [a1 a2 dist]
  (and (enemy-team? a1 a2)
       (attack-dist? a1 a2 dist)
       (> (get a2 :hp ) 0)))

(defn get-targets [actor actors]
  (let [t (actors/get-template actor)
        range (get t :range)]
    (filter #(apply can-attack? [actor % range]) @actors)))

(defn can-move? [x y moves]
  (some #(= [x y] %) moves))

(defn do-damage [id damage actors]
  (let [a (find-actor id @actors)]
    (let [hp (get-in @actors [ (get a 0) :hp ])]
      (swap! actors assoc-in [ (get a 0) :hp ] (- hp damage)))))
