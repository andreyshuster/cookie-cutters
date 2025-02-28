#!/usr/bin/env bb
;; babashka script to create new clojure project with deps.edn

(require '[babashka.fs :as fs]
         '[clojure.string :as str])

;; Function to create the project structure
(defn create-project [project-name]
  (let [base-dir (str project-name)
        ns-name (str/replace project-name #"-" "_")
        src-dir (str base-dir "/src/" ns-name)
        core-file (str src-dir "/core.clj")
        deps-file (str base-dir "/deps.edn")]

    ;; Create directories
    (fs/create-dirs src-dir)

    ;; Write deps.edn
    (spit deps-file
          (str "{:paths [\"src\"]\n"
               " :deps {org.clojure/clojure {:mvn/version \"1.11.1\"}}\n"
               " :aliases {:run {:main-opts [\"-m\" \"" ns-name ".core\"]}}}\n"))

    ;; Write core.clj
    (spit core-file
          (str "(ns " ns-name ".core\n"
               "  (:gen-class))\n\n"
               "(defn -main [& args]\n"
               "  (println \"Hello from " project-name "!\"))\n"))

    (println "Created project:" project-name)
    (println "To run it: cd" project-name "&& clojure -M:run")))

;; Main logic
(let [args *command-line-args*]
  (if (or (nil? args) (empty? args))
    (do
      (println "Please provide a project name! Usage: bb create_project.bb <project-name>")
      (System/exit 1))  ;; Exit with error code
    (create-project (first args))))
