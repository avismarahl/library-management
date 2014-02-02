package com.raremile.library.process;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.raremile.library.dal.AuthorDAL;
import com.raremile.library.dal.CategoryDAL;
import com.raremile.library.dal.AuthorISBNDAL;
import com.raremile.library.dal.BookDAL;
import com.raremile.library.dal.CategoryISBNDAL;
import com.raremile.library.dal.MasterBookDAL;
import com.raremile.library.entities.Book;
import com.raremile.library.entities.Category;
import com.raremile.library.entities.MasterBook;
import com.raremile.library.entities.Author;

public class ProcessData {
	private static Book book = new Book();
	private static List<Author> authors = new ArrayList<Author>();
	private static List<Category> categories = new ArrayList<Category>();

	/***
	 * 
	 * @param isbn
	 * @param con
	 * @return boolean
	 * 
	 *         This method takes in the isbn and asks the DAL to check if the
	 *         method exists. If it doesn't exists. It will pass the control to
	 *         the input asking for the user to enter the author details. It it
	 *         exists, it will receive a masterbook object which will then be
	 *         used to insert into the Books database.
	 */

	public static boolean processISBN(int isbn, Connection con) {
		MasterBook masterbook = new MasterBook();

		masterbook.setIsbn(isbn);
		masterbook = MasterBookDAL.isExists(masterbook, con);
		if (masterbook == null)
			return false;
		else {
			book.setMasterbook(masterbook);
			book.setStatus(true);
			BookDAL.insertBook(book, con);
			return true;

		}

	}

	/***
	 * 
	 * @param isbn
	 * @param title
	 * @param publisher
	 * @param authors
	 * @param categories
	 * @param con
	 *            This method is called only when the masterbook doesn't exist
	 *            in the masterbook database. It will make a masterbook object
	 *            out of it using the data input by the user in the input
	 *            package, and then it will make a master book object out of it.
	 *            It uses the then made masterbook object and inserts into the
	 *            Books database.
	 */

	public static void makeMasterBookObjectAndInsert(int isbn, String title,
			String publisher, List<String> authors, List<String> categories,
			Connection con) {
		MasterBook masterbook = new MasterBook();
		masterbook.setIsbn(isbn);
		masterbook.setTitle(title);
		masterbook.setPublisher(publisher);
		Iterator<String> i = authors.iterator();
		int j = 0;
		while (i.hasNext()) {
			ProcessData.authors.add(j, new Author());
			ProcessData.authors.get(j).setAuthorName(i.next());
			Author localAuth = AuthorDAL.findAuthorID(
					ProcessData.authors.get(j), con);

			if (localAuth == null)
				AuthorDAL.insertAuthor(ProcessData.authors.get(j), con);
			AuthorDAL.findAuthorID(ProcessData.authors.get(j), con);

			j++;
		}
		masterbook.setAuthors(ProcessData.authors);

		i = categories.iterator();
		j = 0;
		while (i.hasNext()) {
			ProcessData.categories.add(j, new Category());
			ProcessData.categories.get(j).setCategoryName(i.next());
			Category localCategory = CategoryDAL.findCategoryID(
					ProcessData.categories.get(j), con);

			if (localCategory == null)
				CategoryDAL.insertCategory(ProcessData.categories.get(j), con);
			localCategory = CategoryDAL.findCategoryID(
					ProcessData.categories.get(j), con);

			j++;
		}
		masterbook.setCategories(ProcessData.categories);
		MasterBookDAL.insertMasterBook(masterbook, con);
		Iterator<Author> AuthorObjectIterator = masterbook.getAuthors()
				.iterator();
		// Clear the bugs starting from here.
		while (AuthorObjectIterator.hasNext()) {
			Author tempAuth = AuthorObjectIterator.next();
			tempAuth = AuthorDAL.findAuthorID(tempAuth, con);
			AuthorISBNDAL.insertAuthorISBN(tempAuth, masterbook, con);

		}
		Iterator<Category> CategoryObjectIterator = masterbook.getCategories()
				.iterator();

		while (CategoryObjectIterator.hasNext()) {
			Category tempCategory = CategoryObjectIterator.next();
			tempCategory = CategoryDAL.findCategoryID(tempCategory, con);
			CategoryISBNDAL.insertCategoryISBN(tempCategory, masterbook, con);
		}
		// till here

		book.setMasterbook(masterbook);
		book.setStatus(true);
		BookDAL.insertBook(book, con);

	}

}
