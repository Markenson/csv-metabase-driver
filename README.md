# csv-metabase-driver
A CSV [metabase](https://www.metabase.com) driver based on incredible [csvjdbc](http://csvjdbc.sourceforge.net) driver. I've been [modified it](https://github.com/Markenson/csvjdbc4metabase) to work with metabase.

# Installation
Put [this file](https://github.com/Markenson/csv-metabase-driver/releases/download/v1.3.1/csv.metabase-driver.jar) on your metabase/plugins directory and restart Metabase. You'll see a CSV driver option on database creation.

You could see [this video](https://youtu.be/M4ccnTXiDtw) too.

# How to configure it
Check [how to load a CSV file by filesystem](https://github.com/Markenson/csv-metabase-driver/issues/1) and [how to load a CSV file by HTTP](https://github.com/Markenson/csv-metabase-driver/releases/tag/1.1.0) for more information.

If tour data contains **date values** check [this video](https://youtu.be/qrTux2jIwns).

# If you need built it

## with ansible

follow [these instructions](https://github.com/Markenson/dev-env/blob/main/README.md#to-build-csv-metabase-driver)

## by hand 

read and understand steps below

[steps for install clojure](https://github.com/Markenson/dev-env/blob/main/roles/clojure/tasks/main.yaml)

[steps for compile driver](https://github.com/Markenson/dev-env/blob/main/roles/csv-metabase-driver/tasks/main.yaml)
