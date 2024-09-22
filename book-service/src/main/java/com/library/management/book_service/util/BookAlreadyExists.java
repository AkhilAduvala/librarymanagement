package com.library.management.book_service.util;

public class BookAlreadyExists extends Exception{

    public BookAlreadyExists(String message){
        super(message);
    }
}
