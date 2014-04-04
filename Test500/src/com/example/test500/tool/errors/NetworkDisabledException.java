package com.example.test500.tool.errors;

import java.io.PrintWriter;

/**
 * Created by Artem on 29.03.14.
 */
public class NetworkDisabledException  extends Exception {

    public NetworkDisabledException() {
        super("Network is disabled");
    }

    public NetworkDisabledException(String message) {
        super(message);
    }

    public NetworkDisabledException(String message, Throwable throwable) {
        super(message, throwable);
    }




}
