package project.ui;
/**
 * 
 * @author Kavitha Muthu, Swathi Prasad
 *
 */
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import project.ConnectDB;

public class GenerateBill extends JPanel implements ActionListener{
	private JButton burglaryAlarm, fireAlarm, statsLog, backButton, eventsHistory;
	private String name,email,address;
	private int phone,fireSensors, burglarySensors, numOfDays, totalFireSensorCost, totalBurglarySensorCost;
	private GridBagConstraints constraints;
	private JPanel billPanel, fireBill,burglaryBill, logStats;
	private java.sql.Date fromDate, toDate;
	private Connection connection;
	private Statement stmt;
	private ConnectDB conn;
	private CardLayout cardLayout;
	private Font font=new Font("Verdana", Font.BOLD, 15);
	private Font font1 = new Font("Verdana", Font.PLAIN, 15);
	private Color myBlue;
	private BufferedImage icon1;
	private URL url;
	private Image scaledImg;
	private Graphics g2d;
	private Dimension screenSize;
	private int height,width;
	
	public GenerateBill(){
			
		//Set styling for Panel
		Dimension size = new Dimension(850, 700);
		this.setMaximumSize(size);
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.setAlignmentX(LEFT_ALIGNMENT);
		
		this.setLayout(new BorderLayout());
			
		//Create navPanel to hold buttons to navigate
		JPanel navPanel=new JPanel();
		int red = 33;
		int green = 65;
		int blue = 114;
		myBlue = new Color(red,green,blue);
		navPanel.setBackground(myBlue);
		navPanel.setOpaque(true);
		this.add(navPanel, BorderLayout.NORTH);
		
		//Button to display Log records
		statsLog= new JButton("Log Records");	
		statsLog.addActionListener(this);
		
		//Button to display Fire Alarm bill
		fireAlarm= new JButton("Fire Alarm bill");
		navPanel.add(fireAlarm);	
		fireAlarm.addActionListener(this);

		//Button to display Burglary Alarm bill
		burglaryAlarm= new JButton("Burglary Alarm bill");
		navPanel.add(burglaryAlarm);	 
		burglaryAlarm.addActionListener(this);
		
		//Get JDBC connection
		connection = null;
     	stmt = null;
     	conn= new ConnectDB();
 		connection=conn.getConnection();
  
 		//Panel to hold back button
     	JPanel backPanel = new JPanel();
     	int red1 = 33;
		int green1 = 65;
		int blue1 = 114;
		myBlue = new Color(red1,green1,blue1);
     	backPanel.setBackground(myBlue);
     	backPanel.setOpaque(true);
     	
     	//Button to display Events History
     	eventsHistory = new JButton("Events History");
     	backPanel.add(eventsHistory);
     	this.add(backPanel,BorderLayout.SOUTH);
     	
     	backPanel.add(statsLog);
     	backButton = new JButton("Back");
     	backPanel.add(backButton);
     	  		
     	//Create Panel for Bill, Log
 		billPanel= new JPanel();
 		fireBill= new JPanel();
 		burglaryBill= new JPanel();
 		logStats= new JPanel();
 		
 		//Place all the panels in cardLayout
 		cardLayout = new CardLayout();
		billPanel.setLayout(cardLayout);
		billPanel.add(fireBill,"1");
		billPanel.add(burglaryBill,"2");
		billPanel.add(logStats, "3");
		
		//Default show billPanel
		cardLayout.show(billPanel, "1");
		
		//Load background image
		imgLoad();			
	}
	
	/**
	 * method to fetch the background image, file path is read to a bufferedImage
	 */
	public void imgLoad() {
		//Get the background image
		url = ClassLoader.getSystemClassLoader().getResource(
				"res/safe_home.jpg");
		//file path is read to the buffered image
		try {
			icon1 = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		//the screen's height and width is obtained
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) screenSize.getWidth();
		height = (int) screenSize.getHeight();
		//Fit the image to the Panel
		scaledImg = icon1.getScaledInstance(width, 650,
				java.awt.Image.SCALE_SMOOTH);
		setVisible(true);
	}
	
