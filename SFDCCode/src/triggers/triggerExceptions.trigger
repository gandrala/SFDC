/*
 Use addError method to return an error message to UI from triggers.
 addError prevents records being saved and throws a fatal error
*/
trigger triggerExceptions on Account (before delete) 
{
	// Here prevent error when an account having opportunities getting deleted.
	// Get the account having opportunity records
	for (Account a:[SELECT Id FROM Account WHERE Id IN (SELECT AccountId FROM Opportunity) AND Id IN :Trigger.old])
	{
		Trigger.oldMap.get(a.Id).addError('Opportunity records exsits, deletion not allowed');
	}
}