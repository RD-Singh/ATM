import java.text.SimpleDateFormat;
import java,util.String;
import java.util.Date;
public class BankAccount 
{
	private	int accountNum;
	private	double balance;
	private	String firstName;
	private	String middleName;
	private	String lastName;
	private	String password;
	private	String message;
	
	void setFName(String str)
	{
		firstName = str;
	}
	
	void setMName(String str)
	{
		middleName = str;
	}
	
	void setLName(String str)
	{
		lastName = str;
	}
	
	void setAcctNum(int x)
	{
		accountNum = x;
	}
	
	void setBalance(double bal)
	{
		balance = bal;
	}
	BankAccount(String	fName,	String	lName,	int acctNum)
	{
		accountNum = acctNum;
		firstName = fName;
		lastName = lName;
		password = fName.charAt(0) + "" + fName.charAt(1) + "" + fName.charAt(2) + "" + lName.charAt(lName.length() - 2) + "" + lName.charAt(lName.length() - 1)+ "";
		balance = 0;
		middleName = "";
		message = "";
		
	}
	
	int getAccountNum ()
	{
		return accountNum;
	}
	
	BankAccount(double amt,	String	fName,	String	mName,	String	lName,	int acctNum, String pswd)
	{
		firstName = fName;
		middleName = mName;
		lastName = lName;
		password = pswd;
		accountNum = acctNum;
		balance = amt;
		message = "";
		
	}
	String getDate()
	{
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		return timeStamp;
	}
	
	boolean deposit (double amount)
	{
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		if (amount > 0)
		{
			
			message += timeStamp + "\nDeposit of amount made\n";
			balance += amount;
			return true;
		}
		else
		{
			message += timeStamp + "\nDeposit unsuccessful: invalid amount\\n";
			return false;
		}
	}
	
	double getBalance()
	{
		return balance;
	}
	
	String getPassword()
	{
		return password;
	}
	String getMessage()
	{
		return message;
	}
	void emptyAccount()
	{
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		balance = 0;
		message += timeStamp + "\n So much for \"Leaving something for a rainy day!\"";
	}
	
	boolean withdraw (double amount)
	{
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		if (amount > 0 && amount <= balance)
		{
			message += timeStamp + "\nWithdraw successful\\n";
			balance -= amount;
			return true;
		}
		else
		{
			message += timeStamp + "\nWithdraw unsuccessful: invalid amount or insufficient balance in account to complete withdrawl\\n";
			return false;
		}
		
	}
	
	boolean transferTo	(double amount,	BankAccount	otherAcct)
	{
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		if(withdraw(amount))
		{
			
			otherAcct.deposit(amount);
			message += timeStamp + "\nTransfer successfuly made\\n";
			return true;
		}
		else
		{
			message += timeStamp + "\nTransfer unsuccessful";
			return false;
		}
		
		
	}
	
	boolean transferFrom (double amount, BankAccount otherAcct,	String pswd)
	{
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		if (otherAcct.checkPassword(pswd))
		{
			otherAcct.withdraw(amount);
			deposit(amount);
			message += timeStamp + "\n Transfer to account of amount successful\n";
			return true;
		}
		else 
		{
			message += timeStamp + "\nTransfer unsuccessful\n";
			return false;
		}
	}
	
	boolean	resetPassword (String	currentPassword,	String	newPassword)
	{
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		if (checkPassword(password))
		{
			password = newPassword;
			message += timeStamp + "\nPassword successfully changed\n";
			return true;
		}
		else
		{
			message += timeStamp + "\nPassword change unsuccessful\n";
			return false;
		}
	}
	
	boolean checkPassword (String pswd)
	{
		return password.equals(pswd);
	}
	void display()
	{
		System.out.println(accountNum);
		System.out.println(message);
		System.out.println(password);
		System.out.println(firstName);
		System.out.println(middleName);
		System.out.println(lastName);
		System.out.println(balance);
		
	}
	
	public static void main (String args[])
	{
		
	}
	
}
