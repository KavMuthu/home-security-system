package project.demo.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import project.ConnectDB;
import project.entities.SubArea;

/**
 * 
 * @author Kavitha Muthu and Swathi Prasad 
 * This class includes methods to trigger the fire and burglary events call monitoring service
 *
 */
public class BuildingLayoutPanel extends JPanel {


	private JLabel areaLabel,
			 phoneLabel = new JLabel();
	public JLabel activationLabel, serviceLabel;
	private JButton generateBill, backButton;
	private BufferedImage icon1;
	private JComboBox<String> eventTrigger;
	private Font font, alertFont, font1;
	private Clip fireSensorClip = null;
	private Clip burglarySensorClip = null;
	private Image fireScaledImg, phoneScaledImg, fireBellScaledImg,
			burglaryBellScaledImg;
	private Graphics2D g2d;
	private static int id;
	private java.sql.Time callTime, responseTime;
	private SubArea subArea = new SubArea();
	private Map<Integer, SubArea> map = new HashMap<Integer, SubArea>();
	private Statement stmt, stmt1;
	private Connection connection;
	private Image burglaryScaledImg = null;
	private Rectangle section = null;
	private boolean flag, sensorsDateActive, sensorsTimeActive;
	private int inputValue;

	/**
	 * The constructor specifies the layout of the panel includes methods to
	 * create labels, select sections in the floor map and combobox events
	 */
	public BuildingLayoutPanel() {

		this.setLayout(new BorderLayout());
		this.setOpaque(true);
		int red = 33;
		int green = 65;
		int blue = 114;
		Color myBlue = new Color(red, green, blue);
		this.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(20.0f),
				myBlue));

		Dimension size = new Dimension(1200, 700);
		this.setMaximumSize(size);
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.setAlignmentX(LEFT_ALIGNMENT);
		this.setBorder(BorderFactory.createLineBorder(Color.blue));
		this.setBackground(Color.WHITE);
		this.setLayout(null);

		font = new Font("Serif", Font.BOLD, 18);
		alertFont = new Font("Serif", Font.BOLD, 12);
		font1 = new Font("Verdana", Font.BOLD, 18);

		connection = null;
		Statement stmt = null;

		ConnectDB conn = new ConnectDB();
		connection = conn.getConnection();

		// methods to create labels
		createSensors();
		createServiceLabel();
		createActivationLabel();
		// method to select the section in the floor map
		areaTracker();
		// method to execute comboBox events
		comboBoxActionListener(eventTrigger);
	}

	/**
	 * this method connects to the database fetches the data from the table
	 * "section" and puts it in the hashmap
	 */
	public void createJDBCConnection() {

		try {
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM section";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				// Retrieve by column name
				subArea = new SubArea();
				subArea.setId(rs.getInt("id"));
				subArea.setTitle(rs.getString("title"));
				subArea.setFireSensor(rs.getBoolean("fireSensor"));
				subArea.setBurglarySensor(rs.getBoolean("burglarySensor"));			
				map.put(rs.getInt("id"), subArea);

			}

		} catch (SQLException e1) {

			e1.printStackTrace();
		}

	}

	/**
	 * this method creates all the labels, comboBox and buttons on the demo
	 * screen
	 */

	public void createSensors() {
		
		int red = 33;
		int green = 65;
		int blue = 114;
		 Color myBlue = new Color(red, green, blue);
		this.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(20.0f),
				myBlue));

		//Title label
		JLabel titleLabel = new JLabel("SO SAFE HOME SECURITY SYSTEM - DEMO");
		titleLabel.setFont(font);
		titleLabel.setBounds(492, 7, 417, 50);
		titleLabel.setForeground(myBlue);
		this.add(titleLabel);

		//label that displays the name of the area selected
		areaLabel = new JLabel("");
		areaLabel.setFont(font);
		areaLabel.setBounds(614, 607, 200, 50);
		areaLabel.setForeground(Color.BLACK);
		this.add(areaLabel);

		//label that displays "Event"
		JLabel eventTitle = new JLabel("Event");
		eventTitle.setFont(font);
		eventTitle.setBounds(35, 190, 100, 50);
		eventTitle.setForeground(Color.BLACK);
		this.add(eventTitle);

		//combo box to trigger events
		String[] events = new String[] { "Select Event", "Fire", "Burglary" };
		eventTrigger = new JComboBox<>(events);
		eventTrigger.setBounds(100, 195, 100, 30);
		eventTrigger.setFont(alertFont);
		eventTrigger.setForeground(Color.BLACK);
		this.add(eventTrigger);
		
		//generate bill button
		generateBill = new JButton("Generate Bill");
		generateBill.setBounds(1000, 630, 100, 30);
		generateBill.setForeground(Color.BLACK);
		this.add(generateBill);
		
		//back button
		backButton = new JButton("Back");
		backButton.setBounds(1108, 630, 60, 30);
		this.add(backButton);

	}

	/**
	 * method to fetch the fire image, file path is read to a bufferedImage
	 */
	public void fetchFireIcon() {
		URL fireIconUrl; 
		BufferedImage fireIcon = null;
		fireIconUrl = ClassLoader.getSystemClassLoader().getResource(
				"res/fire_icon.png");
		//file path is read to the buffered image
		try {
			fireIcon = ImageIO.read(fireIconUrl);
		} catch (IOException e) {

			e.printStackTrace();
		}
		//buffered image is scaled
		fireScaledImg = fireIcon.getScaledInstance(90, 90,
				java.awt.Image.SCALE_SMOOTH);
	}

	/**
	 * method to fetch the burglary image, file path is read to a bufferedImage
	 */
	public void fetchBurglaryIcon() {
		URL burglaryIconUrl;
		BufferedImage theftIcon = null;
		burglaryIconUrl = ClassLoader.getSystemClassLoader().getResource(
				"res/burglar.png");
		//file path is read to the buffered image
		try {
			theftIcon = ImageIO.read(burglaryIconUrl);
		} catch (IOException e) {

			e.printStackTrace();
		}
		//buffered image is scaled
		burglaryScaledImg = theftIcon.getScaledInstance(90, 90,
				java.awt.Image.SCALE_SMOOTH);
	}

	/**
	 * method to fetch the phone image, file path is read to a bufferedImage
	 */

	public void fetchPhoneIcon() {
		flag = true;
		URL phoneIconUrl;
		BufferedImage phoneIcon=null;
		phoneIconUrl = ClassLoader.getSystemClassLoader().getResource(
				"res/call.png");
		//file path is read to the buffered image
		try {
			phoneIcon = ImageIO.read(phoneIconUrl);
		} catch (IOException e) {

			e.printStackTrace();
		}
		//buffered image is scaled
		phoneScaledImg = phoneIcon.getScaledInstance(100, 100,
				java.awt.Image.SCALE_SMOOTH);
	}

	/**
	 * method to fetch the fire bell image, file path is read to a bufferedImage
	 */

	public void fetchfireBell() {
		URL fireBellUrl;
		BufferedImage fireBellIcon=null;
		fireBellUrl = ClassLoader.getSystemClassLoader().getResource(
				"res/fireBell.png");
		//file path is read to the buffered image
		try {
			fireBellIcon = ImageIO.read(fireBellUrl);
		} catch (IOException e) {

			e.printStackTrace();
		}
		//buffered image is scaled
		fireBellScaledImg = fireBellIcon.getScaledInstance(25, 25,
				java.awt.Image.SCALE_SMOOTH);
	}

	/**
	 * method to fetch the burglary bell image, file path is read to a
	 * bufferedImage
	 */

	public void fetchBurglaryBell() {
		URL burglaryBellUrl;
		BufferedImage burglaryBellIcon = null;
		burglaryBellUrl = ClassLoader.getSystemClassLoader().getResource(
				"res/bell1.png");
		//file path is read to the buffered image
		try {
			burglaryBellIcon = ImageIO.read(burglaryBellUrl);
		} catch (IOException e) {

			e.printStackTrace();
		}
		//buffered image is scaled
		burglaryBellScaledImg = burglaryBellIcon.getScaledInstance(25, 25,
				java.awt.Image.SCALE_SMOOTH);
	}

	/**
	 * this method displays the input dialog box to enter the response code the
	 * input value is an integer number - 100 input dialog box appears
	 * repeatedly if a wrong code is entered
	 * 
	 * @param selected
	 *            - the selected value from the combobox is passed
	 * @param id
	 *            - id of the section is passed time of the response code is
	 *            also recorded
	 */
	private void showInputDialog(String selected, int id) {
		// input prompt appears repeatedly until the int value 100 is typed
		do {

			inputValue = Integer.parseInt(JOptionPane.showInputDialog(null,
					"Response code", "Monitoring Service",
					JOptionPane.PLAIN_MESSAGE));
		} while (inputValue != 100);

		if (inputValue == 100) {

		
			// response time is recorded
			Calendar calendar5 = Calendar.getInstance();
			calendar5.add(Calendar.DATE, 1);
			Date d2 = calendar5.getTime();
		

			long t2 = d2.getTime();
			responseTime = new java.sql.Time(t2);
			fireScaledImg = null;
			burglarySensorStop();
			fireSensorStop();
			saveStats(selected, id);
		}
		repaint();
	}

	/**
	 * this method simulates the call to the monitoring service checks if the
	 * fire sensor value is true in the database and if the fire event is
	 * triggered
	 * 
	 * @param selected
	 *            - the selected value from the combo box is passed
	 * @param id
	 *            - id of the section is passed time of the calling monitoring
	 *            service simulation is recorded
	 */
	public void callingMonitoringService(String selected, int id) {
		
		//if the fire sensor is true in the database and if the fire event is triggered the phone icon appears
		if (map.get(id).isFireSensor()
				&& eventTrigger.getSelectedItem().equals("Fire")
				|| map.get(id).isBurglarySensor()
				&& eventTrigger.getSelectedItem().equals("Burglary")
				) {

			//the phone icon is set to a label
			fetchPhoneIcon();
			phoneLabel.setIcon(new ImageIcon(phoneScaledImg));
			phoneLabel.setBounds(1149, 276, 100, 100);
			this.add(phoneLabel);

			// time of the calling monitoring service simulation is recorded
			Calendar calendar5 = Calendar.getInstance();
			calendar5.add(Calendar.DATE, 1);
			Date d1 = calendar5.getTime();
		
			long t = d1.getTime();
			callTime = new java.sql.Time(t);

			phoneScaledImg = null;
			serviceLabel.setText("");
			flag = false;

			//checks if the phone icon is not present
			if (!phoneLabel.equals(null)) {
				// Response code prompt appears with two seconds delay
				Timer timer = new Timer(2000, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						flag = false;
						phoneScaledImg = null;
						showInputDialog(selected, id);
						phoneLabel.setIcon(null);
					}
				});
				timer.setRepeats(false);
				timer.start();
			}

		}
		// if the fire sensor value is false in the database and if the fire
		// event is triggered,
		// calling monitoring service simulation is made null
		if (!map.get(id).isFireSensor()
				&& eventTrigger.getSelectedItem().equals("Fire")) {
			phoneScaledImg = null;
			serviceLabel.setText("");
		}
	}

	/**
	 * paint method paints the fire and burglary icon based on the area
	 * selection
	 */
	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		g2d = (Graphics2D) g.create();

		if (icon1 != null) { // background floor plan is drawn

			g2d.drawImage(icon1, 214, 61, null);

			g2d.setColor(Color.RED);
			g2d.setStroke(new BasicStroke(5));

			if (section != null) { // a rectangle to highlight each section is
									// drawn
				g2d.draw(section);

			}
		}
		// id of each section is checked and the respective images are drawn at
		// the respective x,y co-ordinates
		if (id == 1) {
			if (eventTrigger.getSelectedItem().equals("Fire")) {

				if (map.get(id).isFireSensor()) {
					fetchFireIcon();
					g2d.drawImage(fireScaledImg, 236, 177, null);
					repaint();
				}
			} else if (eventTrigger.getSelectedItem().equals("Burglary")) {
				if (map.get(id).isBurglarySensor()) {
					fetchBurglaryIcon();
					g2d.drawImage(burglaryScaledImg, 236, 177, null);
					repaint();
				}
			}

		}

		if (id == 2) {
			if (eventTrigger.getSelectedItem().equals("Fire")) {

				if (map.get(id).isFireSensor()) {
					fetchFireIcon();
					g2d.drawImage(fireScaledImg, 360, 327, null);
					repaint();
				}
			} else if (eventTrigger.getSelectedItem().equals("Burglary")) {
				if (map.get(id).isBurglarySensor()) {
					fetchBurglaryIcon();
					g2d.drawImage(burglaryScaledImg, 360, 327, null);
					repaint();
				}
			}

		}

		if (id == 3) {
			if (eventTrigger.getSelectedItem().equals("Fire")) {

				if (map.get(id).isFireSensor()) {
					fetchFireIcon();
					g2d.drawImage(fireScaledImg, 522, 285, null);
					repaint();
				}
			} else if (eventTrigger.getSelectedItem().equals("Burglary")) {

				if (map.get(id).isBurglarySensor()) {
					fetchBurglaryIcon();
					g2d.drawImage(burglaryScaledImg, 522, 285, null);
					repaint();
				}
			}

		}

		if (id == 5) {
			if (eventTrigger.getSelectedItem().equals("Fire")) {

				if (map.get(id).isFireSensor()) {
					fetchFireIcon();
					g2d.drawImage(fireScaledImg, 677, 260, null);
					repaint();
				}
			} else if (eventTrigger.getSelectedItem().equals("Burglary")) {
				if (map.get(id).isBurglarySensor()) {
					fetchBurglaryIcon();
					g2d.drawImage(burglaryScaledImg, 677, 260, null);
					repaint();
				}
			}

		}

		if (id == 6) {
			if (eventTrigger.getSelectedItem().equals("Fire")) {

				if (map.get(id).isFireSensor()) {
					fetchFireIcon();
					g2d.drawImage(fireScaledImg, 809, 277, null);
					repaint();
				}
			} else if (eventTrigger.getSelectedItem().equals("Burglary")) {

				if (map.get(id).isBurglarySensor()) {
					fetchBurglaryIcon();
					g2d.drawImage(burglaryScaledImg, 809, 277, null);
					repaint();
				}
			}

		}

		if (id == 8) {
			if (eventTrigger.getSelectedItem().equals("Fire")) {

				if (map.get(id).isFireSensor()) {
					fetchFireIcon();
					g2d.drawImage(fireScaledImg, 960, 317, null);
					repaint();
				}
			} else if (eventTrigger.getSelectedItem().equals("Burglary")) {

				if (map.get(id).isBurglarySensor()) {
					fetchBurglaryIcon();
					g2d.drawImage(burglaryScaledImg, 960, 317, null);
					repaint();
				}
			}
		}

		if (id == 9) {
			if (eventTrigger.getSelectedItem().equals("Fire")) {

				if (map.get(id).isFireSensor()) {
					fetchFireIcon();
					g2d.drawImage(fireScaledImg, 978, 495, null);
					repaint();
				}
			} else if (eventTrigger.getSelectedItem().equals("Burglary")) {

				if (map.get(id).isBurglarySensor()) {
					fetchBurglaryIcon();
					g2d.drawImage(burglaryScaledImg, 978, 495, null);
					repaint();
				}
			}

		}

		if (id == 7) {
			if (eventTrigger.getSelectedItem().equals("Fire")) {

				if (map.get(id).isFireSensor()) {
					fetchFireIcon();
					g2d.drawImage(fireScaledImg, 685, 480, null);
					repaint();
				}
			} else if (eventTrigger.getSelectedItem().equals("Burglary")) {

				if (map.get(id).isBurglarySensor()) {
					fetchBurglaryIcon();
					g2d.drawImage(burglaryScaledImg, 685, 480, null);
					repaint();
				}
			}

		}

		if (id == 4) {
			if (eventTrigger.getSelectedItem().equals("Fire")) {

				if (map.get(id).isFireSensor()) {
					fetchFireIcon();
					g2d.drawImage(fireScaledImg, 535, 475, null);
					repaint();
				}
			} else if (eventTrigger.getSelectedItem().equals("Burglary")) {

				if (map.get(id).isBurglarySensor()) {
					fetchBurglaryIcon();
					g2d.drawImage(burglaryScaledImg, 535, 475, null);
					repaint();
				}
			}

		}

		if (id == 10) {
			if (eventTrigger.getSelectedItem().equals("Fire")) {

				if (map.get(id).isFireSensor()) {
					fetchFireIcon();
					g2d.drawImage(fireScaledImg, 655, 140, null);
					repaint();
				}
			} else if (eventTrigger.getSelectedItem().equals("Burglary")) {

				if (map.get(id).isBurglarySensor()) {
					fetchBurglaryIcon();
					g2d.drawImage(burglaryScaledImg, 655, 140, null);
					repaint();
				}
			}

		}
		// if the response code is 100, fire image ,burglary image and
		// "service not available" label is made null
		if (inputValue == 100) {

			eventTrigger.setSelectedIndex(0);
			fireScaledImg = null;
			burglaryScaledImg = null;
			inputValue = 0;
		}
		// firebell and burglary bell icons are drawn if the sensors are
		// configured in the respective sections
		// the database value is checked and the id of section is checked to
		// draw the bell images at the respective x,y positions
		for (Integer idVal : map.keySet()) {

			if (map.get(idVal).isFireSensor()) {
				if (idVal == 1) {
					fetchfireBell();
					g2d.drawImage(fireBellScaledImg, 244, 256, null);
					repaint();
				}
				if (idVal == 2) {
					fetchfireBell();
					g2d.drawImage(fireBellScaledImg, 339, 473, null);
					repaint();
				}
				if (idVal == 3) {
					fetchfireBell();
					g2d.drawImage(fireBellScaledImg, 514, 245, null);
					repaint();
				}
				if (idVal == 4) {
					fetchfireBell();
					g2d.drawImage(fireBellScaledImg, 636, 551, null);
					repaint();
				}
				if (idVal == 5) {
					fetchfireBell();
					g2d.drawImage(fireBellScaledImg, 663, 374, null);
					repaint();
				}
				if (idVal == 6) {
					fetchfireBell();
					g2d.drawImage(fireBellScaledImg, 864, 372, null);
					repaint();
				}
				if (idVal == 7) {
					fetchfireBell();
					g2d.drawImage(fireBellScaledImg, 700, 469, null);
					repaint();
				}
				if (idVal == 8) {
					fetchfireBell();
					g2d.drawImage(fireBellScaledImg, 1026, 243, null);
					repaint();
				}
				if (idVal == 9) {
					fetchfireBell();
					g2d.drawImage(fireBellScaledImg, 1025, 551, null);
					repaint();
				}
				if (idVal == 10) {
					fetchfireBell();
					g2d.drawImage(fireBellScaledImg, 958, 151, null);
					repaint();
				}

			}
			if (map.get(idVal).isBurglarySensor()) {

				if (idVal == 1) {
					fetchBurglaryBell();
					g2d.drawImage(burglaryBellScaledImg, 271, 256, null);
					repaint();
				}
				if (idVal == 2) {
					fetchBurglaryBell();
					g2d.drawImage(burglaryBellScaledImg, 366, 473, null);
					repaint();
				}
				if (idVal == 3) {
					fetchBurglaryBell();
					g2d.drawImage(burglaryBellScaledImg, 487, 245, null);
					repaint();
				}
				if (idVal == 4) {
					fetchBurglaryBell();
					g2d.drawImage(burglaryBellScaledImg, 663, 551, null);
					repaint();
				}
				if (idVal == 5) {
					fetchBurglaryBell();
					g2d.drawImage(burglaryBellScaledImg, 690, 374, null);
					repaint();
				}
				if (idVal == 6) {
					fetchBurglaryBell();
					g2d.drawImage(burglaryBellScaledImg, 891, 372, null);
					repaint();
				}
				if (idVal == 7) {
					fetchBurglaryBell();
					g2d.drawImage(burglaryBellScaledImg, 727, 469, null);
					repaint();
				}
				if (idVal == 8) {
					fetchBurglaryBell();
					g2d.drawImage(burglaryBellScaledImg, 1053, 243, null);
					repaint();
				}
				if (idVal == 9) {
					fetchBurglaryBell();
					g2d.drawImage(burglaryBellScaledImg, 1052, 551, null);
					repaint();
				}
				if (idVal == 10) {
					fetchBurglaryBell();
					g2d.drawImage(burglaryBellScaledImg, 985, 151, null);
					repaint();
				}

			}

		}

		g2d.dispose();

	}

	// inner class to handle the Mouse Events
	//based the area selection, a rectangle is drawn to highlight the selected area
	//this method also validates time and date of activating sensors 
	class MouseEventAdapterA extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {

			int x = e.getX();
			int y = e.getY();

			phoneLabel.setIcon(null);
			activationLabel.setText("");
			serviceLabel.setText("");
			
			//time and date validation
			//checks if the activation time and date matches with the configured time and date
			if (sensorsDateActive == true && sensorsTimeActive == true) {
				
				//selected area is highlighted by drawing a rectangle around it
				//areaLabel flashes the name of the area selected
				if ((x >= 230 && x <= 329) && (y >= 155 && y <= 295)) {
				
					section = new Rectangle(230, 155, 99, 140);
					repaint();

					areaLabel.setText("TOILET 1");
					id = 1;

				} else if ((x >= 319 && x <= 480) && (y >= 225 && y <= 507)) {
					section = new Rectangle(319, 225, 161, 282);
					repaint();
					areaLabel.setText("BEDROOM 1");

					id = 2;
				} else if ((x >= 478 && x <= 651) && (y >= 226 && y <= 437)) {
					section = new Rectangle(478, 226, 173, 211);
					repaint();

					areaLabel.setText("LIVING ROOM");

					id = 3;

				} else if ((x >= 478 && x <= 686) && (y >= 450 && y <= 581)) {
					section = new Rectangle(478, 450, 208, 131);
					repaint();
					areaLabel.setText("DINING ROOM");

					id = 4;

				} else if ((x >= 654 && x <= 779) && (y >= 227 && y <= 405)) {
					section = new Rectangle(654, 227, 125, 178);
					repaint();
					areaLabel.setText("KITCHEN");
					id = 5;

				} else if ((x >= 776 && x <= 929) && (y >= 225 && y <= 404)) {
					section = new Rectangle(776, 225, 153, 179);
					repaint();
					areaLabel.setText("BEDROOM 2");

					id = 6;

				} else if ((x >= 688 && x <= 782) && (y >= 460 && y <= 589)) {
					section = new Rectangle(688, 460, 94, 129);
					repaint();
					areaLabel.setText("TOILET 2");
					id = 7;

				} else if ((x >= 929 && x <= 1094) && (y >= 226 && y <= 486)) {
					section = new Rectangle(929, 224, 165, 262);
					repaint();
					areaLabel.setText("MASTER BEDROOM");
					id = 8;

				} else if ((x >= 958 && x <= 1095) && (y >= 489 && y <= 590)) {
					section = new Rectangle(958, 489, 137, 101);
					repaint();
					areaLabel.setText("TOILET 3");

					id = 9;

				} else if ((x >= 346 && x <= 1095) && (y >= 138 && y <= 223)) {
					section = new Rectangle(346, 138, 748, 85);
					repaint();

					id = 10;

				} 
				//if either of the areas are selected, the the rectangle,areaLabel is made null
				else {

					section = null;
					areaLabel.setText("");
					
					id = 0;
				}

			} 
			//if the time or date has elapsed, then the label is displayed
			else {
				activationLabel.setText("System is inactive");

			}
		}

	}
