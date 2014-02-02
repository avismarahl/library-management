package com.raremile.library.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.raremile.library.connection.DBConnection;
import com.raremile.library.entities.Author;
import com.raremile.library.entities.MasterBook;

/*
 * Manipulate the contents of the Author_ISBN table.
 */
public class AuthorISBNDAL {
	
	final static String INSERT_STATEMENT = "INSERT INTO AUTHOR_ISBN (AUTHOR_ID,ISBN) VALUES (?,?);";
	private static final Logger logger = Logger.getLogger("AUTHOR_ISBN_DAL_LOGGGER");

	/**
	 * 
	 * @param Author
	 * @param MasterBook
	 * 
	 *            Takes the Author and MasterBook objects and uses them to
	 *            insert into the table
	 * 
	 * 
	 */
	public static void insertAuthorISBN(Author author, MasterBook masterbook,
			Connection con) {
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
		PreparedStatement pstmt = null;
		try {
			logger.info("Preparing to insert Author-ISBN values into the table");
			pstmt = con.prepareStatement(INSERT_STATEMENT);
			pstmt.setInt(1, author.getAuthorID());
			pstmt.setInt(2, masterbook.getIsbn());
			pstmt.executeUpdate();
			con.commit();
			logger.info("The author-isbn has been inserted successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("See the error below\n",e);
		} finally {
			if (hasToCreate)
				DBConnection.closeObjects(null, con, pstmt);
		}

	}
}
