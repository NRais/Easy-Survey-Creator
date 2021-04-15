/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.apache.commons.lang3.SystemUtils;

/**
 *
 * Copyright 2018 Nathan Rais 
 * 
 *      This file is part of The Easy Survey Creator.
 *
 *   The Easy Survey Creator by Nathan Rais is free software 
 *   but is licensed under the terms of the Creative Commons 
 *   Attribution NoDerivatives license (CC BY-ND). Under the CC BY-ND license 
 *   you may redistribute this as long as you give attribution and do not 
 *   modify any part of this software in any way.
 *
 *   The Easy Survey Creator is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   CC BY-ND license for more details.
 *
 *   You should have received a copy of the CC BY-ND license along with 
 *   The Easy Survey Creator. If not, see <https://creativecommons.org/licenses/by-nd/4.0/>.
 *
 */
public class EasySurveyMenu extends javax.swing.JFrame {

    
    String passCheckFrame = "";
    String password = "";
    static int monthsToUpdate = 6;
    
    String pathway = "C:\\Ques";
    
    String defaultLines = pathway + "/Default.csv\r\n/resources/Wizard.jpg\r\nDefault\r\nDefault\r\n/Documents";
    String defaultCSVFileLines = "1,How are you doing today?,Very Good, Okay, Not that good, Terrible\r\n";
    boolean correctPassword = false;

    
    public static Color highlightColor = Color.gray;
    
    
    // -- //
    MainStartup MS = new MainStartup();
    public String version = MS.version;
    // -- //
    
    
    public boolean alreadyUpdateAsk = false;
    
    public void alreadyUpdateAsked() {
        alreadyUpdateAsk = true;
        // ****** System.out.printlnln("update asked changed to true it now is: " + alreadyUpdateAsk);
    }
            
    int passTrys = 0;
    /**
     * Creates new form EasySurveyMenu
     */
     
