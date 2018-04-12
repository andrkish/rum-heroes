# Rum heroes

Simple and minimal HTML turn-based strategy written in [ClojureScript](https://clojurescript.org/) and [Rum](https://github.com/tonsky/rum) (react wrapper). Rendering via HTML dom sprites. In real projects you may have to choose Canvas or WebGL for fast and convenient 2d / 3d rendering.
Rum-heroes has been written just for fun and for educational purposes with Clojure :)

## WIP Screenshot
![wip screenshot](https://i.imgur.com/F7DblHh.jpg)

## Gameplay features

Simple turn-based battle tactic on a grid board.
Select your heroes and kill all enemies.

### Implemented

* Player vs Player
* Selection (manual select in any order and auto-select)
* Grid movement (run and hit / hit and run)
* Melee and range attack with damage
* Dead state
* UI and sprites
* Turns

### Todo

* Simple AI
* Win / lose screen
* Some animations and tooltips

## Building and running

Rum-heroes relies on Leiningen and Figwheel for simplicity.

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL.

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL.

See project.clj for configuration.

## Graphic Assets

All graphic assets taken from open-source project Wesnoth and rogue-like clone Wessense.
License need to be checked.

## License

MIT License

Copyright (c) 2018 Andrew Kishchenko