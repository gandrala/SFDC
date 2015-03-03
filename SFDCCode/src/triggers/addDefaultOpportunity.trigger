/*

Tigger.New contains a list of new versions of sObjects (available in insert and update triggers)
Trigger.Old contains a list of old versions of sObjects (available in update and delete triggers)

Trigger.newMap contains a map of IDs to the new versions of the sObject (available in before update, after insert, and after update triggers)
Trigger.oldMap contains a map of IDs to the old versions of the sObject records (available in update and delete triggers.)

Context variables isInsert,isUpdate,isDelete,isBefore,isAfter,isUndelete can be used to check the trigger operation mode.
Conext variable isExecuting can be used if the current conext of Apex code executing is a trigger and not a visualforce page or a Web service, or an executeanonymous() API call.
*/

trigger addDefaultOpportunity on Account (after insert, after update) 
{
	List<Opportunity> createOpptyList = new List<Opportunity>();
	Map<Id,Account> accountList = new Map<Id,Account>([SELECT Id,(SELECT Id FROM Opportunities) FROM Account WHERE Id IN:Trigger.New]);
	
	// Looping is bad
	/*for(Account a:Trigger.New)
	{
		if (accountList.get(a.Id).Opportunities.size()==0)
		{
			createOpptyList.add(new Opportunity(Name=a.Name+' Opportunity',StageName='Prospecting',CloseDate=System.today().addMonths(1),AccountId=a.Id));
		}
	}*/
	
	// in a single query we can get the list of accounts with no opportunity
	for (Account a :[SELECT Id,Name FROM Account WHERE Id NOT IN (SELECT AccountId from Opportunity) AND Id IN:Trigger.New])
	{
		createOpptyList.add(new Opportunity(Name=a.Name+' Opportunity',StageName='Prospecting',CloseDate=System.today().addMonths(1),AccountId=a.Id));
	}
	
	if (createOpptyList.size()>0)
	{
		Insert createOpptyList;
	}
}