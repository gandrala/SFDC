public class Mailer 
{
	public void sendMail(String toAddress,String subject,String body)
	{
		Messaging.Singleemailmessage mail = new Messaging.Singleemailmessage();
		String[] addressList = new String[] {toAddress};
		mail.setToAddresses(addressList);
		mail.setSubject(subject);
		mail.setPlainTextBody(body);
		Messaging.Sendemailresult[] results = Messaging.sendEmail(new Messaging.Singleemailmessage[]{mail});
		// processing results
		for (Messaging.Sendemailresult result : results)
		{
			if (result.isSuccess())
			{
				System.debug('Mail sent successfully');
			}
			else
			{
				System.debug('Errors while sending email'+result.getErrors());
			}
		}
	}

}