/**
 * method to fetch the url for the floor map image
 */
	public void areaTracker() {
		URL url;
		url = ClassLoader.getSystemClassLoader().getResource(
				"res/floor_plan.jpg");
		//file path is read to the buffered image
		try {
			icon1 = ImageIO.read(url);
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		//mouse listener (to display the selected area) for the mouse events
		addMouseListener(new MouseEventAdapterA());
		setVisible(true);
	}
/**
 * a label to display "service not available" is created and made null
 */
	public void createServiceLabel() {
		serviceLabel = new JLabel("");
		serviceLabel.setBounds(577, 67, 250, 40);
		serviceLabel.setForeground(Color.RED);
		serviceLabel.setFont(font1);
		this.add(serviceLabel);
	}
	
	/**
	 * a label to display "system is inactive" is created and made null
	 */
	public void createActivationLabel() {
		activationLabel = new JLabel("");
		activationLabel.setBounds(577, 67, 250, 40);
		activationLabel.setFont(font1);
		activationLabel.setForeground(Color.RED);
		this.add(activationLabel);
	}
	/**
	 * comboBox action listener 
	 * @param listener
	 */

	public void comboBoxActionListener(ActionListener listener) {

		eventTrigger.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Object selected = eventTrigger.getSelectedItem();
				//label that displays "service not available" is made null
				serviceLabel.setText("");
				//checks for the time and date
				if (sensorsDateActive && sensorsTimeActive) {
					//if the fire event or burglary event is selected the following methods are called
					if (selected.toString().equals("Fire")
							|| selected.toString().equals("Burglary")) {

						//method that checks if the fire or security service is available or not
						checkSensors(selected.toString(), id);
					}
					try {
						//triggers the fire alarm sound
						fireAlarmSound();
						//stops the fire alarm sound
						fireSensorStop();
						//triggers the burglary alarm sound
						burglaryAlarmSound();
						//stops the burglary alarm sound
						burglarySensorStop();
					} catch (Exception e2) {

						e2.printStackTrace();
					}
				}

			}

		});

	}
