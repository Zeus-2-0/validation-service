package com.brihaspathee.zeus.exception;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 04, October 2022
 * Time: 6:12 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.exception
 * To change this template use File | Settings | File and Code Template
 */
public class RuleSetImplNotFound extends RuntimeException{

    public RuleSetImplNotFound(String message){
        super(message);
    }

    public RuleSetImplNotFound(String message, Throwable cause){
        super(message);
    }
}
