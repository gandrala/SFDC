/**
 * This class contains unit tests for validating the behavior of Apex classes
 * and triggers.
 *
 * Unit tests are class methods that verify whether a particular piece
 * of code is working properly. Unit test methods take no arguments,
 * commit no data to the database, and are flagged with the testMethod
 * keyword in the method definition.
 *
 * All test methods in an organization are executed whenever Apex code is deployed
 * to a production organization to confirm correctness, ensure code
 * coverage, and prevent regressions. All Apex classes are
 * required to have at least 75% code coverage in order to be deployed
 * to a production organization. In addition, all triggers must have some code coverage.
 * 
 * The @isTest class annotation indicates this class only contains test
 * methods. Classes defined with the @isTest annotation do not count against
 * the organization size limit for all Apex scripts.
 *
 * See the Apex Language Reference for more information about Testing and Code Coverage.
 */
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
}