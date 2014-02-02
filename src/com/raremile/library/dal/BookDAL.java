package com.raremile.library.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.raremile.library.connection.DBConnection;
import com.raremile.library.entities.Book;
import com.raremile.library.entities.MasterBook;

public class BookDAL {

	private static Logger logger = Logger.getLogger("BooksDAL");
	private static PreparedStatement pstmt = null;

	private static final String BOOKS_INSERT = "INSERT INTO BOOKS(ISBN,STATUS) VALUES(?,?);";
	private static final String BOOKS_CHECK_STATUS = "SELECT BOOK_ID,STATUS FROM BOOKS WHERE (ISBN=? AND STATUS=1) LIMIT 1;";
	private static final String BOOKS_UPDATE_INFO = "UPDATE BOOKS SET STATUS=0 WHERE BOOK_ID=?;";
	private static final String BOOKS_RETRIEVE_INFO = "SELECT STATUS,ISSUE_DATE,DEPOSIT_DATE FROM BOOKS WHERE BOOK_ID=?;";
	private static final String BOOKS_GET_DATES = "SELECT ISSUE_DATE,DUE_DATE FROM BOOKS WHERE BOOK_ID = ?;";

	private static boolean hasToCreate = false;

	/***
	 * 
	 * @param book
	 * @param con
	 * 
	 *            This method takes in the book object and inserts the
	 *            information inside the BOOKS table.
	 */
	public static void insertBook(Book book, Connection con) {
		if (con == null) {
			try {
				con = DBConnection.getConnection();
				hasToCreate = true;
			}

			catch (Exception e) {// for time being working on exception
				e.printStackTrace();
			}
		}

		try {
			pstmt = con.prepareStatement(BOOKS_INSERT);
			pstmt.setInt(1, book.getMasterbook().getIsbn());
			pstmt.setInt(2, 1);
			pstmt.executeUpdate();
			con.commit();
			logger.info("Inserted Successfully");
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (hasToCreate) {// close connection if it is opened here only
				DBConnection.closeObjects(null, con, pstmt);
			}
		}
	}

	/**
	 * 
	 * @param book
	 * @param con
	 * @return
	 * 
	 *         Checks if the book is available or not. First, it checks, if the
	 *         book with such an ISBN exists, and later it checks if such a book
	 *         with the given ISBN is available
	 * 
	 */
	public Book isAvailable(Book book, Connection con) {
		boolean hasToCreate = false;
		if (con == null) {
			try {
				con = DBConnection.getConnection();
				hasToCreate = true;
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			MasterBook masterbook = MasterBookDAL.findMasterBook(
					book.getMasterbook(), con);
			book.setMasterbook(masterbook);
			if (masterbook != null) {
				pstmt = con.prepareStatement(BOOKS_CHECK_STATUS);
				pstmt.setInt(1, book.getMasterbook().getIsbn());
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					book.setBookID(rs.getInt(1));
					book.setStatus(rs.getBoolean(2));

				}

				pstmt = con.prepareStatement(BOOKS_GET_DATES);
				pstmt.setInt(1, book.getBookID());
				pstmt.executeQuery();
				if (rs.next()) {
					book.setIssuedate(rs.getTimestamp(1));
					book.setDuedate(rs.getTimestamp(2));

				}
				return book;
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (hasToCreate)
				DBConnection.closeObjects(null, con, pstmt);
		}
		return (null);

	}

	/***
	 * 
	 * @param book
	 * @param con
	 * @return
	 * 
	 *         This method takes in the book object and issues it. Makes the
	 *         corresponding changes in the BOOKS table.
	 */

	public Book issueBook(Book book, Connection con) {

		if (con == null) {
			try {
				con = DBConnection.getConnection();
				hasToCreate = true;
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			pstmt = con.prepareStatement(BOOKS_UPDATE_INFO);
			pstmt.setInt(1, book.getBookID());
			pstmt.executeQuery();
			con.commit();
			logger.info("Status changed");

			pstmt = con.prepareStatement(BOOKS_RETRIEVE_INFO);
			pstmt.setInt(1, book.getBookID());
			ResultSet rs = pstmt.executeQuery();
			book.setStatus(rs.getBoolean(1));
			book.setIssuedate(rs.getTimestamp(2));
			book.setDuedate(rs.getTimestamp(3));
			return (book);
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (hasToCreate)
				DBConnection.closeObjects(null, con, pstmt);
		}
		return (null);
	}

}
