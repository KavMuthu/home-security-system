package project.ui;
/**
 * 
 * @author Kavitha Muthu, Swathi Prasad
 *
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import project.ConnectDB;
/**
 * 
 * Class CustomerInfoPanel is used to receive information from the user
 *
 */
public class CustomerInfoPanel extends JPanel{
	private JLabel fullNameLabel,passwordLabel, addressLabel;
	private JTextArea addressField;
	private JTextField nameField , emailField, phoneField, mService1Field, mService2Field;
	private JPasswordField passwordField;
	private Font titleFont = new Font("Verdana", Font.BOLD, 20);
	private Font font=new Font("Verdana", Font.BOLD, 15);
	private Font font1 = new Font("Verdana", Font.PLAIN, 15);
	private JButton submit;
	
	private Graphics2D g2d;
	private BufferedImage icon1;
	private URL url;
	private Image scaledImg;
	private Dimension screenSize;
	private int height,width;
	private Connection connection; 
	private Statement stmt;
	private ConnectDB conn;
	private Boolean newUser=true;
	
	public CustomerInfoPanel(){
		
		//Style the layout, background
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) screenSize.getWidth();
		height = (int) screenSize.getHeight();
		
		this.setLayout(null);
		this.setSize(width, height);
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(20.0f), Color.WHITE));
	
		//Page Heading
		JLabel titleLabel = new JLabel("USER PROFILE");
		titleLabel.setFont(titleFont);
		titleLabel.setBounds(50, 50, 200, 80);
		titleLabel.setForeground(Color.WHITE);
		this.add(titleLabel);
		
		//Create Label and textfield for Customer name
		fullNameLabel=new JLabel("Name: ");	
		fullNameLabel.setFont(font);
		fullNameLabel.setBounds(50, 200, 100, 40);
		fullNameLabel.setForeground(Color.WHITE);
		this.add(fullNameLabel);
		
		nameField=new JTextField(20);
		nameField.setColumns( 20 );	
		nameField.setFont(font1);
		nameField.setBounds(200, 200, 200, 30);
		nameField.setBackground(Color.WHITE);
		nameField.setForeground(Color.BLACK);
		this.add(nameField);
		
		//Create Label and textfield for Password
		passwordLabel=new JLabel("Password: ");		
		passwordLabel.setFont(font);
		passwordLabel.setBounds(50, 245, 130, 40);
		passwordLabel.setForeground(Color.WHITE);
		this.add(passwordLabel);
		
		passwordField=new JPasswordField(20);
		passwordField.setFont(font1);
		passwordField.setBackground(Color.white);
		passwordField.setForeground(Color.BLACK);
		passwordField.setBounds(200, 245, 200, 30);				
		this.add(passwordField);
		
		//Create Label and textfield for Customer address
		addressLabel=new JLabel("Address: ");
		addressLabel.setFont(font);
		addressLabel.setBounds(50, 290, 130, 40);
		addressLabel.setForeground(Color.WHITE);
		this.add(addressLabel);
		
		addressField=new JTextArea();
		addressField.setFont(font1);
		addressField.setBackground(Color.white);
		addressField.setForeground(Color.BLACK);
		addressField.setBounds(200, 290, 200, 80);
		this.add(addressField);
		
		//Create Label and textfield for Customer email
		JLabel emailLabel=new JLabel("Email: ");
		emailLabel.setFont(font);
		emailLabel.setBounds(50, 385, 130, 40);
		emailLabel.setForeground(Color.WHITE);
		this.add(emailLabel);
		
		emailField = new JTextField(20);
		emailField.setFont(font1);
		emailField.setBackground(Color.white);
		emailField.setForeground(Color.BLACK);
		emailField.setBounds(200, 385, 200, 30);
		this.add(emailField);
		
		//Create Label and textfield for Customer phone number
		JLabel phoneLabel=new JLabel("Phone No: ");
		phoneLabel.setFont(font);
		phoneLabel.setBounds(50, 430, 130, 40);
		phoneLabel.setForeground(Color.WHITE);
		this.add(phoneLabel);
		
		phoneField = new JTextField(20);
		phoneField.setFont(font1);
		phoneField.setBackground(Color.white);
		phoneField.setForeground(Color.BLACK);
		phoneField.setBounds(200, 430, 200, 30);

		//Verify Phone number entered
		phoneField.setInputVerifier(new InputVerifier() {
	    @Override
	    public boolean verify(JComponent input) {
	        String text = ((JTextField) input).getText();
	        if (text.matches("\\d+")) // Reads: "Any digit one or more times"
	            return true;
	        return false;
	    }
	});
		this.add(phoneField);
		
		//Create Label and textfield for Emergency contact No1
		JLabel mService1Label=new JLabel("Emergency No1:");
		mService1Label.setFont(font);
		mService1Label.setBounds(50, 475, 150, 40);
		mService1Label.setForeground(Color.WHITE);
		this.add(mService1Label);

		mService1Field = new JTextField(20);
		mService1Field.setFont(font1);
		mService1Field.setBackground(Color.white);
		mService1Field.setForeground(Color.BLACK);
		mService1Field.setBounds(200, 475, 200, 30);
		this.add(mService1Field);
	
		//Create Label and textfield for Emergency contact No2
		JLabel mService2Label=new JLabel("Emergency No2: ");
		mService2Label.setFont(font);
		mService2Label.setBounds(50, 520, 150, 40);
		mService2Label.setForeground(Color.WHITE);
		this.add(mService2Label);
		
		mService2Field = new JTextField(20);
		mService2Field.setFont(font1);
		mService2Field.setBackground(Color.white);
		mService2Field.setForeground(Color.BLACK);
		mService2Field.setBounds(200, 520, 200, 30);
		this.add(mService2Field);
		
		//add a button for to save the information entered by user		
		submit= new JButton("Submit");
		submit.setFont(font);
		submit.setBounds(290, 570, 100, 30);
		submit.setBackground(Color.LIGHT_GRAY);
		submit.setContentAreaFilled(false);
		submit.setOpaque(true);
		submit.setForeground(Color.BLACK);
		this.add(submit);	 
		
		//Get JDBC connection
		connection = null;
		stmt = null;	     
		conn= new ConnectDB();
     	connection=conn.getConnection();

     	//Load the background image
		imgLoad();
	}
	
	/**
	 * method to fetch the background image, file path is read to a bufferedImage
	 */
	public void imgLoad() {
		//Get the background image
		url = ClassLoader.getSystemClassLoader().getResource(
				"res/bg.png");
		try {
			// file path is read to a bufferedImage
			icon1 = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		scaledImg = icon1.getScaledInstance(width, height,
				java.awt.Image.SCALE_SMOOTH);
		setVisible(true);
	}
	
	/**
	 * paint method paints the background image
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2d = (Graphics2D) g.create();
		if (scaledImg != null) {
			g2d.drawImage(scaledImg, 0, 0, null);
		}
	}	
	
	/**
	 * Method Checks if the logged in customer is a new user or registered user
	 * if logged in then retrieves customer information from database
	 */
	public void checkLogin(String name, String password){
		try {				
			stmt = connection.createStatement();
			String sql = "SELECT fullName FROM custLog where fullName='"+name+"'";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				newUser=false;
				String sql1="SELECT fullName, password, address, email, phone, mService1, mService2 FROM custLog where fullName='"+name+"'";
				ResultSet rs1 = stmt.executeQuery(sql1);
				while(rs1.next()){
					//get information from database and fill in textfields
					nameField.setText(rs1.getString("fullName"));
					passwordField.setText(rs1.getString("password"));
					addressField.setText(rs1.getString("address"));
					emailField.setText(rs1.getString("email"));
					phoneField.setText(rs1.getString("phone"));
					mService1Field.setText(rs1.getString("mService1"));
					mService2Field.setText(rs1.getString("mService2"));
				}	
			}
			else
			{
				//If new user then all text fields are blank except name and password
				newUser=true;
				nameField.setText(name);
				passwordField.setText(password);
				
			}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
/**
 *  Method saves the textfield data to database for persistence
 */
	public void connectDB(){
    	 try {
    		 //get all the values from textfield
    		String name=nameField.getText();
    		String password=passwordField.getText();
    		String address=addressField.getText();
    		String email=emailField.getText();
    		String phone=phoneField.getText();
    		String mService1=mService1Field.getText();
    		String mService2=mService2Field.getText();
    		
    	   Statement selectStmt = connection.createStatement();
    	     
 			 if(newUser==true){
 				 //if new user then insert all values to table in database
 				 String query="INSERT INTO `custLog`(fullName,password,address,email,phone,mService1,mService2,time) VALUES ('" + name + "','"+password+"','" + address + "','" + email + "','"+phone+"','"+mService1+"','"+mService2+"',now())";
 				stmt.executeUpdate(query);				
 			 }
 			 else{	 
	 			//if existing user then update existing user information
	 			String query="UPDATE custLog SET fullName = ?, password = ? , address = ? ,email= ?, phone = ?, mService1 = ?, mService2 = ? WHERE fullName='"+name+"'";		 
				PreparedStatement prepStmt = connection.prepareStatement(query);				
				prepStmt.setString(1, name);
				prepStmt.setString(2, password);
				prepStmt.setString(3,address);
				prepStmt.setString(4,email);
				prepStmt.setString(5,phone);
				prepStmt.setString(6,mService1);
				prepStmt.setString(7,mService2);
				prepStmt.executeUpdate();			
 			 }
    	 	} 
    	 catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
    	 	}
		}
	/**
	 * 
	 * Register Action listener for submit button
	 */
	public void submitActionListener(ActionListener listener) {		
		submit.addActionListener(listener);
	}	

}

