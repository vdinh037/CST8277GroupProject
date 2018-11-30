/* File: Tuna.java
 * Author: Stanley Pieda
 * Date: March 2018
 */

package com.algonquincollege.assignment4androidclient_rest;

/**
 * Created by Vuong Dinh, Parthashokbh Chauhan, Khanh Vuviet, Aksharbhavin Chauhan, Shubham Sachdeva on 2018-11-20.
 * Tuna Entity
 *  Has the same attributes as the Tuna in the database
 */

public class Tuna {
    public final String id;
    public final String recordNumber;
    public final String omega;
    public final String lambda;
    public final String uuid;

    /**
     * Constructor
     *
     * @param id
     * @param recordNumber
     * @param omega
     * @param lambda
     * @param uuid
     */
    public Tuna(String id, String recordNumber, String omega, String lambda, String uuid){
        this.id = id;
        this.recordNumber = recordNumber;
        this.omega = omega;
        this.lambda = lambda;
        this.uuid = uuid;
    }
}
