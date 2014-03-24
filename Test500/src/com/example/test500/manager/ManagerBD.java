package com.example.test500.manager;

import com.example.test500.App;

/**
 * Created by Artem on 24.03.14.
 */
public class ManagerBD {

    private static ManagerBD sManagerBD = null;
    private String mPathBd = null;

    private ManagerBD(){
        mPathBd = App.getPathBD();
    }

    public static ManagerBD getInstance(){

        if(sManagerBD == null){
            sManagerBD = new ManagerBD();
        }

        return sManagerBD;
    }

}
