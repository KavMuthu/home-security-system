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
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JPanel {
	private JTextField nameField;
	private JPasswordField passwordField;
	private JLabel passwordLabel;
	private JButton saveButton;
	private Graphics2D g2d;
	private BufferedImage icon1;
	private URL url;
	private Image scaledImg;
	private Dimension screenSize;
	private int height,width;
	private Font font,font1,font2;
	
	public LoginPanel() {
		//set styles, properties to the panel
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		this.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(20.0f), Color.WHITE));
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) screenSize.getWidth();
		height = (int) screenSize.getHeight();
		font = new Font("Verdana", Font.BOLD, 18);
		font1 = new Font("Verdana", Font.BOLD, 15);
		font2 = new Font("Verdana", Font.BOLD,22);
		
		// Heading definition and properties 
		JLabel titleLabel = new JLabel("HOME SECURITY SYSTEM");
		titleLabel.setFont(font2);
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBounds(50, 50, 500, 80);
		this.add(titleLabel);
		
		// Heading definition and properties
		JLabel subTitleLabel = new JLabel("SO SAFE....BE SAFE");
		subTitleLabel.setFont(font2);
		subTitleLabel.setForeground(Color.WHITE);
		subTitleLabel.setBounds(75, 100, 500, 80);
		this.add(subTitleLabel);
		
		//Customer name Label
		JLabel custName= new JLabel("Name: ");
		custName.setFont(font);
		custName.setForeground(Color.WHITE);
		custName.setBounds(50, 300, 100, 40);
		this.add(custName);
		
		//Customer name text field
		nameField=new JTextField(20);
		nameField.setFont(font1);
		nameField.setBackground(Color.white);
		nameField.setForeground(Color.BLACK);
		nameField.setBounds(160, 300, 200, 30);
		this.add(nameField);
		
		//Password label
		passwordLabel=new JLabel("Password: ");
		passwordLabel.setForeground(Color.WHITE);
		passwordLabel.setFont(font);
		passwordLabel.setBounds(50, 350, 130, 40);
		this.add(passwordLabel);
		
		//Password text field
		passwordField=new JPasswordField(20);
		passwordField.setColumns( 20 );	
		passwordField.setFont(font1);
		passwordField.setBackground(Color.white);
		passwordField.setForeground(Color.BLACK);
		passwordField.setBounds(160, 350, 200, 30);
		this.add(passwordField);

		//Save button for login details
		saveButton=new JButton("Log in");
		saveButton.setFont(font1);
		saveButton.setBounds(150, 425, 100, 30);
		saveButton.setBackground(Color.LIGHT_GRAY);
		saveButton.setContentAreaFilled(false);
		saveButton.setOpaque(true);
		saveButton.setForeground(Color.BLACK);
		this.add(saveButton);
		
		//Set background image
		imgLoad();
	}

	//Load background image
	public void imgLoad() {
		url = ClassLoader.getSystemClassLoader().getResource("res/bg.png");
		try {
			icon1 = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}			
		scaledImg = icon1.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		setVisible(true);
	}
	//paint method to paint the background image
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2d = (Graphics2D) g.create();
		if (scaledImg != null) {
			g2d.drawImage(scaledImg, 0, 0, null);
		}
	}	
	//action listener for log in button
	public void saveLoginActionListener(ActionListener listener) {		
		saveButton.addActionListener(listener);		
	}
	//getter method for user name
	public String getUserName(){
		return nameField.getText();
	}
	//getter method for password 
	public String getPassword(){
		return passwordField.getText();
	}

}

