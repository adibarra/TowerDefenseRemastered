//Alec Ibarra
public class Particle extends Entity{
	
	String type;
	
	Particle()
	{
		health = 100;
		type = "Bullet";
	}
	
	Particle(int MapPosX, int MapPosY, String type)
	{
		this.mapPosX = MapPosX;
		this.mapPosY = MapPosY;
		this.type = type;
		health = 100;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String getType()
	{
		return type;
	}
}
