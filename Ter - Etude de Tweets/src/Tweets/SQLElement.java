package Tweets;

import java.util.List;

public interface SQLElement {

	public abstract String getSQLTable();

	public abstract String getSQLId();

	public abstract String getSQLIdName();
	
	public abstract String getSQLValuesName();

	public abstract String getSQLValues();

	public abstract String getSQLOnDuplicate();
	
	public abstract List<String> getSQLSaveRequests();



}