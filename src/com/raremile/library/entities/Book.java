package com.raremile.library.entities;

import java.sql.Timestamp;

public class Book {
	private int bookID;
	private MasterBook masterbook;
	private boolean status;
	private Timestamp issuedate;
	private Timestamp duedate;
	public int getBookID() {
		return bookID;
	}
	public void setBookID(int bookID) {
		this.bookID = bookID;
	}
	public MasterBook getMasterbook() {
		return masterbook;
	}
	public void setMasterbook(MasterBook masterbook) {
		this.masterbook = masterbook;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Timestamp getIssuedate() {
		return issuedate;
	}
	public void setIssuedate(Timestamp issuedate) {
		this.issuedate = issuedate;
	}
	public Timestamp getDuedate() {
		return duedate;
	}
	public void setDuedate(Timestamp duedate) {
		this.duedate = duedate;
	}

}