	/**
	 * Paint method paints the background image
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2d = (Graphics2D) g.create();
		if (scaledImg != null) {
			g2d.drawImage(scaledImg, 0, 30, null);
		}
	}	
	
/**
 * 	
 * @param fullName Name of the customer
 * Method retrieves data of customer and Scheduled activation time from database
 */
public void createJDBCConnection(String fullName){		
     	try { 
			stmt = connection.createStatement();
			//Get Customer information
			String sql = "SELECT fullName, address, email, phone, mService1, mService2 FROM custLog where fullName='"+fullName+"'";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
		         //Retrieve by column name
		          phone  = rs.getInt("phone");
		          name = rs.getString("fullName");
		          address = rs.getString("address");
		          email = rs.getString("email");
		      }
			
			//Count how many Fire Sensors are installed
			 String sql2="SELECT count(*) FROM section WHERE fireSensor='1'";
			 ResultSet rs2 = stmt.executeQuery(sql2);
			 rs2.next();			 
			 fireSensors = rs2.getInt(1);
			 
			//Count how many Burglary Sensors are installed
			 String sql3="SELECT count(*) FROM section WHERE burglarySensor='1'";
			 ResultSet rs3 = stmt.executeQuery(sql3);
			 rs3.next();
			 burglarySensors = rs3.getInt(1);
			
			 //Get date of Activation
			 String sql4="SELECT fromDate, toDate FROM systemConfig";
			 ResultSet rs4 = stmt.executeQuery(sql4);
			 if(rs4.next()){	 
				 fromDate=rs4.getDate(1);
				 toDate=rs4.getDate(2);			 
				 long ttime = toDate.getTime();
				 long ftime = fromDate.getTime();
				 //Calculate the number of days for which system is active
				 numOfDays = (int)( (ttime - ftime) / (1000 * 60 * 60 * 24));
				 //Total fire and Burglary sensors cost
				 totalFireSensorCost= fireSensors*numOfDays;
				 totalBurglarySensorCost= burglarySensors*numOfDays;
			 } 			 
		} 
     	catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

/**
 * Generate Fire Bill
 */
	public void generateFireBill(){
	
		//Create Panel for FireBill, set styling for it
		fireBill = new JPanel();
		fireBill.setLayout(null);
		fireBill.setBackground(Color.WHITE);
		this.add(fireBill);

		//Create Labels for Customer name label and value
		JLabel custName= new JLabel("Customer Name: ");
		custName.setFont(font);
		custName.setBounds(400, 50, 200, 40);
		custName.setForeground(myBlue);
		fireBill.add(custName);

		JLabel nameField=new JLabel(name);
		nameField.setFont(font1);
		nameField.setBounds(850, 50, 200, 40);
		nameField.setForeground(Color.BLACK);
		fireBill.add(nameField);

		//Create Labels for Customer Address label and value
		JLabel custAddress= new JLabel("Address: ");
		custAddress.setFont(font1);
		custAddress.setBounds(400, 85, 200, 40);
		custAddress.setFont(font);
		custAddress.setForeground(myBlue);
		fireBill.add(custAddress);

		JLabel addressField=new JLabel(address);
		addressField.setFont(font1);
		addressField.setBounds(850, 85, 200, 40);
		addressField.setForeground(Color.BLACK);
		fireBill.add(addressField);

		//Create Labels for Customer Email label and value
		JLabel custEmail= new JLabel("Email: ");
		custEmail.setFont(font);
		custEmail.setBounds(400, 120, 200, 40);
		custEmail.setForeground(myBlue);
		fireBill.add(custEmail);

		JLabel emailField=new JLabel(email);
		emailField.setFont(font1);
		emailField.setBounds(850, 120, 200, 40);
		emailField.setForeground(Color.BLACK);
		fireBill.add(emailField);

		//Create Labels for Customer Phone label and value
		JLabel custphone= new JLabel("Phone: ");
		custphone.setFont(font);
		custphone.setBounds(400, 155, 200, 40);
		custphone.setForeground(myBlue);
		fireBill.add(custphone);

		JLabel phoneField=new JLabel(String.valueOf(phone));
		phoneField.setFont(font1);
		phoneField.setBounds(850, 155, 200, 40);
		phoneField.setForeground(Color.BLACK);
		fireBill.add(phoneField);
		
		//Create Labels for From Date label and value
		JLabel fromDateLabel=new JLabel("From Date: ");
		fromDateLabel.setFont(font);
		fromDateLabel.setBounds(400, 185, 400, 40);
		fromDateLabel.setForeground(myBlue);
		fireBill.add(fromDateLabel);

		JLabel fromDateField=new JLabel(fromDate.toString());
		fromDateField.setFont(font1);
		fromDateField.setBounds(850, 185, 200, 40);
		fromDateField.setForeground(Color.BLACK);
		fireBill.add(fromDateField);

		//Create Labels for To Date label and value
		JLabel toDateLabel=new JLabel("To Date: ");
		toDateLabel.setFont(font);
		toDateLabel.setBounds(400, 220, 400, 40);
		toDateLabel.setForeground(myBlue);
		fireBill.add(toDateLabel);

		JLabel toDateField=new JLabel( toDate.toString());
		toDateField.setFont(font1);
		toDateField.setBounds(850, 220, 200, 40);
		toDateField.setForeground(Color.BLACK);
		fireBill.add(toDateField);			

		//Create Labels for Number of fire sensors installed label and value
		JLabel fireSensorsLabel=new JLabel("Number of fire sensors installed: ");
		fireSensorsLabel.setFont(font);
		fireSensorsLabel.setBounds(400, 255, 400, 40);
		fireSensorsLabel.setForeground(myBlue);
		fireBill.add(fireSensorsLabel);

		JLabel fireSensorsField=new JLabel(String.valueOf(fireSensors));
		fireSensorsField.setFont(font1);
		fireSensorsField.setBounds(850, 255, 200, 40);
		fireSensorsField.setForeground(Color.BLACK);
		fireBill.add(fireSensorsField);
		
		//Create Labels for Amount for fire sensors label and value
		int amount= fireSensors * 25;
		JLabel amountLabel=new JLabel(" Amount for fire sensors ($25/sensor): ");	
		amountLabel.setFont(font);
		amountLabel.setBounds(395, 285, 400, 40);
		amountLabel.setForeground(myBlue);
		fireBill.add(amountLabel);

		JLabel amountField=new JLabel(String.valueOf(amount));
		amountField.setFont(font1);
		amountField.setBounds(850, 285, 200, 40);
		amountField.setForeground(Color.BLACK);
		fireBill.add(amountField);

		//Create Labels for Total amount for fire sensors label and value
		JLabel totalCostFireLabel=new JLabel("Total amount for "+ fireSensors +" fire Sensors for "+ numOfDays+" Days: ");
		totalCostFireLabel.setFont(font);
		totalCostFireLabel.setBounds(400, 320, 700, 40);
		totalCostFireLabel.setForeground(myBlue);
		fireBill.add(totalCostFireLabel); 
		
		JLabel totalCostFireField=new JLabel(String.valueOf(totalFireSensorCost));
		totalCostFireField.setFont(font1);
		totalCostFireField.setBounds(850, 320, 100, 40);
		totalCostFireField.setForeground(Color.BLACK);
		fireBill.add(totalCostFireField);
		
		//Create Labels for Total amount for fire sensors label and value
		int totalAmount=totalFireSensorCost+amount;
		JLabel totalFireBillLabel=new JLabel("Total amount: ");
		totalFireBillLabel.setFont(font);
		totalFireBillLabel.setBounds(400, 355, 500, 40);
		totalFireBillLabel.setForeground(myBlue);
		fireBill.add(totalFireBillLabel); 

		JLabel totalFireBillField=new JLabel(String.valueOf(totalAmount));
		totalFireBillField.setFont(font1);
		totalFireBillField.setBounds(850, 355, 100, 40);
		totalFireBillField.setForeground(Color.BLACK);
		fireBill.add(totalFireBillField);
		this.revalidate();
		this.repaint();
	}
	
