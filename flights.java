package gsonExample;

public class flights {
	String code;
	String num;
	String dep;
	Boolean checkin;
	String lat;
	String lon;
	
	public String getCode()
	{
		return code;
	}
	public String getNum()
	{
		return num;
	}
	public String getDep()
	{
		return dep;
	}
	public Boolean getCheckin()
	{
		return checkin;
	}
	public String getLat()
	{
		return lat;
	}
	public String getLon()
	{
		return lon;
	}
	public void setCode(String value)
	{
		this.code = value;
	}
	public void setNum(String value)
	{
		this.num = value;
	}
	public void setDep(String value)
	{
		this.dep = value;
	}
	public void setCheckin(Boolean value)
	{
		this.checkin = value;
	}
	public void setLat(String value)
	{
		this.lat = value;
	}
	public void setLon(String value)
	{
		this.lon = value;
	}
}
