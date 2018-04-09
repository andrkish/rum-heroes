(ns rum-heroes.core
  (:require 
    [rum.core :as rum]
    [rum-heroes.world :as world]
    [rum-heroes.grid :as grid]
    [rum-heroes.view.uiview :as ui]
    [rum-heroes.view.gridview :as gridview]))

(enable-console-print!)

;; all game state
(defonce grid-state (atom (world/init-background-grid)))
(defonce grid-overlay-state (atom (world/init-background-overlay)))
(defonce actors-state (atom (into (world/init-army 6) (world/init-enemy-army 6))))

(defonce tile-hover-state (atom [ -1 -1 ]))
(defonce moves-state (atom []))
(defonce targets-state (atom []))
(defonce actor-hover-state (atom '()))
(defonce actor-selected-state (atom '()))


;; event handlers from view / ui 
(defn on-tile-hover [x y]
  (reset! tile-hover-state [x y])
  (when (grid/correct-cell? x y))
    (reset! actor-hover-state 
            (filter (fn [el] (= { :x x :y y } (get el :pos))) @actors-state)))

;; select own actor on click event handler
(defn select-actor []
  (when (not (empty? @actor-hover-state))
    (let [actor (first @actor-hover-state)]
      (reset! actor-selected-state actor)
      (reset! targets-state (world/get-targets @actor-selected-state 2 actors-state))
      (reset! moves-state (world/get-neighbors-move (get actor :pos) actors-state)))))

;; moves - list of available moves
(defn can-move? [x y moves]
  (some #(= [x y] %) moves))

(defn find-actor [id actors]
  (->> (map-indexed vector actors)
       (filter #(= (get-in % [1 :id]) id))
       (first)))

(defn do-move [x y moves actors]
  (let [a (find-actor (get-in @actor-selected-state [:id]) @actors)]
    (swap! actors assoc-in [ (get a 0) :pos] (hash-map :x x :y y))
    (reset! moves-state [])
    (reset! targets-state [])
    (reset! actor-selected-state '())))

(defn move-actor [x y]
  (when (can-move? x y @moves-state)
    (do-move x y @moves-state actors-state)))

(defn do-damage [id damage actors]
  (let [a (find-actor id @actors)]
    (let [hp (get-in @actors [ (get a 0) :hp ])]
      (swap! actors assoc-in [ (get a 0) :hp ] (- hp damage)))))

(defn do-attack [x y targets actors]
  (let [target (first (filter #(= {:x x :y y} (get-in % [:pos])) @targets))]
    (when (not (empty? target))
      (do-damage (get-in target [:id]) 2 actors))))

(defn on-tile-click [x y]
  (do-attack x y targets-state actors-state)
  (select-actor)
  (move-actor x y))

(rum/mount (gridview/grid-component 12 7 grid-state on-tile-hover on-tile-click)
  (. js/document (getElementById "world")))

(rum/mount [(gridview/grid-overlay-component grid-overlay-state)
            (gridview/grid-actors-component actors-state)]
  (. js/document (getElementById "world-overlay")))

(rum/mount [(gridview/grid-hover-component tile-hover-state)
            (gridview/moves-render-component moves-state)
            (gridview/targets-render-component targets-state)
            (gridview/actor-selected-component actor-selected-state)]
  (. js/document (getElementById "world-hover")))

(rum/mount [(ui/tile-hover-component tile-hover-state) 
            (ui/actor-selected-component actor-selected-state)
            (ui/actor-hover-component actor-hover-state)]
  (. js/document (getElementById "baseUI")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

