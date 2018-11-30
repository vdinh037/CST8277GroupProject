/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Tuna Entity class
 * Data transfer object with the same attributes
 * as the column names in the database.
 * The entity must be edited with the getters
 * and setters.
 * 
 * @author Vuong Dinh, Parthashokbh Chauhan, Khanh Vuviet, Aksharbhavin Chauhan, Shubham Sachdeva
 */
@Entity
@XmlRootElement
public class Tuna implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /* Variables for tuna object */
    private int recordNumber;
    private String omega;
    private String lambda;
    private String uuid;
    private String theta;
    private String delta;

    /* Constructor */
    public Tuna(){
        this.setUuid(UUID.randomUUID().toString());
    }
        /*
    * Retrieve the record value
    */
    public int getRecordNumber() {
        return recordNumber;
    }
   /*
    * Set the record value
    */
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    /*
    * Retrieve omegas value
    */
    public String getOmega() {
        return omega;
    }
   /*
    * Set the omega value
    */
    public void setOmega(String omega) {
        this.omega = omega;
    }
    /*
    * Retrieve lambda value
    */
    public String getLambda() {
        return lambda;
    }
   /*
    * Set the delta value
    */
    public void setLambda(String lambda) {
        this.lambda = lambda;
    }
    /*
    * Retrieve uuid value
    */
    public String getUuid() {
        return uuid;
    }
   /*
    * Set the uuid value
    */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
      /*
    * Retrieve theta value
    */  
    public String getTheta() {
        return theta;
    }
    /*
    * Set the theta value
    */
    public void setTheta(String theta) {
        this.theta = theta;
    }
    /*
    * Retrieve delta value
    */
   public String getDelta() {
        return delta;
    }
   /*
    * Set the delta value
    */
    public void setDelta(String delta) {
        this.delta = delta;
    }
    /*
    * Retrieve the id
    */
    public Long getId() {
        return id;
    }
   /*
    * Set the id value
    */
    public void setId(Long id) {
        this.id = id;
    }
    /*
    * Encrypt
    */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    /*
    * Check if 2 tuna objects are the same
    */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tuna)) {
            return false;
        }
        Tuna other = (Tuna) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    /*
    * Convert variable to string
    */
    @Override
    public String toString() {
	return String.format("%d, %d, %s, %s, %s", id, recordNumber, omega, lambda, uuid);
    }
    
}
