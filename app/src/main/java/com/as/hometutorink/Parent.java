package com.as.hometutorink;

class Parent {

    String first_name;
    String last_name;
    String address;
    String parentID;

    public Parent(){

    }

    public Parent(String first_name, String last_name, String address, String parentID) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.parentID = parentID;
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

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }
}
