package com.example.share;
import org.apache.commons.lang3.RandomStringUtils;
public class TestRandomAlphanumeric {

    public static void main(String[] args) {
        String randomed = RandomStringUtils.randomAlphanumeric(6);
        System.out.println(randomed);
    }
}
