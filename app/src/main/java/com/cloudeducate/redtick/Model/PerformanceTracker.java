package com.cloudeducate.redtick.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yogesh on 30/1/16.
 */
public class PerformanceTracker {
    private Integer week;
    private Integer grade;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The week
     */
    public Integer getWeek() {
        return week;
    }

    /**
     * @param week The week
     */
    public void setWeek(Integer week) {
        this.week = week;
    }

    /**
     * @return The grade
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * @param grade The grade
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

