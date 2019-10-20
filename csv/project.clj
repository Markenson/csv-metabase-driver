(defproject metabase/csv-driver "1.0.0-SNAPSHOT"
  :min-lein-version "2.5.0"

  :repositories [
  ["csvjdbc4metabase" {
    :url "https://maven.pkg.github.com/Markenson/csvjdbc4metabase",
	:password "672fece31cd8c83673b2ca364236f6fd71c8e2fb", ;public read-only token
	:username "markenson"
	}
	]
   ]
  :dependencies
  [[net.sourceforge.csvjdbc/csvjdbc "1.0.10-metabase"]]

  :profiles
  {:provided
   {:dependencies [[metabase-core "1.0.0-SNAPSHOT"]]}

   :uberjar
   {:auto-clean    true
    :aot           :all
    :javac-options ["-target" "1.8", "-source" "1.8"]
    :target-path   "target/%s"
    :uberjar-name  "csv.metabase-driver.jar"}})
