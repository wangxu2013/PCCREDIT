package com.cardpay.pccredit.tools;

public class DataSourceContextHolder {
	
	public static final String BANK = "ds1";
	public static final String PCCREDIT = "ds";

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setDbType(String dbType) {
		contextHolder.set(dbType);
	}

	public static String getDbType() {
		return ((String) contextHolder.get());
	}

	public static void clearDbType() {
		contextHolder.remove();
	}

}
