package com.raremile.library.dal;

import com.raremile.library.connection.DBConnection;
import com.raremile.library.entities.Author;
import com.raremile.library.entities.Category;
import com.raremile.library.entities.MasterBook;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class MasterBookDAL {
	private static Logger logger = Logger.getLogger("MasterBookDAL");

	static Connection con = null;
	static PreparedStatement pstmt = null;
	private static final String MASTER_BOOK_INSERT = "INSERT INTO MASTER_BOOKS(ISBN,TITLE,PUBLISHER) VALUES(?,?,?);";
	private static final String MASTER_BOOK_SELECT = "SELECT TITLE,PUBLISHER FROM MASTER_BOOKS WHERE ISBN=?;";
	private static final String MASTER_BOOK_AUTHOR = "SELECT AUTHOR_NAME,AUTHOR_ID FROM AUTHORS WHERE AUTHOR_ID IN (SELECT AUTHOR_ID FROM AUTHOR_ISBN WHERE ISBN=?);";
	private static final String MASTER_BOOK_CATEGORY = "SELECT CATEGORY_NAME,CATEGORY_ID FROM CATEGORIES WHERE CATEGORY_ID IN (SELECT CATEGORY_ID FROM CATEGORY_ISBN WHERE ISBN=?);";
	private static final String MASTER_BOOK_UPDATE = " UPDATE MASTER_BOOKS SET TITLE= ?,PUBLISHER=? WHERE ISBN=?;";

	/*
	 * For inserting entries in database
	 */
	public static void insertMasterBook(MasterBook masterbook, Connection con) {
		boolean hasToCreate = false;

		if (con == null) {
			try {
				con = DBConnection.getConnection();
				hasToCreate = true;
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		try {
			pstmt = con.prepareStatement(MASTER_BOOK_INSERT);
			pstmt.setInt(1, masterbook.getIsbn());
			pstmt.setString(2, masterbook.getTitle());
			pstmt.setString(3, masterbook.getPublisher());
			pstmt.executeUpdate();
			con.commit();
			logger.info("Inserted correctly");
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (hasToCreate)
				DBConnection.closeObjects(null, con, pstmt);
		}
	}

	public static void updateMasterBook(MasterBook masterbook, Connection con) {
		boolean hasToCreate = false;

		if (con == null) {
			try {
				con = DBConnection.getConnection();
				hasToCreate = true;

			}

			catch (Exception e)// for the time being
			{
				e.printStackTrace();
			}
		}

		try {
			pstmt = con.prepareStatement(MASTER_BOOK_UPDATE);
			pstmt.setString(1, masterbook.getTitle());
			pstmt.setString(2, masterbook.getPublisher());
			pstmt.setInt(3, masterbook.getIsbn());
			pstmt.executeUpdate();
			con.commit();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (hasToCreate)
				DBConnection.closeObjects(null, con, pstmt);
		}

	}

	/**
	 * 
	 * @param masterbook
	 * @return masterbook
	 * 
	 *         The point of this method is to query the database for the given
	 *         isbn which would have been sent to the method through the @param.
	 *         Using this object, the method queries the db and returns the
	 *         values of the particular isbn if it exists. The methods makes the
	 *         Masterbook object out of it and will return it to the process.
	 *         Additional process includes making the authors objects and the
	 *         categories objects.
	 * 
	 *         Or else, it will return null, signifying the process that such a
	 *         book with that particular isbn doesn't exist.
	 */

	public static MasterBook isExists(MasterBook masterbook,
			Connection con) {

		List<Author> authors = new ArrayList<Author>();
		List<Category> categories = new ArrayList<Category>();

		if (con == null) {
			try {
				con = DBConnection.getConnection();
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		try {
			pstmt = con.prepareStatement(MASTER_BOOK_SELECT);
			pstmt.setInt(1, masterbook.getIsbn());
			ResultSet rs_select = pstmt.executeQuery();

			if (rs_select.next()) {
				masterbook.setTitle(rs_select.getString(1));
				masterbook.setPublisher(rs_select.getString(2));

				pstmt = con.prepareStatement(MASTER_BOOK_AUTHOR);
				pstmt.setInt(1, masterbook.getIsbn());
				ResultSet rs = pstmt.executeQuery();
				logger.info("The result set is in the following format: AuthorName,AuthorID");
				int i = 0;
				while (rs.next()) {
					authors.add(new Author());
					authors.get(i).setAuthorName(rs.getString(1));
					authors.get(i).setAuthorID(rs.getInt(2));
					i++;
				}
				masterbook.setAuthors(authors);
				pstmt = con.prepareStatement(MASTER_BOOK_CATEGORY);
				pstmt.setInt(1, masterbook.getIsbn());
				rs = pstmt.executeQuery();
				logger.info("The result set is in the following format: CategoryName,CategoryID");
				i = 0;
				while (rs.next()) {

					categories.add(new Category());
					categories.get(i).setCategoryName(rs.getString(1));
					categories.get(i).setCategoryID(rs.getInt(2));
					i++;
				}
				masterbook.setCategories(categories);
				logger.info("The masterbook object has been made and is being sent to process");
				return masterbook;
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("The isbn-query returned false. Null reference is being returned");
		return null;

	}
	/*
	 * public void deleteMasterBook(MasterBook masterbook) {
	 * 
	 * isbn=masterbook.isbn;
	 * 
	 * 
	 * final String BOOK_SELECT="SELECT * FROM BOOKS WHERE ISBN=?" ; final
	 * String MASTER_BOOK_DELETE=" DELETE FROM MASTER_BOOK WHERE ISBN=?";
	 * 
	 * try { con= LibraryConnection.getConnection();
	 * pstmt=con.prepareStatement(BOOK_SELECT); pstmt.setInt(1,isbn); ResultSet
	 * rs=pstmt.executeQuery();
	 * 
	 * if(!rs.next())
	 * 
	 * { pstmt=con.prepareStatement(MASTER_BOOK_DELETE); pstmt.setInt(1,isbn);
	 * pstmt.executeUpdate(); con.commit(); }
	 * 
	 * else throw new MyException me("delete from book"); }
	 * 
	 * catch(MyException me)//for the time being { me.showMsg(); }
	 * 
	 * finally { LibraryConnection.closeObjects(rs,con,pstmt); } }
	 */
}
