public class ApexSOQLPattern {
	
	/*
	Fire inline SOQL by wrapping the SOQL query in square brackets. To bind variables in SOQL query use ":" notation.
	Need to mention the names of columns to retrieve. There is no all or * option like in SQL.
	No need to mention Id as it would be included always in the resultset.
	To filter the rows add the where clause which is case-insensitive.
	To order the rows add ORDER BY field name (ASC or DESC)
	To limit the number of rows use LIMIT n where n is the number of records to retrieve.
	To query child record, fire a inner query with the relationship name not the object name. For custom objects the relationship name ends with __r.
	To query lookup fields or in other words parent records from child can be queried by using relationship name and field name using dot notation.	
	For efficient processing and to get around governer limits use "SOQL for loop". The loop executes ones for every batch of 200 records.
	*/
	
	public static void getContacts()
	{
		Contact[] contacts = [SELECT Id,Name FROM Contact];
		Integer i = 0;
		for(Contact con : contacts)
		{
			i=i+1;
			system.debug(i+') Name:'+con.Name);
		}
	}
	// Lookup relatonship, from child accessing corresponding parent records
	public static void getContactsWithAccount()
	{
		Contact[] contacts = [SELECT Id,Name,Account.Name,Account.Region__c FROM Contact LIMIT 10];
		Integer i = 0;
		for(Contact con : contacts)
		{
			i=i+1;
			system.debug(i+') Contact Name:'+con.Name+' , Account Name:'+con.Account.Name+' , Account Region:'+con.Account.Region__c);
		}
	}
	// One to many relationship.
	public static void getAccountsWithContacts()
	{
		Integer i =0;
		Integer j =0;
		Account[] accountsWithContacts = [SELECT Id,Name,(SELECT Name FROM Contacts) FROM Account];
		for(Account account:accountsWithContacts)
		{
			i=i+1;
			System.debug(i+') Account Name:'+account.Name);
			j=0;
			for(Contact contact:account.Contacts)
			{
				j=j+1;
				System.debug(j+') Contact Name:'+contact.Name);
			}
		}
	}
}