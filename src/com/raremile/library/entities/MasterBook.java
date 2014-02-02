package com.raremile.library.entities;

import java.util.ArrayList;
import java.util.List;

public class MasterBook {
	private int isbn;
	private String title;
	private String publisher;
	private List <Author> authors = new ArrayList<Author>();
	private List <Category> categories = new ArrayList<Category>();
	
	
	
	public int getIsbn() {
		return isbn;
	}
	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}
	public void  setTitle(String title) {
		this.title = title;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	public List<Author> getAuthors() {
		return authors;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
	public String getTitle() {
		return title;
	}
	public String getPublisher() {
		return publisher;
	}
}
