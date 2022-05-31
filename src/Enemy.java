import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//Alec Ibarra
public class Enemy extends Entity{

	Enemy(){}
	
	Enemy(int mapPosX, int mapPosY)
	{
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		sizeX = 25;
		sizeY = 25;
		health = 1;
		speed = 2;
		money = 2;
	}
	
	Enemy(int mapPosX, int mapPosY, BufferedImage icon)
	{
		this.icon = icon;
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		sizeX = 25;
		sizeY = 25;
		health = 1;
		speed = 2;
		money = 2;
	}
	
	Enemy(int mapPosX, int mapPosY, double speed, int health, int damage)
	{
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.damage = damage;
		sizeX = 25;
		sizeY = 25;
		this.health = health;
		this.speed = speed;
		money = 2;
		dynamic = true;
	}
	
	Enemy(int mapPosX, int mapPosY, double speed, int health, BufferedImage icon, int damage)
	{
		this.damage = damage;
		this.icon = icon;
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		sizeX = 25;
		sizeY = 25;
		this.health = health;
		this.speed = speed;
		money = 2;
		dynamic = true;
		Enemy me = this;
		
		new Thread()
		{
			ArrayList<Point> points = new ArrayList<Point>();
			
			public void run()
			{
				points.add(new Point(50,75));
				points.add(new Point(375,75));
				points.add(new Point(375,150));
				points.add(new Point(50,150));
				points.add(new Point(50,225));
				points.add(new Point(375,225));
				points.add(new Point(375,325));
				
				while(points.size() > 0 && !me.isDead())//TODO add bloon splitting
				{	
					if(Logic.notPaused)
					{
						if(moveTo((float)points.get(0).getX(),(float)points.get(0).getY(),(float)getSpeed()))
						{
							points.remove(0);
						}					
					}
					Logic.delay(Logic.TARGET_FRAME_TIME);
				}
			}
		}.start();
	}
	
	Enemy(int mapPosX, int mapPosY, double speed, int health, BufferedImage icon, int damage, ArrayList<Point> pointz)
	{
		this.damage = damage;
		this.icon = icon;
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		sizeX = 25;
		sizeY = 25;
		this.health = health;
		this.speed = speed;
		money = 2;
		dynamic = true;
		Enemy me = this;
		
		new Thread()
		{
			ArrayList<Point> points = pointz;
			
			public void run()
			{	
				while(points.size() > 0 && !me.isDead())
				{	
					if(moveTo((float)points.get(0).getX(),(float)points.get(0).getY(),(float)getSpeed()))
					{
						points.remove(0);
					}
					Logic.delay(Logic.TARGET_FRAME_TIME);
				}
			}
		}.start();
	}
}
