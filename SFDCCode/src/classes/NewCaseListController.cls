public class NewCaseListController 
{
	public List<Case> getNewCases()
    {
        String q = 'New';
        List<Case> results = [SELECT Id, CaseNumber FROM Case WHERE Status=:q];
        return results;                                    
    }
}