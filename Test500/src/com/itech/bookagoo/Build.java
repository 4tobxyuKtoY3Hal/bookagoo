package com.itech.bookagoo;

import com.itech.bookagoo.work.BookAgooApi;

/**
 * Created by Artem on 29.03.14.
 */
public class Build {

    public static final boolean RELEASE = true;
    public static final boolean SEND_REPORT = true;

    public static final String BUUK_AGOO_API_SERVER = RELEASE ?
            BookAgooApi.SERVER.RELISE
            : BookAgooApi.SERVER.TEST;

    public static final String[] ARR_REPORT_EMAIL = RELEASE ?
            new String[] {"art7384@gmail.com"}
            : new String[] {"art7384@gmail.com"};

}
