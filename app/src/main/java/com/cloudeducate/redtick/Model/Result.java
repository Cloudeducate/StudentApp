package com.cloudeducate.redtick.Model;

/**
 * Created by abhishek on 26-01-2016.
 */
public class Result {

    private String examname;
    private String year;
    private String marks;
    private String average;
    private String highest;

    public String getexam(){ return examname;}

    public void setExam(String examname){ this.examname=examname;}

    public String getyear(){ return year;}

    public void setyear(String year){ this.year=year;}

    public String getmarks(){ return marks;}

    public void setmarks(String marks){ this.marks=marks;}

    public String gethighest(){ return highest;}

    public void setHighest(String highest){ this.highest=highest;}

    public String getaverage(){ return average;}

    public void setaverage(String average){ this.average=average;}

}
