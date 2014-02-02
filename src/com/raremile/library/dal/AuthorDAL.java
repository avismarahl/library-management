package com.raremile.library.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.raremile.library.connection.DBConnection;
import com.raremile.library.entities.Author;

public class AuthorDAL {
	private static Logger logger = Logger.getLogger("AuthorDAL");

	private static PreparedStatement pstmt = null;
	private static final String INSERT_STATEMENT = "INSERT INTO AUTHORS(AUTHOR_NAME) VALUES(?);";// AUTHOR_NAME
																									// ADDED
	private static final String FIND_AUTHOR = "SELECT AUTHOR_ID FROM AUTHORS WHERE AUTHOR_NAME = ?;";// AUTHOR_ID
																										// TO
																										// *

	/* *
	 * This method will insert the author values into the author database
	 */
	public static void insertAuthor(Author author, Connection con) {
		BasicConfigurator.configure();
		boolean hasToCreate = false;

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
			logger.info("Preparing to insert Author value into the table");
			pstmt = con.prepareStatement(INSERT_STATEMENT);
			pstmt.setString(1, author.getAuthorName());
			pstmt.executeUpdate();
			con.commit();
			logger.info("The author-isbn has been inserted successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("See the error below\n", e);
		} finally {
			if (hasToCreate)
				DBConnection.closeObjects(null, con, pstmt);
		}

	}

	/*
	 * This method checks the author database if the author-title exists in the
	 * database. It returns true if the title exists. It returns false if the
	 * author doesn't exist
	 */

	/*
	 * This method finds the author_name according to the author_id. It returns
	 * the author object when it encounters the corresponding the author name. s
	 */

	public static Author findAuthorID(Author author, Connection con) {
		BasicConfigurator.configure();
		ResultSet rs = null;
		boolean hasToCreate = false;

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
			pstmt = con.prepareStatement(FIND_AUTHOR);
			pstmt.setString(1, author.getAuthorName());
			System.out.println(author.getAuthorName());
			rs = pstmt.executeQuery();
			if (rs.first()) {
				author.setAuthorID(rs.getInt(1));
				System.out.println("" + rs.getInt(1));
				return author;

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (hasToCreate)
				DBConnection.closeObjects(rs, con, pstmt);
		}

		return null;
	}

}
