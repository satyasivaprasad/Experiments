package com.newgame.validator;

 
public interface Validation {

    String getErrorMessage();

    boolean isValid(String text);

}