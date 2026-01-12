(ns user
  (:require [shadow.cljs.devtools.api :as shadow]
            [shadow.cljs.devtools.server :as server])
  (:import [java.lang ProcessBuilder ProcessBuilder$Redirect]))

(set! *warn-on-reflection* true)

(defonce css-watcher (atom nil))
(defonce debugger-process (atom nil))

(defn- start-flowstorm-debugger []
  (when (nil? @debugger-process)
    (println "==> Starting Flowstorm debugger...")
    (try
      (let [command ["clj" "-Sforce"
                     "-Sdeps" "{:deps {com.github.flow-storm/flow-storm-dbg {:mvn/version \"4.4.4\"}}}"
                     "-X" "flow-storm.debugger.main/start-debugger"
                     ":port" "8778"
                     ":repl-type" ":shadow"
                     ":build-id" ":user-hub"]
            process (ProcessBuilder/.start (ProcessBuilder/new command))]
        (reset! debugger-process process)
        (println "==> Flowstorm debugger started successfully."))
      (catch java.io.IOException e
        (println "==> ERROR: Could not start Flowstorm debugger.")
        (println "    Make sure `clj` is in your PATH and that the deps string is correct.")
        (println "    Details:" (.getMessage e))))))

(defn- stop-debugger! []
  (when-let [process @debugger-process]
    (println "==> Stopping Tailwind CSS watcher...")
    (.destroy process)
    (.waitFor process) ; Wait for the process to terminate
    (reset! css-watcher nil)
    (println "==> Tailwind CSS watcher stopped.")))

(defn- start-css-watcher! []
  (when (nil? @css-watcher)
    (println "==> Starting Tailwind CSS watcher...")
    (try
      (let [process-builder (ProcessBuilder/new ["npm" "run" "watch:css"])
            ;; This is crucial: it redirects the output of the npm process
            ;; to the same output as your REPL, so you can see its logs.
            _ (ProcessBuilder/.redirectOutput process-builder ProcessBuilder$Redirect/INHERIT)
            _ (ProcessBuilder/.redirectError process-builder ProcessBuilder$Redirect/INHERIT)
            process (ProcessBuilder/.start process-builder)]
        (reset! css-watcher process)
        (println "==> Tailwind CSS watcher started successfully."))
      (catch java.io.IOException e
        (println "==> ERROR: Could not start CSS watcher.")
        (println "    Make sure 'npm' is in your system's PATH and `npm install` has been run.")
        (println "    Details:" (.getMessage e))))))

(defn- stop-css-watcher! []
  (when-let [process @css-watcher]
    (println "==> Stopping Tailwind CSS watcher...")
    (.destroy process)
    (.waitFor process) ; Wait for the process to terminate
    (reset! css-watcher nil)
    (println "==> Tailwind CSS watcher stopped.")))

(defn start!
  "Starts all development services: Shadow CLJS server and CSS watcher."
  []
  (server/start!)
  (start-css-watcher!))

(defn stop!
  "Stops all development services."
  []
  (stop-css-watcher!)
  (server/stop!))

(defn restart!
  "Restarts all development services."
  []
  (stop!)
  (Thread/sleep 500) ; Give services a moment to shut down
  (start!))

(defn cljs-repl
  "Starts services if needed and connects to a given build-id's CLJS REPL."
  ([]
   (cljs-repl :user-hub))
  ([build-id]
   (start!)
   (shadow/watch build-id)
   (shadow/nrepl-select build-id)))

(comment
  (start-flowstorm-debugger)
  (stop-debugger!))
