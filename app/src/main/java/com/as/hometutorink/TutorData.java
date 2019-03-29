package com.as.hometutorink;

import java.util.ArrayList;

class TutorData {
    String tutor_id;
    String first_name;
    String last_name;
    String address;
    String job_title;
    String job_desc;
    ArrayList<String> qualifications;
    ArrayList<String> subjects;

    public TutorData(){

    }

    public TutorData(String tutor_id, String first_name, String last_name, String address, String job_title, String job_desc, ArrayList<String> qualifications, ArrayList<String> subjects) {
        this.tutor_id = tutor_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.job_title = job_title;
        this.job_desc = job_desc;
        this.qualifications = qualifications;
        this.subjects = subjects;
    }

    public String getTutor_id() {
        return tutor_id;
    }

    public void setTutor_id(String tutor_id) {
        this.tutor_id = tutor_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getJob_desc() {
        return job_desc;
    }

    public void setJob_desc(String job_desc) {
        this.job_desc = job_desc;
    }

    public ArrayList<String> getQualifications() {
        return qualifications;
    }

    public void setQualifications(ArrayList<String> qualifications) {
        this.qualifications = qualifications;
    }

    public ArrayList<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<String> subjects) {
        this.subjects = subjects;
    }
}
