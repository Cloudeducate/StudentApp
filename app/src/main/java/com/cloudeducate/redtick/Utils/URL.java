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
    public static String getAttendanceURL()
    {
        String url4="student/attendance.json";
        return Constants.BASE_URL + url4;
    }

    public static String getAssignmentDownloadURL(String filename){
        String url4 = "public/assets/uploads/assignments/" + filename;
        return Constants.BASE_URL + url4;
    }

    public static String getPerformanceRequestURL()
    {
        String url5="/student/performance.json";
        return Constants.BASE_URL + url5;
    }

    public static String getNotificationURL()
    {
        String url5="/notification/fetch";
        return Constants.BASE_URL + url5;
    }
    public static String sendNotificationURL(String id)
    {
        String url5="/notification/update/";
        return Constants.BASE_URL + url5+id+".json";
    }

    public static String getConversationStart()
    {
        String url6 = "conversation/find.json";
        return Constants.BASE_URL + url6;
    }

    public static String getMessageRequestURL(String messageID)
    {
        String url7 = "conversation/message/" + messageID + ".json";
        return Constants.BASE_URL + url7;
    }

    public static String getNotificationFetchURL()
    {
        String url8 = "notification/fetch.json";
        return Constants.BASE_URL + url8;
    }

    public static String updateNotificationStatusURL(String notificationID)
    {
        String url9 = "notification/update/" + notificationID + ".json";
        return Constants.BASE_URL + url9;
    }

    public static String getconversation() {
        String url4 = "conversation/all.json";
        return Constants.BASE_URL +url4 ;
    }
    public static String getmessages(String converstaionid) {
        String url6 = "/conversation/view/";
        return Constants.BASE_URL+url6+converstaionid+".json" ;
    }
    public static String sendmessages(String converstaionid) {
        String url6 = "/conversation/message/";
        return Constants.BASE_URL+url6+converstaionid+".json" ;
    }
    public static String getsubmitgradeURL(String assignment_id) {
        String url5 = "assignments/gradeIt/";
        return Constants.BASE_URL + url5+assignment_id+".json";
    }

}
