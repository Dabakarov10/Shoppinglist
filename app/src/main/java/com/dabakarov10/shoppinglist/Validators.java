package com.dabakarov10.shoppinglist;

import android.util.Patterns;

import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Validators {

    public boolean check_number_presence(String str) {
        if (str.length() == 0) return false;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) > '9' || str.charAt(i) < '0')
                return false;
        }
        return true;
    }

    public String check_password(String password) {
        if (password.equals("")) {
            return ("יש להכניס סיסמא ");
        } else if (password.length() < 6) {
            return ("הסיסמא צריכה להיות לפחות 6 תווים");
        }
        return "";
    }

    public String check_username(String username) {
        if (username.equals("")) {
            return ("יש להכניס שם משתמש");
        }
        return "";
    }

    public String check_Email(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return "יש להכניס אימייל תקין";
        else return "";
    }

    public String check_phone(String phone) {
        if (!Patterns.PHONE.matcher(phone).matches() || phone.length() < 10)
            return "יש להכניס מספר טלפון תקין";
        else return "";
    }

    public String check_Date(String strDate) {
        if (strDate.trim().equals("")) return "יש להכניס תאריך";
        else {
            SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
            sdfrmt.setLenient(false);
            try {
                Date javaDate = sdfrmt.parse(strDate);
                return "";
            } catch (ParseException e) {
                return "יש להכניס תאריך תקין";
            }
        }
    }


    public String cheek_EmptyString(String str) {
        if (!str.equals(""))
            return "";
        else
            return "יש להכניס טקסט";

    }

    public String check_allValidators(String password, String username, String email, String phone) {
        String outPut = "";

        if (!check_password(password).equals(""))
            outPut += check_password(password) + "\n";
        if (!check_username(username).equals(""))
            outPut += check_username(username) + "\n";
        if (!check_Email(email).equals(""))
            outPut += check_Email(email) + "\n";
        if (!check_phone(phone).equals(""))
            outPut += check_phone(phone) + "\n";

        return outPut;
    }

    public String check_allValidators(String password, String email) {
        String outPut = "";

        if (!check_password(password).equals(""))
            outPut += check_password(password) + "\n";

        if (!check_Email(email).equals(""))
            outPut += check_Email(email) + "\n";

        return outPut;
    }

    public void sode1() {

    }

    public String check_allValidators(String EventDate, String EventName, String eventDescription) {
        String outPut = "";

        if (!check_Date(EventDate).equals(""))
            outPut += check_Date(EventDate) + "\n";

        if (!cheek_EmptyString(EventName).equals(""))
            outPut += cheek_EmptyString(EventName) + "\n";

        if (!cheek_EmptyString(eventDescription).equals(""))
            outPut += cheek_EmptyString(eventDescription) + "\n";

        return outPut;
    }

}
