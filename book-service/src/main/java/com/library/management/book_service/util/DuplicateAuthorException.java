package com.library.management.book_service.util;

public class DuplicateAuthorException extends Exception{

    public DuplicateAuthorException(String message){
        super(message);
    }
}
