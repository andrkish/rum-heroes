(ns rum-heroes.view.uiview
  (:require
    [rum-heroes.config.actors :as actors]
    [rum.core :as rum]))

;; state = tile-hover-state
(rum/defc tile-hover-component < rum/reactive [state]
  (let [cursor (rum/cursor-in state [])
        x (get @cursor 0)
        y (get @cursor 1)]
    (when (rum/react cursor)
      (if (and (>= x 0) (>= y 0))
        [:h3 (str "x: " x " y: " y)]
        [:h3 ""]))))

(rum/defc end-turn-button [on-end-turn-click]
  [:a.turnButton { :on-click on-end-turn-click } "End Turn"])

(defn get-actor [selected hover teamId]
  (if (and (not (empty? hover)) (= teamId (get hover :teamId)))
    hover 
    selected))

(defn get-actor-body [actor teamId]
  (when (and (not (empty? actor)) (= teamId (get actor :teamId)))
    (let [t (get actors/actors-template (keyword (get actor :template)))]
      [:div.hover-unit
        [ :div "SELECTED UNIT "]
        [ :div "template: " (get actor :template)]
        [ :div "damage: " (get t :damage)]
        [ :div "range: " (get t :range)]
        [ :div "moves: " (get t :moves)]
        [ :div "hp: " (get t :hpMax)]
        [ :div "teamId: " (get actor :teamId)]])))

(rum/defc actor-ui-component < rum/reactive [hover selected teamId]
  (let [hc (rum/cursor-in hover [])
        sc (rum/cursor-in selected [])]
    (when (or (rum/react hc) (rum/react sc))
      (js/console.log "trigger")
      (get-actor-body (get-actor @sc (first @hc) teamId) teamId))))