package com.raremile.library.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.raremile.library.connection.DBConnection;
import com.raremile.library.entities.Category;

public class CategoryDAL {
	private static Logger logger = Logger.getLogger("CategoryDAL");

	private static PreparedStatement pstmt = null;
	private static final String INSERT_STATEMENT = "INSERT INTO CATEGORIES (CATEGORY_NAME) VALUES(?);";

	private static final String FIND_CATEGORY = "SELECT CATEGORY_ID FROM CATEGORIES WHERE CATEGORY_NAME = ?;";

	private static boolean hasToCreate = false;
	/***
	 * 
	 * @param Category
	 * @param con
	 *     			This method inserts the category object into the CATEGORY table
	 */

	public static void insertCategory(Category Category, Connection con) {
		BasicConfigurator.configure();

		if (con == null) {
			hasToCreate = true;
			try {
				con = DBConnection.getConnection();
			} catch (Exception e1) {

				e1.printStackTrace();
			}
		}
		pstmt = null;
		try {
			logger.info("Preparing to insert Category value into the table");
			pstmt = con.prepareStatement(INSERT_STATEMENT);
			pstmt.setString(1, Category.getCategoryName());
			pstmt.executeUpdate();
			con.commit();
			logger.info("The Category-isbn has been inserted successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("See the error below\n", e);
		} finally {
			if (hasToCreate)
				DBConnection.closeObjects(null, con, pstmt);
		}

	}
	/***
	 * 
	 * @param category
	 * @param con
	 * @return
	 * 
	 * This method queries the CATEGORY table and finds the categoryID of the category object as given 
	 * by the category.categoryName
	 */

	public static Category findCategoryID(Category category, Connection con) {
		BasicConfigurator.configure();
		ResultSet rs = null;

		if (con == null) {
			hasToCreate = true;
			try {
				con = DBConnection.getConnection();
			} catch (Exception e1) {

				e1.printStackTrace();
			}
		}
		pstmt = null;
		try {
			pstmt = con.prepareStatement(FIND_CATEGORY);
			pstmt.setString(1, category.getCategoryName());
			rs = pstmt.executeQuery();
			if (rs.first()) {
				category.setCategoryID(rs.getInt(1));
				return category;

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (hasToCreate)
				DBConnection.closeObjects(rs, con, null);
		}

		return null;
	}
}
