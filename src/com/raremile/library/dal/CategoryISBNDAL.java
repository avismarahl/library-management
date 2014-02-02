package com.raremile.library.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.log4j.Logger;

import com.raremile.library.connection.DBConnection;
import com.raremile.library.entities.Category;
import com.raremile.library.entities.MasterBook;

/*
 * To manipulate the contents of the Category-ISBN table
 */
public class CategoryISBNDAL {
	private final static String INSERT_STATEMENT = "INSERT INTO CATEGORY_ISBN (Category_ID,ISBN) VALUES (?,?);";
	private final static Logger logger = Logger
			.getLogger("CATEGORY_ISBN_DAL_LOGGER");

	/**
	 * 
	 * @param category
	 * @param masterbook
	 * @param con
	 * 
	 *            Takes in the category and the masterbook object to update the
	 *            contents of the category-isbn table
	 */

	public static void insertCategoryISBN(Category category,
			MasterBook masterbook, Connection con) {
		boolean flag = true;

		/*
		 * Does the NULL check and creates a new connection if the connection
		 * doesn't exist
		 */
		if (con == null) {
			try {
				flag = false;
				con = DBConnection.getConnection();
			} catch (Exception e1) {

				e1.printStackTrace();
			}
		}
		PreparedStatement pstmt = null;
		try {
			logger.info("Preparing to insert the values into the logger");
			pstmt = con.prepareStatement(INSERT_STATEMENT);
			pstmt.setInt(1, category.getCategoryID());
			pstmt.setInt(2, masterbook.getIsbn());
			pstmt.executeUpdate();
			con.commit();
			System.out.println("Category-ISBN Inserted Successfully");
		} catch (Exception e) {
			logger.info("Exception explanation: ", e);
		} finally {
			if (!flag)
				DBConnection.closeObjects(null, con, pstmt);
		}
	}

}
