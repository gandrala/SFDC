public class ApexTxnPattern 
{
	/*
	 Each DML statement (insert, update,upsert,delete,undelete,merge) accepts a single or list of sObjects.
	 Operating on a list of objects is more efficient which is called a bulk operations. 
	 150 DML statements per transactions is the limit. All the objects in a list would count as one DML.
	 DML statements throws exception if an error is encountered and rollback the transactions.
	 DML statemetns works on the concept of all or nothing basis.
	 
	 For partial commits use Database methods.The available methods are 
	 (Database.insert(),Database.update(),Database.upsert(),Database.delete(),Database.undelete(),Database.merge()).
	 The method take an allOrNone parameter as input. Pass the value as flase for partial commits, default is true.	 
	 	 
	*/ 
	public static void simpleDMLOperation(String accountName,String phone,String region)
	{
		try 
		{		
			Account acct = new Account(Name=accountName,Phone=phone);
			insert acct; // return an ID
			System.debug('Account Id:'+acct.ID);
			// We can use the same acct reference variable to update the row.
			acct.Region__c='APJC';
			update acct;	
		}
		catch(Exception e)
		{
			System.debug('Error Occurred:'+e.getMessage());
		}
		
	}
	public static void simpleBulkDMLOperation()
	{
		try
		{
			List<Contact> contactList = new List<Contact>();
			contactList.add(new Contact(FirstName='F1',LastName='L4',Department='IT'));
			contactList.add(new Contact(FirstName='F2',LastName='L4',Department='Finance'));
			contactList.add(new Contact(FirstName='F3',LastName='L4',Department='Legal'));
			contactList.add(new Contact(FirstName='F4',LastName='L4',Department='HR'));	
			// run bulk insert/update		
			upsert contactList; // using upsert so that when we run this method again it dosen't insert dupes.
			// use the same list to run a upate.
			List<Contact> contactUpdateList = new List<Contact>();			
			for (Contact con:contactList)
			{
				if (con.Department='Legal')
				{
					contactUpdateList.add(con);
				}
			}
			// run bult update
			update contactUpdateList;
		}
		catch(Exception e)
		{
			System.debug('Error occurred while processing:'+e.getMessage());
		}
	}
}