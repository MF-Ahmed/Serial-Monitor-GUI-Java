
import java.awt.Color;
import com.fazecast.jSerialComm.*;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Panel;
import static java.awt.SystemColor.window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.Clock;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import javafx.scene.chart.XYChart;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jfree.ui.about.AboutDialog;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Farhan
 */
public class test_gui extends javax.swing.JFrame {

    /**
     * Creates new form test_gui
     */

    final int max_val=20;
    int Val=0;
    int portnum=0;  
    int x=0;
    int counter=0;    
    boolean Start_log = false;   
    boolean Show_Graph = false;
    
    int len = SerialPort.getCommPorts().length; 
    
    Thread thread;
    boolean exitflag = false;
    static SerialPort ChosenPort;
    SerialPort SerialPorts[] = new SerialPort[len];
    String BR="";
    String Parity="";
    int Par=0;

    XYSeries series = new XYSeries("Temperature Sensor readings");
    XYSeriesCollection dataset = new XYSeriesCollection(series);
    JFreeChart chart = ChartFactory.createXYLineChart("Tempurature Sensor Readings", "Time","ADC Vals", dataset);
  

    JFrame graph_frame = new JFrame();    
    
    
    
    
    public test_gui() {
     
        initComponents();
                clock();
       graph_frame.setTitle("Temprature Graph");
       graph_frame.setSize(600,400);
       graph_frame.setLayout(new BorderLayout());
       graph_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       graph_frame.add(new ChartPanel(chart), BorderLayout.CENTER);     
       graph_frame.setVisible(false);
       
 
       
       //JButton Graph_Exit = new JButton("Exit Graph");
       //graph_frame.add(Graph_Exit);
       

         //  Graph_Panel.add(new ChartPanel(chart),BorderLayout.CENTER);
        //Graph_Panel.setVisible(true);       

        
      
        SerialPorts = SerialPort.getCommPorts();
        for (int i=0; i<SerialPorts.length; i++)
            SelectPort.addItem(SerialPorts[i].getSystemPortName());       
     
          Connect.addActionListener(new ActionListener(){
          
          
          public void actionPerformed(ActionEvent e)
          {
            
            if(Connect.getText().equals("Connect"))
                  
            { 
                if (SelectPort.getSelectedIndex()==0)// if serial port is not selected form list
                {             
                    JOptionPane.showMessageDialog(null,null,"Please Select a Serial Port First! ",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                else if (SelectBR.getSelectedIndex()==0)// if serial port is not selected form list
                {             
                    JOptionPane.showMessageDialog(null,null,"Please Select Baud Rate First! ",JOptionPane.WARNING_MESSAGE);
                    
                    return;
                }
                else if (SelBR.getSelectedIndex()==0)// if serial port is not selected form list
                {             
                    JOptionPane.showMessageDialog(null,null,"Please Select Parity First! ",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                        
                     
                
                exitflag = false;
                        // open the selected Port
                        
                System.out.println(SelectPort.getItemAt(SelectPort.getSelectedIndex()));
                ChosenPort = SerialPort.getCommPort(SelectPort.getItemAt(SelectPort.getSelectedIndex())); 
                                
                
                 BR = SelectBR.getItemAt(SelectBR.getSelectedIndex());     
                 Parity = SelBR.getItemAt(SelBR.getSelectedIndex());                     
                 int Baud = Integer.parseInt(BR); 
                 //int Par = Integer.parseInt(Parity);
                if(Parity == "Even")Par=2;
                 else if(Parity == "Odd")Par=1;
                else Par =0;                             
                //ChosenPort.writeBytes(buffer, SOMEBITS)
                //System.out.println("Parity Selected is "+String.valueOf(Par));   
                
                //ChosenPort.setComPortParameters(Baud, 8, 1, SerialPort.ODD_PARITY);
                
                ChosenPort.setComPortParameters(Baud,8,1,Par);
                
                if(ChosenPort.openPort())
                {                 
                    Connect.setText("Disconnect");
                    Connect.setBackground(Color.red);  
                    ClearData.setEnabled(false);
                    SelectPort.setEnabled(false);
                    SelectBR.setEnabled(false);
                    SelBR.setEnabled(false);
                   
                }
                Thread RXThread = new Thread() {
                        @Override
                        public void run(){                              
                            String line = "";
                            while(true)
                            {
                                if (exitflag)
                                    break;
                                try
                                {
                                    byte[] buf = new byte[1];
                                    int num = ChosenPort.readBytes(buf, 1);                                    
                                    if (num > 0)
                                    {
                                        line += getString(buf[0]) + " ";
                                        //int yval = Byte.toUnsignedInt(buf[0]);
                                        int yval = Integer.parseUnsignedInt(getString(buf[0]));   
                                        RxDataField.setText(line);
                                        series.add(++x, yval-80);
                                        Container.setValue(yval);                                
                                    }
                                }
                                catch(Exception e){}
                            }
                            System.out.println("Thread is exiting . . . ");
                        }
                };
                RXThread.start();
            }
            else 
            {
                exitflag = true;
                x=0;
                ChosenPort.closePort();         
                SelectPort.setEnabled(true);
                SelectBR.setEnabled(true);
                SelBR.setEnabled(true);
                ClearData.setEnabled(true);
                Connect.setText("Connect");                
                Connect.setBackground(Color.GREEN);               
                
            }
          }          
          
        });
    
    }

    public String getNibble(byte b) {
            String str = "";
            switch(b) {
                case 0x00:
                    str = "0";
                    break;
                case 0x01:
                    str = "1";
                    break;
                case 0x02:
                    str = "2";
                    break;
                case 0x03:
                    str = "3";
                    break;
                case 0x04:
                    str = "4";
                    break;
                case 0x05:
                    str = "5";
                    break;
                case 0x06:
                    str = "6";
                    break;
                case 0x07:
                    str = "7";
                    break;
                case 0x08:
                    str = "8";
                    break;
                case 0x09:
                    str = "9";
                    break;
                case 0x0A:
                    str = "A";
                    break;
                case 0x0B:
                    str = "B";
                    break;
                case 0x0C:
                    str = "C";
                    break;
                case 0x0D:
                    str = "D";
                    break;
                case 0x0E:
                    str = "E";
                    break;
                case 0x0F:
                    str = "F";
                    break;
                default:str = "\n Invalid data Recieved Please Check connection";
            }
            return str;
        }
        
    public String getString(byte b) {
            String str = getNibble((byte)((b>>4)&0x0F)) + getNibble((byte)(b&0x0F));
            return str;
        }
        
        
    public void clock()
    {
        Thread clock = new Thread()
        {            
            public void run()
            {
                try{
                    while(true){
                        Calendar caldr = new GregorianCalendar();
                        int day = caldr.get(Calendar.DAY_OF_MONTH);
                        int month = caldr.get(Calendar.MONTH);
                        int year = caldr.get(Calendar.YEAR); 

                        int sec = caldr.get(Calendar.SECOND);
                        int min = caldr.get(Calendar.MINUTE);
                        int hour = caldr.get(Calendar.HOUR);
                        
                       Date_Time.setText(" Date  : "+year+" /"+month+" /"+day+"  Time : "+hour+" :"+min+" :"+sec);                       
                       sleep(1000);
                    }   
                }catch(InterruptedException e){
                    e.printStackTrace();                
                }
                
            }
        
            };
        
        clock.start();
   
        
        }       
        
          

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        jMenuItem1 = new javax.swing.JMenuItem();
        Container = new javax.swing.JProgressBar();
        Jtext1 = new javax.swing.JTextField();
        UP_Button = new javax.swing.JButton();
        Down_Button = new javax.swing.JButton();
        Slider = new javax.swing.JSlider();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        SelectPort = new javax.swing.JComboBox<>();
        SelectBR = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        TxDataField = new javax.swing.JTextArea();
        ClearData = new javax.swing.JButton();
        Connect = new javax.swing.JButton();
        View_Graph = new javax.swing.JButton();
        SelBR = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        RxDataField = new javax.swing.JTextArea();
        Send = new javax.swing.JButton();
        Save_to_File = new javax.swing.JButton();
        Open_File = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        Start_Logging = new javax.swing.JButton();
        Timer_Time1 = new javax.swing.JTextField();
        log_time = new javax.swing.JTextField();
        Date_Time = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        FilejMenu1 = new javax.swing.JMenu();
        File_Open_File = new javax.swing.JMenuItem();
        File_Save_As = new javax.swing.JMenuItem();
        File_Exit = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Container.setMaximum(100);
        Container.setOrientation(1);

        Jtext1.setToolTipText("");

        UP_Button.setText("UP");
        UP_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UP_ButtonActionPerformed(evt);
            }
        });

        Down_Button.setText("DN");
        Down_Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Down_ButtonMouseClicked(evt);
            }
        });
        Down_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Down_ButtonActionPerformed(evt);
            }
        });

        Slider.setMajorTickSpacing(5);
        Slider.setMinorTickSpacing(1);
        Slider.setOrientation(javax.swing.JSlider.VERTICAL);
        Slider.setPaintLabels(true);
        Slider.setPaintTicks(true);
        Slider.setSnapToTicks(true);
        Slider.setToolTipText("");
        Slider.setValue(0);
        Slider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                SliderStateChanged(evt);
            }
        });

        jTextField1.setBackground(new java.awt.Color(153, 153, 255));
        jTextField1.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("Test GUI ");
        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jTextField2.setEditable(false);
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("Container");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        SelectPort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Port" }));
        SelectPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectPortActionPerformed(evt);
            }
        });

        SelectBR.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select BR", "9600", "19200", "115200", "256000" }));

        TxDataField.setColumns(20);
        TxDataField.setForeground(new java.awt.Color(51, 51, 51));
        TxDataField.setLineWrap(true);
        TxDataField.setRows(5);
        TxDataField.setBorder(javax.swing.BorderFactory.createTitledBorder("Data To Be Tranmissted"));
        jScrollPane1.setViewportView(TxDataField);
        TxDataField.getAccessibleContext().setAccessibleName("Data To Be Transmitted");

        ClearData.setText("Clear Data");
        ClearData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearDataActionPerformed(evt);
            }
        });

        Connect.setBackground(java.awt.Color.green);
        Connect.setText("Connect");
        Connect.setToolTipText("");
        Connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectActionPerformed(evt);
            }
        });

        View_Graph.setText("View_Graph");
        View_Graph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                View_GraphActionPerformed(evt);
            }
        });

        SelBR.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Parity", "Even", "Odd", "None" }));
        SelBR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelBRActionPerformed(evt);
            }
        });

        RxDataField.setEditable(false);
        RxDataField.setColumns(20);
        RxDataField.setLineWrap(true);
        RxDataField.setRows(5);
        RxDataField.setText(" ");
        RxDataField.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Recieved"));
        jScrollPane2.setViewportView(RxDataField);

        Send.setText("Send");
        Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendActionPerformed(evt);
            }
        });

        Save_to_File.setText("Save to File");
        Save_to_File.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Save_to_FileActionPerformed(evt);
            }
        });

        Open_File.setText("Open File");
        Open_File.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Open_FileActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Logging"));

        Start_Logging.setBackground(java.awt.Color.green);
        Start_Logging.setText("Start Logging");
        Start_Logging.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Start_LoggingActionPerformed(evt);
            }
        });

        Timer_Time1.setEditable(false);
        Timer_Time1.setBackground(java.awt.Color.lightGray);
        Timer_Time1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Timer_Time1.setText("HH:MM:SS ");
        Timer_Time1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Timer_Time1ActionPerformed(evt);
            }
        });

        log_time.setEditable(false);
        log_time.setBackground(java.awt.Color.lightGray);
        log_time.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        log_time.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                log_timeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Start_Logging, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(log_time, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(Timer_Time1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Start_Logging)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Timer_Time1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(log_time, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Date_Time.setEditable(false);
        Date_Time.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Date_Time.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Date_TimeActionPerformed(evt);
            }
        });

        FilejMenu1.setText("File");

        File_Open_File.setText("Open File");
        File_Open_File.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                File_Open_FileActionPerformed(evt);
            }
        });
        FilejMenu1.add(File_Open_File);

        File_Save_As.setText("Save As");
        File_Save_As.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                File_Save_AsActionPerformed(evt);
            }
        });
        FilejMenu1.add(File_Save_As);

        File_Exit.setText("EXIT");
        File_Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                File_ExitActionPerformed(evt);
            }
        });
        FilejMenu1.add(File_Exit);

        jMenuBar1.add(FilejMenu1);

        jMenu1.setText("Help");

        jMenuItem2.setText("How to Use");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("About");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(Container, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)))
                        .addComponent(Slider, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Down_Button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Jtext1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(UP_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Date_Time))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(SelectPort, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(SelectBR, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(7, 7, 7)
                                        .addComponent(SelBR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(31, 31, 31)
                                        .addComponent(Connect, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(Open_File)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Send))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(ClearData)
                                .addGap(283, 283, 283)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(Save_to_File)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(View_Graph)))))
                        .addGap(0, 138, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Date_Time, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Slider, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Container, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Down_Button)
                            .addComponent(Jtext1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(UP_Button))
                        .addGap(38, 38, 38))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SelectPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SelectBR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Connect)
                            .addComponent(SelBR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ClearData)
                            .addComponent(View_Graph)
                            .addComponent(Save_to_File))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(Open_File, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Send, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Down_ButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Down_ButtonMouseClicked
           Val--;
            if (Val == -1)Val=0;
            System.out.println("Val = " + Val);
            Jtext1.setText("   "+Val);
            Container.setValue((Val));
            
         // TODO add your handling code here:
    }//GEN-LAST:event_Down_ButtonMouseClicked

    private void UP_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UP_ButtonActionPerformed
        
        Val++;
        if (Val == max_val)Val=0;
        System.out.println("Val = " + Val);
        Jtext1.setText("   "+Val);
        Container.setValue(Val);

        // TODO add your handling code here:
    }//GEN-LAST:event_UP_ButtonActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void SliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_SliderStateChanged
        if (Val == max_val)Val=0;
        Val = Slider.getValue();        // TODO add your handling code here:
       Jtext1.setText(Integer.toString(Slider.getValue()));
       Container.setValue(Val);

    }//GEN-LAST:event_SliderStateChanged

    private void SelectPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectPortActionPerformed
        // TODO add your handling code here:
        portnum = SelectPort.getSelectedIndex(); 
        System.out.println("\n Serial Port Selected = " + portnum);          
        
    }//GEN-LAST:event_SelectPortActionPerformed

    private void ClearDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearDataActionPerformed
        // TODO add your handling code here:
        RxDataField.setText("   ");
        
    }//GEN-LAST:event_ClearDataActionPerformed

    private void File_Open_FileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_File_Open_FileActionPerformed
        System.out.println("Open a file");        // TODO add your handling code here:
    }//GEN-LAST:event_File_Open_FileActionPerformed

    private void File_Save_AsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_File_Save_AsActionPerformed
        // TODO add your handling code here:
        System.out.println("Save data in a file");  
    }//GEN-LAST:event_File_Save_AsActionPerformed

    private void File_ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_File_ExitActionPerformed
    System.exit(0);        // TODO add your handling code here:
    }//GEN-LAST:event_File_ExitActionPerformed

    private void View_GraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_View_GraphActionPerformed
        // TODO add your handling code here:
        if(Show_Graph==false){
           graph_frame.setVisible(true); 

           View_Graph.setText("Close Graph");           
           Show_Graph = true;
        }
        else {         
           graph_frame.setVisible(false); 
           View_Graph.setText("View Graph"); 
           x=0;  // initialize X-Axis of graph
           series.clear();
           graph_frame.dispose();
           Show_Graph = false;        
        }          

        
    }//GEN-LAST:event_View_GraphActionPerformed

    private void SelBRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelBRActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SelBRActionPerformed

    private void SendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendActionPerformed
       
            if(ChosenPort.openPort()){

                        thread = new Thread() {
                        @Override
                        public void run(){                              
                          
                            while(true)
                            {
                                //if (exitflag)
                                   // break;
                                try
                                {
                                    byte[] buf = new byte[1];
                                    
                                   //TxDataField.read(in, len);
                                    buf[0] = 0x01; 
                                    if(buf[0]!=0)
                                    {    
                                        ChosenPort.writeBytes(buf, buf.length);
                                        TxDataField.setText(Integer.toUnsignedString(buf[0]));
                                        
                                       System.out.println(" is sent to Port" + portnum);
                                        buf[0]=0;
                                        
                                    }
                                }
                                catch(Exception e){}
                            }
                            //System.out.println("Thread 2 is exiting . . . ");
                        }
                };
                thread.start();        
            }
            
            
            else {
             JOptionPane.showMessageDialog(null, "Please select a Serial Port First");
            
            }