/**
 * the following method turns on the fire alarm sound
 * @throws Exception
 */
	public void fireAlarmSound() throws Exception {

		AudioInputStream sensorInputStream = null;

		//checks if the fire event is selected and the area selected is not null
		if (eventTrigger.getSelectedItem().equals("Fire") && areaLabel != null) {
		
			try {
				//reads audio in the wav format from the input stream
				sensorInputStream = AudioSystem.getAudioInputStream(getClass()
						.getResource("/res/sensor.wav"));
				
				//opens the audio clip
				fireSensorClip = AudioSystem.getClip();
				fireSensorClip.open(sensorInputStream);
				
				//starts the audio clip
				fireSensorClip.start();
				//after a few seconds the audio is played back from the first frame
				fireSensorClip.loop(Clip.LOOP_CONTINUOUSLY);

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "File not found");
				e.printStackTrace();
			}
		}

	}
/**
 * this method turns off the fire alarm sound
 */
	public void fireSensorStop() {
		if (inputValue == 100 || !map.get(id).isFireSensor()) {

			if (fireSensorClip == null)
				return;
			//stops the audio clip
			fireSensorClip.stop();

		}
	}
	/**
	 * the following method turns on the burglary alarm sound
	 * @throws Exception
	 */
	public void burglaryAlarmSound() throws Exception {

		AudioInputStream sensorInputStream = null;
		//checks if the burglary event is selected and the area selected is not null
		if (eventTrigger.getSelectedItem().equals("Burglary")
				&& areaLabel != null) {
			try {
				//reads audio in the wav format from the input stream
				sensorInputStream = AudioSystem.getAudioInputStream(getClass()
						.getResource("/res/theftSensor.wav"));
				burglarySensorClip = AudioSystem.getClip();
				
				//opens the audio clip
				burglarySensorClip.open(sensorInputStream);

				//starts the audio clip
				burglarySensorClip.start();
				
				//after a few seconds the audio is played back from the first frame
				burglarySensorClip.loop(Clip.LOOP_CONTINUOUSLY);

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "File not found");
				e.printStackTrace();
			}
		}

	}
	/**
	 * this method turns off the fire alarm sound
	 */
	public void burglarySensorStop() {
		if (inputValue == 100 || !map.get(id).isBurglarySensor()) {

			if (burglarySensorClip == null)
				return;
			//stops the burglary audio clip
			burglarySensorClip.stop();

		}
	}
	/**
	 * action listener for generate bill button
	 * @param listener
	 */
	public void viewDemoActionListener(ActionListener listener) {
		generateBill.addActionListener(listener);

	}
