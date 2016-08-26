package com.iamadonar.utils;

import java.util.regex.Pattern;

import android.widget.EditText;

public class Validation {
	 
    // Regular Expression
    // you can change the expression based on your need
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String ALPHABETS_REGEX = "^[a-zA-Z ]*$";
    private static final String DATE_REGEX = "^([1-9]|[12][0-9]|3[01])[// /.]([1-9]|1[012])[// /.](19|20)\\d{2}$";
    private static final String PHONE_REGX = "^\\d{10}$";
 
    // Error Messages
    private static final String REQUIRED_MSG = "required";
    private static final String EMAIL_MSG = "invalid email";
    private static final String ALPHABET_MSG = "invalid input";
    private static final String DATE_MSG = "invalid DATE";
    private static final String NULL_MSG = "Null Values";
    private static final String PHONE_MSG = "Invalid Pin Number,Please Enter 10 Digit number";
    
 
    // call this method when you need to check email validation
    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }
 
    // call this method when you need to check phone number validation
    public static boolean isAlphabets(EditText editText, boolean required) {
        return isValid(editText, ALPHABETS_REGEX, ALPHABET_MSG, required);
    }
   
    
 // call this method when you need to check phone number validation
    public static boolean isDate(EditText editText, boolean required) {
        return isValid(editText, DATE_REGEX, DATE_MSG, required);
    }
    
    public static boolean isNull(EditText editText, boolean required) {
        return isValidNull(editText, NULL_MSG, required);
    }
    
    public static boolean isPhoneNum(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGX, PHONE_MSG, required);
    }
    
    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {
 
        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);
 
        // text required and editText is blank, so return false
        if ( required && !hasText(editText) ) return false;
 
        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        };
 
        return true;
    }
    
    
	public static boolean isValidNull(EditText editText, String errMsg, boolean required) {
    	 
        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);
 
        // text required and editText is blank, so return false
        if ( required && !hasText(editText) ) return false;
        
        if (text.length() == 0) {
        	return false;
		}

 
        return true;
    }
 
    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {
 
        String text = editText.getText().toString().trim();
        editText.setError(null);
 
        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }
 
        return true;
    }
}
