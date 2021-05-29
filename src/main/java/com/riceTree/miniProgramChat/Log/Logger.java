package com.riceTree.miniProgramChat.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Logger {
    private static LocalDateTime dateTime;
    private static DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static <T> void errorLog(Class<T> clazz, String message){
        dateTime=LocalDateTime.now();
        System.out.println(dateTime.format(dateTimeFormatter)+" error:"+clazz.toString()+" detail:"+message);
    }
    public static <T> void SystemLog(Class<T> clazz, String message){
        dateTime=LocalDateTime.now();
        System.out.println(dateTime.format(dateTimeFormatter)+" system:"+clazz.toString()+" detail:"+message);
    }
}