	public void generateBurglaryBill(){
		
		System.out.println("BURGLARY BILL");
		burglaryBill=new JPanel();
		
		burglaryBill.setBackground(Color.WHITE);
		burglaryBill.setLayout(null);	
		this.add(burglaryBill);
		
		//Create Labels for Customer name label and value
		JLabel custName= new JLabel("Customer Name: ");
		custName.setFont(font);
		custName.setBounds(400, 50, 200, 40);
		custName.setForeground(myBlue);
		burglaryBill.add(custName);
		
		JLabel nameField=new JLabel(name);
		nameField.setFont(font1);
		nameField.setBounds(850, 50, 200, 40);
		nameField.setForeground(Color.BLACK);
		burglaryBill.add(nameField);
		
		//Create Labels for Customer Address label and value
		JLabel custAddress= new JLabel("Address: ");
		custAddress.setBounds(400, 85, 200, 40);
		custAddress.setFont(font);
		custAddress.setForeground(myBlue);
		burglaryBill.add(custAddress);

		JLabel addressField=new JLabel(address);
		addressField.setBounds(850, 85, 200, 40);
		addressField.setFont(font1);
		addressField.setForeground(Color.BLACK);
		burglaryBill.add(addressField);

		//Create Labels for Email  label and value
		JLabel custEmail= new JLabel("Email: ");
		custEmail.setBounds(400, 120, 200, 40);
		custEmail.setFont(font);
		custEmail.setForeground(myBlue);
		burglaryBill.add(custEmail);

		JLabel emailField=new JLabel(email);
		emailField.setBounds(850, 120, 200, 40);
		emailField.setFont(font1);
		emailField.setForeground(Color.BLACK);
		burglaryBill.add(emailField);

		//Create Labels for Customer Phone label and value
		JLabel custphone= new JLabel("Phone: ");
		custphone.setBounds(400, 155, 200, 40);
		custphone.setFont(font);
		custphone.setForeground(myBlue);
		burglaryBill.add(custphone);

		JLabel phoneField=new JLabel(String.valueOf(phone));
		phoneField.setBounds(850, 155, 200, 40);
		phoneField.setFont(font1);
		phoneField.setForeground(Color.BLACK);
		burglaryBill.add(phoneField);
		
		//Create Labels for From Date label and value
		JLabel fromDateLabel=new JLabel("From Date: ");
		fromDateLabel.setFont(font);
		fromDateLabel.setBounds(400, 185, 400, 40);
		fromDateLabel.setForeground(myBlue);
		burglaryBill.add(fromDateLabel,constraints);
		
		JLabel fromDateField=new JLabel(fromDate.toString());
		fromDateField.setFont(font1);
		fromDateField.setBounds(850, 185, 200, 40);
		fromDateField.setForeground(Color.BLACK);
		burglaryBill.add(fromDateField);
		
		//Create Labels for To Date label and value
		JLabel toDateLabel=new JLabel("To Date: ");
		toDateLabel.setFont(font);
		toDateLabel.setBounds(400, 220, 400, 40);
		toDateLabel.setForeground(myBlue);
		burglaryBill.add(toDateLabel);
		
		JLabel toDateField=new JLabel( toDate.toString());
		toDateField.setFont(font1);
		toDateField.setBounds(850, 220, 200, 40);
		toDateField.setForeground(Color.BLACK);
		burglaryBill.add(toDateField,constraints);

		//Create Labels for Number of burglary sensors installed label and value
		JLabel burglarySensorsLabel=new JLabel("Number of burglary sensors installed: ");
		burglarySensorsLabel.setFont(font);
		burglarySensorsLabel.setBounds(400, 255, 400, 40);
		burglarySensorsLabel.setForeground(myBlue);
		burglaryBill.add(burglarySensorsLabel);

		JLabel burglarySensorsField=new JLabel(String.valueOf(burglarySensors));
		burglarySensorsField.setFont(font1);
		burglarySensorsField.setBounds(850, 255, 200, 40);
		burglarySensorsField.setForeground(Color.BLACK);
		burglaryBill.add(burglarySensorsField);

		//Create Labels for Total amount for burglary sensors label and value
		int amount= burglarySensors * 25;
		JLabel amountLabel=new JLabel("Total amount for burglary sensors: ");	
		amountLabel.setFont(font);
		amountLabel.setBounds(400, 285, 500, 40);
		amountLabel.setForeground(myBlue);
		burglaryBill.add(amountLabel);

		JLabel amountField=new JLabel(String.valueOf(amount));
		amountField.setFont(font1);
		amountField.setBounds(850, 285, 200, 40);
		amountField.setForeground(Color.BLACK);
		burglaryBill.add(amountField);

		//Create Labels for Total amount for burglary sensors label and value
		JLabel totalCostBurglaryLabel=new JLabel("Total amount for "+ burglarySensors +" Burglary Sensors for "+ numOfDays+" Days: ");
		totalCostBurglaryLabel.setFont(font);
		totalCostBurglaryLabel.setBounds(400, 320, 700, 40);
		totalCostBurglaryLabel.setForeground(myBlue);
		burglaryBill.add(totalCostBurglaryLabel); 

		JLabel totalCostBurglaryField=new JLabel(String.valueOf(totalBurglarySensorCost));
		totalCostBurglaryField.setFont(font1);
		totalCostBurglaryField.setBounds(850, 320, 100, 40);
		totalCostBurglaryField.setForeground(Color.BLACK);
		burglaryBill.add(totalCostBurglaryField);
		
		//Create Labels for Total amount label and value
		int totalAmount=totalBurglarySensorCost+amount;
		JLabel totalBurglaryBillLabel=new JLabel("Total amount: ");
		totalBurglaryBillLabel.setFont(font);
		totalBurglaryBillLabel.setBounds(400, 355, 500, 40);
		totalBurglaryBillLabel.setForeground(myBlue);
		burglaryBill.add(totalBurglaryBillLabel); 

		JLabel totalBurglaryBillField=new JLabel(String.valueOf(totalAmount));
		//Styling for field
		totalBurglaryBillField.setFont(font1);
		totalBurglaryBillField.setBounds(850, 355, 100, 40);
		totalBurglaryBillField.setForeground(Color.BLACK);
		burglaryBill.add(totalBurglaryBillField);
		
		this.validate();	
	}

	/**
	 * Action Listeners for  buttons on nav
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		 if(e.getSource()==statsLog){
			 cardLayout.show(billPanel, "3");
	}	
	else if(e.getSource()==fireAlarm)
		{
			cardLayout.show(billPanel, "1");
			generateFireBill();
							
		}
		
		else if(e.getSource()==burglaryAlarm){
			
			cardLayout.show(billPanel, "2");
			generateBurglaryBill();	
		}
	
	};

	/**
	 * 
	 * Register Action listener for back button
	 */
	public void backActionListener(ActionListener listener) {
		backButton.addActionListener(listener);

	}
	/**
	 * 
	 * Register Action listener for statslog button
	 */
	public void stat(ActionListener listener) {
		statsLog.addActionListener(listener);

	}
	/**
	 * 
	 * Register Action listener for events History button
	 */
	public void eventsHistActionListener(ActionListener listener) {
		eventsHistory.addActionListener(listener);

	}

	
}
