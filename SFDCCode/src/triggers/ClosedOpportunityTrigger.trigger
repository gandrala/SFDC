trigger ClosedOpportunityTrigger on Opportunity (after insert, after update) 
{
	List<Task> taskList = new List<Task>();
	for (Opportunity oppty : Trigger.new)
	{
		if (Trigger.isInsert)
		{
			if (oppty.StageName=='Closed Won')
			{
				 taskList.add(new Task(Subject = 'Follow Up Test Task', WhatId = oppty.Id));
			}
		}
		
		if (Trigger.isUpdate)
		{
			if (oppty.StageName=='Closed Won' && oppty.StageName!=Trigger.oldMap.get(Oppty.Id).StageName)
			{
				taskList.add(new Task(Subject = 'Follow Up Test Task', WhatId = oppty.Id));
			}
		}
	}
 	if (taskList.size()>0)
 	{
 		insert taskList;
 	}
}