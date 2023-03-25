package com.lin.bili.common.utils;

public class ParseBilibiliUtils {
    public static String parseActor(String s) {
        for (int i=0; i<s.length(); i++) {
            if (s.charAt(i)==':' || s.charAt(i)=='：') {
                return s.substring(i+1);
            }
        }
        return s;
    }

    public static String parseStaff(String staff) {
        int l = 0;
        int r = 0;
        for (int i=0; i<staff.length(); i++) {
            if (staff.charAt(i) == '：' || staff.charAt(i)==':') {
                l = i + 1;
            }
            if (l > 0 && (staff.charAt(i) == '（' || staff.charAt(i) == '\n')) {
                r = i;
                break;
            }
        }
        return r>l?staff.substring(l, r):"";
    }

    public static String parseMediaScript(String targetScript) {
        int l = 0;
        int r = 0;
        for (int i=0; i<targetScript.length(); i++) {
            if (l==0 && targetScript.charAt(i)=='{') {
                l = i;
                continue;
            }
            if (targetScript.charAt(i)==';' && targetScript.charAt(i-1)=='}') {
                r = i;
                break;
            }
        }
        return targetScript.substring(l, r);
    }
}