/**
 * action listener for back button
 * @param listener
 */
	public void backActionListener(ActionListener listener) {
		backButton.addActionListener(listener);

	}
	/**
	 * Method checks if fire or burglary alarms are set
	 * @param selected
	 * @param id
	 */
	public void checkSensors(String selected, int id) {

		if ((selected.equals("Fire")) && (!map.get(id).isFireSensor())) {
			//if fire sensor is selected, But the area does not have fire sensor enabled
			phoneScaledImg = null;
			serviceLabel.setText("Service not available");
			this.revalidate();
			this.validate();
			fireScaledImg = null;
			fireSensorStop();
			eventTrigger.setSelectedItem("Select Event");
		}

		else if (selected.equals("Burglary")
				&& (!map.get(id).isBurglarySensor())) {
			//if Burglary sensor is selected, But the area does not have Burglary sensor enabled
			phoneScaledImg = null;
			serviceLabel.setText("Service not available");
			burglaryScaledImg = null;
			burglarySensorStop();
			eventTrigger.setSelectedItem("Select Event");
		} else if (selected.equals("Fire") || selected.equals("Burglary")) {

			//If Either Burglary or Fire sensor is selected
			callingMonitoringService(selected, id);
			java.util.Date date = new java.util.Date();
			long t = date.getTime();
		}

	}
