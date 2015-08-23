package com.github.eostermueller.sqlmetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;

/**
 * Inserts some dummy performance metrics into a localhost InfluxDB time series database.
 * This is a first attempt to structure a set of metrics to be analyzed/viewed in Grafana.
 * Ultimately, wuqiSpank.org will capture the JDBC metrics by using the java.lang.instrument API.
 * wuqiSpank will then push the sql metrics to InfluxDB for viewing by Grafana.
 * 
 * Instructions:
 * 1) Install and start InfluxDB on localhost.
 * 2) Run this java program.  It will create dummy metrics in the InfluxDB.
 * 3) Install and start Grafana using these instructions:
 * 		http://docs.grafana.org/installation/installation/  
 * 4) Open Grafana in a browser (http://localhost:3000)   
 * 5) Configure Grafana to connect to the above InfluxDB.
 * 		http://docs.grafana.org/datasources/influxdb/
 * 6) Use these instructions to import src/main/resources/grafanaSqlDashboard.json into Grafana
 * 		http://docs.grafana.org/reference/export_import/ 
 * 
 * @author erikostermueller
 *
 */
public class SqlInfluxJava {

	public static void main(String args[]) {
		InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086", "root", "root");
		String dbName = "wuqiSpankDb";
  		influxDB.deleteDatabase(dbName);
		influxDB.createDatabase(dbName);

		
		long endTimeOfTestData = System.currentTimeMillis();
		int numSeriesToCreate = 1000;
		int numCharactersPerLine = 100;
		int numLines = numSeriesToCreate / numCharactersPerLine;
		for(int i = 0; i < numSeriesToCreate;i++){

			writeSeries( influxDB, dbName, SqlMetricType.createRandom(), endTimeOfTestData );
			System.out.print(".");
			if ( (i%numCharactersPerLine==0) && (i!=0) ) {
				System.out.println(" " + numLines--);
			}
		}
		
		System.out.println("Finished with metric write.");
		//queryDb(influxDB, dbName);
	}


	/**
	 * INSERT all metrics for a few seconds of activity for a single SQL statement.
	 * @param influxDB -- the time series database that we'll insert metrics into
	 * @param dbName -- the name of the InfluxDB database.
	 * @param smt -- Details of the environment in which the SQL statement is running.
	 * @param endTime
	 */
	private static void writeSeries(InfluxDB influxDB, String dbName, SqlMetricType smt, long endTime) {
		
		BatchPoints batchPoints = BatchPoints
                .database(dbName)
                .tag("async", "true")
                .retentionPolicy("default")
                .consistency(ConsistencyLevel.ALL)
                .build();
		
		Random generator = new Random(); 
		int decrement = 0;
		/**
		 * How many of this query will be executed in each 100ms time period?
		 * This random variable determines that.
		 */
		int maxNumberOfInvocationsPerWindow = 50;
		int inovcationsPermsWindow = generator.nextInt(maxNumberOfInvocationsPerWindow)+1; //as many as one per 10ms.
		int millisBetweenEachRequest = maxNumberOfInvocationsPerWindow / inovcationsPermsWindow;
		int numberOfWindows = 100;
		
		for (int i = 0; i < numberOfWindows; i++) {
			for( int j = 1; j < inovcationsPermsWindow; j++ ) {
				int myRandomResponseTime = generator.nextInt(smt.range) + smt.min;			
				decrement +=millisBetweenEachRequest;//will display metrics every 100ms, _ending_ with the given endTime.
				batchPoints.point( Point.measurement( smt.getMetricName())
	                    .time(endTime-decrement, TimeUnit.MILLISECONDS)
	                    .field("value", myRandomResponseTime)
	                    .tag("webRqId","2lfkdfljlf") //hard coded for now.
	                    .tag("appSrv",smt.getAppServer() )
	                    .tag("instance",smt.getInstance() )
	                    .tag("webCtx",smt.getWebCtx() )
	                    .tag("dbSrv",smt.getDbServer() )
	                    .tag("sqlType",smt.getSqlType() )
	                    .tag("id",smt.getGuid() )
	                    .build() );
			}
		}
		influxDB.write(batchPoints);
	}
	private static void queryDb(InfluxDB influxDB, String dbName) {
		Query query = new Query("SELECT idle FROM cpu", dbName);
		QueryResult result = influxDB.query(query);
		
		for(Result r : result.getResults()) {
			if (r!=null && r.getSeries() != null) {
				for(Series s : r.getSeries()) {
					System.out.println("queried series [" + s.getName() + "]");
					for (Object o : s.getValues() ) {
						System.out.println("Values [" + o.getClass().getName() + "]");
						if (o instanceof ArrayList) {
							ArrayList al = (ArrayList)o;
							for(Object myObject : al) {
								System.out.println("myObject [" + myObject.getClass().getName() + "]");
								String str = null;
								Double d = null;
								if (myObject instanceof String) {
									str = (String)myObject;
									System.out.println("Found string [" + s + "]");
								} else if (myObject instanceof Double) {
									Double dd = (Double)myObject;
									System.out.println("Found double [" + dd.toString() + "]");
								}
							}
						}
					}
				}
			}
		}
		
	}
}
