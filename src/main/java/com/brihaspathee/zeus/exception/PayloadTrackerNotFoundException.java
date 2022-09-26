package com.brihaspathee.zeus.exception;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 26, September 2022
 * Time: 6:15 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.exception
 * To change this template use File | Settings | File and Code Template
 */
public class PayloadTrackerNotFoundException extends RuntimeException{

    public PayloadTrackerNotFoundException(String message){
        super(message);
    }

    public PayloadTrackerNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
