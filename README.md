# sqlMetricsPusher
This repo contains sample code that creates metrics of dummy SQL statement executions.  It INSERTs them into an InfluxDB for viewing in a Grafana dashboard.

The dashboard below shows response time and throughput of the dummy SQL metrics.
Note the bar at the top for drilling down into the various machines/environments reporting metrics to this one InfluxDB server.

For instructions, [read here](https://github.com/eostermueller/sqlMetricsPusher/blob/master/src/main/java/com/github/eostermueller/sqlmetrics/SqlInfluxJava.java).

![Dashboard](https://cloud.githubusercontent.com/assets/175773/9430473/8fb4e5d4-49bc-11e5-9f26-176a730b3f76.png)
