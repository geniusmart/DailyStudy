package com.genius.binder;

import com.genius.binder.Book;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}
