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
	public static void SimpleDMLOperation(String accountName,String phone,String region)
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
}