(ns rum-heroes.grid)

;; total background tiles sprite count
(def ^:const grid-back-sprite-count 3)
(def ^:const grid-overlay-sprite-count 3)

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

;; generate default background tile
(defn gen-background-tile [x]
  (rand-int grid-back-sprite-count))

;; generate default overlay tile
(defn gen-overlay-tile []
  (rand-int grid-overlay-sprite-count))

;; check density random
(defn overlay? [x]
  (if (<= (rand-int 100) grid-dens) x 0))

(defn not-zero? [x]
  (not (zero? x)))

;; get initial background grid data
(defn init-background-grid []
  (->> (map gen-background-tile (range grid-total))
       (partition grid-width)
       (mapv vec)))

;; create overlay entity { k1 : { posX, posY, visual }}
(defn create-overlay [x]
  (hash-map (keyword (str "overlay" x))
            { :posX (get-cell-x x) :posY (get-cell-y x) :visual (gen-overlay-tile)}))

;; setup all initial overlay
(defn init-background-overlay []
  (->> (range 0 grid-total)
       (map overlay?)
       (filter not-zero?)
       (map create-overlay)
       (into {})))