// TODO add your handling code here:
    }//GEN-LAST:event_SendActionPerformed

    private void Save_to_FileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Save_to_FileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Save_to_FileActionPerformed

    private void ConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectActionPerformed

    


    }//GEN-LAST:event_ConnectActionPerformed

    private void Open_FileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Open_FileActionPerformed

           JFileChooser Filechoser = new JFileChooser();
           if (Filechoser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION )
           {
               java.io.File file = Filechoser.getSelectedFile();

               try {
                   Scanner input = new Scanner(file);
                   while(input.hasNext())
                   {
                       System.out.println(input.nextLine());
                       TxDataField.setText(input.nextLine());
                   }
                   input.close();
               } catch (FileNotFoundException ex) {
                   Logger.getLogger(test_gui.class.getName()).log(Level.SEVERE, null, ex);
               }
       
           
           }
           
                else
               {
                System.out.println("No File Selected");
                JOptionPane.showMessageDialog(null, "Please select a file");
               }
           
         
        // TODO add your handling code here:
    }//GEN-LAST:event_Open_FileActionPerformed

    private void Down_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Down_ButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Down_ButtonActionPerformed

    private void Start_LoggingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Start_LoggingActionPerformed
        // TODO add your handling code here:
        if(Start_log==false)
        {
          Start_Logging.setBackground(Color.RED);
          Start_Logging.setText("Stop Logging");
          Start_log=true;
          Timer Logtimer = new Timer();
          
          TimerTask task = new TimerTask(){
          
              public void run(){      
                log_time.setText("00:00:"+Integer.toString(counter));
                counter++;
                if(counter == 60){
                    Logtimer.cancel();
                }
                if(Start_log == false)
                {                
                  Logtimer.cancel();                
                }                          
              }        
          };
          
          Logtimer.scheduleAtFixedRate(task, 1000, 1000);      
          
        }
        else if(Start_log==true)
        {  
          counter=0;
          Start_Logging.setBackground(Color.GREEN);
          Start_Logging.setText("Start Logging");
          log_time.setText("00:00:"+Integer.toString(counter));                   
          Start_log=false;       
        }
        
        
        
        
        
    }//GEN-LAST:event_Start_LoggingActionPerformed

    private void log_timeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_log_timeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_log_timeActionPerformed

    private void Timer_Time1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Timer_Time1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Timer_Time1ActionPerformed

    private void Date_TimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Date_TimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Date_TimeActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
      
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
    if(evt.getSource() == jMenuItem3) {
        // Create about dialog with the app window as parent
        //AboutDialog AbtDialog = new AboutDialog
        //AboutDialog aboutDlg = new AboutDialog(this, “About Sketcher”, “Sketcher Copyright Ivor Horton 2011”);
        }    
    }//GEN-LAST:event_jMenuItem3ActionPerformed
    
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
            java.util.logging.Logger.getLogger(test_gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(test_gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(test_gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(test_gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new test_gui().setVisible(true);
                
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ClearData;
    private javax.swing.JButton Connect;
    private javax.swing.JProgressBar Container;
    private javax.swing.JTextField Date_Time;
    private javax.swing.JButton Down_Button;
    private javax.swing.JMenuItem File_Exit;
    private javax.swing.JMenuItem File_Open_File;
    private javax.swing.JMenuItem File_Save_As;
    private javax.swing.JMenu FilejMenu1;
    private javax.swing.JTextField Jtext1;
    private javax.swing.JButton Open_File;
    private javax.swing.JTextArea RxDataField;
    private javax.swing.JButton Save_to_File;
    private javax.swing.JComboBox<String> SelBR;
    private javax.swing.JComboBox<String> SelectBR;
    private javax.swing.JComboBox<String> SelectPort;
    private javax.swing.JButton Send;
    private javax.swing.JSlider Slider;
    private javax.swing.JButton Start_Logging;
    private javax.swing.JTextField Timer_Time1;
    private javax.swing.JTextArea TxDataField;
    private javax.swing.JButton UP_Button;
    private javax.swing.JButton View_Graph;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField log_time;
    // End of variables declaration//GEN-END:variables
}
