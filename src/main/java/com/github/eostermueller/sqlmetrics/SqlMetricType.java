package com.github.eostermueller.sqlmetrics;

import java.util.Random;

/**
 * The static createRandom() creates a new instance of this object that
 * represents a single SQL statement, and the environment (server name, cluster instance, webctx, db name, etc...) where it was executed.
 * @author erikostermueller
 *
 */
class SqlMetricType {
	
	/**
	 * Low maint!  Just add values to these arrays and re-run  the program to get more/less/different values.
	 */
	private static String appSrvAry[] = {"myAppServer", "yourAppServer", "otherAppServer", "juicyAppServer", "awnryAppServer", "troubleAppServer"};
	private static String instanceAry[] = {"8080", "8081", "8082"};
	
	private static String webCtxAry[] = {"myCtx", "yourCtx", "otherCtx" };
	private static String dbSrvAry[] = {"myDbServer", "yourDbServer", "otherDbServer"};
	private static String sqlTypeAry[] = {"d", "u", "s", "i", "o"};
	private static String sqlTableNameAry[] = {"CUST", "ORDER", "ACCOUNT", "PROD", "INVENTORY", "EMPLOYEE", "CONFIG"};
	private static String guidAry[] = {"d05f", "1aa1", "f3d0", "39bd", "59a0", "6e1f", "05d0", "c9bd", "7af0"};

	private static final int RANGE_VARIANCE = 1000;
	private static final int RANGE_MIN = 500;
	private static final Object TABLE_DELIM = "~";
	private static final Object FIRST_LEVEL = "ws.sql";
	int appServerNdx;
	int instanceNdx;
	int webCtxNdx;
	int dbServerNdx;
	int sqlTypeNdx;
	String tables;
	int range;
	int min;
	int guidNdx;
	String getAppServer() {
		return appSrvAry[this.appServerNdx];
	}
	String getInstance() {
		return instanceAry[this.instanceNdx];
	}
	String getDbServer() {
		return dbSrvAry[this.dbServerNdx];
	}
	String getWebCtx() {
		return webCtxAry[this.webCtxNdx];
	}
	String getSqlType() {
		return sqlTypeAry[ this.sqlTypeNdx ];
	}
	String getGuid() {
		return guidAry[ this.guidNdx ];
	}
	String getTables() {
		return this.tables;
	}
	
	/**
	 * Based in values in this object, return a string that looks smthg like this:
	 * 
	 * 		ws.myAppSrv.myWebCtx.myDb.s.CUSTOMER~ORDER~PRODUCT.a8g6
	 * 
	 * @return
	 */
	String getMetricName() {
		StringBuilder sb = new StringBuilder();
		
		sb
			.append(FIRST_LEVEL)
//			.append(".").append(this.getAppServer() )
//			.append(".").append(this.getWebCtx() )
//			.append(".").append(this.getDbServer() )
			.append(".").append(this.getSqlType() )
			.append(".").append(this.getTables() )
			.append(".").append(this.getGuid() );
		
		return sb.toString();
	}
	static SqlMetricType createRandom() {
		SqlMetricType sm = new SqlMetricType();
		Random generator = new Random(); 
		sm.range = generator.nextInt(RANGE_VARIANCE)+1;
		sm.min = generator.nextInt(RANGE_MIN);
		
		sm.appServerNdx = generator.nextInt(appSrvAry.length);
		sm.instanceNdx = generator.nextInt(instanceAry.length);
		sm.webCtxNdx = generator.nextInt(webCtxAry.length);
		sm.dbServerNdx = generator.nextInt(dbSrvAry.length);
		sm.sqlTypeNdx = generator.nextInt(sqlTypeAry.length);
		sm.guidNdx = generator.nextInt( guidAry.length);
		
		int tableCount = generator.nextInt(sqlTableNameAry.length) +1;
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < tableCount; i++) {
			int sqlTableNdx = generator.nextInt(sqlTableNameAry.length);
			String tableName = sqlTableNameAry[ sqlTableNdx ];

			if (i>0) sb.append(TABLE_DELIM);
			sb.append(tableName);
		}
		sm.tables = sb.toString();
		
		return sm;
	}
}