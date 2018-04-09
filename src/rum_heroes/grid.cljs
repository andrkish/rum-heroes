(ns rum-heroes.grid)

;; grid tile info
(def ^:const grid-tile-width 72)
(def ^:const grid-tile-height 72)

;; grid size (tiles count)
(def ^:const grid-width 12)
(def ^:const grid-height 7)
(def ^:const grid-total (* grid-width grid-height))

;; grid overlay density percentage
(def ^:const grid-dens 35)

;; coordinate helpers
(defn get-coord-x [tileX]
  (* tileX grid-tile-width))

(defn get-coord-y [tileY]
  (* tileY grid-tile-height))

(defn get-cell-x [index]
  (mod index grid-width))

(defn get-cell-y [index]
  (quot index grid-width))

(defn correct-cell? [x y]
  (and (>= x 0) (>= y 0) (< x grid-width) (< y grid-height)))

(defn correct-cell-vec? [v]
  (correct-cell? (get v 0) (get v 1)))

(defn busy-cell? [cell actors]
  (let [x (get cell 0)
        y (get cell 1)]
    (not (empty? (filter #(= { :x x :y y } (get % :pos)) @actors)))))

;; add direction to cell (dir = [x y])
(defn add-cell-dir [x y dir]
  [ (+ x (get dir 0)) (+ y (get dir 1)) ])

;; check density random
(defn overlay? [x density]
  (if (<= (rand-int 100) grid-dens) x 0))
