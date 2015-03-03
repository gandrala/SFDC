
@isTest
private class TestAccountDeletion {

	@isTest
    static void checkTriggerError() 
    {
        // setup the data
        // this scenario is trigger is going to prevent account deletion if there are any child opportunities.
        Account account = new Account(Name='Testing Account');
        insert account;
        //Associate account to the opportunity 
        Opportunity oppty = new Opportunity(Name='Test Opty',StageName='Prospecting',closeDate=System.today().addMonths(1),AccountId=account.Id);
        insert oppty;
        Test.startTest(); // this would give new governer limits
        Database.Deleteresult result = Database.delete(account,false);
        Test.stopTest();
        System.debug(result.isSuccess());
        // Verify the results
        System.assert(!result.isSuccess()); // isSuccess() should return false;
        System.assert(result.getErrors().size()>0,result.getErrors()[0].getMessage()); // should return >0                                
    }
    
    @isTest 
    static void checkTriggerError2()
    {
    	// call the untility method to insert test data;
    	Account[] accountList = TestUtility.loadAccountsWithOpportunities(5,5);
    	Test.startTest();
    	Database.Deleteresult[] result = Database.delete(accountList,false);
    	Test.stopTest();
    	System.debug('isSuccess'+result[0].isSuccess());
    	System.assert(!(result[0].isSuccess()));
    }
}