package project;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import project.demo.ui.BuildingLayoutPanel;
import project.ui.BuildingConfigPanel;
import project.ui.CustomerInfoPanel;
import project.ui.EventsGraph;
import project.ui.GenerateBill;
import project.ui.LogRecords;
import project.ui.LoginPanel;
/**
 * 
 * @author Kavitha Muthu, Swathi Prasad
 * The main class specifies the layout for the mainPanel and main frame
 * contains all the action handlers
 *
 */

public class Main {

	private JPanel mainPanel;
	private CustomerInfoPanel customerInfoPanel;
	private BuildingConfigPanel buildingConfigPanel;
	private GenerateBill generateBill;
	private BuildingLayoutPanel buildingLayoutPanel;
	private LoginPanel loginPanel;
	private LogRecords logRecords;
	private EventsGraph eventsGraph;
	private String name,password;
/**
 * the main method is instantiated
 * @param args
 */
	public static void main(String[] args) {
		Main mainObject = new Main();

	}
/**
 * the constructor consists of mainPanel(main card container)
 * all the panels(cards) contained in the mainPanel are instantiated and added to mainPanel
 * specifications for the main frame is mentioned here
 */
	public Main() {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
				}

				JFrame frame = new JFrame("SoSafe Home Security System");

				//all the panels are instantiated
				buildingConfigPanel = new BuildingConfigPanel();		
				customerInfoPanel = new CustomerInfoPanel();
				generateBill = new GenerateBill();
				buildingLayoutPanel = new BuildingLayoutPanel();
				loginPanel= new LoginPanel();
				logRecords = new LogRecords();
				eventsGraph = new EventsGraph();

				//mainPanel is instantiated and all the panels are added to the mainPanel
				mainPanel = new JPanel();
				mainPanel.setLayout(new CardLayout());
				mainPanel.setBackground(Color.WHITE);
				mainPanel.add(loginPanel,"1");
				mainPanel.add(customerInfoPanel,"2");
				mainPanel.add(buildingConfigPanel, "3");
				mainPanel.add(buildingLayoutPanel, "4");
				mainPanel.add(generateBill,"5");
				mainPanel.add(logRecords,"6");
				mainPanel.add(eventsGraph, "7");
				
				CardLayout cardLayout = (CardLayout) (mainPanel.getLayout());
				cardLayout.show(mainPanel, "1");				
				frame.setBackground(Color.WHITE);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLayout(new BorderLayout());

				//mainPanel is added to the main frame
				frame.add(mainPanel);
				//action handler method for all the action listeners
				handleActionListeners();		

				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});

	}
/**
 * action handlers for all the buttons - Log In button, Submit button, Save sensors button, Activate sensors button,
 * Generate Bill button, Log Records button and Event History button.
 */
	public void handleActionListeners() {
		//login in button action handler
		loginPanel.saveLoginActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//user name and password from the login panel is passed as parameters to the customer info panel
				name=loginPanel.getUserName();
				password= loginPanel.getPassword();
				customerInfoPanel.checkLogin(name,password);
				
				//user profile panel is displayed
				CardLayout cardLayout = (CardLayout) (mainPanel.getLayout());
				cardLayout.show(mainPanel, "2");
			}
			
		});

		//customer profile panel - "submit" button action handler
		customerInfoPanel.submitActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				//building configuration panel is displayed
				CardLayout cardLayout = (CardLayout) (mainPanel.getLayout());
				cardLayout.show(mainPanel, "3");
				
				customerInfoPanel.connectDB();

			}

		});
		//building configuration panel activate button sensor action handler
		buildingConfigPanel.activateSensorsActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {	
				//building layout panel is displayed
				CardLayout cardLayout = (CardLayout) (mainPanel.getLayout());
				cardLayout.show(mainPanel, "4");
				
				try {
					buildingConfigPanel.saveSensorsDB();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				//JDBC connection to save dates
				buildingConfigPanel.saveDate();	
				//JDBC connection to show the configured sensors
				buildingLayoutPanel.createJDBCConnection();
				try {
					//JDBC connection to validate date and time
					buildingLayoutPanel.checkDateTime();
				} catch (ParseException ex) {
					ex.printStackTrace();
				}
				buildingLayoutPanel.activationLabel.setText("");
				buildingLayoutPanel.serviceLabel.setText("");

			}

		});
		//building configuration panel save button action handler
		buildingConfigPanel.saveSensorsActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//arming and disarming of sensors in confirmed via the following method
				buildingConfigPanel.confirmSensors();

			}

		});
		//building layout panel generate bill button action handler
		buildingLayoutPanel.viewDemoActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//generate bill panel is displayed
				CardLayout cardLayout = (CardLayout) (mainPanel.getLayout());
				cardLayout.show(mainPanel, "5");
				
				generateBill.createJDBCConnection(name);

			}

		});
		//building layout panel back button action listener
		buildingLayoutPanel.backActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//back operation - building configuration panel is displayed
				CardLayout cardLayout = (CardLayout) (mainPanel.getLayout());
				cardLayout.show(mainPanel, "3");
			}

		});
		//generate bill back button action listener
		generateBill.backActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//back operation - building layout panel is displayed
				CardLayout cardLayout = (CardLayout) (mainPanel.getLayout());
				cardLayout.show(mainPanel, "4");
				//JDBC connection for building layout panel to display the configured sensors
				buildingLayoutPanel.createJDBCConnection();
			}

		});
		//generate bill log records button action listener
		generateBill.stat(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//log records panel is displayed
				CardLayout cardLayout = (CardLayout) (mainPanel.getLayout());
				cardLayout.show(mainPanel, "6");
				
				//JDBC connection for logRecords panel
				logRecords.getJDBCData();				
			}

		});
		//log records panel back button action handler
		logRecords.backActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//generate bill panel is displayed
				CardLayout cardLayout = (CardLayout) (mainPanel.getLayout());
				cardLayout.show(mainPanel, "5");
			}

		});
		//generate bill event history button action listener
		generateBill.eventsHistActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//events history (bar graph) panel is displayed
				CardLayout cardLayout = (CardLayout) (mainPanel.getLayout());
				cardLayout.show(mainPanel, "7");
				eventsGraph.graphData();

			}

		});
		//events graph panel "back" button action listener
		eventsGraph.backActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//generate bill panel is displayed
				CardLayout cardLayout = (CardLayout) (mainPanel.getLayout());
				cardLayout.show(mainPanel, "5");
			}

		});
	}

}
