package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Conndb {
	
	//打开数据库
	public static Connection openConn() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://localhost:3306/mydb";
			conn = DriverManager.getConnection(url,"root","");
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}

	//关闭数据库
	public static void closeConn(ResultSet rs ,PreparedStatement ps,Connection conn) {
		try {
			if(rs != null) {
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
			if(conn != null) {
				conn.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}



//package com.hjq.db;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//public class Conndb {
//
//	/**
//	 * 打开数据库
//	 */
//	public static Connection openConn(){
//		Connection conn = null;
//		try {
//			Class.forName("com.mysql.jdbc.Driver");//利用反射实例化驱动类
//			String url = "jdbc:mysql://localhost:3306/mydb";
//			conn = DriverManager.getConnection(url, "root","");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return conn;
//	}
//	
//	
//	
//	/**
//	 * 关闭数据库
//	 */
//	public static void closeConn(ResultSet rs ,PreparedStatement ps,Connection conn){
//		try {
//			if(rs!=null){
//				rs.close();
//			}
//			if(ps!=null){
//				ps.close();
//			}
//			if(conn!=null){
//				conn.close();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//}
