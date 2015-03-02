public class ApexSOSLPattern 
{
	/*
	SOSL is a search language. Searches fields across multiple objects 
	By default SOSL search is a wild card search like *Digital*
	SOQL can retrieve records from a single object but SOSL can return records of multiple objects	
	Pattern is FIND 'SearchQuery' [IN SearchGroup] [RETURNING ObjectsAndFields]
	- SearchQuery, text to search. It can be single or multiple. To search a phrase put it in double quotes like "Digital River". Case-insensitive. Can use OR and AND
	- SearchGroup, this is optional. If not specified then it searches all the fields. It can take one of the following
					ALL FIELDS
					NAME FIELDS
					EMAIL FIELDS
					PHONE FIELDS
					SIDEBAR FIELDS
	- ObjectsAndFields, Optional. Returns a list of one or more objects and within each sObject a list of one or mode fields. Possible to specify fiters, order by and limit
	Example 
		FIND 'global' IN ALL FIELDS RETURNING Account(Name), Contact(FirstName,LastName,Department)
		FIND 'global OR local' IN NAME FIELDS RETURNING Account(Name, Industry WHERE Industry='Apparel' ORDER BY Name LIMIT 10) 	
	*/
	
	public static void searchAccountsAndContacts(String SearchName)
	{
		List<List<sObject>> searchList = [FIND :searchName IN ALL FIELDS RETURNING Account(Name), Contact(FirstName,LastName)];
		Account[] accounts = (Account[])searchList[0];
		Contact[] contacts = (Contact[])searchList[1];
		System.debug('Accounts Found');
		Integer i=0;
		for(Account account:accounts)
		{
			i=i+1;
			System.debug(i+')'+account.Name);
		}
		i=0;
		System.debug('Contacts Found');
		for(Contact contact:contacts)
		{
			i=i=1;
			System.debug(i+')'+contact.FirstName+'||'+contact.LastName);
		}
	}

}