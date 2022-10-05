package com.brihaspathee.zeus.exception;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 04, October 2022
 * Time: 6:24 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.exception
 * To change this template use File | Settings | File and Code Template
 */
public class RuleImplNotFound extends RuntimeException{

    public RuleImplNotFound(String message){
        super(message);
    }

    public RuleImplNotFound(String message, Throwable cause){
        super(message, cause);
    }
}
