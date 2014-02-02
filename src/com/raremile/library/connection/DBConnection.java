package com.raremile.library.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_DRIVER_NAME = "com.mysql.jdbc.Driver";
	
	/**
	 * Gets the connection for MYSQL DB
	 * @param hostName
	 * @param portNumber
	 * @param userId
	 * @param password
	 * @return connection
	 * @throws Exception 
	 */
	public static Connection getConnection() throws Exception{
		Connection con = null;
		try {
			Class.forName(DB_DRIVER_NAME).newInstance();
			con = DriverManager.getConnection("jdbc:mysql://"+DBConstants.HOST_DB+":"+DBConstants.PORT_DB+"/"+DBConstants.DB_NAME, DBConstants.DB_USERNAME, DBConstants.DB_PASSWORD);
			con.setAutoCommit(false);
		}
		catch (InstantiationException e) {
			throw new Exception("InstantiationException "+e.getMessage(), e);
		}
		catch (IllegalAccessException e) {
			throw new Exception("IllegalAccessException "+e.getMessage(), e);
		}
		catch (ClassNotFoundException e) {
			throw new Exception("ClassNotFoundException "+e.getMessage(), e);
		}
		catch (SQLException e) {
			throw new Exception("SQLException "+e.getMessage(), e);
		}
		return con;
	}
	
	public static void closeObjects(ResultSet rs, Connection con, PreparedStatement pstmt){
		if(rs != null){
			try {
				rs.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(pstmt != null){
			try {
				pstmt.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(con != null){
			try {
				con.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	

}