    public EasySurveyMenu() {
        
        initComponents();
        
        Container cMain = this.getContentPane();               
        cMain.setBackground(Color.white);
                
        this.setSize(720,510);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        getPassword.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if ("Enter password in this field".equals(getPassword.getText())) {
                    getPassword.selectAll();
                }
            }
        });

        enterPassword1.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if ("Enter password in this field".equals(enterPassword1.getText())) {
                    enterPassword1.selectAll();
                }
            }
        });
        
        updateFrame.getRootPane().setDefaultButton(ButtonWebsiteUpdate);
        getPasswordFrame.getRootPane().setDefaultButton(GPcreateNewSurveyButton);
        
        createPasswordFrame();
        
        passwordFrame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        
        getPasswordFrame.setResizable(false);
        getPasswordFrame.setLocationRelativeTo(null);
        getPasswordFrame.setSize(371,210);
        
        getPasswordFrame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        
        versionLabel.setText("" + (char)67 + (char)111 + (char)112 + (char)121 + (char)114 + (char)105 + (char)103 + (char)104 + (char)116 + (char)32 + (char)78 + (char)97 + (char)116 + (char)104 + (char)97 + (char)110 + (char)83 + (char)111 + (char)102 + (char)116 + (char)119 + (char)97 + (char)114 + (char)101 + (char)46 + (char)99 + (char)111 + (char)109 + (char)32 + (char)118 + (char)101 + (char)114 + (char)115 + (char)105 + (char)111 + (char)110 + " " + version);
                              
        buttonBackRemoval(LicenseLabel);
        buttonBackRemoval(GPHelpButton);
        buttonBackRemoval(PFgoButton);
        buttonBackRemoval(ButtonWebsiteUpdate);
        buttonBackRemoval(ButtonWebsite);
        buttonBackRemoval(ButtonConductSurvey);
        buttonBackRemoval(ButtonAnalyseData);
        buttonBackRemoval(ButtonHelp);
        buttonBackRemoval(ButtonCreateSurvey);
        buttonBackRemoval(GPcreateNewSurveyButton);
         

        prestart();
        
        // ****** System.out.printlnln("update asked? - " + alreadyUpdateAsk);
        
    }
    
    public void createPasswordFrame() {
        passwordFrame.setResizable(false);
        passwordFrame.setSize(440,193);
        jLabel2.setSize(passwordFrame.getWidth(),jLabel2.getHeight());
        PFgoButton.setSize(passwordFrame.getWidth(), PFgoButton.getHeight());

        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
        passwordFrame.setIconImages(icons);

        passwordFrame.setLocationRelativeTo(null);
    }
    
    public void buttonBackRemoval(JButton button) {
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);        
    }
        
    public void prestart() {
        //get system variables
        
        // if the system is windows we use the default C:\\Ques
        // otherwise we find the user's home directory and make a folder Ques there
        if (!SystemUtils.IS_OS_WINDOWS) {
            pathway = SystemUtils.USER_HOME + "/Ques";
            // ****** System.out.printlnln("Home " + pathway);
        }
        
        /// FOR LOADING THE PASSWORD AND VERIFYING QUESLOAD ///
        BufferedReader read = null;
        try {
            // ****** System.out.printlnln("Trying to find quesload");
            read = new BufferedReader(new FileReader(pathway + "/QuesLoad.txt"));
            
            //read the first three lines and skip
            read.readLine(); read.readLine(); read.readLine();
            
            //then store the fourth line which is the password line
            password = read.readLine();
            
            read.close();
        } catch (FileNotFoundException ex) {
            //if this happens there is no quesLoad file so make one
            //          *IF THERE IS NO QUESLOAD*
            // ****** System.out.printlnln("couldn't find quesload");
            
            PrintWriter creator = null;
            try {
                File f = new File(pathway);
                f.mkdirs();

                creator = new PrintWriter(pathway + "/QuesLoad.txt", "UTF-8");
            } catch (Exception e) {
                // ****** System.out.printlnln("can't create quesload file.. no premissions");
            }
            creator.close();
            
            try {
                BufferedWriter write = new BufferedWriter(new FileWriter(pathway + "/QuesLoad.txt"));
                write.write(defaultLines);
                write.close();
            } catch (IOException ex1) {
                Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
        } catch (IOException ex) {
            // ****** System.out.printlnln("no fourth line");
            //if this happens there is no forth line
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        /// FOR DEFAULT CSV FILE ///
        BufferedReader readDefaultCSV = null;
        try {
            // ****** System.out.printlnln("Trying to find quesload");
            readDefaultCSV = new BufferedReader(new FileReader(pathway + "/Default.csv"));
            
            //read the first line for posterity's sake :)
            //just kidding its read to make sure that this file isn't broken
            readDefaultCSV.readLine();
                        
            readDefaultCSV.close();
        } catch (FileNotFoundException ex) {
            //if this happens there is no file
            CreateDefaultCSV();
            
        } catch (IOException ex) {
            // ****** System.out.printlnln("No file Default.csv file obviously");
            //if this happens there is no line
            CreateDefaultCSV();
        }
        
    }
          
    public void CreateDefaultCSV() {
        //if this happens there is no default.csv file so make one
            //       *IF THERE IS NO DEFAULT CSV*
            // ****** System.out.printlnln("couldn't find default csv");
            
            PrintWriter creator = null;
            try {
                File f = new File(pathway + "/");
                f.mkdirs();

                creator = new PrintWriter(pathway + "/Default.csv", "UTF-8");
                
                creator.close();
            } catch (Exception e) {}
            
            
            try {
                BufferedWriter write = new BufferedWriter(new FileWriter(pathway + "/Default.csv"));
                write.write(defaultCSVFileLines);
                write.close();
            } catch (IOException ex1) {
                Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex1);
            }
    }
        
    // note that this takes Integers not ints because we want to deference them later
    public static void checkDate(Integer dateY, Integer dateM, SimpleDateFormat ourFormat) {
        //first get current date
        SimpleDateFormat sdf = ourFormat; // this is the format we are importing (for example 22017 2month2017year
                
        // ****** System.out.printlnln(new Date());
        
        // get the current date
        Date nowDate = new Date();
        LocalDate localDate = nowDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        // figure out what the old date was
        Date oldDate = null;
        try {
            oldDate = sdf.parse(dateM.toString() + dateY.toString()); // get the old date by parsing our number as a string
        } catch (ParseException ex) {
            // if it fails then i messed up something...
        }
        
        // but in order to read it we are going to write the date in a more logical way
        SimpleDateFormat finalDateFormater = new SimpleDateFormat("dd/MM/yyyy");
        
        // ****** System.out.printlnln("NOW: " + finalDateFormater.format(nowDate) + " + THEN: " + finalDateFormater.format(oldDate));
        
        //spilt the date into its components to be read
        

        // from the current local date we find out the month and year to calculate
        int dateCurrentM = localDate.getMonthValue(); //this should be the month so store it as such
        int dateCurrentY = localDate.getYear(); //this should be the year so store it as such
    
        // ****** System.out.printlnln("Old Month: " + dateM);
        // ****** System.out.printlnln("Old Year: " + dateY);
        
        // ?-- UPDATE TIME --? //
        // now we calculate if it is time for an update
        
        //if the new date is after the old one
        if (nowDate.after(oldDate)) {
            //then check if a year has changed
            if (dateCurrentY > dateY) {
                int numYears = dateCurrentY - dateY;

                // years*12 will give us how many months it has been in years
                // THEN subtract what month it was made on
                // THEN add what month it is this year
                // (and that tells us if it has been long enough)
                if (12*(numYears) - dateM + dateCurrentM >= monthsToUpdate) { 
                    
                    //if so then show update frame
                    updateDue();
                }
            }
            //if it hasn't been a year then check if its been X months
            else if (dateCurrentM >= dateM + monthsToUpdate) {
                
                //if so show update frame
                updateDue();
            }
        }
    }
    
    public static void updateDue() {
        Container cU = updateFrame.getContentPane();               
        cU.setBackground(Color.white);

        // ****** System.out.printlnln("Showing update frame");
        updateFrame.setLocationRelativeTo(ButtonCreateSurvey); // this is the centre
        updateFrame.setResizable(false);
        updateFrame.setSize(405,329);
        updateFrame.setAlwaysOnTop(true);
        updateFrame.toFront();
        updateFrame.repaint();
          
        /* TODO
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
        
        updateFrame.setIconImages(icons);*/
        
        updateFrame.setVisible(true);
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        passwordFrame = new javax.swing.JDialog();
        jLabel2 = new javax.swing.JLabel();
        PFgoButton = new javax.swing.JButton();
        enterPassword1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        getPasswordFrame = new javax.swing.JDialog();
        GPHelpButton = new javax.swing.JButton();
        GPlabel = new javax.swing.JLabel();
        getPassword = new javax.swing.JTextField();
        GPcreateNewSurveyButton = new javax.swing.JButton();
        updateFrame = new javax.swing.JFrame();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pictureLabel = new javax.swing.JLabel();
        ButtonWebsiteUpdate = new javax.swing.JButton();
        androidLink = new javax.swing.JLabel();
        LicenseLabel = new javax.swing.JButton();
        versionLabel = new javax.swing.JLabel();
        LabelMain = new javax.swing.JLabel();
        LabelTT = new javax.swing.JLabel();
        ButtonAnalyseData = new javax.swing.JButton();
        ButtonCreateSurvey = new javax.swing.JButton();
        ButtonConductSurvey = new javax.swing.JButton();
        ButtonHelp = new javax.swing.JButton();
        ButtonWebsite = new javax.swing.JButton();
        Background = new javax.swing.JLabel();

        passwordFrame.setTitle("Create a admin password");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Please create an admin password.");

        PFgoButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        PFgoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png"))); // NOI18N
        PFgoButton.setText("GO     ");
        PFgoButton.setToolTipText("");
        PFgoButton.setIconTextGap(-36);
        PFgoButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PFgoButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PFgoButtonMouseExited(evt);
            }
        });
        PFgoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PFgoButtonActionPerformed(evt);
            }
        });

        enterPassword1.setText("Enter password in this field");
        enterPassword1.setSelectionEnd(0);

        jLabel4.setText("It is important to create a password so users cannot modify your surveys.");

        javax.swing.GroupLayout passwordFrameLayout = new javax.swing.GroupLayout(passwordFrame.getContentPane());
        passwordFrame.getContentPane().setLayout(passwordFrameLayout);
        passwordFrameLayout.setHorizontalGroup(
            passwordFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(passwordFrameLayout.createSequentialGroup()
                .addContainerGap(91, Short.MAX_VALUE)
                .addComponent(enterPassword1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85))
            .addComponent(PFgoButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(passwordFrameLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        passwordFrameLayout.setVerticalGroup(
            passwordFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(passwordFrameLayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(8, 8, 8)
                .addComponent(enterPassword1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PFgoButton)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        getPasswordFrame.setBackground(new java.awt.Color(255, 255, 255));

        GPHelpButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        GPHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/helpIcon.jpg"))); // NOI18N
        GPHelpButton.setToolTipText("Help");
        GPHelpButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                GPHelpButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                GPHelpButtonMouseExited(evt);
            }
        });
        GPHelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GPHelpButtonActionPerformed(evt);
            }
        });

        GPlabel.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        GPlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        GPlabel.setText("Enter the password of the admin tools:");

        getPassword.setText("Enter password in this field");
        getPassword.setSelectionEnd(0);

        GPcreateNewSurveyButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        GPcreateNewSurveyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png"))); // NOI18N
        GPcreateNewSurveyButton.setText("Continue");
        GPcreateNewSurveyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                GPcreateNewSurveyButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                GPcreateNewSurveyButtonMouseExited(evt);
            }
        });
        GPcreateNewSurveyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GPcreateNewSurveyButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout getPasswordFrameLayout = new javax.swing.GroupLayout(getPasswordFrame.getContentPane());
        getPasswordFrame.getContentPane().setLayout(getPasswordFrameLayout);
        getPasswordFrameLayout.setHorizontalGroup(
            getPasswordFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(getPasswordFrameLayout.createSequentialGroup()
                .addGroup(getPasswordFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(getPasswordFrameLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(getPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(getPasswordFrameLayout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(GPcreateNewSurveyButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, getPasswordFrameLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(getPasswordFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, getPasswordFrameLayout.createSequentialGroup()
                        .addComponent(GPlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, getPasswordFrameLayout.createSequentialGroup()
                        .addComponent(GPHelpButton)
                        .addGap(64, 64, 64))))
        );
        getPasswordFrameLayout.setVerticalGroup(
            getPasswordFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(getPasswordFrameLayout.createSequentialGroup()
                .addComponent(GPlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(getPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(GPcreateNewSurveyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(GPHelpButton)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("NEW UPDATE AVAILABLE");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel3.setText("Your version of the Easy Survey Creator is more then 6 months old!");

        pictureLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/MenPlanetMapGraphic.jpg"))); // NOI18N

        ButtonWebsiteUpdate.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        ButtonWebsiteUpdate.setText("Click to download the newest version");
        ButtonWebsiteUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonWebsiteUpdateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonWebsiteUpdateMouseExited(evt);
            }
        });
        ButtonWebsiteUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonWebsiteUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout updateFrameLayout = new javax.swing.GroupLayout(updateFrame.getContentPane());
        updateFrame.getContentPane().setLayout(updateFrameLayout);
        updateFrameLayout.setHorizontalGroup(
            updateFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateFrameLayout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addGroup(updateFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateFrameLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(91, 91, 91))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateFrameLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(41, 41, 41))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateFrameLayout.createSequentialGroup()
                        .addComponent(ButtonWebsiteUpdate)
                        .addGap(91, 91, 91))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateFrameLayout.createSequentialGroup()
                        .addComponent(pictureLabel)
                        .addGap(66, 66, 66))))
        );
        updateFrameLayout.setVerticalGroup(
            updateFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateFrameLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(38, 38, 38)
                .addComponent(ButtonWebsiteUpdate)
                .addGap(18, 18, 18)
                .addComponent(pictureLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        getContentPane().setLayout(null);

        androidLink.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/Android.png"))); // NOI18N
        androidLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                androidLinkMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                androidLinkMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                androidLinkMouseExited(evt);
            }
        });
        getContentPane().add(androidLink);
        androidLink.setBounds(600, 320, 100, 110);

        LicenseLabel.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        LicenseLabel.setText("" + (char)84 + (char)104 + (char)105 + (char)115 + (char)32 + (char)115 + (char)111 + (char)102 + (char)116 + (char)119 + (char)97 + (char)114 + (char)101 + (char)32 + (char)97 + (char)110 + (char)100 + (char)32 + (char)97 + (char)108 + (char)108 + (char)32 + (char)111 + (char)102 + (char)32 + (char)105 + (char)116 + (char)115 + (char)32 + (char)99 + (char)111 + (char)109 + (char)112 + (char)111 + (char)110 + (char)101 + (char)110 + (char)116 + (char)115 + (char)32 + (char)97 + (char)114 + (char)101 + (char)32 + (char)99 + (char)111 + (char)112 + (char)121 + (char)114 + (char)105 + (char)103 + (char)104 + (char)116 + (char)101 + (char)100 + (char)32 + (char)117 + (char)110 + (char)100 + (char)101 + (char)114 + (char)32 + (char)116 + (char)104 + (char)101 + (char)32 + (char)67 + (char)114 + (char)101 + (char)97 + (char)116 + (char)105 + (char)118 + (char)101 + (char)32 + (char)67 + (char)111 + (char)109 + (char)109 + (char)111 + (char)110 + (char)115 + (char)32 + (char)65 + (char)116 + (char)116 + (char)114 + (char)105 + (char)98 + (char)117 + (char)116 + (char)105 + (char)111 + (char)110 + (char)45 + (char)78 + (char)111 + (char)68 + (char)101 + (char)114 + (char)105 + (char)118 + (char)97 + (char)116 + (char)105 + (char)118 + (char)101 + (char)115 + (char)32 + (char)52 + (char)46 + (char)48 + (char)32 + (char)76 + (char)105 + (char)99 + (char)101 + (char)110 + (char)115 + (char)101);
        LicenseLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LicenseLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                LicenseLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                LicenseLabelMouseExited(evt);
            }
        });
        getContentPane().add(LicenseLabel);
        LicenseLabel.setBounds(20, 440, 660, 21);

        versionLabel.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        versionLabel.setText("Copyright NathanSoftware.com version 2.2");
        getContentPane().add(versionLabel);
        versionLabel.setBounds(463, 60, 240, 14);

        LabelMain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/MainLabelSml.png"))); // NOI18N
        getContentPane().add(LabelMain);
        LabelMain.setBounds(-3, 10, 720, 50);

        LabelTT.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        LabelTT.setText(" ");
        getContentPane().add(LabelTT);
        LabelTT.setBounds(390, 190, 109, 19);

        ButtonAnalyseData.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ButtonAnalyseData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png"))); // NOI18N
        ButtonAnalyseData.setText("Analyse Data");
        ButtonAnalyseData.setToolTipText("View the results of your surveys");
        ButtonAnalyseData.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ButtonAnalyseData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonAnalyseDataMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonAnalyseDataMouseExited(evt);
            }
        });
        ButtonAnalyseData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonAnalyseDataActionPerformed(evt);
            }
        });
        getContentPane().add(ButtonAnalyseData);
        ButtonAnalyseData.setBounds(470, 100, 190, 50);

        ButtonCreateSurvey.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ButtonCreateSurvey.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-purple.png"))); // NOI18N
        ButtonCreateSurvey.setText("Create Survey");
        ButtonCreateSurvey.setToolTipText("Manage your surveys");
        ButtonCreateSurvey.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ButtonCreateSurvey.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonCreateSurveyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonCreateSurveyMouseExited(evt);
            }
        });
        ButtonCreateSurvey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCreateSurveyActionPerformed(evt);
            }
        });
        getContentPane().add(ButtonCreateSurvey);
        ButtonCreateSurvey.setBounds(80, 100, 200, 50);

        ButtonConductSurvey.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ButtonConductSurvey.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-green.png"))); // NOI18N
        ButtonConductSurvey.setText("Conduct Survey");
        ButtonConductSurvey.setToolTipText("Give the current assigned survey");
        ButtonConductSurvey.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ButtonConductSurvey.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonConductSurveyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonConductSurveyMouseExited(evt);
            }
        });
        ButtonConductSurvey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonConductSurveyActionPerformed(evt);
            }
        });
        getContentPane().add(ButtonConductSurvey);
        ButtonConductSurvey.setBounds(270, 100, 210, 50);

        ButtonHelp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ButtonHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-red.png"))); // NOI18N
        ButtonHelp.setText("" + (char)32 + (char)72 + (char)101 + (char)108 + (char)112);
        ButtonHelp.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ButtonHelp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonHelpMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonHelpMouseExited(evt);
            }
        });
        ButtonHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonHelpActionPerformed(evt);
            }
        });
        getContentPane().add(ButtonHelp);
        ButtonHelp.setBounds(80, 200, 170, 50);

        ButtonWebsite.setFont(new java.awt.Font("Arial", 2, 11)); // NOI18N
        ButtonWebsite.setText("" + (char)86 + (char)105 + (char)115 + (char)105 + (char)116 + (char)32 + (char)78 + (char)97 + (char)116 + (char)104 + (char)97 + (char)110 + (char)115 + (char)111 + (char)102 + (char)116 + (char)119 + (char)97 + (char)114 + (char)101 + (char)46 + (char)99 + (char)111 + (char)109);
        ButtonWebsite.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonWebsiteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonWebsiteMouseExited(evt);
            }
        });
        ButtonWebsite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonWebsiteActionPerformed(evt);
            }
        });
        getContentPane().add(ButtonWebsite);
        ButtonWebsite.setBounds(470, 220, 170, 23);

        Background.setBackground(new java.awt.Color(255, 255, 255));
        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/Menu.jpg"))); // NOI18N
        getContentPane().add(Background);
        Background.setBounds(-10, 0, 740, 450);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonCreateSurveyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonCreateSurveyMouseEntered
        ButtonCreateSurvey.setForeground(highlightColor);
        ButtonCreateSurvey.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-purpleSelected.png")));
    }//GEN-LAST:event_ButtonCreateSurveyMouseEntered

    private void ButtonCreateSurveyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonCreateSurveyMouseExited
        ButtonCreateSurvey.setForeground(null);
        ButtonCreateSurvey.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-purple.png")));
    }//GEN-LAST:event_ButtonCreateSurveyMouseExited

    private void ButtonAnalyseDataMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonAnalyseDataMouseEntered
        ButtonAnalyseData.setForeground(highlightColor);
        ButtonAnalyseData.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blueSelected.png")));
    }//GEN-LAST:event_ButtonAnalyseDataMouseEntered

    private void ButtonAnalyseDataMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonAnalyseDataMouseExited
        ButtonAnalyseData.setForeground(null);
        ButtonAnalyseData.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png")));
    }//GEN-LAST:event_ButtonAnalyseDataMouseExited

    private void ButtonHelpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonHelpMouseEntered
        ButtonHelp.setForeground(highlightColor);
        ButtonHelp.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-redSelected.png")));
    }//GEN-LAST:event_ButtonHelpMouseEntered

    private void ButtonHelpMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonHelpMouseExited
        ButtonHelp.setForeground(null);
        ButtonHelp.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-red.png")));
    }//GEN-LAST:event_ButtonHelpMouseExited

    private void ButtonHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonHelpActionPerformed
        URI domain = null;
        try {
            domain = new URI("" + (char)104 + (char)116 + (char)116 + (char)112 + (char)58 + (char)47 + (char)47 + (char)119 + (char)119 + (char)119 + (char)46 + (char)110 + (char)97 + (char)116 + (char)104 + (char)97 + (char)110 + (char)115 + (char)111 + (char)102 + (char)116 + (char)119 + (char)97 + (char)114 + (char)101 + (char)46 + (char)99 + (char)111 + (char)109 + (char)47 + (char)119 + (char)111 + (char)114 + (char)100 + (char)112 + (char)114 + (char)101 + (char)115 + (char)115 + (char)47 + (char)101 + (char)97 + (char)115 + (char)121 + (char)45 + (char)115 + (char)117 + (char)114 + (char)118 + (char)101 + (char)121 + (char)45 + (char)99 + (char)114 + (char)101 + (char)97 + (char)116 + (char)111 + (char)114 + (char)45 + (char)109 + (char)97 + (char)110 + (char)117 + (char)97 + (char)108 + (char)47); //launches the default help page
        } catch (URISyntaxException ex) {
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);
    }//GEN-LAST:event_ButtonHelpActionPerformed

    private void ButtonConductSurveyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonConductSurveyMouseEntered
        ButtonConductSurvey.setForeground(highlightColor);
        ButtonConductSurvey.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-greenSelected.png")));
    }//GEN-LAST:event_ButtonConductSurveyMouseEntered

    private void ButtonConductSurveyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonConductSurveyMouseExited
        ButtonConductSurvey.setForeground(null);
        ButtonConductSurvey.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-green.png")));
    }//GEN-LAST:event_ButtonConductSurveyMouseExited

    private void ButtonWebsiteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonWebsiteMouseEntered
        ButtonWebsite.setForeground(highlightColor);
    }//GEN-LAST:event_ButtonWebsiteMouseEntered

    private void ButtonWebsiteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonWebsiteMouseExited
        ButtonWebsite.setForeground(null);
    }//GEN-LAST:event_ButtonWebsiteMouseExited

    private void ButtonWebsiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonWebsiteActionPerformed
        URI domain = null;
        try {
            domain = new URI("" + (char)104 + (char)116 + (char)116 + (char)112 + (char)58 + (char)47 + (char)47 + (char)119 + (char)119 + (char)119 + (char)46 + (char)78 + (char)97 + (char)116 + (char)104 + (char)97 + (char)110 + (char)115 + (char)111 + (char)102 + (char)116 + (char)119 + (char)97 + (char)114 + (char)101 + (char)46 + (char)99 + (char)111 + (char)109); //launches the website
        } catch (URISyntaxException ex) {
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);
    }//GEN-LAST:event_ButtonWebsiteActionPerformed

    private void ButtonCreateSurveyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCreateSurveyActionPerformed
        
        
        passCheckFrame = "CF";
                
        adminPassCheck();        
    }//GEN-LAST:event_ButtonCreateSurveyActionPerformed
    
    private void ButtonAnalyseDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonAnalyseDataActionPerformed
        
        passCheckFrame = "AF";
        
        adminPassCheck();
    }//GEN-LAST:event_ButtonAnalyseDataActionPerformed

    private void ButtonConductSurveyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonConductSurveyActionPerformed
        QuestionFrame QF = new QuestionFrame();
        
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
        QF.setIconImages(icons);
        
        QF.setVisible(true);
        
        this.dispose(); //hide the original frame
    }//GEN-LAST:event_ButtonConductSurveyActionPerformed
                                       
                                    
                                                                             

    private void PFgoButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PFgoButtonMouseExited
        PFgoButton.setForeground(null);
        PFgoButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png")));
    }//GEN-LAST:event_PFgoButtonMouseExited

    private void PFgoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PFgoButtonActionPerformed
        if ("Enter password in this field".equals(enterPassword1.getText()) | "".equals(enterPassword1.getText())) { //if it still says the enter password text or says nothing text then dont continue
            enterPassword1.setText("");
        } else {
            password = enterPassword1.getText(); //first store password

            setPassword(true); //then save password

            correctPassword = true; //then record that you know the password

            //then close the window
            passwordFrame.dispose();
            
            
            // then launch the frame you wanted it to
            startChoosenFrame();
        
            this.dispose(); //hide the original frame
        }
    }//GEN-LAST:event_PFgoButtonActionPerformed

    private void PFgoButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PFgoButtonMouseEntered
        PFgoButton.setForeground(highlightColor);
        PFgoButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blueSelected.png")));
    }//GEN-LAST:event_PFgoButtonMouseEntered

    private void GPHelpButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GPHelpButtonMouseEntered
        GPHelpButton.setIcon(new ImageIcon(getClass().getResource("/resources/helpIconSelected.jpg")));
    }//GEN-LAST:event_GPHelpButtonMouseEntered

    private void GPHelpButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GPHelpButtonMouseExited
        GPHelpButton.setIcon(new ImageIcon(getClass().getResource("/resources/helpIcon.jpg")));
    }//GEN-LAST:event_GPHelpButtonMouseExited

    private void GPHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GPHelpButtonActionPerformed
        URI domain = null;
        try {
            domain = new URI("" + (char)104 + (char)116 + (char)116 + (char)112 + (char)58 + (char)47 + (char)47 + (char)119 + (char)119 + (char)119 + (char)46 + (char)110 + (char)97 + (char)116 + (char)104 + (char)97 + (char)110 + (char)115 + (char)111 + (char)102 + (char)116 + (char)119 + (char)97 + (char)114 + (char)101 + (char)46 + (char)99 + (char)111 + (char)109 + (char)47 + (char)119 + (char)111 + (char)114 + (char)100 + (char)112 + (char)114 + (char)101 + (char)115 + (char)115 + (char)47 + (char)101 + (char)97 + (char)115 + (char)121 + (char)45 + (char)115 + (char)117 + (char)114 + (char)118 + (char)101 + (char)121 + (char)45 + (char)99 + (char)114 + (char)101 + (char)97 + (char)116 + (char)111 + (char)114 + (char)45 + (char)109 + (char)97 + (char)110 + (char)117 + (char)97 + (char)108 + (char)47); //launches the password help window
        } catch (URISyntaxException ex) {
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);
    }//GEN-LAST:event_GPHelpButtonActionPerformed

    private void GPcreateNewSurveyButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GPcreateNewSurveyButtonMouseEntered
        GPcreateNewSurveyButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blueSelected.png")));
    }//GEN-LAST:event_GPcreateNewSurveyButtonMouseEntered

    private void GPcreateNewSurveyButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GPcreateNewSurveyButtonMouseExited
        GPcreateNewSurveyButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png")));
    }//GEN-LAST:event_GPcreateNewSurveyButtonMouseExited

    private void GPcreateNewSurveyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GPcreateNewSurveyButtonActionPerformed
        if (getPassword.getText().equals(password)) { //if you've typed the right password
            correctPassword = true;
            getPasswordFrame.dispose();

            //then run the password check
            //and since the passwords already been verified  then it should launch
            adminPassCheck();
        }
        else { //if you got it wrong then tell you you've got it wrong
            GPlabel.setText("Incorrect Password, try again.");
            passTrys++;

            if (passTrys > 5) {
                GPlabel.setText("Incorrect Password, Press the help button to reset");
            }
        }
    }//GEN-LAST:event_GPcreateNewSurveyButtonActionPerformed

    private void ButtonWebsiteUpdateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonWebsiteUpdateMouseEntered
        ButtonWebsiteUpdate.setForeground(highlightColor);
    }//GEN-LAST:event_ButtonWebsiteUpdateMouseEntered

    private void ButtonWebsiteUpdateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonWebsiteUpdateMouseExited
        ButtonWebsiteUpdate.setForeground(null);
    }//GEN-LAST:event_ButtonWebsiteUpdateMouseExited

    private void ButtonWebsiteUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonWebsiteUpdateActionPerformed
        URI domain = null;
        try {
            domain = new URI("" + (char)104 + (char)116 + (char)116 + (char)112 + (char)58 + (char)47 + (char)47 + (char)119 + (char)119 + (char)119 + (char)46 + (char)78 + (char)97 + (char)116 + (char)104 + (char)97 + (char)110 + (char)115 + (char)111 + (char)102 + (char)116 + (char)119 + (char)97 + (char)114 + (char)101 + (char)46 + (char)99 + (char)111 + (char)109 + (char)47 + (char)101 + (char)97 + (char)115 + (char)121 + (char)45 + (char)115 + (char)117 + (char)114 + (char)118 + (char)101 + (char)121 + (char)45 + (char)99 + (char)114 + (char)101 + (char)97 + (char)116 + (char)111 + (char)114 + (char)45 + (char)50 + (char)47); //launches the website
        } catch (URISyntaxException ex) {
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);
        
        updateFrame.dispose();
    }//GEN-LAST:event_ButtonWebsiteUpdateActionPerformed

    private void LicenseLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LicenseLabelMouseClicked
        URI domain = null;
        try {
            domain = new URI("https://creativecommons.org/licenses/by-nd/4.0/"); //launches the password help window
        } catch (URISyntaxException ex) {
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);         
    }//GEN-LAST:event_LicenseLabelMouseClicked

    private void LicenseLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LicenseLabelMouseEntered
        LicenseLabel.setForeground(highlightColor);
    }//GEN-LAST:event_LicenseLabelMouseEntered

    private void LicenseLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LicenseLabelMouseExited
        LicenseLabel.setForeground(null);
    }//GEN-LAST:event_LicenseLabelMouseExited

    private void androidLinkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_androidLinkMouseClicked
        URI domain = null;
        try {
            domain = new URI("http://nathansoftware.com/wordpress/easy-survey-creator-downloads/"); //launches the password help window
        } catch (URISyntaxException ex) {
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);    
    }//GEN-LAST:event_androidLinkMouseClicked

    private void androidLinkMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_androidLinkMouseEntered
        androidLink.setIcon(new ImageIcon(getClass().getResource("/resources/menu/AndroidSelected.png")));
    }//GEN-LAST:event_androidLinkMouseEntered

    private void androidLinkMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_androidLinkMouseExited
        androidLink.setIcon(new ImageIcon(getClass().getResource("/resources/menu/Android.png")));
    }//GEN-LAST:event_androidLinkMouseExited
    
    public void startChoosenFrame() {
        if ("CF".equals(passCheckFrame)) {
            CreationFrame CF = new CreationFrame();


            List<Image> icons = new ArrayList<>();
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
            CF.setIconImages(icons);
            
            CF.setVisible(true);
            CF.initialize();
            
        } else if ("AF".equals(passCheckFrame)) {
        
            AnswerFrame AF = new AnswerFrame();

            List<Image> icons = new ArrayList<>();
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
            AF.setIconImages(icons);
            
            AF.setVisible(true);
        }
    }
    
    
    public void adminPassCheck() {
        BufferedReader read = null;
        try {
            // ****** System.out.printlnln("Trying to find quesload");
            read = new BufferedReader(new FileReader(pathway + "/QuesLoad.txt"));
            
            //read the first three lines and skip
            read.readLine(); read.readLine(); read.readLine();
            
            //then store the fourth line which is the password line
            password = read.readLine();
            
            read.close();
        } catch (FileNotFoundException ex) {
            //if this happens there is no quesLoad file so make one
            //          *IF THERE IS NO QUESLOAD*
            // ****** System.out.printlnln("couldn't find quesload");
            
            PrintWriter creator = null;
            try {
                File f = new File(pathway + "/");
                f.mkdirs();

                creator = new PrintWriter(pathway + "/QuesLoad.txt", "UTF-8");
            } catch (Exception e) {}
            creator.close();
            
            try {
                BufferedWriter write = new BufferedWriter(new FileWriter(pathway + "/QuesLoad.txt"));
                write.write(defaultLines);
                write.close();
            } catch (IOException ex1) {
                Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
        } catch (IOException ex) {
            // ****** System.out.printlnln("no fourth line");
            //if this happens there is no forth line
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if ("none".equals(password)) { //then the user has choosen to have none and so do nothing
            // ****** System.out.printlnln("password = none");
            
            startChoosenFrame();
            this.dispose(); //hide the original frame
        }
        else if ("Default".equals(password)) { //then this is the first time they've clicked this and so let them choose a password
            Container cf = passwordFrame.getContentPane();               
            cf.setBackground(Color.white);
            
            // ****** System.out.printlnln("password = default (i.e. not set yet)");
            createPasswordFrame();
                        
                        
            enterPassword1.setText("Enter password in this field");
            
            enterPassword1.selectAll();
            
            passwordFrame.setVisible(true);
        }
        else if ("".equals(password)) { //then it must be broken so ask for new password
            Container cd = passwordFrame.getContentPane();               
            cd.setBackground(Color.white);   
            
            // ****** System.out.printlnln("password = blank (so is broken)");
            createPasswordFrame();
                                                    
            
            enterPassword1.setText("Enter password in this field");
            
            enterPassword1.selectAll();
      
            passwordFrame.setVisible(true);
        }
        else if (password == null) { //then it must be broken so ask for new password
            Container cp = passwordFrame.getContentPane();               
            cp.setBackground(Color.white);  
            
            // ****** System.out.printlnln("password = null (so is broken)");
            createPasswordFrame();
                                          
            
            enterPassword1.setText("Enter password in this field");
            
            enterPassword1.selectAll();
            
            passwordFrame.setVisible(true);

        }
        else { //then there is a password and they must enter it
            // ****** System.out.printlnln("password exists they must type it in");
            if (correctPassword == true) {
                
                startChoosenFrame();
        
                this.dispose(); //hide the original frame
                
            } else {
                Container cr = getPasswordFrame.getContentPane();               
                cr.setBackground(Color.white);
                
                getPasswordFrame.setResizable(false);
                getPasswordFrame.setSize(365,210);
                
                
                List<Image> icons = new ArrayList<>();
                icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
                icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
                icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
                getPasswordFrame.setIconImages(icons);
                
                getPasswordFrame.setLocationRelativeTo(null);
                
                getPassword.setText("Enter password in this field");
                                               
                
                getPassword.selectAll();
                
                getPasswordFrame.setVisible(true);
            }
        }
    }
    
    public void setPassword(boolean havePass) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader read = new BufferedReader(new FileReader(pathway + "/QuesLoad.txt"));
            
            for (int i = 0; i < 3; i++) { //read the first 3 lines in the quesload file
                String line = read.readLine();
                sb.append(line + "\r\n");
            }
            //then add the password line to the string builder
            if (havePass == true) {
                sb.append(password + "\r\n");
            } else { //otherwise set it to an option so it doesn't bother you!
                sb.append("none" + "\r\n");
            }
            
            // then stick the location line at the end
            String line = read.readLine();
            sb.append(line);
        } catch (Exception ex) {
        }
        
        String lines = sb.toString();
        
        try {
            BufferedWriter write = new BufferedWriter(new FileWriter(pathway + "/QuesLoad.txt"));
            
            write.write(lines);
            write.close();
        } catch (IOException ex) {
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }    
    
    
    public static void openWebpage(URI uri) {
       Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
       if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
           try {
               desktop.browse(uri);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    }

        
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EasySurveyMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EasySurveyMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EasySurveyMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EasySurveyMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EasySurveyMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Background;
    private javax.swing.JButton ButtonAnalyseData;
    private javax.swing.JButton ButtonConductSurvey;
    private static javax.swing.JButton ButtonCreateSurvey;
    private javax.swing.JButton ButtonHelp;
    private javax.swing.JButton ButtonWebsite;
    private javax.swing.JButton ButtonWebsiteUpdate;
    private javax.swing.JButton GPHelpButton;
    private javax.swing.JButton GPcreateNewSurveyButton;
    private javax.swing.JLabel GPlabel;
    private javax.swing.JLabel LabelMain;
    private javax.swing.JLabel LabelTT;
    private javax.swing.JButton LicenseLabel;
    private javax.swing.JButton PFgoButton;
    private javax.swing.JLabel androidLink;
    private javax.swing.JTextField enterPassword1;
    private javax.swing.JTextField getPassword;
    private javax.swing.JDialog getPasswordFrame;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JDialog passwordFrame;
    private javax.swing.JLabel pictureLabel;
    private static javax.swing.JFrame updateFrame;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables
}
