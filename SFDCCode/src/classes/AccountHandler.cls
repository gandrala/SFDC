/*
Sample class to write test cases.
*/
public class AccountHandler 
{
	public static Account insertNewAccount(String accountName)
	{
		Account acct;		
		try
		{
			acct = new Account(Name=accountName);
			insert acct;		
		}
		catch(DMLException e)
		{
			System.debug('Error'+e.getMessage());
			acct=null;
		}
		return acct;
	}

}