package test;

import java.util.Arrays;
import java.util.Random;

public class LeatCode {

    public static void main(String args[]){
        String longest = lengthOfLongestSubstring("Vipinnndwrfiaaaaaasdgf");
        System.out.print("Longest = "+  longest.toCharArray() );
    }

    public static String lengthOfLongestSubstring(String s) {
        char[] mainString = s.toCharArray();
        int mainStringLength = mainString.length;
        for(int i=0; i<mainString.length-1; i++){
            int low = 0;
            int high = 1;
            if(mainString[low] != mainString[high]){
                low ++;
                high ++;
            }else{
                high ++;
            }
        }

        return "";
    }
}
