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
			contactList.add(new Contact(FirstName='F1',LastName='L1',Department='IT',email='f1@l1.com'));
			contactList.add(new Contact(FirstName='F2',LastName='L2',Department='Finance',email='f2@l2.com'));
			contactList.add(new Contact(FirstName='F3',LastName='L3',Department='Legal',email='f3@l3.com'));
			contactList.add(new Contact(FirstName='F4',LastName='L4',Department='HR',email='f4@l4.com'));	
			// run bulk insert/update		
			// using upsert so that when we run this method again it dosen't insert dupes.
			upsert contactList Contact.Fields.email; // unsing email index field to check for dupes, only PK,index,external id fields are allowed
			// use the same list to run a upate.
			List<Contact> contactUpdateList = new List<Contact>();			
			for (Contact con:contactList)
			{								
				if (con.Department.equals('Legal'))
				{
					con.Department='Sales';
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
	public static void simpleDatabaseMethodOperation()
	{
			List<Contact> contactList = new List<Contact>();
			contactList.add(new Contact(FirstName='FF1',LastName='LL1',Department='IT',email='ff1@ll1.com'));
			contactList.add(new Contact(FirstName='FF2',LastName='LL2',Department='Finance',email='ff2@ll2.com'));			
			contactList.add(new Contact()); // this should throw error
			// bulk insert with partial commits allowed
			Database.UpsertResult[] results = Database.upsert(contactList,Contact.Fields.email,false); // pass false to allow partial updates.
			// iterate the results to check for errors.
			for (Database.UpsertResult result:results)
			{
				if (result.isSuccess())
				{
					System.debug('Success:'+result.getId());
				}
				else
				{
						for (Database.Error err : result.getErrors())
						{
							System.debug('List of errors');
							System.debug('Err Code:'+err.getStatusCode()+':'+err.getMessage());
							System.debug('List of fields affected:'+err.getFields());
						}
				}				
			}								
	}
	public static void simpleRelatedRecordOperation(String accountName,String phone,String fName,String lName,String cPhone)
	{		
		Account acct = new Account(Name=accountName,Phone=phone);
		insert acct;
		System.debug('Account Id:'+acct.Id);
		Contact con = new Contact(FirstName=fName,LastName=lName,Phone=cPhone,AccountId=acct.Id); // acct.Id is going to build the relationship
		insert con;
		System.debug('Contact Id:'+con.Id);
		// Fields on the related records cannot be updated in single DML. Need to fire seperate DML's		
		Contact contact = [SELECT Account.Name FROM Contact WHERE FirstName=:fName and LastName=:lName LIMIT 1];
		// to update contact do the following
		contact.Phone='+65 8989 7766';
		update contact;
		// to update the account do the following
		contact.Account.Region__c='EMEA';
		update contact.Account;
	}
}