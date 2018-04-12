(ns rum-heroes.config.actors)

(defonce actors-good-pack [ "knight" "mage" "archer" "swordman" "berserk" ])
(defonce actors-evil-pack [ "rider" "warlock" "sniper" "skeleton" "werewolf" ])

;; helper function
;; todo: learn about macro? 
(defn create-template 
  [ key sprite hpMax damage range moves ]
  (hash-map 
    key { :visual sprite 
          :damage damage
          :range range
          :moves moves
          :attacks 1
          :hpMax hpMax }))

;; get static template from actor entity
(defn get-template [actor]
  (get actors-template (keyword (get actor :template))))

(def actors-template 
  (into {} 
    [(create-template :knight "actor-knight" 15 3 1 3)
     (create-template :mage "actor-mage" 5 3 2 1)
     (create-template :archer "actor-archer" 8 2 3 1)
     (create-template :swordman "actor-swordman" 10 2 1 2)
     (create-template :berserk "actor-berserk" 8 3 1 2)

     (create-template :rider "actor-rider" 12 3 1 3)
     (create-template :warlock "actor-warlock" 8 2 2 1)
     (create-template :skeleton "actor-skeleton" 8 2 1 2)
     (create-template :sniper "actor-sniper" 6 2 3 2)
     (create-template :werewolf "actor-werewolf" 18 3 1 2)]))