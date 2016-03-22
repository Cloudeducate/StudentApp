package com.cloudeducate.redtick.Model;

/**
 * Created by abhishek on 25-01-2016.
 */
public class Dashboard {
    private String metavalue;
    private String name;
    private String organisationname;
    private String rollno;
    private String email;
    private Boolean mobile;
    private String filename;
    public String getmetavalue() {
        return metavalue;
    }

    /**
     *
     * @param metavalue
     * The title
     */
    public void setMetavalue(String metavalue) {
        this.metavalue = metavalue;
    }

    /**
     *
     * @return
     * The description
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The description
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The deadline
     */
    public String getOrganisationname() {
        return organisationname;
    }

    /**
     *
     * @param organisationname
     * The deadline
     */
    public void setOrganisationname(String organisationname) {
        this.organisationname = organisationname;
    }

    /**
     *
     * @return
     * The id
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The id
     */
    public void setId(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The course
     */

}
