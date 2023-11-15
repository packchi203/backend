package com.example.forums_backend.utils;

import java.util.Random;

public class GeneratingPassword {

    private static String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
    private static String specialCharacters = "_@";
    private static String numbers = "1234567890";

    public static char[] generatePassword(int length){
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];
        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));
        for(int i = 4; i< length ; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password;
    }
}
