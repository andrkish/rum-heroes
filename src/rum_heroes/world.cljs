(ns rum-heroes.world
  (:require
    [rum-heroes.grid :as grid]
    [rum-heroes.spawn :as spawn]))

;; get initial background grid data
(defn init-background-grid []
  (->> (map spawn/gen-background-tile (range grid/grid-total))
       (partition grid/grid-width)
       (mapv vec)))

;; setup all initial overlay
(defn init-background-overlay []
  (->> (range 0 grid/grid-total)
       (map grid/overlay?)
       (remove zero?)
       (map spawn/spawn-overlay)
       (into {})))

;; setup player initial army
(defn init-army [count]
  (->> (range 0 count)
       (map spawn/spawn-actor)
       (into [])))

;; setup enemy initial army
(defn init-enemy-army [count]
  (->> (range 0 count)
       (map spawn/spawn-enemy-actor)
       (into [])))

;; check if cell busy by actor or incorrect
(defn cannot-move? [cell actors] 
  (or (not (grid/correct-cell-vec? cell))
      (grid/busy-cell? cell actors)))

(defn get-neighbors-move [x y actors]
  (let [dirs [[1 0] [0 1] [-1 0] [0 -1]]]
    (->> (map #(apply grid/add-cell-dir [x y %]) dirs)
         (remove #(apply cannot-move? [% actors]))
         (into []))))