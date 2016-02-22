package com.genius.dailystudy;

import com.genius.dailystudy.Book;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}
