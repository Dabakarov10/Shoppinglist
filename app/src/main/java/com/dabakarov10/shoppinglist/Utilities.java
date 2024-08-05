package com.dabakarov10.shoppinglist;

import java.util.Random;

public class Utilities {


    // function to generate a random string of length n
    public  String getAlphaNumericString() {
       int n = 6;
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
 /* אין צורך בפעולה זו ---> למחוק אותה בהמשך
    public int[] randomArr(int x, int y) {
        Random random = new Random();
        int big, small;
        int[] arr;
        int num;
        big = y;
        small = x;
        if (x > y) {
            big = x;
            small = y;
        }
        arr = new int[(big - small) + 1];
        int j;
        for (int i = 0; i < arr.length; i++) {
            num = random.nextInt((big - small) + 1) + small;
            for (j = 0; j < i; j++)
                if (arr[j] == num) j = i + 1;
            if (j == i) arr[i] = num;
            else i--;
        }
        return arr;
    } */
}
