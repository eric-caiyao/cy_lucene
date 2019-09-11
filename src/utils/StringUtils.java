package utils;

public class StringUtils {

    static public int sharePrefixLength(String s1, String s2){
        int sharePrefixLength = 0;
        char[] s1Array = s1.toCharArray();
        char[] s2Array = s2.toCharArray();
        int compareLength = s1Array.length > s2Array.length ? s2Array.length : s1Array.length;
        for(int i = 0; i < compareLength; i ++){
            if(s1Array[i] != s2Array[i]){
                break;
            }
            sharePrefixLength ++;
        }
        return sharePrefixLength;
    }

    public static void main(String[] args){
        System.out.println(sharePrefixLength("aaabc","aaaaaaad"));
    }
}
