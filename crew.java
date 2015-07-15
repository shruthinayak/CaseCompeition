package gsonExample;

public class crew {
	String crewid;
	String fname;
	String lname;
	flights flight;

	public String getcrewid()
	{
		return crewid;
	}
	public String getfname()
	{
		return fname;
	}
	public String getlname()
	{
		return lname;
	}
	public flights getflights()
	{
		return flight;
	}
	public void setcrewid(String value)
	{
		this.crewid = value;
	}
	public void setfname(String value)
	{
		this.fname = value;
	}
	public void setlname(String value)
	{
		this.lname = value;
	}
	public void setflight(flights value)
	{
		this.flight = value;
	}
}
