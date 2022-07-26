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

public class SampleGUI extends JFrame {
    
    JFrame myframe = new JFrame("SampleGUI ");
    // make 3 buttons
    JButton UpButton = new JButton("UP");
    JButton DownButton = new JButton("DOWN");
    JButton Center = new JButton("Center");

    //DownButton.setSize(100, 50);
    JPanel Panel1 = new JPanel(); 
    JPanel Panel2 = new JPanel(); 
     
    public  SampleGUI (){
        
        CreateGUI();
    }
    
    public void CreateGUI()
    {
        myframe.setBounds(0, 0, 500, 400);
        Container content = myframe.getContentPane();
        content.setLayout(null);
        content.setBounds(0, 0, 500, 400);
        //myframe.setSize(500, 400); // width, height
        
        UpButton.setToolTipText("This is the Up Button");
        DownButton.setToolTipText("This is the Down Button");
        Center.setToolTipText("This is the Center Button");

        UpButton.setBackground(Color.white);
        DownButton.setBackground(Color.white);
        Center.setBackground(Color.white);

        UpButton.setForeground(Color.blue);
        DownButton.setForeground(Color.red);
        Center.setForeground(Color.black);
        
        UpButton.setBounds(50,50, 100, 50);
        DownButton.setBounds(300, 50, 100, 50);
        Center.setBounds(170,50, 100, 50);

        GUIListener mylistner = new GUIListener();
        UpButton.addActionListener(mylistner);
        DownButton.addActionListener(mylistner);
        Center.addActionListener(mylistner);        

 /////////////////////////////////////////////////////////////////       
        GUIMouseListener mouseclick = new GUIMouseListener();   
        
        UpButton.addMouseListener(mouseclick);
        DownButton.addMouseListener(mouseclick);
        Center.addMouseListener(mouseclick);        
              
    
        
       // myframe.add(UpButton);
        //myframe.add(DownButton);
        
        // 1st JPanel
        //Panel1.setLayout(new GridLayout(2,10));
        Panel1.setBorder(new TitledBorder("This is Pannel 1 Border"));
        Panel1.setLayout(null);
        Panel1.setBounds(0, 0, 475, 200);

       // 2nd JPanel    
        Panel2.setBorder(new TitledBorder("This is Pannel 2 Border"));
        Panel2.setLayout(null);
        Panel2.setBounds(0, 205, 475, 150);
        //UpButton.setLocation(20,20);
        //Panel2.setLayout(null);
        Panel2.add(Center);

        Panel1.add(UpButton);
        Panel1.add(DownButton);    
        
        ///////////////////////////////////////////////////////      
        //Panel1.setSize(500, 100); // width, height;
        //Panel1.setLocation(0, 10);       
        
        ///////////////////////////////////////////////////////       
 
        //Panel2.setSize(500, 100);        
        //Panel2.setLocation(0, 120);

        //Panel1.setLayout(new GridLayout(2,50));
        content.add(Panel1);    
        content.add(Panel2);
        //myframe.setLocationRelativeTo(null);
        //myframe.setLayout(new GridLayout(2,5));        
        myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myframe.setVisible(true);   
    
    }
  
    /**
     *
     */
    public class GUIMouseListener implements MouseListener
    {
        public void mouseClicked(MouseEvent e)
        {}

        /**
         * Invoked when a mouse button has been pressed on a component.
         */
        public void mousePressed(MouseEvent e)
        {}

        /**
         * Invoked when a mouse button has been released on a component.
         */
        public void mouseReleased(MouseEvent e)
        {}

        /**
         * Invoked when the mouse enters a component.
         */
        public void mouseEntered(MouseEvent e)
        {
            if(e.getSource() == UpButton)
            {
                System.out.println("Cursor Moved to UpButton");

            }
            else if(e.getSource()== DownButton)
            {               
                System.out.println("Cursor Moved to DownButton");

            }
            else if(e.getSource()== Center)
            {             
                System.out.println("Cursor Moved to Center");

            }      


        }
        /**
         * Invoked when the mouse exits a component.
         */
        public void mouseExited(MouseEvent e)
        {}
                 
           
   }
    
    
        public class GUIListener implements ActionListener
        {
         
            @Override
            public void actionPerformed (ActionEvent e)
            {
                if(e.getSource() == UpButton)
                {
                    System.out.println("Up Button Pressed");
                                              
                }
                else if(e.getSource()== DownButton)
                {               
                    System.out.println("Down Button Pressed");
                       
                }
                else if(e.getSource()== Center)
                {             
                    System.out.println("Center Button Pressed");
                               
                }      
        
                
            }     
        
        
        
        }
    


}
