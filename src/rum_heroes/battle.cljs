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

;; 4 directions - north, south, west, south
(defn get-dirs []
  [[1 0] [0 1] [-1 0] [0 -1]])

;; get adjacent cells for moves
(defn get-cells [x y actors dirs]
  (->> (map #(apply grid/add-cell-dir [x y %]) dirs)
       (remove #(apply cannot-move? [% actors]))
       (into #{})))

;; get adjacent cells from list of cells 
(defn get-cells-list [list actors dirs]
  (reduce clojure.set/union 
    (map (fn [n] (get-cells (get n 0) (get n 1) actors dirs)) list)))

(defn get-neighbors-move [pos d actors]
  (loop [visited (into #{} (get-cells (get pos :x) (get pos :y) actors (get-dirs)))
         depth (dec d)
         a actors]
    (if (zero? depth)
      (into [] visited)
      (recur (clojure.set/union visited (get-cells-list visited actors (get-dirs)))
             (dec depth)
             a))))

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
