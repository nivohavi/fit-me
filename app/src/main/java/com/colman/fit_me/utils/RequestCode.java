package com.colman.fit_me.utils;

import java.util.ArrayList;
import java.util.List;

public class RequestCode {

    private static String myRQ;
    private static List<RQChangedListener> listeners = new ArrayList<RQChangedListener>();

    public static String getMyRQ() { return myRQ; }

    public static void setMyRQ(String value) {
        myRQ = value;

        for (RQChangedListener l : listeners) {
            l.OnMyRQChanged();
        }
    }

    public static void addMyRQListener(RQChangedListener l) {
        listeners.add(l);
    }
}
