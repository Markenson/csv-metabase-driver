(defproject metabase/csv-driver "1.0.1-SNAPSHOT"
  :min-lein-version "2.5.0"

  :repositories {"sonatype snapshots" "https://oss.sonatype.org/content/repositories/snapshots"}
  :dependencies
  [[br.com.markenson/csvjdbc "1.1.1-metabase-SNAPSHOT"]]

  :profiles
  {:provided
   {:dependencies [[metabase-core "1.0.0-SNAPSHOT"]]}

   :uberjar
   {:auto-clean    true
    :aot           :all
    :javac-options ["-target" "1.8", "-source" "1.8"]
    :target-path   "target/%s"
    :uberjar-name  "csv.metabase-driver.jar"}})
