//Alec Ibarra
public class Laser {

	int x1 = 0;
	int y1 = 0;
	int x2 = 0;
	int y2 = 0;
	String type = "Bullet";
	
	Laser(){}
	
	Laser(int x1, int y1, int x2, int y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	Laser(int x1, int y1, int x2, int y2, String type)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.type = type;
	}
	
	public int getx1(){
		return x1;
	}
	
	public int gety1(){
		return y1;
	}
	
	public int getx2(){
		return x2;
	}
	
	public int gety2(){
		return y2;
	}
	
	public String getType(){
		return type;
	}
}
