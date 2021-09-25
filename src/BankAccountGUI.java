import java.awt.EventQueue;
import java.util.Random;
import javax.swing.JFrame;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Color;
import java.awt.Button;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.JCheckBox;
import javax.swing.JPasswordField;
import java.lang.Integer;
import java.awt.TextArea;
import java.awt.Label;
import java.awt.ScrollPane;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
public class BankAccountGUI {

	final int MAX_NUM_ACCOUNT = 100;
	int numAccounts = 0;
	
	CardLayout cards = new CardLayout(0, 0);
	BankAccount b1 =  new BankAccount(1000,	"Ripdaman",	"Singh", "Malhans",	123445, "MANN");
	BankAccount b2 = new BankAccount(1000, "Sam", "Samuel", "Samson", 123456, "HIBS");
	BankAccount[] account = new BankAccount[MAX_NUM_ACCOUNT];
	
	BankAccount b = new BankAccount(0,"","","",0,"");
	BankAccount other = null;
	private JFrame frame;
	private JTextField acctNumTF;
	private JTextField signUpfNameTF;
	private JTextField signUpmNameTF;
	private JTextField signUplNameTF;
	private JPasswordField loginPasswordPF;
	private JTextField tToOtherTF;
	private JTextField otherAcctNumTF;
	private JTextField withdrawAmountTF;
	private JTextField depositAmountTF;
	private JTextField transferToAmountTF;
	private JLabel lblCurrentFunds;
	private JTextField transferFromTF;
	private JPasswordField otherPswdPF;
	private JTextArea activityLogTA;
	private JLabel lblActualAcctNum;
	private JCheckBox chckbxIHaveRead;
	private JLabel lblCurrentBalance;
	private JPasswordField passwordPF;
	private JPasswordField againPasswordPF;
	

