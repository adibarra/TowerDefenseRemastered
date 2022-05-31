
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.ArrayList;

//Alec Ibarra
public class EntityKeeper {
	
	static ArrayList<Enemy> enemyQueue = new ArrayList<Enemy>();
	static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	static ArrayList<Turret> turrets = new ArrayList<Turret>();
	static ArrayList<Laser> lasers = new ArrayList<Laser>();
	static ArrayList<Particle> particles = new ArrayList<Particle>();
	
	static boolean world[][] = new boolean[(int)Logic.worldSize.getWidth()+1][(int)Logic.worldSize.getHeight()+2];
	
	static Rectangle upgrade1 = new Rectangle(75,275,50,25);
	static Rectangle upgrade2 = new Rectangle(125,275,50,25);
	static Rectangle upgrade3 = new Rectangle(175,275,50,25);
	
	static Rectangle buy = new Rectangle(0,275,25,25);
	static Rectangle sell = new Rectangle(25,275,25,25);
	static Rectangle upgrade = new Rectangle(50,275,25,25);
	
	static Rectangle backButton = new Rectangle(10,35,25,25);
	static Rectangle pauseButton = new Rectangle(410,35,25,25);
	    
	static Rectangle controls = new Rectangle(25,200,100,50);
	static Rectangle play = new Rectangle(150,200,150,50);
	static Rectangle difficulty = new Rectangle(325,200,100,50);

	static Rectangle pauseMenu = new Rectangle(150,75,150,50);
	static Rectangle pauseControls = new Rectangle(150,150,150,50);
	static Rectangle pauseDifficulty = new Rectangle(150,225,150,50);
	    
	static Rectangle easy = new Rectangle(150,75,150,50);
	static Rectangle medium = new Rectangle(150,150,150,50);
	static Rectangle hard = new Rectangle(150,225,150,50);

	static Area path = new Area();
	static Area tempRect;
	
	public static boolean isOccupied(int x, int y)
	{
		if(x/Logic.blockSize > Logic.worldSize.getWidth() || x/Logic.blockSize < 0)
		{
			return true;
		}
		else if(y/Logic.blockSize > Logic.worldSize.getHeight() || y/Logic.blockSize < 1)
		{
			return true;
		}
				
		return world[x/Logic.blockSize][y/Logic.blockSize];
	}
	
	public static void setOccupied(int x, int y, boolean value)
	{
		world[x/Logic.blockSize][y/Logic.blockSize] = value;
	}

	public static void buildPath()
	{	
		path = new Area(new Rectangle());
		
		tempRect = new Area(new Rectangle(2*25, 0*25, 25, 25));
			path.add(tempRect);
		tempRect = new Area(new Rectangle(2*25, 1*25, 25, 25));
			path.add(tempRect);
    	tempRect = new Area(new Rectangle(2*25, 2*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(2*25, 3*25, 25, 25));
    		path.add(tempRect);
    	//firstSide
    	tempRect = new Area(new Rectangle(3*25, 3*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(4*25, 3*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(5*25, 3*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(6*25, 3*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(7*25, 3*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(8*25, 3*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(9*25, 3*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(10*25, 3*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(11*25, 3*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(12*25, 3*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(13*25, 3*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(14*25, 3*25, 25, 25));
    		path.add(tempRect);
    	//secondDown
    	tempRect = new Area(new Rectangle(15*25, 3*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(15*25, 4*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(15*25, 5*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(15*25, 6*25, 25, 25));
    		path.add(tempRect);
    	//secondSide
    	tempRect = new Area(new Rectangle(3*25, 6*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(4*25, 6*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(5*25, 6*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(6*25, 6*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(7*25, 6*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(8*25, 6*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(9*25, 6*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(10*25, 6*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(11*25, 6*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(12*25, 6*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(13*25, 6*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(14*25, 6*25, 25, 25));
    		path.add(tempRect);
    	//thirdDown
    	tempRect = new Area(new Rectangle(2*25, 6*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(2*25, 7*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(2*25, 8*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(2*25, 9*25, 25, 25));
    		path.add(tempRect);
    	//thirdSide
    	tempRect = new Area(new Rectangle(3*25, 9*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(4*25, 9*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(5*25, 9*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(6*25, 9*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(7*25, 9*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(8*25, 9*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(9*25, 9*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(10*25, 9*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(11*25, 9*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(12*25, 9*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(13*25, 9*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(14*25, 9*25, 25, 25));
    		path.add(tempRect);
    	//fourthDown
    	tempRect = new Area(new Rectangle(15*25, 9*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(15*25, 10*25, 25, 25));
    		path.add(tempRect);
    	tempRect = new Area(new Rectangle(15*25, 11*25, 25, 25));
			path.add(tempRect);
		tempRect = new Area(new Rectangle(15*25, 12*25, 25, 25));
			path.add(tempRect);
	}
	
}
