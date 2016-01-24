package com.cloudeducate.redtick.Utils;

/**
 * Created by yogesh on 25/1/16.
 */
public class URL {

    public static String getStudentAssignmentRequestURL() {
        String url = "student/assignments/1.json";
        return Constants.BASE_URL + url;
    }

}
