/**
 Generates necessay test data.
 Actual testing methods can call loadAccountsWithOpportunities method with necessay paramters to generate necessay setup data.
 */
@isTest
public class TestUtility {


    public static List<Account> loadAccountsWithOpportunities(Integer numOfAccounts,Integer numOfOptysPerAccount)
    {
        List<Account> accountList = new List<Account>();
        for(Integer i=0;i<numOfAccounts;i++)
        {
        	Account a = new Account(Name='Testing Account '+i);
        	accountList.add(a);
        }
        insert accountList;
        
        List<Opportunity> opptyList = new List<Opportunity>();
        for (Account account : accountList)
        {
        	for (Integer j=0;j<numOfOptysPerAccount;j++)
        	{
        		Opportunity opty = new Opportunity(Name='Test Opty '+j,StageName='Prospecting',CloseDate=System.today().addMonths(1),AccountId=account.Id);
        		opptyList.add(opty);
        	}
        }
        insert opptyList;
        return accountList;
    }
}