public class ApexSOQLPattern {
	
	/*
	Fire inline SOQL by wrapping the SOQL query in square brackets. To bind variables in SOQL query use ":" notation.
	To filter the rows add the where clause which is case-insensitive.
	To order the rows add ORDER BY field name (ASC or DESC)
	To limit the number of rows use LIMIT n where n is the number of records to retrieve.
	To query child record, fire a inner query with the relationship name not the object name. For custom objects the relationship name ends with __r.
	To query lookup fields or in other words parent records from child can be queried by using relationship name and field name using dot notation.	
	For efficient processing and to get around governer limits use "SOQL for loop". The loop executes ones for every batch of 200 records.
	*/
	

}