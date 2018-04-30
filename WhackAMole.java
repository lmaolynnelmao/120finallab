import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class WhackAMole extends JFrame implements ActionListener{
	
	public static ImageIcon appIcon = new ImageIcon("icon.jpg"); 	// The icon of the game
	
	int size  = 7;	// number of rows and columns in the mole array
	int square = size * size;
	
	GameThread game; 	// The game thread that should be triggered by the event of player clicking the btnGameStart button
	ButtonGroup radioButtonGroup = new ButtonGroup();		// A button group to organize the radio buttons
	public boolean terminated = false;

	//JComponents objects are declared here as instance variables, except JPanel and JLabel instances
	Container contentPane = this.getContentPane(); // Grab the content pane from the frame
	Mole[] moleArray = new Mole[square]; 	// The array of mole buttons that are used to display the moles and interact with the player	
	JComboBox comboTimeToLive = new JComboBox();		// A combo box from which the player selects the life span (in milliseconds) for the moles 
	JComboBox comboDeadTime = new JComboBox();	// A combo box from which the user selects the time duration (in milliseconds) of dead mole(s) staying above ground
	JRadioButton rBtnNumMole1 = new JRadioButton("1");	// Only one mole appears at a time
	JRadioButton rBtnNumMole2 = new JRadioButton("2"); // Maximum of two moles appear at a time
	JRadioButton rBtnNumMole3 = new JRadioButton("3"); // Maximum of three moles appear at a time
	JComboBox comboGameTime = new JComboBox(); 	// A combo box from which the player selects the game duration (in seconds)
	JProgressBar progBar = new JProgressBar(0,100); 	// A progress bar that indicate the progress (percentage) of the game 
	JButton btnGameStart = new JButton("Start whacking!"); 	// A button to trigger the game
	JButton btnTerminate = new JButton("Abort"); 	// A button to terminate the game
	JTextField tFieldNumHits = new JTextField(); 	// TextField shows the number of moles that have been struck by the player 
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static void main(String[] args)
	{
		WhackAMole game = new WhackAMole();
	}
	
	WhackAMole()
	{
		
		/*
		 * The following is a small section of the constructor where 
		 * the components, corresponding layout managers, and labels (with wordings) are added and arranged
		 * in the game window.
		 * You may refer to the lab instructions or the jar file to arrange the components
		 * All the necessary JComponent instances, except JPanel and JLabel instances, have been declared as instance variables above
		 */
		
		//JFrame this = new JFrame();
		this.setTitle("Welcome to the Whack-A-Mole Game Created by Lynne Thompson.");

		this.setIconImage(appIcon.getImage());

		contentPane.setLayout(new BorderLayout(0, 35));

		JPanel controlPanel = new JPanel();
		JPanel gamePanel = new JPanel();

		contentPane.add(controlPanel, BorderLayout.NORTH);
		contentPane.add(gamePanel, BorderLayout.CENTER);
		
		controlPanel.setLayout(new GridLayout(4, 1, 0, 10));
		
		JPanel timeConfigPanel = new JPanel();
		JPanel numConfigPanel = new JPanel();
		JPanel progressPanel = new JPanel();
		JPanel statusPanel = new JPanel();
		
		controlPanel.add(timeConfigPanel);
		controlPanel.add(numConfigPanel);
		controlPanel.add(progressPanel);
		controlPanel.add(statusPanel);
		
		timeConfigPanel.setLayout(new GridLayout(1, 4));
		
		JPanel[] panelTimeControl=new JPanel[4];	
		for(int i = 0; i < 4; i++)
		{
			panelTimeControl[i]=new JPanel();
			timeConfigPanel.add(panelTimeControl[i]);
		}
		
		panelTimeControl[0].add(new JLabel("Mole life span (milliseconds): "));
		panelTimeControl[2].add(new JLabel("Mole dead time (milliseconds): "));
		
		panelTimeControl[1].add(comboTimeToLive);
		panelTimeControl[3].add(comboDeadTime);
		
		numConfigPanel.setLayout(new GridLayout(1, 4));
		
		JPanel[] panelNumControl=new JPanel[4];	
		for(int i = 0; i < 4; i++)
		{
			panelNumControl[i]=new JPanel();
			numConfigPanel.add(panelNumControl[i]);
		}
		
		panelNumControl[0].add(new JLabel("Maximum number of moles: "));
		
		JRadioButton rBtnMole1 = new JRadioButton("1");
		panelNumControl[1].add(rBtnMole1);
		
		JRadioButton rBtnMole2 = new JRadioButton("2");
		panelNumControl[2].add(rBtnMole2);
		
		JRadioButton rBtnMole3 = new JRadioButton("3");
		panelNumControl[3].add(rBtnMole3);
		
		progressPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		progressPanel.add(new JLabel("How long(seconds) do you want to play?"));
		
		progressPanel.add(comboGameTime);
		
		progressPanel.add(progBar);
		
		statusPanel.setLayout(new GridLayout());
		
		statusPanel.add(btnGameStart);
		statusPanel.add(btnTerminate);
		
		JPanel hitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel molesPanel = new JPanel(new FlowLayout());
		
		statusPanel.add(hitPanel);
		
		hitPanel.add(new JLabel("You have hit "));
		statusPanel.add(tFieldNumHits);
		statusPanel.add(molesPanel);
		molesPanel.add(new JLabel("moles."));
		
		gamePanel.setLayout(new GridLayout(7, 7));		

		for(int i = 0; i < moleArray.length; i++)
		{
			moleArray[i] = new Mole();
			gamePanel.add(moleArray[i]);
		}
		
		/*
		 * The following small section is for adding action listeners to the components that 
		 * need to listen to real-time actions
		 */

		btnGameStart.addActionListener(this);
		btnTerminate.addActionListener(this);
		
		for(int i = 0; i < moleArray.length; i++)
		{
			moleArray[i].addActionListener(this);
		}
		
		/*
		 * The following small section is for adding items to the combo boxes
		 * You may assume the players know the units of the item values
		 */
		
		for(int i = 0; i <= 8; i++)
		{
			comboTimeToLive.addItem(i*100);
		}
		
		comboTimeToLive.setSelectedIndex(5);
		
		for(int i = 2; i <= 6; i++)
		{
			comboDeadTime.addItem(i*100);
		}
		
		comboDeadTime.setSelectedIndex(1);
		
		for(int i = 6; i <= 20; i+=2)
		{
			comboGameTime.addItem(i);
		}
		
		comboGameTime.setSelectedIndex(1);

		/*
		 * Other configurations are done here
		 */

		tFieldNumHits.setEditable(false);
		tFieldNumHits.setText("0");
		
		btnTerminate.setEnabled(false);
		
		radioButtonGroup.add(rBtnMole1);
		radioButtonGroup.add(rBtnMole2);
		radioButtonGroup.add(rBtnMole3);
		
		progBar.setValue(0);
		
		this.setResizable(false);
		this.setSize(850, 700);
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

		// TODO Other statements that are needed
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		/*
		 * This method handles different user behaviors
		 */
		// TODO Complete the method here


	}
	
	// If the game time runs out, invoke this method
	// Uncomment the following method, and replace the count with the player's score (1 point for 1 mole)
//	public void timeUpDialog(){
//		JOptionPane.showMessageDialog(this, "Uh-oh! Time is up!\n Your score is " + count + "!");
//	}
	
	// If the game is terminated by the player, invoke this method
	// Uncomment this following method, and replace the count with the player's score (1 point for 1 mole)
//	public void terminateDialog(){
//		JOptionPane.showMessageDialog(this, "Game stopped by player.\n Your score is " + count +"!");
//	}
	
	/*
	 * The following is the definition of the inner class named GameThread that extends the ThreadClass
	 * You may treat this class as a regular class
	 * The variables of outer class are accessible in the GameThread
	 * The purpose of this class is to start an additional thread for one game
	 * The thread is started when the player hits the "Let's start whacking!" button
	 * The thread is stopped when the player hits the "Abort" button
	 */
	// TODO complete the public void run() method, you may declare new variables
	// You can directly access the methods and variables that are class/instance variables/methods of the WhackAMole class.
	class GameThread extends Thread{

		public void run(){
			
		}
	}

}
