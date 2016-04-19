package project.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JPanel;

import project.ConnectDB;

/**
 * 
 * @author Kavitha Muthu, Swathi Prasad
 * This class draws bar graph based on the fire accidents and break in events 
 *
 */
public class EventsGraph extends JPanel {
	
	private Connection connection;
	private ConnectDB conn;
	private Dimension screenSize;
	private int height,width,j,nameWidth;
	private Color myBlue;
	private Map<String, Integer> fireGraphMap = new HashMap<String, Integer>();
	private Map<String, Integer> burglaryGraphMap = new HashMap<String, Integer>();
	private ResultSet rs,rs1;
	private ArrayList sectionsArray = new ArrayList();
	private ArrayList fireEventCountArray = new ArrayList();
	private ArrayList burglaryEventCountArray = new ArrayList();
	private JButton backButton;
	
	/**
	 * the constructor specifies the layout of the panel
	 * connects to the database
	 * creates the back button
	 * 
	 */
	public EventsGraph(){
	
		
		this.setLayout(null);
		this.setPreferredSize(new Dimension(1200, 700));
		this.setOpaque(true);
		
		connection = null;
     	Statement stmt = null;
     	//connects to the database
     	conn= new ConnectDB();
 		connection=conn.getConnection();
		
 		//back button
		backButton = new JButton("Back");
		backButton.setBounds(1100, 30, 60, 30);
		this.add(backButton);
		
		this.repaint();
		this.revalidate();

	}
	//back button action listener
	public void backActionListener(ActionListener listener) {
			backButton.addActionListener(listener);
		this.repaint();

	}
	/**
	 * data for the bar graphs are fetched from the database
	 * count of the fire events and burglary events are placed in two different hash maps
	 * section names are retrieved are placed in the hash maps along with the count of the events
	 */
	public void graphData(){
		
		//count of the fire accidents are calculated
		String sql="select area , count(*) as'count' FROM statsLog WHERE event='fire' GROUP By area";
		Statement stmt1=null;
		try {
			stmt1 = connection.createStatement();
		} catch (SQLException e) {
				e.printStackTrace();
		}
		try {
			ResultSet rs = stmt1.executeQuery(sql);
			while(rs.next()){
				//section and count of the fire accidents are placed in the hash map
				fireGraphMap.put(rs.getString(1), rs.getInt(2));	
			}
				} catch (SQLException e) {
			
			e.printStackTrace();
		}
		//count of the break-in events are calculated
		String sql1="select area , count(*) as'count' FROM statsLog WHERE event='burglary' GROUP By area";
		Statement stmt2=null;
		try {
			stmt2 = connection.createStatement();
		} catch (SQLException e) {
	
			e.printStackTrace();
		}
		try {
			ResultSet rs = stmt2.executeQuery(sql1);
			while(rs.next()){
				//section and count of the burglary events are placed in the hash map
				burglaryGraphMap.put(rs.getString(1), rs.getInt(2));	
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		
		//section title is retrived from the section table
		String sql3="select title FROM section";
		Statement stmt3=null;
		try {
			stmt3 = connection.createStatement();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		try {
			ResultSet rs = stmt3.executeQuery(sql3);
			//if the hash maps- fireGraphMap and burglaryGraphMap contains keys, 
			//then it is replaced with section names from the section table 
			while(rs.next()){
				if(!fireGraphMap.containsKey(rs.getString(1))){
					fireGraphMap.put(rs.getString(1), 0);
				}
				if(!burglaryGraphMap.containsKey(rs.getString(1))){
					burglaryGraphMap.put(rs.getString(1), 0);
				}
					
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
		}
		fireEventStats();
		burglaryEventStats();
	}
	/**
	 * section names alone are added to an arraylist - sectionsArray
	 */
	public void fireEventStats(){
		
		for(String sections : fireGraphMap.keySet()){
	
			sectionsArray.add(sections);

		}
		//count of the fire events are added to a array list
		for(Integer eventCount : fireGraphMap.values()){
			
			fireEventCountArray.add(eventCount);
			
		}
	}
	/**
	 * section names alone are added to an arraylist - sectionsArray
	 */
	public void burglaryEventStats(){
		
		for(String sections : burglaryGraphMap.keySet()){
			
			sectionsArray.add(sections);
		}
		//count of the burglary events are added to a array list
		for(Integer burglaryEventCount : burglaryGraphMap.values()){
			
			burglaryEventCountArray.add(burglaryEventCount);
			
		}
	}

	//paint method to draw the bar graphs
	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		setBackground(Color.WHITE);
		//title for the graph
		String title = "VISUAL REPRESENTATION OF FIRE ACCIDENTS AND BREAK-INS ";
		
		//key - indicates red - fire accidents bar graph
		//blue - burglary incidents bar graph
		g.setColor(Color.RED);
		g.fillRect(890,30 , 30,30);
		g.drawString("Fire", 930, 60);
		
		g.setColor(myBlue);
		g.fillRect(970, 30, 30,30);
		g.drawString("Burglary", 1010, 60);
		
		// longest bar in the graph is calculated
		Dimension d = getSize();
		int Width = d.width - 25;
		int Height = d.height;
		int barWidth = Width / fireEventCountArray.size();

		//max and min values are determined
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		for(int l = 0;l<fireEventCountArray.size();l++) {
			max = Math.max(max, l);
			min = Math.min(min, l);
	}
	
		// x-axis y-axis are drawn
		g.setColor(Color.RED);
		g.fillRect(25, 20, 3, 1199);
		g.fillRect(0, d.height - 45, 1370, 3);
		g.drawString("0", 5, d.height - 28);
		g.setColor(Color.GRAY);
		g.drawLine(0,  d.height - 195, d.width, d.height - 195);
		g.drawString("25",5, d.height - 198);
		g.drawLine(0,  d.height - 345, d.width, d.height - 345);
		g.drawString("50", 5, d.height - 348);
		g.drawLine(0,  d.height - 495, d.width, d.height - 495);
		g.drawString("75", 5, d.height - 498);
		g.drawLine(0,  d.height - 635, d.width, d.height - 635);
		g.drawString("100", 5, d.height - 638);
			
		
		//height of each bar is determined for fire accidents
		int width = (getWidth() / fireEventCountArray.size()) - 30;
		int x = 25;
	
		for(int k =0;k<fireEventCountArray.size();k++){
			int fireCount = (int) fireEventCountArray.get(k);
			height = (int) ((getHeight() - 80) * ((double) fireCount / max));
			//red color for the fire accidents bar 
			g.setColor(Color.RED);
			g.fillRect(x, getHeight() - height, width-80, height - 45);
			//blue color for the break-in accidents bar
			g.setColor(Color.black);
			g.drawRect(x, getHeight() - height, width-80, height - 45);
			x += (width + 33);

		}
		
		int red = 33;
		int green = 65;
		int blue = 114;
		myBlue = new Color(red,green,blue);
		
	
		//height of each bar is determined for break-in accidents
		int width1 = (getWidth() / burglaryEventCountArray.size())-30;
		int x1 = 50;
	
		for(int b =0;b<burglaryEventCountArray.size();b++){
			int burglaryCount = (int) burglaryEventCountArray.get(b);
			height = (int) ((getHeight() - 80) * ((double) burglaryCount / max));
			g.setColor(myBlue);
			g.fillRect(x1, getHeight() - height, width1-80, height - 45);
			g.setColor(Color.black);
			g.drawRect(x1, getHeight() - height, width1-80, height - 45);
			x1 += (width1 + 33);

		}
		

		//Font and format patterns for the title
		g.setColor(Color.DARK_GRAY);
		Font titleFont = new Font("Verdana", Font.BOLD, 20);
		FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
		int titleWidth = titleFontMetrics.stringWidth(title);
		int y = titleFontMetrics.getAscent();
		int titleX = (Width - titleWidth) / 2;
		g.setFont(titleFont);
		g.drawString(title, titleX, y);
		

		//Font and format pattern for country names
		Font labelFont = new Font("SansSerif", Font.BOLD, 15);
		FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

		if (fireGraphMap.keySet() == null || fireGraphMap.size() == 0)
			return;

		int top = titleFontMetrics.getHeight();
		int bottom = labelFontMetrics.getHeight();
		if (max == min)
			return;
		double scale = (Height - top - bottom) / (max - min);
		y = Height - labelFontMetrics.getDescent()-10;
		g.setFont(labelFont);

			//the section names are spaced based on the length of the string
				for (j = 0; j < fireGraphMap.size(); j++) {

					nameWidth = labelFontMetrics.stringWidth(sectionsArray.get(j)
							.toString());
					x = j * barWidth + (barWidth - nameWidth-15);
					g.drawString(sectionsArray.get(j).toString(), x, y);
					
				}
	}
}	

