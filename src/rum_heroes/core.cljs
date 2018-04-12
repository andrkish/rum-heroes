(ns rum-heroes.core
  (:require 
    [rum.core :as rum]
    [rum-heroes.world :as world]
    [rum-heroes.battle :as battle]
    [rum-heroes.grid :as grid]
    [rum-heroes.view.uiview :as ui]
    [rum-heroes.view.gridview :as gridview]))

(enable-console-print!)

;; all game state
(defonce grid-state (atom (world/init-background-grid)))
(defonce grid-overlay-state (atom (world/init-background-overlay)))
(defonce actors-state (atom (into (world/init-army 6) (world/init-enemy-army 6))))

(defonce team-turn (atom 0))

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
            (filter #(apply battle/hover-actor? [x y %]) @actors-state)))

;; select own actor on click event handler
(defn select-actor [actor]
  (reset! actor-selected-state actor)
  (reset! targets-state (battle/get-targets actor 2 actors-state))
  (reset! moves-state (battle/get-neighbors-move (get actor :pos) actors-state)))

(defn unselect-actor []
  (reset! actor-selected-state '())
  (reset! moves-state [])
  (reset! targets-state []))

(defn select-hover-actor []
  (when (not (empty? @actor-hover-state))
    (let [actor (first @actor-hover-state)]
      (when (= @team-turn (get actor :teamId))
        (select-actor actor)))))

(defn do-move [x y moves actors]
  (let [a (battle/find-actor (get-in @actor-selected-state [:id]) @actors)]
    (swap! actors assoc-in [ (get a 0) :pos] (hash-map :x x :y y))
    (unselect-actor)))

(defn move-actor [x y]
  (when (battle/can-move? x y @moves-state)
    (do-move x y @moves-state actors-state)))

(defn do-attack [x y targets actors]
  (let [target (first (filter #(= {:x x :y y} (get-in % [:pos])) @targets))]
    (when (not (empty? target))
      (battle/do-damage (get-in target [:id]) 2 actors))))

(defn end-turn []
  (unselect-actor)
  (reset! team-turn (mod (+ @team-turn 1) 2)))

(defn on-tile-click [x y]
  (do-attack x y targets-state actors-state)
  (select-hover-actor)
  (move-actor x y))

(defn on-end-turn-click [_]
  (end-turn))

(rum/mount (gridview/grid-component 12 7 grid-state on-tile-hover on-tile-click)
  (. js/document (getElementById "world")))

(rum/mount [(gridview/grid-overlay-component grid-overlay-state)
            (gridview/grid-actors-component actors-state)]
  (. js/document (getElementById "worldOverlay")))

(rum/mount [(gridview/grid-hover-component tile-hover-state)
            (gridview/moves-render-component moves-state)
            (gridview/targets-render-component targets-state)
            (gridview/actor-selected-component actor-selected-state)]
  (. js/document (getElementById "worldHover")))

(rum/mount [(ui/player-title "Player 1" 0 team-turn)
            (ui/actor-ui-component actor-hover-state actor-selected-state 0)]
  (. js/document (getElementById "leftPanel")))

(rum/mount [(ui/player-title "Player 2" 1 team-turn)
            (ui/actor-ui-component actor-hover-state actor-selected-state 1)]
  (. js/document (getElementById "rightPanel")))

(rum/mount [(ui/tile-hover-component tile-hover-state) 
            (ui/end-turn-button on-end-turn-click)]
  (. js/document (getElementById "worldFooter")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

