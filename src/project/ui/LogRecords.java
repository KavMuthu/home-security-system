package project.ui;
/**
 * 
 * @author Kavitha Muthu, Swathi Prasad
 *
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import project.ConnectDB;

public class LogRecords  extends JPanel{
	private JLabel titleLabel;
	private Font font=new Font("Verdana", Font.BOLD, 18);
	private JButton backButton;
	private JTable table;
	private Connection connection;
	private Statement stmt,stmt1;
	private ConnectDB conn;
	private Color myBlue;
	private Dimension screenSize;
	private int height,width;

	/**
	 * This Class displays the list of log records
	 */
	public LogRecords(){	
		//Set the syling attributes for the Panel
		this.setLayout(new BorderLayout());
		int red = 33;
		int green = 65;
		int blue = 114;
		myBlue = new Color(red,green,blue);
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) screenSize.getWidth();
		height = (int) screenSize.getHeight();
		
		//Set title for the page
		JPanel titlePanel = new JPanel();
		titleLabel = new JLabel("ACTIVITY LOG");
		titleLabel.setFont(font);
		titleLabel.setForeground(Color.WHITE);
		titlePanel.add(titleLabel);
		titlePanel.setBackground(myBlue);
		this.add(titlePanel, BorderLayout.NORTH);

		//Get JDBC connection
		connection = null;
     	stmt = null;
     	conn= new ConnectDB();
 		connection=conn.getConnection();
	     
 		//Back button to go back to generate bill page
	 	JPanel backPanel = new JPanel();
	    backButton = new JButton("Back");
	    backButton.setBounds(1258, 650, 60, 30);
	    backPanel.setBackground(myBlue);
	    backPanel.add(backButton);
	     
	    this.add(backPanel,BorderLayout.SOUTH);
	}
	
	//Create JDBC connection statement
	public void getJDBCData(){
		try {
			stmt1 = connection.createStatement();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		// Query to select log record fields from database
		String sql1= "SELECT area, event, date, time, callTime, responseTime FROM statsLog";
		ResultSet rs1=null;
		try {
			rs1 = stmt1.executeQuery(sql1);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	
	    // It creates and displays the table
	     try {
			table = new JTable(buildTableModel(rs1));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	    TableColumn column = null;
	    for (int i = 0; i < 6; i++) {
	        column = table.getColumnModel().getColumn(i);
	        column.setPreferredWidth(50);
	     }
	 	
	    //Set styling for table
	    table.setBorder(BorderFactory.createLineBorder(Color.black));
	    table.setGridColor(Color.BLUE);
	    JScrollPane tableSP = new JScrollPane(table);
	    tableSP.setPreferredSize( new Dimension(1000,1000) );
	    this.add(tableSP, BorderLayout.CENTER);
	    table.setPreferredSize(new Dimension(1000,1000));
	}
	
	/**
	 * 
	 * @param rs	ResultSet of the queried statement
	 * @return		Returns table model with header set for the table and the row data
	 * @throws SQLException
	 */
	public static DefaultTableModel buildTableModel(ResultSet rs)
	        throws SQLException {

	    ResultSetMetaData metaData = rs.getMetaData();
	    // add header of the table	 		
	    int columnCount = metaData.getColumnCount();	    
	    Vector<String> columnNames = new Vector<String>();
	    columnNames.addElement("Area");
	    columnNames.addElement("Event");
	    columnNames.addElement("Date");
	    columnNames.addElement("Time");
	    columnNames.addElement("Call Time");
	    columnNames.addElement("Response Time");
	    
	    //Retrieve data from database and fill the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	    	//get data from database and store it in Vector object
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }
	    return new DefaultTableModel(data, columnNames);
	}

	/**
	 * 
	 * Register Action listener for back button
	 */
	public void backActionListener(ActionListener listener) {
		backButton.addActionListener(listener);
	}
}