	boolean addAccount (BankAccount acct)
	{
		if (numAccounts <= MAX_NUM_ACCOUNT)
		{
			account[numAccounts] = acct;
			numAccounts++;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	boolean numberCheck (String l)
	{
		try {
		     Integer.parseInt(l);
		     System.out.println("An integer");
		     
		     return true;
		}
		catch (NumberFormatException e) {
		     
			return false;
		}
	}
	
	boolean getAccount(int acctNum)
	{
		BAHttpURLConnection http = new BAHttpURLConnection();

		try {
				http.sendPost(BAHttpURLConnection.URL+"getUserInfo.php?", 
						   "acctNum=" + acctNum);

			
			if (http.response != null)
			{
				// retrieves entire row
				System.out.println("User Info Successfully retrieved");
				parseResult(http.response.toString());
				return true;
			}
			else
			{
				System.out.println("No User Info Retrieved!");
				return false;
			}
		} 
		catch (Exception e) { e.printStackTrace(); }
		JOptionPane.showMessageDialog(frame,
		        "Error Retrieving User Info",
		        "SERVER ERROR",
		        JOptionPane.ERROR_MESSAGE);
		
		return false;
	}

	void parseResult(String result)
	{	
	 	System.out.println(result);
	 	
	 	BankAccount temp = new BankAccount(0,"","","",0,"");
		try 
		{
			if (result!=null && !result.equals("[]") && !result.equals("") && result.startsWith("{"))
			{
				
				JSONObject obj = new JSONObject(result);
				b.setFName(obj.getString("first_name"));
				b.setLName(obj.getString("last_name"));
				b.setMName(obj.getString("middle_name"));
				b.setBalance(obj.getDouble("balance"));
				b.setAcctNum(obj.getInt("acct_num"));
				b.display();	
				System.out.println("");
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	boolean login()
	{
		BAHttpURLConnection http = new BAHttpURLConnection();
		try {
			http.sendPost(BAHttpURLConnection.URL+"login.php?", 
					   "acctNum=" + acctNumTF.getText() + 
					   "&password=" + String.valueOf(loginPasswordPF.getPassword()));
			System.out.println(http.response.toString());	
			if (http.response != null)
			{
				if (http.response.toString().trim().equals("Login successful."))
				{
					// handle success!
					return true;
				}
				else if (http.response.toString().trim().equals("Login failed."))
				{
					// handle login failed!
					JOptionPane.showMessageDialog (frame, "Incorrect account number/password.", "Please try again.", JOptionPane.ERROR_MESSAGE);
					
				}
				else
				{
					// handle php error
					System.out.println("Php Error!\n"+http.response.toString());
					
				}
			
			}
			
		} 
		catch (Exception e) 
		{ 
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Server Unreachable.\nPlease try again after checking your Internet connection.", "Network Error", JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}
	
	void updateBalance(double balance)
	{
		BAHttpURLConnection http = new BAHttpURLConnection();
			
		try {		
			http.sendPost(BAHttpURLConnection.URL+"updateBalance.php?", 
					   "acctNum=" + acctNumTF.getText() +
					   "&balance=" + balance);
					   
			if (http.response != null)
			{
				System.out.println("Balance Updated");
				System.out.println(http.response.toString());
			}
		} 
		catch (Exception e) { e.printStackTrace(); }
	}
	
	boolean letterCheck (String v)
	{
		
		if(v.length() < 3)
		{
			JOptionPane.showMessageDialog(frame,
			        "Please fill name that is more than 3 letter long",
			        "NOT ENOUGH LETTERS",
			        JOptionPane.ERROR_MESSAGE);
			return true;
		}
		else
		{
			return false;
		}
		
	
		
	}

	boolean varify(String f)
	{
	
		if(f.equals(""))
		{
			JOptionPane.showMessageDialog(frame,
			        "You have not filled out all necessary requirements. Please Try Again",
			        "SIGN UP UNSUCCESSFUL",
			        JOptionPane.ERROR_MESSAGE);
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	BankAccount searchAccount (int acctNum)
	{
		for(int i = 0; i < numAccounts; i++) 
		{
			if(acctNum == account[i].getAccountNum())
			{
				return account[i];
			}

		}
		return null;
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BankAccountGUI window = new BankAccountGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BankAccountGUI() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 712, 459);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(cards);
		
		
	
		addAccount (b1);
		addAccount (b2);

		
		JPanel startPanel = new JPanel();
		startPanel.setBackground(new Color(51, 153, 204));
		frame.getContentPane().add(startPanel, "START PANEL");
		startPanel.setLayout(null);
		
		JLabel lblWelcomeToHib = new JLabel("Welcome To H.I.B. - The Fastest and Most Trusted Bank in the World");
		lblWelcomeToHib.setBackground(new Color(255, 255, 255));
		lblWelcomeToHib.setForeground(new Color(204, 0, 0));
		lblWelcomeToHib.setFont(new Font("Dialog", Font.BOLD, 20));
		lblWelcomeToHib.setBounds(26, 50, 660, 26);
		startPanel.add(lblWelcomeToHib);
		
		JPanel loginPanel = new JPanel();
		loginPanel.setBackground(new Color(51, 153, 204));
		frame.getContentPane().add(loginPanel, "LOGIN PANEL");
		loginPanel.setLayout(null);
		
		JButton createAcctBtn = new JButton("CREATE ACCOUNT");
		createAcctBtn.setBackground(new Color(255, 255, 255));
		createAcctBtn.setForeground(new Color(51, 0, 204));
		createAcctBtn.setFont(new Font("Tahoma", Font.BOLD, 13));
		createAcctBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cards.show(frame.getContentPane(), "SIGN UP PANEL");
			}
		});
		createAcctBtn.setBounds(59, 293, 156, 50);
		startPanel.add(createAcctBtn);
		
		
		
		Button loginBtn = new Button("LOGIN");
		loginBtn.setBackground(new Color(255, 255, 255));
		loginBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		loginBtn.setForeground(new Color(51, 0, 204));
		loginBtn.setBounds(484, 293, 150, 50);
		startPanel.add(loginBtn);
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cards.show(frame.getContentPane(), "LOGIN PANEL");
			}
		});
		
		JLabel lblNewLabel_1 = new JLabel("SNAP");
		lblNewLabel_1.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 40));
		lblNewLabel_1.setForeground(new Color(255, 153, 102));
		lblNewLabel_1.setBounds(59, 114, 140, 67);
		startPanel.add(lblNewLabel_1);
		
		JLabel lblClick = new JLabel("CLICK");
		lblClick.setHorizontalAlignment(SwingConstants.CENTER);
		lblClick.setForeground(new Color(51, 204, 0));
		lblClick.setFont(new Font("Charlemagne Std", Font.BOLD | Font.ITALIC, 40));
		lblClick.setBounds(261, 111, 156, 67);
		startPanel.add(lblClick);
		
		JLabel lblDone = new JLabel("DONE");
		lblDone.setFont(new Font("Brush Script Std", Font.BOLD | Font.ITALIC, 40));
		lblDone.setBounds(491, 119, 143, 67);
		startPanel.add(lblDone);
		
		
		
		JButton acctLogin = new JButton("LOGIN");
		acctLogin.setForeground(new Color(51, 0, 204));
		acctLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
		acctLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//TODO
				if(login())
				{
					if (getAccount(Integer.parseInt(acctNumTF.getText())))
					{
					cards.show(frame.getContentPane(), "MAIN PANEL");
					}
				}
			}
		});
  
		acctLogin.setBounds(278, 271, 150, 51);
		loginPanel.add(acctLogin);
		
		loginPasswordPF = new JPasswordField();
		loginPasswordPF.setBounds(292, 165, 120, 28);
		loginPanel.add(loginPasswordPF);
		
		JLabel lblPassword = new JLabel("PASSWORD:");
		lblPassword.setForeground(new Color(255, 153, 51));
		lblPassword.setFont(new Font("Verdana", Font.BOLD, 14));
		lblPassword.setBounds(168, 163, 112, 28);
		loginPanel.add(lblPassword);
		
		acctNumTF = new JTextField();
		acctNumTF.setColumns(10);
		acctNumTF.setBounds(292, 107, 120, 28);
		loginPanel.add(acctNumTF);
		
		JLabel lblAccountNumber = new JLabel("ACCOUNT NUMBER:");
		lblAccountNumber.setForeground(new Color(255, 153, 51));
		lblAccountNumber.setFont(new Font("Verdana", Font.BOLD, 14));
		lblAccountNumber.setBounds(114, 105, 166, 28);
		loginPanel.add(lblAccountNumber);
		
		JLabel lblLogin = new JLabel("LOGIN");
		lblLogin.setForeground(new Color(255, 153, 0));
		lblLogin.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblLogin.setBounds(287, 11, 164, 36);
		loginPanel.add(lblLogin);
		
		JButton button = new JButton("BACK");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cards.show(frame.getContentPane(), "START PANEL");
				acctNumTF.setText("");
				loginPasswordPF.setText("");
			}
		});
		button.setForeground(new Color(51, 0, 204));
		button.setFont(new Font("Tahoma", Font.BOLD, 14));
		button.setBounds(12, 348, 150, 51);
		loginPanel.add(button);
		
		JPanel signUp = new JPanel();
		signUp.setBackground(new Color(51, 153, 204));
		frame.getContentPane().add(signUp, "SIGN UP PANEL");
		signUp.setLayout(null);
		
		JLabel lblSignUp = new JLabel("SIGN UP");
		lblSignUp.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblSignUp.setForeground(new Color(255, 153, 0));
		lblSignUp.setBounds(259, 22, 198, 36);
		signUp.add(lblSignUp);
		
		signUpfNameTF = new JTextField();
		signUpfNameTF.setColumns(10);
		signUpfNameTF.setBounds(286, 92, 120, 28);
		signUp.add(signUpfNameTF);
		
		signUpmNameTF = new JTextField();
		signUpmNameTF.setColumns(10);
		signUpmNameTF.setBounds(286, 144, 120, 28);
		signUp.add(signUpmNameTF);
		
		signUplNameTF = new JTextField();
		signUplNameTF.setColumns(10);
		signUplNameTF.setBounds(286, 198, 120, 28);
		signUp.add(signUplNameTF);
		
		JLabel lblFirstName = new JLabel("FIRST NAME:");
		lblFirstName.setForeground(new Color(255, 153, 51));
		lblFirstName.setFont(new Font("Verdana", Font.BOLD, 14));
		lblFirstName.setBounds(165, 89, 111, 28);
		signUp.add(lblFirstName);
		
		JLabel lblMiddleName = new JLabel("MIDDLE NAME:");
		lblMiddleName.setForeground(new Color(255, 153, 51));
		lblMiddleName.setFont(new Font("Verdana", Font.BOLD, 14));
		lblMiddleName.setBounds(153, 141, 125, 28);
		signUp.add(lblMiddleName);
		
		JLabel lblC = new JLabel("LAST NAME:");
		lblC.setForeground(new Color(255, 153, 51));
		lblC.setFont(new Font("Verdana", Font.BOLD, 14));
		lblC.setBounds(177, 195, 99, 28);
		signUp.add(lblC);
		
		JButton btnSignUp = new JButton("SIGN UP");
		btnSignUp.setForeground(new Color(51, 0, 204));
		btnSignUp.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSignUp.setBounds(271, 360, 150, 51);
		signUp.add(btnSignUp);
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//TODO
				
				String f = signUpfNameTF.getText();
				String l = signUplNameTF.getText();
				String m = signUpmNameTF.getText();
				String p = String.copyValueOf(passwordPF.getPassword());
				String ap = String.copyValueOf(againPasswordPF.getPassword());
				if(varify(f) || varify(l) || varify(p) || varify(ap))
				{
					
				}
				else
				{
					if(letterCheck(f) || letterCheck(l))
					{
						
							
						signUpfNameTF.setText("");
						signUpmNameTF.setText("");
						signUplNameTF.setText("");
						passwordPF.setText("");
						againPasswordPF.setText("");
					}
					else
					{
						if(numberCheck(f) || numberCheck(m) || numberCheck(l))
						{
							JOptionPane.showMessageDialog(frame,
							        "You cannot have numbers in your name.",
							        "NAME ERROR",
							        JOptionPane.ERROR_MESSAGE);
							signUpfNameTF.setText("");
							signUpmNameTF.setText("");
							signUplNameTF.setText("");
							
							
						}
						else
						{
							if(p.equals(ap))
							{
								
								cards.show(frame.getContentPane(), "TOS PANEL 2");
							}
							else
							{
								JOptionPane.showMessageDialog(frame,
								        "Your retyped password doesn't match your first password.",
								        "PASSWORD ERROR",
								        JOptionPane.ERROR_MESSAGE);
									passwordPF.setText("");
									againPasswordPF.setText("");
							}
						}
						
						
					}
				}
				
			}
		});
		
		JButton btnBack = new JButton("BACK");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cards.show(frame.getContentPane(), "START PANEL");
				signUpfNameTF.setText("");
				signUpmNameTF.setText("");
				signUplNameTF.setText("");
				
			}
		});
		btnBack.setForeground(new Color(51, 0, 204));
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnBack.setBounds(12, 347, 150, 51);
		signUp.add(btnBack);
		
		JLabel lblRetypePassword = new JLabel("RE-TYPE PASSWORD:");
		lblRetypePassword.setForeground(new Color(255, 153, 51));
		lblRetypePassword.setFont(new Font("Verdana", Font.BOLD, 14));
		lblRetypePassword.setBounds(103, 309, 183, 28);
		signUp.add(lblRetypePassword);
		
		JLabel lblPassword_1 = new JLabel("PASSWORD:");
		lblPassword_1.setForeground(new Color(255, 153, 51));
		lblPassword_1.setFont(new Font("Verdana", Font.BOLD, 14));
		lblPassword_1.setBounds(177, 252, 99, 28);
		signUp.add(lblPassword_1);
		
		passwordPF = new JPasswordField();
		passwordPF.setBounds(286, 255, 120, 28);
		signUp.add(passwordPF);
		
		againPasswordPF = new JPasswordField();
		againPasswordPF.setBounds(286, 309, 120, 28);
		signUp.add(againPasswordPF);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(new Color(51, 153, 204));
		frame.getContentPane().add(mainPanel, "MAIN PANEL");
		mainPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("WELCOME");
		lblNewLabel.setForeground(new Color(255, 255, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 23));
		lblNewLabel.setBackground(new Color(102, 102, 255));
		lblNewLabel.setBounds(286, 161, 130, 36);
		mainPanel.add(lblNewLabel);
		
		Button transBtn = new Button("TRANSFER");
		transBtn.setForeground(new Color(51, 0, 204));
		transBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
		transBtn.setBounds(20, 32, 150, 50);
		mainPanel.add(transBtn);
		transBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cards.show(frame.getContentPane(), "TRANSFER PANEL");
			}
		});
		
		Button depBtn = new Button("DEPOSIT");
		depBtn.setForeground(new Color(51, 0, 204));
		depBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
		depBtn.setBounds(20, 161, 150, 50);
		mainPanel.add(depBtn);
		depBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cards.show(frame.getContentPane(), "DEPOSIT PANEL");
				
			}
		});
		
		Button widBtn = new Button("WITHDRAW");
		widBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cards.show(frame.getContentPane(), "WITHDRAW PANEL");
			}
		});
		widBtn.setForeground(new Color(51, 0, 204));
		widBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
		widBtn.setBounds(525, 32, 150, 50);
		mainPanel.add(widBtn);
		
		Button logoutBtn = new Button("LOGOUT");
		logoutBtn.setForeground(new Color(51, 0, 204));
		logoutBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
		logoutBtn.setBounds(374, 307, 150, 50);
		mainPanel.add(logoutBtn);
		
		Button acctSettingsBtn = new Button("ACCOUNT SETTINGS");
		acctSettingsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		acctSettingsBtn.setActionCommand("ACCOUNT SETTINGS");
		acctSettingsBtn.setForeground(new Color(51, 0, 204));
		acctSettingsBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		acctSettingsBtn.setBounds(525, 161, 150, 50);
		mainPanel.add(acctSettingsBtn);
		
		Button acctInfoBtn = new Button("ACCOUNT INFO");
		acctInfoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cards.show(frame.getContentPane(), "ACCOUNT INFO PANEL");
			}
		});
		acctInfoBtn.setActionCommand("ACCOUNT INFO");
		acctInfoBtn.setForeground(new Color(51, 0, 204));
		acctInfoBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
		acctInfoBtn.setBounds(183, 307, 150, 50);
		mainPanel.add(acctInfoBtn);
		
		
		logoutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cards.show(frame.getContentPane(), "START PANEL");
				activityLogTA.setText("");
				lblCurrentFunds.setText("");
				lblActualAcctNum.setText("");
				
			}
		});
		
		
		JPanel transferPanel = new JPanel();
		transferPanel.setBackground(new Color(51, 153, 204));
		frame.getContentPane().add(transferPanel, "TRANSFER PANEL");
		transferPanel.setLayout(null);
		
		Button transferToButton = new Button("TRANSFER TO");
		transferToButton.setFont(new Font("Tahoma", Font.PLAIN, 45));
		transferToButton.setForeground(new Color(51, 0, 255));
		transferToButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cards.show(frame.getContentPane(), "TRANSFER TO PANEL");
			}
		});
		transferToButton.setBounds(67, 25, 562, 105);
		transferPanel.add(transferToButton);
		
		Button transferFromButton = new Button("TRANSFER FROM");
		transferFromButton.setForeground(new Color(51, 0, 255));
		transferFromButton.setFont(new Font("Tahoma", Font.PLAIN, 45));
		transferFromButton.setBounds(67, 290, 562, 105);
		transferPanel.add(transferFromButton);
		transferFromButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cards.show(frame.getContentPane(), "TRANSFER FROM PANEL");
			}
		});
		
		Button backBtn1 = new Button("BACK");
		backBtn1.setForeground(new Color(51, 0, 204));
		backBtn1.setFont(new Font("Tahoma", Font.BOLD, 16));
		backBtn1.setBounds(265, 185, 150, 50);
		transferPanel.add(backBtn1);
		backBtn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cards.show(frame.getContentPane(), "MAIN PANEL");
			}
		});
		
		JPanel transferToPanel = new JPanel();
		transferToPanel.setBackground(new Color(51, 153, 204));
		frame.getContentPane().add(transferToPanel, "TRANSFER TO PANEL");
		transferToPanel.setLayout(null);
		
		tToOtherTF = new JTextField();
		tToOtherTF.setColumns(10);
		tToOtherTF.setBounds(347, 98, 120, 28);
		transferToPanel.add(tToOtherTF);
		
		JLabel lblOtherAccountNumber = new JLabel("OTHER ACCOUNT NUMBER:");
		lblOtherAccountNumber.setForeground(new Color(255, 153, 51));
		lblOtherAccountNumber.setFont(new Font("Verdana", Font.BOLD, 14));
		lblOtherAccountNumber.setBounds(112, 96, 225, 28);
		transferToPanel.add(lblOtherAccountNumber);
		
		JLabel lblTransferToAccount = new JLabel("TRANSFER TO ACCOUNT");
		lblTransferToAccount.setForeground(new Color(255, 153, 0));
		lblTransferToAccount.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblTransferToAccount.setBounds(106, 11, 492, 36);
		transferToPanel.add(lblTransferToAccount);
		
		JLabel lblTransferAmount = new JLabel("TRANSFER AMOUNT");
		lblTransferAmount.setForeground(new Color(255, 153, 51));
		lblTransferAmount.setFont(new Font("Verdana", Font.BOLD, 14));
		lblTransferAmount.setBounds(264, 160, 167, 28);
		transferToPanel.add(lblTransferAmount);
		
		JButton btnConfirm = new JButton("CONFIRM");
		btnConfirm.setForeground(new Color(51, 0, 204));
		btnConfirm.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnConfirm.setBounds(265, 317, 150, 51);
		transferToPanel.add(btnConfirm);
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					//transfer to
					//TODO
					double tAmt = Double.parseDouble(transferToAmountTF.getText()); 
					int c = Integer.valueOf(tToOtherTF.getText());
					other = searchAccount(c);
					int q = Integer.valueOf(lblActualAcctNum.getText());
					b = searchAccount (q);
					
					if(other != null  &&  other != b)
					{
						if(b.transferTo(tAmt, other))
						{
							updateBalance(b.getBalance());
							lblCurrentFunds.setText(String.valueOf(b.getBalance()));
							JOptionPane.showMessageDialog(frame,
							        "Transfer Successful",
							        "TRANSFER SUCCESSFUL",
							        JOptionPane.INFORMATION_MESSAGE);
						 transferToAmountTF.setText("");
						 tToOtherTF.setText("");
						
						cards.show(frame.getContentPane(), "MAIN PANEL");
						}
						else
						{
							
							JOptionPane.showMessageDialog(frame,
							        "Please try again",
							        "TRANSFER TO ERROR",
							        JOptionPane.ERROR_MESSAGE);
						 transferToAmountTF.setText("");
						}
					}
					else
					{
						
						JOptionPane.showMessageDialog(frame,
						        "Please try again",
						        "INCORRECT ACCOUNT NUMBER",
						        JOptionPane.ERROR_MESSAGE);
					 transferToAmountTF.setText("");
					 tToOtherTF.setText("");
					}
				}
				catch(Exception exc)
				{
					exc.printStackTrace();
					JOptionPane.showMessageDialog(frame,
					        "Please try again",
					        "FIELD ERROR",
					        JOptionPane.ERROR_MESSAGE);
				 transferToAmountTF.setText("");
				 tToOtherTF.setText("");
				}
				
			}
		});
		
		Button backBtn2 = new Button("BACK");
		backBtn2.setForeground(new Color(51, 0, 204));
		backBtn2.setFont(new Font("Tahoma", Font.BOLD, 16));
		backBtn2.setBounds(10, 360, 150, 50);
		transferToPanel.add(backBtn2);
		
		transferToAmountTF = new JTextField();
		transferToAmountTF.setFont(new Font("Tahoma", Font.BOLD, 20));
		transferToAmountTF.setColumns(10);
		transferToAmountTF.setBounds(264, 199, 150, 51);
		transferToPanel.add(transferToAmountTF);
		backBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cards.show(frame.getContentPane(), "MAIN PANEL");	
				transferToAmountTF.setText("");
				tToOtherTF.setText("");
			}
		});
		
		JPanel transferFromPanel = new JPanel();
		transferFromPanel.setLayout(null);
		transferFromPanel.setBackground(new Color(51, 153, 204));
		frame.getContentPane().add(transferFromPanel, "TRANSFER FROM PANEL");
		
		otherAcctNumTF = new JTextField();
		otherAcctNumTF.setColumns(10);
		otherAcctNumTF.setBounds(347, 98, 120, 28);
		transferFromPanel.add(otherAcctNumTF);
		
		JLabel label_2 = new JLabel("OTHER ACCOUNT NUMBER:");
		label_2.setForeground(new Color(255, 153, 51));
		label_2.setFont(new Font("Verdana", Font.BOLD, 14));
		label_2.setBounds(112, 96, 225, 28);
		transferFromPanel.add(label_2);
		
		JLabel lblTransferFromAccount = new JLabel("TRANSFER FROM ACCOUNT");
		lblTransferFromAccount.setForeground(new Color(255, 153, 0));
		lblTransferFromAccount.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblTransferFromAccount.setBounds(83, 11, 555, 36);
		transferFromPanel.add(lblTransferFromAccount);
		
		JLabel label_4 = new JLabel("TRANSFER AMOUNT");
		label_4.setForeground(new Color(255, 153, 51));
		label_4.setFont(new Font("Verdana", Font.BOLD, 14));
		label_4.setBounds(265, 193, 167, 28);
		transferFromPanel.add(label_4);
		
		JButton btnConfirm1 = new JButton("CONFIRM");
		btnConfirm1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try 
				{
					//TODO transfer from
					double tFAmt = Double.parseDouble(transferFromTF.getText());
					int p = Integer.valueOf(otherAcctNumTF.getText());
					other = searchAccount(p);
					int q = Integer.valueOf(lblActualAcctNum.getText());
					b = searchAccount (q);
					String pswd = String.copyValueOf(otherPswdPF.getPassword());
					if (other != null && other != b)
					{
						if(b.transferFrom(tFAmt, other, pswd))
						{
							
							lblCurrentFunds.setText(String.valueOf(b.getBalance()));
							JOptionPane.showMessageDialog(frame,
						        "Transfer successful",
						        "TRANSFER SUCCESSFUL",
						        JOptionPane.INFORMATION_MESSAGE);
								transferFromTF.setText("");
								otherPswdPF.setText("");
								otherAcctNumTF.setText("");	
								cards.show(frame.getContentPane(), "MAIN PANEL");
								  
						}
						else
						{
							
							JOptionPane.showMessageDialog(frame,
									"Please try again",
									"TRANSFER FROM ERROR",
									JOptionPane.ERROR_MESSAGE);
							transferFromTF.setText("");
							otherPswdPF.setText("");
							otherAcctNumTF.setText("");	
						}
					}
					else
					{
						
						JOptionPane.showMessageDialog(frame,
								"Please try again",
								"FIELD ERROR",
								JOptionPane.ERROR_MESSAGE);
						transferFromTF.setText("");
						otherPswdPF.setText("");
						otherAcctNumTF.setText("");	
					}
				}
				catch(Exception exc)
				{
					exc.printStackTrace();
					JOptionPane.showMessageDialog(frame,
							"Please try again",
							"FIELD ERROR",
							JOptionPane.ERROR_MESSAGE);
					transferFromTF.setText("");
					otherPswdPF.setText("");
					otherAcctNumTF.setText("");	
				}
				

				
				
			}
		});
		btnConfirm1.setForeground(new Color(51, 0, 204));
		btnConfirm1.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnConfirm1.setBounds(265, 317, 150, 51);
		transferFromPanel.add(btnConfirm1);
		
		JLabel lblOtherAccountPassword = new JLabel("OTHER ACCOUNT PASSWORD:");
		lblOtherAccountPassword.setForeground(new Color(255, 153, 51));
		lblOtherAccountPassword.setFont(new Font("Verdana", Font.BOLD, 14));
		lblOtherAccountPassword.setBounds(94, 140, 243, 28);
		transferFromPanel.add(lblOtherAccountPassword);
		
		Button backBtn3 = new Button("BACK");
		backBtn3.setForeground(new Color(51, 0, 204));
		backBtn3.setFont(new Font("Tahoma", Font.BOLD, 16));
		backBtn3.setBounds(10, 360, 150, 50);
		transferFromPanel.add(backBtn3);
		
		transferFromTF = new JTextField();
		transferFromTF.setFont(new Font("Tahoma", Font.BOLD, 20));
		transferFromTF.setColumns(10);
		transferFromTF.setBounds(265, 232, 150, 51);
		transferFromPanel.add(transferFromTF);
		
		otherPswdPF = new JPasswordField();
		otherPswdPF.setBounds(347, 142, 120, 28);
		transferFromPanel.add(otherPswdPF);
		backBtn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cards.show(frame.getContentPane(), "MAIN PANEL");
				transferFromTF.setText("");
				otherPswdPF.setText("");
				otherAcctNumTF.setText("");
			}
		});
		
		JPanel depositPanel = new JPanel();
		depositPanel.setBackground(new Color(51, 153, 204));
		frame.getContentPane().add(depositPanel, "DEPOSIT PANEL");
		depositPanel.setLayout(null);
		
		JLabel lblDeposit = new JLabel("DEPOSIT");
		lblDeposit.setForeground(new Color(255, 153, 0));
		lblDeposit.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblDeposit.setBounds(254, 11, 191, 36);
		depositPanel.add(lblDeposit);
		
		JLabel lblDepositAmount = new JLabel("DEPOSIT AMOUNT");
		lblDepositAmount.setForeground(new Color(255, 153, 51));
		lblDepositAmount.setFont(new Font("Verdana", Font.BOLD, 25));
		lblDepositAmount.setBounds(220, 161, 257, 28);
		depositPanel.add(lblDepositAmount);
		
		JButton confirmBtn2 = new JButton("CONFIRM");
		confirmBtn2.setForeground(new Color(51, 0, 204));
		confirmBtn2.setFont(new Font("Tahoma", Font.BOLD, 14));
		confirmBtn2.setBounds(273, 334, 150, 51);
		depositPanel.add(confirmBtn2);
		confirmBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					//TODO
					System.out.println(b.getBalance());
					double dep = Double.parseDouble(depositAmountTF.getText());
					if(b.deposit(dep))
					{
						System.out.println(b.getBalance());
						
						lblCurrentFunds.setText(String.valueOf(b.getBalance()));
						activityLogTA.setText(String.valueOf(b.getMessage()));
						updateBalance(b.getBalance());
						 JOptionPane.showMessageDialog(frame,
							        "Deposit of amount successful",
							        "DEPOSIT SUCCESSFUL",
							        JOptionPane.INFORMATION_MESSAGE);
						 depositAmountTF.setText("");
						 
						cards.show(frame.getContentPane(), "MAIN PANEL");
					}
					else
					{
					
						
						 JOptionPane.showMessageDialog(frame,
							        "Please try again",
							        "DEPOSIT ERROR",
							        JOptionPane.ERROR_MESSAGE);
						 depositAmountTF.setText("");
						 
					}
				}
					
					catch(Exception exc)
					{
						exc.printStackTrace();
						JOptionPane.showMessageDialog(frame,
						        "You can't put letters in a deposit",
						        "DEPOSIT FIELDS ERROR",
						        JOptionPane.ERROR_MESSAGE);
					 depositAmountTF.setText("");
					
				}
			}
		});
		
		Button backBtn4 = new Button("BACK");
		backBtn4.setForeground(new Color(51, 0, 204));
		backBtn4.setFont(new Font("Tahoma", Font.BOLD, 16));
		backBtn4.setBounds(10, 360, 150, 50);
		depositPanel.add(backBtn4);
		
		depositAmountTF = new JTextField();
		depositAmountTF.setFont(new Font("Tahoma", Font.BOLD, 20));
		depositAmountTF.setColumns(10);
		depositAmountTF.setBounds(273, 208, 150, 51);
		depositPanel.add(depositAmountTF);
		backBtn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cards.show(frame.getContentPane(), "MAIN PANEL");
				depositAmountTF.setText("");
			}
		});
		
		JPanel withdrawPanel = new JPanel();
		withdrawPanel.setLayout(null);
		withdrawPanel.setBackground(new Color(51, 153, 204));
		frame.getContentPane().add(withdrawPanel, "WITHDRAW PANEL");
		
		JLabel lblWithdraw = new JLabel("WITHDRAW");
		lblWithdraw.setForeground(new Color(255, 153, 0));
		lblWithdraw.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblWithdraw.setBounds(226, 11, 246, 36);
		withdrawPanel.add(lblWithdraw);
		
		JLabel lblWithdrawAmount = new JLabel("WITHDRAW AMOUNT");
		lblWithdrawAmount.setForeground(new Color(255, 153, 51));
		lblWithdrawAmount.setFont(new Font("Verdana", Font.BOLD, 25));
		lblWithdrawAmount.setBounds(203, 157, 310, 28);
		withdrawPanel.add(lblWithdrawAmount);
		
	
		Button backBtnWid = new Button("BACK");
		backBtnWid.setForeground(new Color(51, 0, 204));
		backBtnWid.setFont(new Font("Tahoma", Font.BOLD, 16));
		backBtnWid.setBounds(10, 360, 150, 50);
		withdrawPanel.add(backBtnWid);
		
		withdrawAmountTF = new JTextField();
		withdrawAmountTF.setFont(new Font("Tahoma", Font.BOLD, 20));
		withdrawAmountTF.setColumns(10);
		withdrawAmountTF.setBounds(273, 196, 150, 51);
		withdrawPanel.add(withdrawAmountTF);
		
		JButton confirmBtn3 = new JButton("CONFIRM");
		confirmBtn3.setForeground(new Color(51, 0, 204));
		confirmBtn3.setFont(new Font("Tahoma", Font.BOLD, 14));
		confirmBtn3.setBounds(273, 334, 150, 51);
		withdrawPanel.add(confirmBtn3);
		confirmBtn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try
				{
					//TODO
					double wid = Double.parseDouble(withdrawAmountTF.getText());
					
					System.out.println(b.getBalance());
					if(b.withdraw(wid))
					{
						System.out.println(b.getBalance());
						updateBalance(b.getBalance());
						lblCurrentFunds.setText(String.valueOf(b.getBalance()));
						
						cards.show(frame.getContentPane(), "MAIN PANEL");
						JOptionPane.showMessageDialog(frame,
						        "Withdraw of amount successful",
						        "WITHDRAW SUCCESSFUL",
						        JOptionPane.INFORMATION_MESSAGE);
						withdrawAmountTF.setText("");
					}
					else
					{
						 
						 JOptionPane.showMessageDialog(frame,
							        "Please try again",
							        "WITHDRAW ERROR",
							        JOptionPane.ERROR_MESSAGE);
						 withdrawAmountTF.setText("");
						 
					}
				}
				catch(Exception exc)
				{
					exc.printStackTrace();
					JOptionPane.showMessageDialog(frame,
					        "You can't put letters in a withdraw",
					        "WITHDRAW FIELDS ERROR",
					        JOptionPane.ERROR_MESSAGE);
				 withdrawAmountTF.setText("");
				}
			
				
			}
		});
		
		JPanel acctInfoPanel = new JPanel();
		acctInfoPanel.setBackground(new Color(51, 153, 204));
		frame.getContentPane().add(acctInfoPanel, "ACCOUNT INFO PANEL");
		acctInfoPanel.setLayout(null);
		
		JLabel lblAcctInfo = new JLabel("ACCOUNT INFO");
		lblAcctInfo.setForeground(new Color(255, 153, 0));
		lblAcctInfo.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblAcctInfo.setBounds(194, 11, 323, 55);
		acctInfoPanel.add(lblAcctInfo);
		
		lblCurrentBalance = new JLabel("Current Balance:");
		lblCurrentBalance.setForeground(new Color(255, 153, 51));
		lblCurrentBalance.setFont(new Font("Verdana", Font.BOLD, 25));
		lblCurrentBalance.setBounds(10, 96, 274, 37);
		acctInfoPanel.add(lblCurrentBalance);
		
		lblCurrentFunds = new JLabel("");
		lblCurrentFunds.setForeground(new Color(255, 153, 51));
		lblCurrentFunds.setFont(new Font("Verdana", Font.BOLD, 16));
		lblCurrentFunds.setBounds(296, 96, 390, 37);
		acctInfoPanel.add(lblCurrentFunds);
		
		Button backBtnAcctInfo = new Button("BACK");
		backBtnAcctInfo.setForeground(new Color(51, 0, 204));
		backBtnAcctInfo.setFont(new Font("Tahoma", Font.BOLD, 16));
		backBtnAcctInfo.setBounds(8, 361, 150, 50);
		acctInfoPanel.add(backBtnAcctInfo);
		
		JLabel lblAccountNumber_1 = new JLabel("Account Number:");
		lblAccountNumber_1.setForeground(new Color(255, 153, 51));
		lblAccountNumber_1.setFont(new Font("Verdana", Font.BOLD, 25));
		lblAccountNumber_1.setBounds(10, 146, 274, 37);
		acctInfoPanel.add(lblAccountNumber_1);
		
		lblActualAcctNum = new JLabel("");
		lblActualAcctNum.setForeground(new Color(255, 153, 51));
		lblActualAcctNum.setFont(new Font("Verdana", Font.BOLD, 16));
		lblActualAcctNum.setBounds(296, 146, 390, 37);
		acctInfoPanel.add(lblActualAcctNum);
		
		Button actLogBtn = new Button("ACCOUNT INFO");
		actLogBtn.setForeground(new Color(51, 0, 204));
		actLogBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
		actLogBtn.setActionCommand("ACTIVTY LOG ");
		actLogBtn.setBounds(263, 278, 150, 50);
		acctInfoPanel.add(actLogBtn);
		
		JPanel activityLogPanel = new JPanel();
		activityLogPanel.setBackground(new Color(51, 153, 204));
		frame.getContentPane().add(activityLogPanel, "ACTIVITY LOG PANEL");
		activityLogPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 79, 696, 341);
		activityLogPanel.add(scrollPane);
		
		activityLogTA = new JTextArea();
		activityLogTA.setEditable(false);
		scrollPane.setViewportView(activityLogTA);
		JLabel lblAccountActivityLo = new JLabel("ACCOUNT ACTIVITY LOG");
		lblAccountActivityLo.setForeground(new Color(255, 153, 0));
		lblAccountActivityLo.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblAccountActivityLo.setBounds(87, 13, 512, 55);
		activityLogPanel.add(lblAccountActivityLo);
		
		Button backBtn5 = new Button("BACK");
		backBtn5.setBounds(10, 13, 71, 55);
		activityLogPanel.add(backBtn5);
		backBtn5.setForeground(new Color(51, 0, 204));
		backBtn5.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JPanel TOSpanel = new JPanel();
		frame.getContentPane().add(TOSpanel, "TOS PANEL");
		TOSpanel.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 0, 696, 420);
		TOSpanel.add(scrollPane_1);
		
		JTextArea termsOserviceTA = new JTextArea();
		termsOserviceTA.setWrapStyleWord(true);
		termsOserviceTA.setEditable(false);
		termsOserviceTA.setLineWrap(true);
		termsOserviceTA.setText("1. Introduction\r\nThese Terms of Service are a legal agreement between you and HIB that governs your access to and use of any eligible HIB credit or debit card  (\u201CCard\u201D) when you add, use or remove a Card in Apple Pay on any Eligible Device and provide the terms and conditions of the HIB Digital Rewards Program (\u201CProgram Terms\u201D).  It is important that you read and understand these Terms of Service and Program Terms because you will indicate your acceptance of these Terms of Service and Program Terms when enrolling in Apple Pay or provisioning one or more Cards for use in Apple Pay.\r\n2. Other Documents and Agreements\r\nCardholder Agreement\r\nUse of a Card with Apple Pay is governed by your Cardholder Agreement for HIB credit cards or your Banking Service Agreement for HIB debit cards ( collectively \u201CCard Agreement\u201D). You understand and agree that all of the terms and conditions of your Card Agreement, as applicable, form part of these Terms of Service.  All references to \u201CCard\u201D in your Card Agreement include Apple Pay.  If there is a conflict between these Terms of Service and the applicable Card Agreement, these Terms of Service will govern to the extent necessary to resolve the conflict.\r\nApple Pay Agreements\r\nYour use of Apple Pay may be governed by or subject to agreements or terms of use entered into with Apple as provider of Apple Pay.\r\n3. Use of Cards in Apple Pay\r\nIf you want to add a Card to Apple Pay, you must follow the procedures adopted by Apple and any instructions provided by HIB, and any further procedures Apple or HIB adopt. You understand that we may not add a Card to Apple Pay if we cannot verify the Card, if your account is not in good standing, or if we otherwise suspect that there may be fraud associated with your Card.\r\nApple Pay allows you to make purchases using an added Card wherever Apple Pay is accepted. Apple Pay  may not be accepted at all places where your Card is accepted. \r\n4. Removal of your Card\r\nHIB may not permit the addition of a Card to Apple Pay, or may remove, suspend or cancel your access to Apple Pay at any time, if we cannot verify the Card, if we suspect that there may be fraud associated with the Card, if your account is not in good standing, if applicable laws change, or for any other reason determined by HIB in its sole discretion. You may suspend, delete or reactivate a Card from Apple Pay by following Apple's procedures for suspension, deletion or reactivation.  In certain circumstances, your Card may be suspended or removed from Apple Pay by Apple.\r\n5. Maximum Dollar Limit For Transactions\r\nPayment networks, HIB or Participating Merchants may, in their discretion, establish from time to time a maximum dollar limit for a single Transaction that may be completed using Apple Pay.  As a result, you may not be able to use Apple Pay to complete a Transaction, even if you have met the eligibility requirements for Apple Pay. If a Transaction is not completed because it exceeds a maximum dollar limit, we encourage you to use your physical Card instead of your Eligible Device to complete the Transaction.\r\n6. Applicable Fees\r\nWe do not charge you any fees for adding a Card to Apple Pay. Please consult your Card Agreement for any applicable fees, interest, or other charges associated with your Card. Your mobile service carrier or provider, Apple or other third parties may charge you service fees in connection with your use of your Eligible Device or Apple Pay. \r\n7. Security Requirements\r\nYou are required  to contact us immediately if your Card is lost or stolen, if your Eligible Device is lost or stolen, or if your Card account is compromised.  If you report your Card lost or stolen, you will be able to continue to make Transactions using Apple Pay on your Eligible Device unless you instruct us to suspend Apple Pay.  If you report your Eligible Device lost or stolen, you will be able to continue to make Transactions using your Card. If you get a new supported iOS device, please be sure to delete all your Cards and other personal information from your prior supported iOS device.\r\nYou are required to contact us immediately  if there are errors or if you suspect fraud with your Card. We will resolve any potential error or fraudulent purchase in accordance with your Card Agreement, as applicable. \r\nYou agree to protect and keep confidential your Apple User ID, Apple passwords (including your fingerprint set up for Touch ID, if applicable) and all other information required for you to make purchases with your Card using Apple Pay.  If you share these credentials with others, they may be able to access Apple Pay and make purchases with your Card or obtain your personal information.\r\nIn addition to your efforts to keep your credentials secure, we take reasonable steps to help ensure that information we send to others from your use of a Card in Apple Pay is sent in a secure manner. However, Apple is responsible for the security of information provided to Apple or stored in Apple Pay. HIB is not responsible if there is a security breach affecting information stored in or sent from Apple Pay.\r\nYou are prohibited from using Apple Pay on an Eligible Device that you know or have reason to believe has had its security or integrity compromised (e.g. where the device has been \"rooted\" or had its security mechanisms bypassed). You will be solely liable for any losses, damages and expenses incurred as a result of your use of Apple Pay on a compromised device.\r\n8. HIB Digital Reward Program\r\nBy adding a Card to Apple Pay you are automatically enrolled in the HIB Digital Reward Program (\u201CProgram\u201D). Subject to the following terms and conditions, under the Program you are entitled to receive, from time to time, benefits, products or services  that are provided by HIB or in participation with our program partners.  This may include coupons to receive goods, products, services, discounts, or other benefits from HIB or one of its program partners.  Such benefits may be tailored to your interests based on information we may have collected including your transaction information. In addition, from time to time you may receive personalized or exclusive rewards.\r\nThe first Program benefits are targeted to begin in the second half of 2016.\r\nHIB may restrict, suspend, terminate or otherwise amend the terms of the Program at any time without notice. Neither HIB nor its program partners have any responsibility or liability for any expenses, losses, costs or any other matter or thing however suffered or caused (including compensatory, incidental, indirect, special, punitive, consequential or exemplary damages or other losses) directly or indirectly arising out of or related to the Program, HIB\u2019s administration of the Program (including any amendment of the Program terms and conditions) or your participation in the Program.  HIB assumes no liability with respect to rewards claimed and in particular, has no liability for any delay or failure to deliver rewards or for their condition.\r\n9. Liability for Loss\r\nThe Cardholder is responsible for all Transactions and any resulting interest, fees or losses incurred that are made using your Card or via Apple Pay if you fail to comply with obligations described under \u201CSecurity Requirements\u201D until we receive written or verbal notice from you in accordance with that Section. \r\nHIB will not be liable for any losses you incur except as set out in these Terms of Service or specifically described in your Card Agreement, or otherwise provided by applicable law.\r\n10.  Privacy\r\nYou consent to the collection, use and sharing of your personal information from time to time as provided in HIB\u2019s privacy policy. HIB\u2019s privacy policy is available at any branch or at www.HIB.com. This policy may be amended, replaced or supplemented from time to time. HIB may share with or receive from Apple such information as may reasonably be necessary to determine your eligibility for, enrollment in and use of Apple Pay and any Apple Pay features you may select (for example, your name and Card details such as Card number and expiration date). Apple may aggregate your information with other information or make it anonymous and may use any such aggregated or anonymous information for purposes set out in its privacy policy or in the Apple Pay agreements or terms of use. Although your Transactions may be processed through Apple Pay, HIB does not permit Apple to view, use, or store your Transaction details, including your most recent Transactions if this feature is enabled.  To help protect you and HIB from error and criminal activities, HIB and Apple may share information  reasonably required for such purposes as fraud detection and prevention (for example, informing Apple if you notify HIB of a lost or stolen Eligible Device). \r\n11. Electronic Communications\r\nYou agree to receive electronic communications from us, including emails to the email address or text message to the mobile number you have provided in connection with your Card account. These electronic communications will relate to your use of your Card(s) in Apple Pay and to deliver your benefits to which you may be entitled under the Program. You agree to update your email address or mobile number when it changes by contacting us using the contact information in your agreements with HIB. You may also contact us if you wish to withdraw your consent to receive these electronic communications, but doing so will result in your inability to continue to use your Card(s) in the Apple Pay or receive benefits under the Program.\r\n12. No Warranty and Exclusion of Liability\r\nFor the purposes of this Section, \u201CHIB\u201D means HIB and its agents, contractors, distributors, channel partners and associated service providers, and each of their subsidiaries. All of the parties listed in the preceding sentence are third party beneficiaries of this Section. The provisions set out in this Section shall survive termination of these Terms of Service.\r\nApple Pay is provided by Apple and HIB is not responsible for its use or function. You acknowledge and agree that  HIB makes no representations, warranties or conditions relating to Apple Pay of any kind, and in particular: (a) HIB does not warrant the operability or functionality of Apple Pay or that Apple Pay will be available to complete a Transaction; (b) HIB does not warrant that any particular merchant will be a Participating Merchant at which Apple Pay is available; (c) HIB does not warrant that Apple Pay will meet your requirements or that the operation of Apple Pay will be uninterrupted or error-free; and (d) HIB does not guarantee the availability or operability of the wireless networks of any Eligible Device.  You may want to consider keeping your physical Card with you to use in the event you cannot make Apple Pay transactions.\r\nHIB will have no liability whatsoever in relation to Apple Pay, including without limitation in relation to the sale, distribution or use thereof, or the performance or non-performance of Apple Pay, or any loss, injury or inconvenience you suffer.\r\n13.  Changes to the Terms of Service\r\nWe may change, either permanently or temporarily, any term of these Terms of Service or replace these Terms of Service with another agreement, at any time. We will give you written notice of a change and any other information required by law at least 30 days before the change is stated to come into effect in the notice, by any method allowed by applicable law including by posting a notice on www.HIB.com/apple-pay. You may refuse the change by terminating these Terms of Service without cost, penalty or cancellation indemnity by stopping your use of Apple Pay within 30 days of the effective date of the change.\r\n14. Contacting Us\r\nYou may contact us about anything concerning mobile payment services or these Terms of Service by calling the phone number found on www.HIB.com/apple-pay.\r\nIf you have any questions, disputes or complaints about Apple Pay, you should contact Apple.\r\n15. Definitions\r\nIn these Terms of Service,\r\nCardholder means primary cardholder of any HIB credit card and the cardholder of any HIB debit card.\r\nCardholder Agreement means the agreements governing the credit card account associated with your HIB credit card, as it may be updated, enhanced or modified by HIB from time to time.\r\nHIB, we, our or us means Canadian Imperial Bank of Commerce and its subsidiaries.\r\nBanking Service Agreement means the agreements governing the debit card account associated with your HIB debit card, as it may be updated, enhanced or modified by HIB from time to time.\r\nEligible Device means any iPhone, iPad or other Apple device that can be used to make Transactions using Apple Pay.\r\nParticipating Merchant means a merchant that allows customers to complete Transactions using Apple Pay.\r\nTransaction means any credit or debit transaction made using Apple Pay, including any transactions by any HIB credit card authorized user.\r\nYou or your means each Cardholder.\r\n\r\n");
		scrollPane_1.setViewportView(termsOserviceTA);
		
		JLabel lblTermsOfServices = new JLabel("TERMS OF SERVICES");
		lblTermsOfServices.setBackground(new Color(51, 153, 204));
		lblTermsOfServices.setForeground(new Color(255, 153, 0));
		lblTermsOfServices.setFont(new Font("Tahoma", Font.PLAIN, 30));
		scrollPane_1.setColumnHeaderView(lblTermsOfServices);
		
		JButton btnBack_1 = new JButton("BACK");
		btnBack_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cards.show(frame.getContentPane(), "MAIN PANEL");
			}
		});
		btnBack_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnBack_1.setForeground(new Color(51, 0, 204));
		scrollPane_1.setRowHeaderView(btnBack_1);
		
		JPanel TOSpanel2 = new JPanel();
		TOSpanel2.setBackground(new Color(51, 153, 204));
		TOSpanel2.setLayout(null);
		frame.getContentPane().add(TOSpanel2, "TOS PANEL 2");
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(0, 0, 696, 420);
		TOSpanel2.add(scrollPane_2);
		
		JButton btnConfirm_1 = new JButton("CONFIRM");
		btnConfirm_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Random rand = new Random();
				int randInt = rand.nextInt(99999);
				double amount = 0;
				String f = signUpfNameTF.getText();
				String l = signUplNameTF.getText();
				String m = signUpmNameTF.getText();
				String p = String.copyValueOf(passwordPF.getPassword());
				
				try
				{
					//TODO
					if(chckbxIHaveRead.isSelected())
					{
					
						BankAccount temp = new BankAccount(amount, f, m, l, randInt, p);
						addAccount (temp);
						JOptionPane.showMessageDialog(frame,
								"This is your account number",
								"ACCOUNT NUMBER: " + randInt,
								JOptionPane.INFORMATION_MESSAGE);
						cards.show(frame.getContentPane(), "START PANEL");
						signUpfNameTF.setText("");
						signUpmNameTF.setText("");
						signUplNameTF.setText("");
						passwordPF.setText("");
						againPasswordPF.setText("");
				
					}
					else
					{
						JOptionPane.showMessageDialog(frame,
								"Terms Of Services not Agreed with",
								"TERMS OF SERVICES NOT AGREED WITH",
								JOptionPane.ERROR_MESSAGE);
					}
				}
				catch(Exception k)
				{
					k.printStackTrace();
					
				}
			}
		});
		btnConfirm_1.setForeground(new Color(51, 0, 204));
		btnConfirm_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		scrollPane_2.setRowHeaderView(btnConfirm_1);
		
		JTextArea textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setText("1. Introduction\r\nThese Terms of Service are a legal agreement between you and HIB that governs your access to and use of any eligible HIB credit or debit card  (\u201CCard\u201D) when you add, use or remove a Card in Apple Pay on any Eligible Device and provide the terms and conditions of the HIB Digital Rewards Program (\u201CProgram Terms\u201D).  It is important that you read and understand these Terms of Service and Program Terms because you will indicate your acceptance of these Terms of Service and Program Terms when enrolling in Apple Pay or provisioning one or more Cards for use in Apple Pay.\r\n2. Other Documents and Agreements\r\nCardholder Agreement\r\nUse of a Card with Apple Pay is governed by your Cardholder Agreement for HIB credit cards or your Banking Service Agreement for HIB debit cards ( collectively \u201CCard Agreement\u201D). You understand and agree that all of the terms and conditions of your Card Agreement, as applicable, form part of these Terms of Service.  All references to \u201CCard\u201D in your Card Agreement include Apple Pay.  If there is a conflict between these Terms of Service and the applicable Card Agreement, these Terms of Service will govern to the extent necessary to resolve the conflict.\r\nApple Pay Agreements\r\nYour use of Apple Pay may be governed by or subject to agreements or terms of use entered into with Apple as provider of Apple Pay.\r\n3. Use of Cards in Apple Pay\r\nIf you want to add a Card to Apple Pay, you must follow the procedures adopted by Apple and any instructions provided by HIB, and any further procedures Apple or HIB adopt. You understand that we may not add a Card to Apple Pay if we cannot verify the Card, if your account is not in good standing, or if we otherwise suspect that there may be fraud associated with your Card.\r\nApple Pay allows you to make purchases using an added Card wherever Apple Pay is accepted. Apple Pay  may not be accepted at all places where your Card is accepted. \r\n4. Removal of your Card\r\nHIB may not permit the addition of a Card to Apple Pay, or may remove, suspend or cancel your access to Apple Pay at any time, if we cannot verify the Card, if we suspect that there may be fraud associated with the Card, if your account is not in good standing, if applicable laws change, or for any other reason determined by HIB in its sole discretion. You may suspend, delete or reactivate a Card from Apple Pay by following Apple's procedures for suspension, deletion or reactivation.  In certain circumstances, your Card may be suspended or removed from Apple Pay by Apple.\r\n5. Maximum Dollar Limit For Transactions\r\nPayment networks, HIB or Participating Merchants may, in their discretion, establish from time to time a maximum dollar limit for a single Transaction that may be completed using Apple Pay.  As a result, you may not be able to use Apple Pay to complete a Transaction, even if you have met the eligibility requirements for Apple Pay. If a Transaction is not completed because it exceeds a maximum dollar limit, we encourage you to use your physical Card instead of your Eligible Device to complete the Transaction.\r\n6. Applicable Fees\r\nWe do not charge you any fees for adding a Card to Apple Pay. Please consult your Card Agreement for any applicable fees, interest, or other charges associated with your Card. Your mobile service carrier or provider, Apple or other third parties may charge you service fees in connection with your use of your Eligible Device or Apple Pay. \r\n7. Security Requirements\r\nYou are required  to contact us immediately if your Card is lost or stolen, if your Eligible Device is lost or stolen, or if your Card account is compromised.  If you report your Card lost or stolen, you will be able to continue to make Transactions using Apple Pay on your Eligible Device unless you instruct us to suspend Apple Pay.  If you report your Eligible Device lost or stolen, you will be able to continue to make Transactions using your Card. If you get a new supported iOS device, please be sure to delete all your Cards and other personal information from your prior supported iOS device.\r\nYou are required to contact us immediately  if there are errors or if you suspect fraud with your Card. We will resolve any potential error or fraudulent purchase in accordance with your Card Agreement, as applicable. \r\nYou agree to protect and keep confidential your Apple User ID, Apple passwords (including your fingerprint set up for Touch ID, if applicable) and all other information required for you to make purchases with your Card using Apple Pay.  If you share these credentials with others, they may be able to access Apple Pay and make purchases with your Card or obtain your personal information.\r\nIn addition to your efforts to keep your credentials secure, we take reasonable steps to help ensure that information we send to others from your use of a Card in Apple Pay is sent in a secure manner. However, Apple is responsible for the security of information provided to Apple or stored in Apple Pay. HIB is not responsible if there is a security breach affecting information stored in or sent from Apple Pay.\r\nYou are prohibited from using Apple Pay on an Eligible Device that you know or have reason to believe has had its security or integrity compromised (e.g. where the device has been \"rooted\" or had its security mechanisms bypassed). You will be solely liable for any losses, damages and expenses incurred as a result of your use of Apple Pay on a compromised device.\r\n8. HIB Digital Reward Program\r\nBy adding a Card to Apple Pay you are automatically enrolled in the HIB Digital Reward Program (\u201CProgram\u201D). Subject to the following terms and conditions, under the Program you are entitled to receive, from time to time, benefits, products or services  that are provided by HIB or in participation with our program partners.  This may include coupons to receive goods, products, services, discounts, or other benefits from HIB or one of its program partners.  Such benefits may be tailored to your interests based on information we may have collected including your transaction information. In addition, from time to time you may receive personalized or exclusive rewards.\r\nThe first Program benefits are targeted to begin in the second half of 2016.\r\nHIB may restrict, suspend, terminate or otherwise amend the terms of the Program at any time without notice. Neither HIB nor its program partners have any responsibility or liability for any expenses, losses, costs or any other matter or thing however suffered or caused (including compensatory, incidental, indirect, special, punitive, consequential or exemplary damages or other losses) directly or indirectly arising out of or related to the Program, HIB\u2019s administration of the Program (including any amendment of the Program terms and conditions) or your participation in the Program.  HIB assumes no liability with respect to rewards claimed and in particular, has no liability for any delay or failure to deliver rewards or for their condition.\r\n9. Liability for Loss\r\nThe Cardholder is responsible for all Transactions and any resulting interest, fees or losses incurred that are made using your Card or via Apple Pay if you fail to comply with obligations described under \u201CSecurity Requirements\u201D until we receive written or verbal notice from you in accordance with that Section. \r\nHIB will not be liable for any losses you incur except as set out in these Terms of Service or specifically described in your Card Agreement, or otherwise provided by applicable law.\r\n10.  Privacy\r\nYou consent to the collection, use and sharing of your personal information from time to time as provided in HIB\u2019s privacy policy. HIB\u2019s privacy policy is available at any branch or at www.HIB.com. This policy may be amended, replaced or supplemented from time to time. HIB may share with or receive from Apple such information as may reasonably be necessary to determine your eligibility for, enrollment in and use of Apple Pay and any Apple Pay features you may select (for example, your name and Card details such as Card number and expiration date). Apple may aggregate your information with other information or make it anonymous and may use any such aggregated or anonymous information for purposes set out in its privacy policy or in the Apple Pay agreements or terms of use. Although your Transactions may be processed through Apple Pay, HIB does not permit Apple to view, use, or store your Transaction details, including your most recent Transactions if this feature is enabled.  To help protect you and HIB from error and criminal activities, HIB and Apple may share information  reasonably required for such purposes as fraud detection and prevention (for example, informing Apple if you notify HIB of a lost or stolen Eligible Device). \r\n11. Electronic Communications\r\nYou agree to receive electronic communications from us, including emails to the email address or text message to the mobile number you have provided in connection with your Card account. These electronic communications will relate to your use of your Card(s) in Apple Pay and to deliver your benefits to which you may be entitled under the Program. You agree to update your email address or mobile number when it changes by contacting us using the contact information in your agreements with HIB. You may also contact us if you wish to withdraw your consent to receive these electronic communications, but doing so will result in your inability to continue to use your Card(s) in the Apple Pay or receive benefits under the Program.\r\n12. No Warranty and Exclusion of Liability\r\nFor the purposes of this Section, \u201CHIB\u201D means HIB and its agents, contractors, distributors, channel partners and associated service providers, and each of their subsidiaries. All of the parties listed in the preceding sentence are third party beneficiaries of this Section. The provisions set out in this Section shall survive termination of these Terms of Service.\r\nApple Pay is provided by Apple and HIB is not responsible for its use or function. You acknowledge and agree that  HIB makes no representations, warranties or conditions relating to Apple Pay of any kind, and in particular: (a) HIB does not warrant the operability or functionality of Apple Pay or that Apple Pay will be available to complete a Transaction; (b) HIB does not warrant that any particular merchant will be a Participating Merchant at which Apple Pay is available; (c) HIB does not warrant that Apple Pay will meet your requirements or that the operation of Apple Pay will be uninterrupted or error-free; and (d) HIB does not guarantee the availability or operability of the wireless networks of any Eligible Device.  You may want to consider keeping your physical Card with you to use in the event you cannot make Apple Pay transactions.\r\nHIB will have no liability whatsoever in relation to Apple Pay, including without limitation in relation to the sale, distribution or use thereof, or the performance or non-performance of Apple Pay, or any loss, injury or inconvenience you suffer.\r\n13.  Changes to the Terms of Service\r\nWe may change, either permanently or temporarily, any term of these Terms of Service or replace these Terms of Service with another agreement, at any time. We will give you written notice of a change and any other information required by law at least 30 days before the change is stated to come into effect in the notice, by any method allowed by applicable law including by posting a notice on www.HIB.com/apple-pay. You may refuse the change by terminating these Terms of Service without cost, penalty or cancellation indemnity by stopping your use of Apple Pay within 30 days of the effective date of the change.\r\n14. Contacting Us\r\nYou may contact us about anything concerning mobile payment services or these Terms of Service by calling the phone number found on www.HIB.com/apple-pay.\r\nIf you have any questions, disputes or complaints about Apple Pay, you should contact Apple.\r\n15. Definitions\r\nIn these Terms of Service,\r\nCardholder means primary cardholder of any HIB credit card and the cardholder of any HIB debit card.\r\nCardholder Agreement means the agreements governing the credit card account associated with your HIB credit card, as it may be updated, enhanced or modified by HIB from time to time.\r\nHIB, we, our or us means Canadian Imperial Bank of Commerce and its subsidiaries.\r\nBanking Service Agreement means the agreements governing the debit card account associated with your HIB debit card, as it may be updated, enhanced or modified by HIB from time to time.\r\nEligible Device means any iPhone, iPad or other Apple device that can be used to make Transactions using Apple Pay.\r\nParticipating Merchant means a merchant that allows customers to complete Transactions using Apple Pay.\r\nTransaction means any credit or debit transaction made using Apple Pay, including any transactions by any HIB credit card authorized user.\r\nYou or your means each Cardholder.\r\n\r\n");
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		scrollPane_2.setViewportView(textArea);
		
		chckbxIHaveRead = new JCheckBox("I have read and agreed the terms and conditions");
		chckbxIHaveRead.setFont(new Font("Lucida Console", Font.BOLD | Font.ITALIC, 11));
		chckbxIHaveRead.setBackground(new Color(255, 255, 255));
		scrollPane_2.setColumnHeaderView(chckbxIHaveRead);
		
		JPanel acctSettingsPanel = new JPanel();
		acctSettingsPanel.setBackground(new Color(51, 153, 204));
		frame.getContentPane().add(acctSettingsPanel, "name_686432205571427");
		acctSettingsPanel.setLayout(null);
		
		JLabel lblAccountSettings = new JLabel("ACCOUNT SETTINGS");
		lblAccountSettings.setForeground(new Color(255, 153, 0));
		lblAccountSettings.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblAccountSettings.setBounds(132, 10, 414, 36);
		acctSettingsPanel.add(lblAccountSettings);
		
		Button termsOfService = new Button("TERMS OF SERVICES");
		termsOfService.setForeground(new Color(51, 0, 204));
		termsOfService.setFont(new Font("Tahoma", Font.BOLD, 14));
		termsOfService.setBounds(498, 100, 150, 50);
		acctSettingsPanel.add(termsOfService);
		
		Button resetPswdBtn = new Button("RESET PASSWORD");
		resetPswdBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		resetPswdBtn.setActionCommand("RESET PASSWORD");
		resetPswdBtn.setForeground(new Color(51, 0, 204));
		resetPswdBtn.setFont(new Font("Tahoma", Font.BOLD, 15));
		resetPswdBtn.setBounds(55, 280, 150, 50);
		acctSettingsPanel.add(resetPswdBtn);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(51, 153, 204));
		frame.getContentPane().add(panel, "name_687943829638489");
		panel.setLayout(null);
		
		JLabel lblResetPassword = new JLabel("RESET PASSWORD");
		lblResetPassword.setForeground(new Color(255, 153, 0));
		lblResetPassword.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblResetPassword.setBounds(163, 10, 377, 36);
		panel.add(lblResetPassword);
		backBtn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cards.show(frame.getContentPane(), "ACCOUNT INFO PANEL");
			}
		});
		backBtnAcctInfo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				cards.show(frame.getContentPane(), "MAIN PANEL");
			}
		});
		backBtnWid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cards.show(frame.getContentPane(), "MAIN PANEL");
				withdrawAmountTF.setText("");
			}
		});
	}
}