package com.raremile.library.input;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.raremile.library.connection.DBConnection;
import com.raremile.library.process.ProcessData;

public class LibraryDriver {
	/**
	 * 
	 * @param args
	 * 
	 *            Temporary front end for the software
	 */

	public static void main(String args[]) {
		int isbn;

		List<String> authors = new ArrayList<String>();
		List<String> categories = new ArrayList<String>();

		boolean flowcheck;
		Connection con = null;
		try {
			con = DBConnection.getConnection();
		} catch (Exception e) {

			e.printStackTrace();
		}
		System.out.println("Please enter the ISBN");
		Scanner sc = new Scanner(System.in);
		isbn = sc.nextInt();
		flowcheck = ProcessData.processISBN(isbn, con);
		if (!flowcheck) {

			System.out.println("Enter the list of authors. Enter -1 to finish");

			while (true) {
				String author_name;
				author_name = sc.nextLine();
				if (author_name.equalsIgnoreCase("-1"))
					break;
				authors.add(author_name);
			}
			System.out
					.println("Enter the list of categories. Enter -1 to finish");

			while (true) {
				String category_name;
				category_name = sc.nextLine();
				if (category_name.equalsIgnoreCase("-1"))
					break;
				categories.add(category_name);
			}
			System.out.println("Enter the publisher");
			String publisher;
			publisher = sc.nextLine();
			System.out.println("Enter the title");
			String title;
			title = sc.nextLine();
			ProcessData.makeMasterBookObjectAndInsert(isbn, title, publisher,
					authors, categories, con);
			DBConnection.closeObjects(null, con, null);

		}

	}

}
