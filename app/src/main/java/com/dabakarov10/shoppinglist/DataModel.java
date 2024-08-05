package com.dabakarov10.shoppinglist;

public class DataModel {

    public String name;
    boolean checked;

    DataModel(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


    @Override
    public String toString() {
        return "DataModel{" +
                "name='" + name
                //+ '\'' +
              //  ", checked=" + checked
                 + '}';
    }
}
