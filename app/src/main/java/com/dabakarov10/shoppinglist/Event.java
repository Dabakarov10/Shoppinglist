package com.dabakarov10.shoppinglist;

import java.util.ArrayList;

public class Event {
    public String eventName;
    public String eventCountUsers;
    public String eventDate;
    public String eventCode;
    public String eventDescription;
    public String event_type;
    public ArrayList<DataModel> itemsListDM = new ArrayList<>();

    public Event() {
        this.eventName = null;
        this.eventCountUsers = null;
        this.eventDate = null;
        this.eventCode = null;
        this.eventDescription = null;
        this.event_type = null;
    }

    public Event(String eventName, String eventCountUsers, String eventDate, String eventCode, String eventDescription, String event_type,ArrayList<DataModel> itemsListDM) {
        this.eventName = eventName;
        this.eventCountUsers = eventCountUsers;
        this.eventDate = eventDate;
        this.eventCode = eventCode;
        this.eventDescription = eventDescription;
        this.event_type = event_type;
        this.itemsListDM =itemsListDM;
    }

    public Event(Event event) {
        this.eventName = event.getEventName();
        this.eventCountUsers = event.getEventCountUsers();
        this.eventDate = event.getEventDate();
        this.eventCode = event.getEventCode();
        this.eventDescription = event.getEventDescription();
        this.event_type = event.getEvent_type();
        this.itemsListDM =event.getItemsListDM();
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventCountUsers() {
        return eventCountUsers;
    }

    public void setEventCountUsers(String eventCountUsers) {
        this.eventCountUsers = eventCountUsers;
    }

    public ArrayList<DataModel> getItemsListDM() {
        return itemsListDM;
    }

    public void setItemsListDM(ArrayList<DataModel> itemsListDM) {
        this.itemsListDM = itemsListDM;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String toStringItamList(){
        String output="";
        for (int i=0;i<itemsListDM.size();i++){
            output = output + itemsListDM.get(i);
        }
        return output;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", eventCountUsers='" + eventCountUsers + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", eventCode='" + eventCode + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", event_type='" + event_type + '\'' +
                ", itemsListDM=" + toStringItamList() +
                '}';
    }
}
