# sqlMetricsPusher
This repo contains sample code that creates metrics of dummy SQL statement executions.  It INSERTs them into an InfluxDB for viewing in a Grafana dashboard.

The dashboard below shows response time and throughput of the dummy SQL metrics.
Note the bar at the top for drilling down into the various machines/environments reporting metrics to this one InfluxDB server.

# Instructions
1) Install and start InfluxDB on localhost.

2) Run the SqlInfluxJava java program.  It will create dummy metrics in the InfluxDB.  
* Prerequisites:  Maven and a JDK
* Download this [zip file](https://github.com/eostermueller/sqlMetricsPusher/archive/master.zip).
* 'cd' to the same folder as the pom.xml file and type 'mvn clean package' to build the code.
* Run the program with this command:
* <code>
mvn exec:java -Dexec.mainClass="com.github.eostermueller.sqlmetrics.SqlInfluxJava"
</code>

3) Install and start Grafana using [these instructions](http://docs.grafana.org/installation/installation/).

4) Open Grafana in a browser (http://localhost:3000).

5) Configure Grafana to connect to the above InfluxDB, using [these instructions](http://docs.grafana.org/datasources/influxdb/).

6) Use [these instructions](http://docs.grafana.org/reference/export_import/) to import the [dasboard in json format](https://github.com/eostermueller/sqlMetricsPusher/raw/master/src/main/resources/grafanaSqlDashboard.json) into Grafana.

![Dashboard](https://cloud.githubusercontent.com/assets/175773/9430473/8fb4e5d4-49bc-11e5-9f26-176a730b3f76.png)
