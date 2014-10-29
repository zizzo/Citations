package com.faraday.citations;

/**
 * @author gabriele
 *
 *
 * The Citation class contains the id, text, author and category of a certain citation.
 */

public class Citation {
	
	private String text;
	private String author;
	private Category category;
	private Integer id;
	
	public Citation(Integer id, String text, String author, Category category) {
		setText(text);
		setAuthor(author);
		setCategory(category);
		setId(id);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
