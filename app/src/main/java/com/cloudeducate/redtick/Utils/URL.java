package com.cloudeducate.redtick.Utils;

/**
 * Created by yogesh on 25/1/16.
 */
public class URL {

    public static String getStudentAssignmentRequestURL(String course_id) {
        String url = "student/assignments/";
        return Constants.BASE_URL + url + course_id + ".json";
    }
    public static String getStudentLoginURL()
    {
        String url1="auth/login.json";
        return Constants.BASE_URL + url1;
    }
    public static String getResultRequestURL()
    {
        String url2="student/result.json";
        return Constants.BASE_URL + url2;
    }
    public static String getCoursesURL()
    {
        String url3="student.json";
        return Constants.BASE_URL + url3;
    }

    public static String getAssignmentDownloadURL(String filename){
        String url4 = "public/assets/uploads/assignments/" + filename;
        return Constants.BASE_URL + url4;
    }


}
