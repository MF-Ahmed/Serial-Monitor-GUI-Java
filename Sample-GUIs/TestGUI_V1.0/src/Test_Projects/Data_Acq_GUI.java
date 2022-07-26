package Test_Projects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Farhan
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class Data_Acq_GUI extends JFrame {
    
    JFrame myframe = new JFrame("Data Acquisition GUI");
    
       
    // make buttons
    
 
    
    JButton Connect = new javax.swing.JButton("Connect");
    JButton Disconnect = new javax.swing.JButton("Disconnect");
    JButton TwoDGraph = new javax.swing.JButton("2D Graph");
    
    JButton BarChart = new javax.swing.JButton("BarChart");
    JButton PieChart = new javax.swing.JButton("PieChart");
    JButton ThreeDGraph = new javax.swing.JButton("3DGraph");
    JButton Exit = new javax.swing.JButton("Exit");    
    
  
    // Misc 

    JTextField TextField1 = new JTextField();
    JComboBox ComboBox1 = new JComboBox<>();
    JComboBox ComboBox2 = new JComboBox<>(); 
    JComboBox ComboBox3 = new JComboBox<>();

    JScrollPane ScrollPane1 = new javax.swing.JScrollPane();
    JTextArea TextArea1 = new javax.swing.JTextArea();

    

    //
    JPanel Panel1 = new JPanel(); 
    JPanel Panel2 = new JPanel(); 
    JPanel Panel3 = new JPanel();   
    JPanel Panel4 = new JPanel();  
    JPanel Panel5 = new JPanel(); 
    
    
     JMenuBar JMenuBar1=new JMenuBar();
     
    JMenu Menu1 = new JMenu();
    JMenu Menu2 = new JMenu();       
    
    JMenuItem MenuItem1 = new JMenuItem();
    JMenuItem MenuItem2 = new JMenuItem();
    JMenuItem MenuItem3 = new JMenuItem();
    JMenuItem MenuItem4 = new JMenuItem();
    JMenuItem MenuItem5 = new JMenuItem();
    JMenuItem MenuItem6 = new JMenuItem();

     
     
    public  Data_Acq_GUI (){
              
        Menu1.setText("File");
        Menu1.setToolTipText("File Operations");

        MenuItem1.setText("Open");
        Menu1.add(MenuItem1);
        
        MenuItem2.setText("Save");
        Menu1.add(MenuItem2);

        MenuItem3.setText("Save As");
        Menu1.add(MenuItem3);

        MenuItem4.setText("Close");
        Menu1.add(MenuItem4);  

        MenuItem5.setText("Exit");
        Menu1.add(MenuItem5);        
         
        Menu2.setText("Help");
        JMenuBar1.add(Menu2);  
 
        CreateGUI();
    }
    
    public void CreateGUI()
    {    
         
        JMenuBar1.add(Menu1);
        JMenuBar1.add(Menu2);
        
        setJMenuBar(JMenuBar1);      
        
        myframe.add(JMenuBar1);
       
        myframe.setLayout(null);
        myframe.setLocationRelativeTo(null);
        myframe.setBounds(0, 0, 750, 800);  
        
          
        
            // 1st JPanel
        Panel1.setBackground(new Color(204, 204, 204));
        Panel1.setBorder(BorderFactory.createTitledBorder("Set Comm. Parameters"));
        Panel1.setLayout(null);
        Panel1.setBounds(myframe.getX()+50, myframe.getY()+100, 280,100);

        ComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Port", "COM1", "COM2", "COM3" }));    
        ComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Baud Rate","9600", "19200","115200"}));    
        ComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Parity", "None", "Even", "Odd" }));    
             
              
        
        
        ComboBox1.setBounds(Panel1.getX()-40,  Panel1.getY()-75 ,70, 20);
        ComboBox2.setBounds(Panel1.getX()+40,  Panel1.getY()-75 ,100, 20);
        ComboBox3.setBounds(Panel1.getX()+150,  Panel1.getY()-75 ,70, 20);        
        
        Connect.setBounds(Panel1.getX()-40,  Panel1.getY()-30,100, 20);
        Disconnect.setBounds(Panel1.getX()+120,  Panel1.getY()-30,100, 20);
        
        Panel1.add(Connect);   
        Panel1.add(Disconnect);       
        Panel1.add(ComboBox1);
        Panel1.add(ComboBox2);
        Panel1.add(ComboBox3);        
              
        
        
        
        // 2nd JPanel
        Panel2.setBackground(new Color(204, 204, 204));
        Panel2.setBorder(BorderFactory.createTitledBorder("Plot Data"));
        Panel2.setLayout(null);
        Panel2.setBounds(myframe.getX()+400, myframe.getY()+100, 250, 100); 
        
        //System.out.println("Panel2 getx() is = "+Panel2.getX());
        //System.out.println("Panel2 getY() is = "+Panel2.getY()); 
        
        //TwoDGraph.setBounds(Panel2.getX()+10,Panel2.getY()+50, 100,50);
        TwoDGraph.setBounds(Panel2.getX()+10,Panel2.getY()+30, 100,20);
        BarChart.setBounds(Panel2.getX()+125,Panel2.getY()+30, 100,20);
        PieChart.setBounds(Panel2.getX()+10,Panel2.getY()+70, 100,20);
        ThreeDGraph.setBounds(Panel2.getX()+125,Panel2.getY()+70, 100,20);        
        //TwoDGraph.setBounds(410,150,50, 20);    
         //BarChart 
         //PieChart 
         //ThreeDGraph       
        
        
        myframe.add(TwoDGraph); 
        myframe.add(BarChart);        
        myframe.add(PieChart);      
        myframe.add(ThreeDGraph);          
        
        //Panel2.add(TwoDGraph); 
        //Panel2.add(BarChart);        
        //Panel2.add(PieChart);      
        //Panel2.add(ThreeDGraph);   
        
        
      // 3rd JPanel    

        Panel3.setBackground(new Color(255, 255, 255));
        Panel3.setBorder(BorderFactory.createTitledBorder("Data Visulaization"));
        Panel3.setLayout(null);
        Panel3.setBounds(Panel1.getX(), Panel1.getY()+150, 600, 300); 
      
    
        
           // 4th JPanel    

        Panel4.setBackground(new Color(200, 200, 200));
        Panel4.setBorder(BorderFactory.createTitledBorder("Recieved Data(HEX)"));
        Panel4.setLayout(null);
        Panel4.setBounds(Panel1.getX(), Panel1.getY()+470, 600, 150);    
        
              
        
        TextArea1.setBounds(Panel1.getX()+10, Panel1.getY()+480, 500, 100); 
        Panel4.add(TextArea1);
        
        
        
        
        
        TextField1.setBackground(new java.awt.Color(51, 153, 255));
        TextField1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        TextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextField1.setText("Serial Port Data Acquisition GUI");
        TextField1.setBounds(myframe.getX()+150, myframe.getY()+30, 400, 50);     
   
        
        
        Exit.setBounds(myframe.getX()+580, myframe.getY()+725, 70, 30); 

 /////////////////////////////////////////////////////////////////       
     
      
        ButtonListener mylistner = new ButtonListener();
        Exit.addActionListener(mylistner);
        Connect.addActionListener(mylistner);
        Disconnect.addActionListener(mylistner);
          
          
      
        myframe.add(TextField1);  
        myframe.add(Panel1);
        myframe.add(Panel2);     
        myframe.add(Panel3);    
        myframe.add(Panel4);    
        myframe.add(Exit);          
        //myframe.add(TextArea1);      
     
        
        myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myframe.setVisible(true);      
        
        
        
        
        
        
    
    }
      public class ButtonListener implements ActionListener
        {
         
            @Override
            public void actionPerformed (ActionEvent e)
            {
                if(e.getSource() == Exit)
                {
                    System.exit(0);
                    //System.out.println("Exit Button Pressed");
                                              
                }
                else if(e.getSource()== Connect)
                {               
                    System.out.println("Serial Port Connected");
                       
                }
                else if(e.getSource()== Disconnect)
                {             
                    System.out.println("Serial Port Disconnected");
                               
                }      
        
                
            }     
        
        
        
        }
 


}
