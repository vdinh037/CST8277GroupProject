/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment4swingclient_corba;

import business.TunaFacadeRemote;
import entity.Tuna;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 * Swing client for the assignment 4
 * JEE Project
 * 
 * Permits clients to 
 *  view the data
 *  insert a tuna
 * 
 * @author Vuong Dinh, Parthashokbh Chauhan, Khanh Vuviet, Aksharbhavin Chauhan, Shubham Sachdeva
 */
public class Assignment4SwingClient extends JFrame{

       /* TunaFacadeRemote object instance */
    private final TunaFacadeRemote remoteTuna;

    /* variables for text fields */
    private JTextField recordNumTxtField = new JTextField();
    private JTextField omegaTxtField = new JTextField();
    private JTextField thetaTxtField = new JTextField();
    private JTextField deltaTxtField = new JTextField();
    private JTextField uuidTxtField = new JTextField();
    private JTextField idTxtField = new JTextField();
    private JTextArea TunaTxtArea = new JTextArea(10, 0); // 10 rows, 0 columns

    /*
    * Constructor
    */
    public Assignment4SwingClient() {
        buildGUI();
        remoteTuna = getRemoteSession();
    }

    /*
    * Get the reomte session by looking up in the tuna facade
    */
    private TunaFacadeRemote getRemoteSession() {
        TunaFacadeRemote session = null;
        
        // CORBA properties and values and lookup taken after earlier work provided by
        // Todd Kelley (2016) Personal Communication
        System.setProperty("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
        System.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
        try {
            JOptionPane.showMessageDialog(this, "Trying for a session...");
            InitialContext ic = new InitialContext();
            session = (TunaFacadeRemote) ic.lookup("java:global/Assignment4/Assignment4-ejb/TunaFacade");
            JOptionPane.showMessageDialog(this, "Got a session :) ");

        } catch (NamingException e) {
            JOptionPane.showMessageDialog(this, "Problem. \n Cause: \n" + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Problem. \n Cause: \n" + e.getMessage());
        }
        return session;
    }

    /*
    * The gui for adding tuna objects
    */
    private void buildGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Swing Client Demo - CORBA");
        
        GridLayout westGridLayout = new GridLayout(4, 1); // rows, columns
        westGridLayout.setHgap(10);
        westGridLayout.setVgap(10);
        JPanel dataEntryWestJPanel = new JPanel(westGridLayout); 
        dataEntryWestJPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        
        GridLayout dataEntryGridLayout = new GridLayout(4, 1); // rows, columns
        dataEntryGridLayout.setHgap(10);
        dataEntryGridLayout.setVgap(10);
        JPanel dataEntryCenterJPanel = new JPanel(dataEntryGridLayout);
        dataEntryCenterJPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));
        
        FlowLayout buttonLayout = new FlowLayout(FlowLayout.RIGHT);
        buttonLayout.setHgap(10);
        JPanel dataEntryButtonJPanel = new JPanel(buttonLayout);
        
        
        JPanel dataEntryJPanel = new JPanel(new BorderLayout());
        JPanel dataViewJPanel = new JPanel(new GridLayout(1, 1));
        dataViewJPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel recordNumLbl = new JLabel("Record Number");
        JLabel omegaLbl = new JLabel("Omega");
        JLabel thetaLbl = new JLabel("Theta");
        JLabel deltaLbl = new JLabel("Delat");
        JLabel uuidLbl = new JLabel("UUID");
        JLabel idLbl = new JLabel("ID");

        JButton addJButton = new JButton("Add Tuna");
        JButton viewAllJButton = new JButton("View All Tunas");
        JButton cleanJButton = new JButton("Clean text fields");
        
        recordNumLbl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));//top,left,bottom,right
        omegaLbl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        thetaLbl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        deltaLbl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        uuidLbl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        idLbl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        TunaTxtArea.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        addJButton.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        viewAllJButton.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        cleanJButton.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
             
        dataEntryWestJPanel.add(recordNumLbl);
        dataEntryWestJPanel.add(omegaLbl);
        dataEntryWestJPanel.add(thetaLbl);
        dataEntryWestJPanel.add(deltaLbl);
        dataEntryWestJPanel.add(uuidLbl);
        dataEntryWestJPanel.add(idLbl);

        dataEntryButtonJPanel.add(addJButton);
        dataEntryButtonJPanel.add(viewAllJButton);
        dataEntryButtonJPanel.add(cleanJButton);

        dataEntryCenterJPanel.add(recordNumTxtField);
        dataEntryCenterJPanel.add(omegaTxtField);
        dataEntryCenterJPanel.add(thetaTxtField);
        dataEntryCenterJPanel.add(deltaTxtField);
        dataEntryCenterJPanel.add(uuidTxtField);
        dataEntryCenterJPanel.add(idTxtField);
        dataEntryCenterJPanel.add(dataEntryButtonJPanel);

        dataEntryJPanel.add(dataEntryWestJPanel, BorderLayout.WEST);
        dataEntryJPanel.add(dataEntryCenterJPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(TunaTxtArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        dataViewJPanel.add(scrollPane);

        this.getContentPane().add(dataEntryJPanel, BorderLayout.NORTH);
        this.getContentPane().add(dataViewJPanel, BorderLayout.CENTER);

        addJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(remoteTuna != null){
                    Tuna Tuna = new Tuna();
                    Tuna.setRecordNumber(Integer.parseInt(recordNumTxtField.getText()));
                    Tuna.setOmega(omegaTxtField.getText());
                    Tuna.setLambda(thetaTxtField.getText());
                    Tuna.setOmega(deltaTxtField.getText());
                    Tuna.setLambda(uuidTxtField.getText());
                    Tuna.setOmega(idTxtField.getText());
                   
                    remoteTuna.create(Tuna);
                    }
                    else{
                        JOptionPane.showMessageDialog(
                                Assignment4SwingClient.this, "Problem. No remote object available");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            Assignment4SwingClient.this, "Problem Cause: \n" + ex.getMessage());
                }
            }
        });

        viewAllJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(remoteTuna != null){
                    List<Tuna> tunas = remoteTuna.findAll();
                    TunaTxtArea.setText("");
                    for (Tuna s : tunas) {
                        TunaTxtArea.append(s.toString() + "\n");
                    }
                    }
                    else{
                        JOptionPane.showMessageDialog(
                                Assignment4SwingClient.this, "Problem. No remote object available");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            Assignment4SwingClient.this, "Problem Cause: \n" + ex.getMessage());
                }
            }
        });
        
          /** click on clean button to bound to an event to clean the data hold in the text fields */
        cleanJButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            recordNumTxtField.setText("");
            omegaTxtField.setText("");
            thetaTxtField.setText("");
            deltaTxtField.setText("");
            uuidTxtField.setText("");
            idTxtField.setText("");
         }
    });

        //this.pack();
        this.setSize(800, 600);
        // null causes window to be centered on screen
        // see: stackoverflow.com (). How to set JFrame to appear centered, regardless of monitor resolution? Retrieved from
        // https://stackoverflow.com/questions/2442599/how-to-set-jframe-to-appear-centered-regardless-of-monitor-resolution
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(true);
    }

    public static void main(String[] args) {
        Assignment4SwingClient client = new Assignment4SwingClient();
    }
    
}
