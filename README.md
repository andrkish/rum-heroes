# Rum heroes

Simple and minimal HTML turn-based strategy written in ClojureScript and Rum (react wrapper). Rendering via html dom sprites. In real projects you should use canvas or webgl for fast rendering.

## WIP Screenshot
...

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