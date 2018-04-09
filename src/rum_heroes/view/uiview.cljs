(ns rum-heroes.view.uiview
  (:require
    [rum.core :as rum]))

;; state = tile-hover-state
(rum/defc tile-hover-component < rum/reactive [state]
  [:h3 "hover: " (rum/react state)])

(rum/defc actor-hover-component < rum/reactive [actor]
  (let [cursor (rum/cursor-in actor [])]
    (when (and (rum/react cursor) (not (empty? @cursor)))
      (let [a (first @cursor)]
        [:div.hover-unit
          [ :div "HOVER UNIT "]
          [ :div "template: " (get a :template)]
          [ :div "teamId: " (get a :teamId)]]))))

(rum/defc actor-selected-component < rum/reactive [actor]
  (let [cursor (rum/cursor-in actor [])]
    (when (and (rum/react cursor) (not (empty? @cursor)))
      [:div.hover-unit
        [ :div "SELECTED UNIT "]
        [ :div "template: " (get @cursor :template)]
        [ :div "teamId: " (get @cursor :teamId)]])))
