package com.ttpw.shorturl.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author willlee
 * @Date 2022/4/23 21:50
 **/
public class ShortCodeUtil {

    private final static String str="AB6h5CnDpEqF4sGfkHv3wIzJcK2yLmM7ajtx1NeOPdQgRuST8mUVWrX9YbZ";

    private static String makeCode(){

        char[] data=new char[6];
        for (int i = 0; i < 6; i++) {

            char c = str.charAt(ThreadLocalRandom.current().nextInt(str.length()));
            data[i]=c;
        }

        String value = String.copyValueOf(data);
        data=null;
        return value;
    }

    public static Set<String> getCodes() {
        Set<String> codeSet = new HashSet<>();

        for (int i = 0; i < 100_0000; i++) {
            String code = makeCode();
            boolean add = codeSet.add(code);
            if(!add  ){
                char[] chars = code.toCharArray();
                char aChar = chars[5];
                int k = aChar;
                if(k==57  ){
                    k=48;
                }else if(k==90){
                    k=65;
                }else if(k==122){
                    k=97;
                }
                int i1 = k + 1;
                chars[5]=(char) i1;
                codeSet.add(String.copyValueOf(chars));
            }

        }

        return codeSet;

    }



}
