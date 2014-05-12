package com.itech.bookagoo.tool.errors;

/**
 * Created by Artem on 02.04.14.
 */
public class ApiException extends Exception {

    private int mCode = -1;

    public ApiException(String message, int code) {
        super(message);
        mCode = code;
    }

    public int getCode(){
        return mCode;
    }

}
