
import business.TunaFacadeRemote;
import entity.Tuna;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Console client for the assignment 4
 * JEE Project
 * 
 * Permits clients to 
 *  [get] the data
 *  [insert] records
 *  [quit] the application
 * 
 * @author Vuong Dinh, Parthashokbh Chauhan, Khanh Vuviet, Aksharbhavin Chauhan, Shubham Sachdeva
 */
public class Assignment4ConsoleClient {

    public static void main(String[] args) {
        TunaFacadeRemote session = null;
        
        // CORBA properties and values and lookup taken after earlier work provided by
        // Todd Kelley (2016) Personal Communication
        System.setProperty("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
        System.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

        try {
            System.out.println("about to try for a session...");
            InitialContext ic = new InitialContext();
            //Grab a session
            session = (TunaFacadeRemote) 
            ic.lookup("java:global/Assignment4/Assignment4-ejb/TunaFacade");
            System.out.println("Got a session :) ");
            
            System.out.println("Creating and inserting a Tuna record into database");
            //Line reader for client input
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line;
            
            //Loop while the client doesnt write quit
            do{
                System.out.println("Enter Command:");
                //Read the line
                line = br.readLine();
                switch(line){
                    case "insert" :
                        System.out.println("Inserting Tuna");
                        Tuna Tuna = new Tuna();
                        
                        //Loop while record number isnt a number
                        do {
                            System.out.print("Please enter a record number: ");
                            line = br.readLine();// read the user input
			} while (!line.matches("-?\\d+")); // while the input isn't only numeric
			Tuna.setRecordNumber(Integer.parseInt(line)); // set the tuna record number
                        System.out.print("Please enter omega: ");
			Tuna.setOmega(br.readLine()); // set the tuna omega
			
                        System.out.print("Please enter Delta: ");
			Tuna.setDelta(br.readLine()); // set the tuna Delta
                        
                        System.out.print("Please enter Theta: ");
			Tuna.setTheta(br.readLine()); // set the tuna Theta   
                        
                        System.out.print("Please enter UUID: ");
			Tuna.setUuid(br.readLine()); // set the tuna UUID
                        
                        System.out.print("Please enter ID: ");
			Tuna.setId(Long.valueOf(br.readLine())); // set the tuna ID
                        
                        session.create(Tuna);
                        
                        break;
                    case "get" :
                        System.out.println("Listing contents of database");
                        for(Tuna s: session.findAll()){
                            //For each tuna, print it
                            System.out.println(s.toString());
                        }  
                        break;
                }
                
            }while(!line.equals("quit"));
        } catch (NamingException e) {
            System.out.println(e.getMessage());
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
        } 
    }
}