/**
 * Saves data to log records whenever an alarm is set off
 * @param selected
 * @param id
 */
	public void saveStats(String selected, int id) {
		try {

			//Insert the values to statsLog table in database
			String sql = "INSERT INTO statsLog (id, area, event, date ,time, callTime, responseTime) VALUES(?, ?, ?, ?, ?,?,?)";
			PreparedStatement stmt = connection.prepareStatement(sql);
			
			//Convert date and time to sql date and time and then save to database
			java.util.Date utilDate = new java.util.Date();
			java.sql.Date date = new java.sql.Date(utilDate.getTime());
			long tim = date.getTime();
			java.sql.Time time = new java.sql.Time(tim);
			
			//Set all the values to the prepared statements values
			stmt.setInt(1, id);
			stmt.setString(2, map.get(id).getTitle());
			stmt.setString(3, selected);
			stmt.setDate(4, date);
			stmt.setTime(5, time);
			stmt.setTime(6, callTime);
			stmt.setTime(7, responseTime);
			stmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Method checks if the current date and time matches with system activation date and time
	 * @throws ParseException
	 */
	public void checkDateTime() throws ParseException {

		java.sql.Date fromDate = null, toDate = null;
		java.sql.Time wdfromTime =null, wdtoTime=null, wendsFromTime=null, wendsToTime=null;
		ResultSet rs4 = null;
		String sql4 = "SELECT fromDate, toDate, wdfromTime, wdtoTime, wendsFromTime, wendsToTime FROM systemConfig";
		try {
			stmt1 = connection.createStatement();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		try {
			rs4 = stmt1.executeQuery(sql4);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			if (rs4.next()) {
				//Get date and time from database
				SimpleDateFormat dfFormat = new SimpleDateFormat("dd.MM.yyyy");
				fromDate = rs4.getDate(1);
				toDate = rs4.getDate(2);
				wdfromTime = rs4.getTime(3);

				wdtoTime = rs4.getTime(4);
				wendsFromTime = rs4.getTime(5);
				wendsToTime = rs4.getTime(6);
			}
		} catch (SQLException e1) {

			e1.printStackTrace();
		}

		java.util.Date date = new java.util.Date();
		long tim = date.getTime();

		java.sql.Date currentTime = new java.sql.Date(tim);
		//Check if current date falls within from date and to date
		if (date.compareTo(fromDate) >= 0 && date.compareTo(toDate) <= 0) {
			sensorsDateActive = true;
		} else {
			sensorsDateActive = false;
		}
		
		String r = new SimpleDateFormat("HH:mm:ss")
				.format(new Date().getTime());

		Calendar calendar5 = Calendar.getInstance();
		int day = calendar5.get(Calendar.DAY_OF_WEEK);
		boolean isWeekday = ((day >= Calendar.MONDAY) && (day <= Calendar.FRIDAY));

		//Check if it is a weekday or weekend
		//Accordingly select date and time values
		if (isWeekday) {
			//if weekday
			java.util.Date startTime = new SimpleDateFormat("HH:mm:ss")
					.parse(wdfromTime.toString());
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(startTime);

			// Current Time
			java.util.Date currentTim = new SimpleDateFormat("HH:mm:ss")
					.parse(r);
			Calendar currentCalendar = Calendar.getInstance();
			currentCalendar.setTime(currentTim);

			// End Time
			java.util.Date endTime = new SimpleDateFormat("HH:mm:ss")
					.parse(wdtoTime.toString());
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(endTime);

			//Check if current time falls within from time and to time
			if (currentTim.compareTo(endTime) < 0) {
				currentCalendar.add(Calendar.DATE, 1);
				currentTim = currentCalendar.getTime();
			}

			if (startTime.compareTo(endTime) < 0) {
				startCalendar.add(Calendar.DATE, 1);
				startTime = startCalendar.getTime();
			}

			if (currentTim.before(startTime)) {
				sensorsTimeActive = false;
			} else {
				if (currentTim.after(endTime)) {
					endCalendar.add(Calendar.DATE, 1);
					endTime = endCalendar.getTime();
				}

				if (currentTim.before(endTime)) {
					sensorsTimeActive = true;
				} else {
					sensorsTimeActive = false;
				}
			}
		} else {
			//If weekend
			java.util.Date startTime = new SimpleDateFormat("HH:mm:ss")
					.parse(wendsFromTime.toString());
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(startTime);

			// Current Time
			java.util.Date currentTim = new SimpleDateFormat("HH:mm:ss")
					.parse(r);
			Calendar currentCalendar = Calendar.getInstance();
			currentCalendar.setTime(currentTim);

			// End Time
			java.util.Date endTime = new SimpleDateFormat("HH:mm:ss")
					.parse(wendsToTime.toString());
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(endTime);

			//Check if current time falls within from time and to time
			if (currentTim.compareTo(endTime) < 0) {
				currentCalendar.add(Calendar.DATE, 1);
				currentTim = currentCalendar.getTime();
			}
			if (startTime.compareTo(endTime) < 0) {
				startCalendar.add(Calendar.DATE, 1);
				startTime = startCalendar.getTime();
			}
			if (currentTim.before(startTime)) {
				sensorsTimeActive = false;
			} else {
				if (currentTim.after(endTime)) {
					endCalendar.add(Calendar.DATE, 1);
					endTime = endCalendar.getTime();
				}

				if (currentTim.before(endTime)) {
					sensorsTimeActive = true;
				} else {
					sensorsTimeActive = false;
				}
			}
		}
	}
}
