package project.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
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
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import project.ConnectDB;
import project.demo.ui.BuildingLayoutPanel;
import project.entities.SubArea;

/**
 * 
 * @author Kavitha Muthu and Swathi Prasad This class configures the sensors and
 *         schedules time and date for the activation of sensors
 *
 */
public class BuildingConfigPanel extends JPanel {

	private BufferedImage fireBellIcon, burglaryBellIcon;
	private JLabel titleLabel, dateLabel, WDaysLbl, WEndsLbl, sectionLabel,
			fireBellLabel, burglaryBellLabel;
	private JTextField dateToTf, dateFromTf, WDToTf, WDFromTf, WEndsTo,
			WEndsFrom;
	private Font font3 = new Font("Verdana", Font.BOLD, 15);
	private JButton activateSensorsButton, saveSensorsButton;
	private Image fireBellScaledImg, burglaryBellScaledImg;
	private BufferedImage icon1;
	private URL url, fireBellUrl, burglaryBellUrl;
	private JCheckBox fireSensor, burglarySensor;
	private SubArea subArea;
	private Map<Integer, SubArea> map = new HashMap<Integer, SubArea>();
	private static int id;
	private Font font, font1;
	private java.sql.Date fromDate, toDate;
	private java.sql.Time wDayTo, wDayFrom, wEndTo, wEndFrom;
	private Rectangle section;

	/**
	 * The constructor specifies the layout of the panel calls method to create
	 * sensors checkboxes, connect to database, and shows the area of selection
	 */
	public BuildingConfigPanel() {

		this.setLayout(new BorderLayout());
		this.setOpaque(true);
		Dimension size = new Dimension(1200, 700);
		this.setMaximumSize(size);
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.setAlignmentX(LEFT_ALIGNMENT);
		this.setBorder(BorderFactory.createLineBorder(Color.blue));
		this.setBackground(Color.WHITE);
		this.setLayout(null);

		font = new Font("Serif", Font.BOLD, 18);
		font1 = new Font("Serif", Font.PLAIN, 15);

		// method to create sensors checkboxes, date and time fields
		createSensors();
		// method to connect to database
		createJDBCConnection();
		// method to highlight the area of selection
		areaTracker();

	}

