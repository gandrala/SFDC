/**
	Visibility(private,public) of a test method dosen't matter, they are always accessable to testing framework.
	This the reason why modifier is ommitted for testing methods.
	if the test class is strictly for testing then we can declare the class as private.
	Annotate @isTest or return type of the method is testMethod. Annotation is a preferred approach.
	By default data in salesforce is not visibile for test method, to make it visible pass @isTest(SeeAllData=true)
	Data created during testing would not get committed to database.
	Cannot sent emails.
	Cannot make external callouts.
	Code does not count aganist the org limits
 */
@isTest
private class AccountHandlerTestingPattern {

	// One way of declaring the test method with return type testMethod
    static testMethod void testAccountcreation() 
    {
        Account a = AccountHandler.insertNewAccount('Test Account');
        // query the account to see if the record is inserted.
        Account acct = [Select Name From Account where Id=:a.Id];
        System.assertEquals('Test Account', acct.Name, 'testAccountcreation:Account Names should match');
        System.assertNotEquals('Test Account1', acct.Name, 'testAccountcreation:Account Names should not match');
    }
    
    // Another way of declaring the test method with annotation.
    // Eventhough there is an Account by name "One Account" exists it would not a be visibile for test method.
    // To get the visibility of existing data pass the param 
    @isTest    
    static void testAccountcreation2() 
    {
        Account a = AccountHandler.insertNewAccount('One Account');
        // query the account to see if the record is inserted.
        Account acct = [Select Name From Account where Name='One Account'];
        System.assertEquals('One Account', acct.Name, 'testAccountcreation2:Account Names should match');        
    }
}