	/**
	 * the following method defines the rendition of fire,burglary sensors
	 * checkboxes, date, time fields save button and activate button
	 */
	public void createSensors() {
		// calculates the height and width of the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();

		// color for border
		int red = 33;
		int green = 65;
		int blue = 114;
		Color myBlue = new Color(red, green, blue);

		// title label
		titleLabel = new JLabel("CONFIGURATION PANEL");
		titleLabel.setFont(font);
		titleLabel.setBounds(600, 7, 417, 50);
		titleLabel.setForeground(myBlue);
		this.add(titleLabel, BorderLayout.NORTH);

		this.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(20.0f),
				myBlue));

		// label the displays the selected area
		sectionLabel = new JLabel("");
		sectionLabel.setFont(font3);
		sectionLabel.setBounds(40, 145, 200, 50);
		sectionLabel.setForeground(Color.RED);
		this.add(sectionLabel);

		// fire sensor check box
		fireSensor = new JCheckBox("Fire Sensor");
		fireSensor.setFont(font);
		fireSensor.setBounds(35, 195, 115, 50);
		fireSensor.setForeground(Color.BLACK);
		fireSensor.setBackground(Color.WHITE);
		this.add(fireSensor);

		// fire bell icon is displayed using the following label
		fetchfireBell();
		fireBellLabel = new JLabel(new ImageIcon(fireBellScaledImg));
		fireBellLabel.setBounds(150, 205, 25, 25);
		this.add(fireBellLabel);

		// burglary sensor check box
		burglarySensor = new JCheckBox("Burglary Sensor");
		burglarySensor.setFont(font);
		burglarySensor.setBounds(35, 245, 150, 50);
		burglarySensor.setForeground(Color.BLACK);
		burglarySensor.setBackground(Color.WHITE);
		this.add(burglarySensor);

		// burglary bell icon is displayed using the following label
		fetchBurglaryBell();
		burglaryBellLabel = new JLabel(new ImageIcon(burglaryBellScaledImg));
		burglaryBellLabel.setBounds(189, 254, 25, 25);
		this.add(burglaryBellLabel);

		// date label
		dateLabel = new JLabel("Date: ");
		dateLabel.setFont(font);
		dateLabel.setBounds(1106, 153, 150, 50);
		dateLabel.setForeground(Color.BLACK);
		dateLabel.setBackground(Color.WHITE);
		this.add(dateLabel);

		// from date text field
		dateFromTf = new JTextField();
		dateFromTf.setFont(font1);
		dateFromTf.setBounds(1106, 193, 75, 30);
		dateFromTf.setForeground(Color.BLACK);
		dateFromTf.setBackground(Color.WHITE);
		this.add(dateFromTf);

		// to date text field
		dateToTf = new JTextField();
		dateToTf.setFont(font1);
		dateToTf.setBounds(1186, 193, 75, 30);
		dateToTf.setForeground(Color.BLACK);
		dateToTf.setBackground(Color.WHITE);
		this.add(dateToTf);

		// Weekdays label
		WDaysLbl = new JLabel("Time: Weekdays ");
		WDaysLbl.setFont(font);
		WDaysLbl.setBounds(1106, 243, 150, 50);
		WDaysLbl.setForeground(Color.BLACK);
		WDaysLbl.setBackground(Color.WHITE);
		this.add(WDaysLbl);

		// util.date is converted to sql.date for compatibility with database
		java.util.Date date = new java.util.Date();
		long t = date.getTime();
		java.sql.Time endTime = new java.sql.Time(t);

		// From time during week days text field
		WDFromTf = new JTextField(endTime.toString());
		WDFromTf.setFont(font1);
		WDFromTf.setBounds(1106, 293, 70, 30);
		WDFromTf.setForeground(Color.BLACK);
		WDFromTf.setBackground(Color.WHITE);
		this.add(WDFromTf);

		// To time during week days text field
		WDToTf = new JTextField(endTime.toString());
		WDToTf.setFont(font1);
		WDToTf.setBounds(1186, 293, 70, 30);
		WDToTf.setForeground(Color.BLACK);
		WDToTf.setBackground(Color.WHITE);
		this.add(WDToTf);

		// from time during week ends text field
		WEndsLbl = new JLabel("Time: Weekends");
		WEndsLbl.setFont(font);
		WEndsLbl.setBounds(1106, 343, 150, 50);
		WEndsLbl.setForeground(Color.BLACK);
		WEndsLbl.setBackground(Color.WHITE);
		this.add(WEndsLbl);

		// from time during week ends text field
		WEndsFrom = new JTextField(endTime.toString());
		WEndsFrom.setFont(font1);
		WEndsFrom.setBounds(1106, 393, 70, 30);
		WEndsFrom.setForeground(Color.BLACK);
		WEndsFrom.setBackground(Color.WHITE);
		this.add(WEndsFrom);

		// to time during week ends text field
		WEndsTo = new JTextField(endTime.toString());
		WEndsTo.setFont(font1);
		WEndsTo.setBounds(1186, 393, 70, 30);
		WEndsTo.setForeground(Color.BLACK);
		WEndsTo.setBackground(Color.WHITE);
		this.add(WEndsTo);

		// save sensors selection button
		saveSensorsButton = new JButton("Save");
		saveSensorsButton.setFocusPainted(false);
		saveSensorsButton.setFont(font1);
		saveSensorsButton.setBounds(35, 295, 100, 30);
		saveSensorsButton.setForeground(Color.BLACK);
		this.add(saveSensorsButton);

		// activate sensors button to activate the schedule of the sensors
		// configured
		activateSensorsButton = new JButton("Activate");
		activateSensorsButton.setFocusPainted(false);
		activateSensorsButton.setFont(font1);
		activateSensorsButton.setBounds(1105, 443, 100, 30);
		activateSensorsButton.setForeground(Color.BLACK);
		this.add(activateSensorsButton);

	}

	/**
	 * method to fetch the fire bell image, file path is read to a bufferedImage
	 */

	public void fetchfireBell() {
		fireBellUrl = ClassLoader.getSystemClassLoader().getResource(
				"res/fireBell.png");
		// file path is read to the buffered image
		try {
			fireBellIcon = ImageIO.read(fireBellUrl);
		} catch (IOException e) {

			e.printStackTrace();
		}
		// buffered image is scaled
		fireBellScaledImg = fireBellIcon.getScaledInstance(25, 25,
				java.awt.Image.SCALE_SMOOTH);
	}

	/**
	 * method to fetch the burglary bell image, file path is read to a
	 * bufferedImage
	 */
	public void fetchBurglaryBell() {
		burglaryBellUrl = ClassLoader.getSystemClassLoader().getResource(
				"res/bell1.png");
		// file path is read to the buffered image
		try {
			burglaryBellIcon = ImageIO.read(burglaryBellUrl);
		} catch (IOException e) {

			e.printStackTrace();
		}
		// buffered image is scaled
		burglaryBellScaledImg = burglaryBellIcon.getScaledInstance(25, 25,
				java.awt.Image.SCALE_SMOOTH);
	}

	/**
	 * paint method to draw the floor plan image
	 */

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		// checks for the buffered image
		if (icon1 != null) {
			// draws the floor plan at the respective x and y axis
			g2d.drawImage(icon1, 214, 61, null);

			g2d.setColor(Color.RED);
			g2d.setStroke(new BasicStroke(5));

			if (section != null) {

				g2d.draw(section);

			}

		}
		// iterates through the key set and draws the fire bell icon and
		// burglary bell icon on
		// the rooms that are configured with fire ,burglary sensors
		for (Integer idVal : map.keySet()) {
			// if the id value of the room has value 1 for fire sensor then the
			// fire bell is drawn on the room
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
			// if the id value of the room has value 1 for burglary sensor then
			// the burglary bell is drawn on the room
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
	class MouseEventAdapterA extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {

			// x and y points on the window is stored in the following integer
			// values.
			int x = e.getX();
			int y = e.getY();

			// selected area is highlighted by drawing a rectangle around it
			// areaLabel flashes the name of the area selected
			if ((x >= 230 && x <= 329) && (y >= 155 && y <= 295)) {

				section = new Rectangle(230, 155, 99, 140);
				repaint();
				sectionLabel.setText("TOILET 1");

				id = 1;

			} else if ((x >= 319 && x <= 480) && (y >= 225 && y <= 507)) {
				section = new Rectangle(319, 225, 161, 282);
				repaint();

				sectionLabel.setText("BEDROOM 1");
				sectionLabel.revalidate();

				id = 2;

			} else if ((x >= 478 && x <= 651) && (y >= 226 && y <= 437)) {
				section = new Rectangle(478, 226, 173, 211);
				repaint();

				sectionLabel.setText("LIVING ROOM");
				sectionLabel.revalidate();

				id = 3;

			} else if ((x >= 478 && x <= 686) && (y >= 450 && y <= 581)) {
				section = new Rectangle(478, 450, 208, 131);
				repaint();

				sectionLabel.setText("DINING ROOM");
				sectionLabel.revalidate();

				id = 4;

			} else if ((x >= 654 && x <= 779) && (y >= 227 && y <= 405)) {
				section = new Rectangle(654, 227, 125, 178);
				repaint();

				sectionLabel.setText("KITCHEN");
				sectionLabel.revalidate();

				id = 5;

			} else if ((x >= 776 && x <= 929) && (y >= 225 && y <= 404)) {
				section = new Rectangle(776, 225, 153, 179);
				repaint();

				sectionLabel.setText("BEDROOM 2");
				sectionLabel.revalidate();

				id = 6;

			} else if ((x >= 688 && x <= 782) && (y >= 460 && y <= 589)) {
				section = new Rectangle(688, 460, 94, 129);
				repaint();

				sectionLabel.setText("TOILET 2");
				sectionLabel.revalidate();

				id = 7;
			} else if ((x >= 929 && x <= 1094) && (y >= 226 && y <= 486)) {
				section = new Rectangle(929, 224, 165, 262);
				repaint();

				sectionLabel.setText("MASTER BEDROOM");

				sectionLabel.revalidate();

				id = 8;

			} else if ((x >= 958 && x <= 1095) && (y >= 489 && y <= 590)) {
				section = new Rectangle(958, 489, 137, 101);
				repaint();

				sectionLabel.setText("TOILET 3");
				sectionLabel.revalidate();

				id = 9;

			} else if ((x >= 346 && x <= 1095) && (y >= 138 && y <= 223)) {
				section = new Rectangle(346, 138, 748, 85);
				repaint();

				sectionLabel.setText("BALCONY");
				sectionLabel.revalidate();

				id = 10;
			} else {
				section = null;
				sectionLabel.setText("");

				id = 0;
			}

		}
	}

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		BuildingConfigPanel.id = id;
	}

	public Map<Integer, SubArea> getMap() {
		return map;
	}

	public void setMap(Map<Integer, SubArea> map) {
		this.map = map;
	}

	/**
	 * connects to the database and fetches the values for id, fire sensor and
	 * burglary sensor the values are placed in a hash map
	 */
	public void createJDBCConnection() {
		Connection connection = null;
		Statement stmt = null;

		ConnectDB conn = new ConnectDB();
		connection = conn.getConnection();

		try {

			stmt = connection.createStatement();
			String sql = "SELECT * FROM section";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				// Retrieve by column name
				subArea = new SubArea();
				subArea.setId(rs.getInt("id"));
				subArea.setFireSensor(rs.getBoolean("fireSensor"));
				subArea.setBurglarySensor(rs.getBoolean("burglarySensor"));
				getMap().put(rs.getInt("id"), subArea);

			}

			// from date, to date,weekdays - from time ,to time, weekends - from
			// time, to time values
			// are fetched and displayed in the respective text fields
			String sql4 = "SELECT fromDate, toDate, wdfromTime, wdtoTime, wendsFromTime, wendsToTime FROM systemConfig";
			ResultSet rs4 = stmt.executeQuery(sql4);

			if (rs4.next()) {

				//Fetch the values from database
				fromDate = rs4.getDate(1);
				toDate = rs4.getDate(2);
				wDayFrom = rs4.getTime(3);
				wDayTo = rs4.getTime(4);
				wEndFrom = rs4.getTime(5);
				wEndTo = rs4.getTime(6);

				//Fetched value is populated in the respective text fields
				SimpleDateFormat dFormat = new SimpleDateFormat("dd.MM.yyyy");
				dateFromTf.setText(dFormat.format(fromDate));
				dateToTf.setText(dFormat.format(toDate));
				WDFromTf.setText(wDayFrom.toString());
				WDToTf.setText(wDayTo.toString());
				WEndsFrom.setText(wEndFrom.toString());
				WEndsTo.setText(wEndTo.toString());
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * method to fetch the url for the floor map image
	 */
	public void areaTracker() {

		url = ClassLoader.getSystemClassLoader().getResource(
				"res/floor_plan.jpg");

		// file path is read to the buffered image
		try {
			icon1 = ImageIO.read(url);
		} catch (IOException e) {

			e.printStackTrace();
		}
		// mouse listener (to display the selected area) for the mouse events

		addMouseListener(new MouseEventAdapterA());
		setVisible(true);
	}

	/**
	 * activate button action listener
	 * 
	 * @param listener
	 */
	public void activateSensorsActionListener(ActionListener listener) {
		activateSensorsButton.addActionListener(listener);

	}

	/**
	 * save button action listener
	 * 
	 * @param listener
	 */

	public void saveSensorsActionListener(ActionListener listener) {
		saveSensorsButton.addActionListener(listener);

	}

	public void confirmSensors() {

		SubArea selectedArea = getMap().get(BuildingConfigPanel.getId());

		if (!fireSensor.isSelected()) {
			selectedArea.setFireSensor(false);
		} else if (fireSensor.isSelected()) {
			selectedArea.setFireSensor(true);

		}
		if (!burglarySensor.isSelected()) {
			selectedArea.setBurglarySensor(false);
		} else if (burglarySensor.isSelected()) {
			selectedArea.setBurglarySensor(true);
		}

		// clear checkboxes
		fireSensor.setSelected(false);
		burglarySensor.setSelected(false);

	}

	/**
	 * Method to save date and time from text fields to database
	 */
	public void saveDate() {
		//Get JDBC connection
		Connection connection = null;
		ConnectDB conn = new ConnectDB();
		connection = conn.getConnection();

		try {
			//Parse the date and time from text field to save it into Database
			SimpleDateFormat dfFormat = new SimpleDateFormat("dd.MM.yyyy");

			Date fromDate = dfFormat.parse(dateFromTf.getText());
			long ftime = fromDate.getTime();
			java.sql.Date fdate = new java.sql.Date(ftime);

			Date toDate = dfFormat.parse(dateToTf.getText());
			long ttime = toDate.getTime();
			java.sql.Date tdate = new java.sql.Date(ttime);
			
			//Calculate the number of days for which the system is active
			int numOfDays = (int) ((ttime - ftime) / (1000 * 60 * 60 * 24));
			System.out.println("Difference days b/w dates" + numOfDays);

			String time1 = WDFromTf.getText(), time2 = WDToTf.getText(), time3 = WEndsFrom
					.getText(), time4 = WEndsTo.getText();

			Date date1 = null, date2 = null, date3 = null, date4 = null;

			try {
				date1 = new SimpleDateFormat("HH:mm:ss").parse(time1);
				date2 = new SimpleDateFormat("HH:mm:ss").parse(time2);
				date3 = new SimpleDateFormat("HH:mm:ss").parse(time3);
				date4 = new SimpleDateFormat("HH:mm:ss").parse(time4);
			} catch (ParseException e) {

			}

			Statement selectStmt = connection.createStatement();
			String sql1 = "SELECT * FROM systemConfig";
			ResultSet rs1 = selectStmt.executeQuery(sql1);

			if (rs1.next() == false) {
				
				//Insert the calculated values to database if the user is registered
				String query = "INSERT INTO `systemConfig`(fromDate,toDate,wdfromTime,wdtoTime,wendsFromTime,wendsToTime) VALUES (?,?,?,?,?,?)";
				PreparedStatement stmt = connection.prepareStatement(query);

				stmt.setDate(1, fdate);
				stmt.setDate(2, tdate);
				stmt.setTime(3, new Time(date1.getTime()));
				stmt.setTime(4, new Time(date2.getTime()));
				stmt.setTime(5, new Time(date3.getTime()));
				stmt.setTime(6, new Time(date4.getTime()));
				stmt.executeUpdate();
			} else {
				//Insert the calculated values to database if the user is new user
				String query = "UPDATE systemConfig SET fromDate = ? , toDate = ? ,wdfromTime= ?, wdtoTime= ? , wendsFromTime= ?, wendsToTime= ?";
				PreparedStatement stmt = connection.prepareStatement(query);

				stmt.setDate(1, fdate);
				stmt.setDate(2, tdate);
				stmt.setTime(3, new Time(date1.getTime()));
				stmt.setTime(4, new Time(date2.getTime()));
				stmt.setTime(5, new Time(date3.getTime()));
				stmt.setTime(6, new Time(date4.getTime()));
				stmt.executeUpdate();
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Method saves the selected sensors on selected areas into database since the system is persistant.
	 * 
	 * @throws Exception
	 */
	public void saveSensorsDB() throws Exception {

		//Get JDBC connection
		Connection connection = null;
		Statement stmt = null;
		ConnectDB conn = new ConnectDB();
		connection = conn.getConnection();

		stmt = connection.createStatement();
		for (int key : getMap().keySet()) {
			SubArea value = getMap().get(key);
			int fireValue = (value.isFireSensor()) ? 1 : 0;
			int burglaryValue = (value.isBurglarySensor()) ? 1 : 0;
			//Save Fire and Burglary sensors
			String sql = "UPDATE section SET fireSensor=" + fireValue
					+ ", burglarySensor=" + burglaryValue + " WHERE id=" + key;
			stmt.executeUpdate(sql);
		}

	}

}
