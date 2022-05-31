import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

//Alec Ibarra
public class TowerDefenseRemastered
{

    public static void main(String[] args)
    {
    	Logic game = new Logic();
		
		game.addWindowListener(new WindowAdapter()
		{public void windowClosing(WindowEvent e)
		{System.exit(0);}});
		game.setSize((18*25),(11*25)+29);
		game.setResizable(false);
		game.setVisible(true);
    
		game.addComponentListener(new ComponentListener() {
        public void componentResized(ComponentEvent e) {}
		public void componentMoved(ComponentEvent e) {}
		public void componentShown(ComponentEvent e) {}
		public void componentHidden(ComponentEvent e) {}
		});
    }
}

@SuppressWarnings("serial")
class Logic extends Frame implements MouseListener,MouseMotionListener,KeyListener,MouseWheelListener
{
	//useful variables
	static int blockSize = 25;
	static Dimension worldSize = new Dimension(18,11);
	static Dimension screenSize = new Dimension((worldSize.width*blockSize),(worldSize.height*blockSize)+29);
	
	static boolean gameOver = false;	
	static boolean gridToggled = true;
	static boolean notPaused = true;
	static boolean drawDebug = false;
	static boolean drawGhostTurret = false;
		
	static boolean gameNotStarted = true;
	static boolean showControls = false;
	static boolean chooseDifficulty = false;
	static boolean showPauseMenu = false;
	
	static double waveDifficulty = 1;
	static int enemiesRemaining = 0;
	static int wavesRemaining = 0;
	static int currentRound = 0;
	static int turretChooser = 0;//current turret to place
	static int maxTurrets = 2;//number of turrets in game-1
	static int mousex = 0;
	static int mousey = 0;
	static int mousex2 = 0;
	static int mousey2 = 0;
	static int clicks = 0;
	
	static boolean buy = false;
	static boolean sell = false;
	static boolean upgrade = false;
	
	static final double TARGET_FRAME_TIME = 1000/60;
	static double start = System.nanoTime();
	static double elapsed = 0;
	
	//display setup
	static BufferedImage offscreen = null;
    static Graphics g2;
    static RenderingHints rh;
    
    //set up class instances
	static Turret choosenTurret = null;
	static Player player = new Player();
	static ImageLoader im = new ImageLoader();
	
	//set up temps
	static Turret tempTurret = new Turret();
	static Enemy tempEnemy = new Enemy();
	static Enemy tempEnemy2 = new Enemy();
	static Enemy target = new Enemy();
	static Laser laser = new Laser();
	static Particle tempParticle = new Particle();
	

	public Logic()
    {
        super("Tower Defense Remastered");//sets program name
        List<Image> icons = new ArrayList<Image>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Icon128x128.png")));//loads icon 128x128
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Icon64x64.png")));//loads icon 64x64
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Icon32x32.png")));//loads icon 32x32
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Icon16x16.png")));//loads icon 16x16
        super.setIconImages(icons);//loads icons
        addMouseListener(this);//adds neccessary listener
		addMouseMotionListener(this);//adds neccessary listener
		addMouseWheelListener(this);//adds neccessary listener
		addKeyListener(this);//adds neccessary listener
        requestFocusInWindow();//calls focus for window
        
        EntityKeeper.buildPath();//builds path area
        Spawner.start();//starts enemy spawning
        
        rh = new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    }
	
	
	/***************************************** 
				Main method
	*****************************************/

	public void update(Graphics g)//main method
    {    			
		if(!gameOver)
		{	
			if(notPaused)
			{
				//prepare double buffer
				offscreen = (BufferedImage)createImage(screenSize.width,screenSize.height);
				g2 = offscreen.getGraphics();
			    ((Graphics2D) g2).setRenderingHints(rh);
				
				if(gameNotStarted && !showControls && !chooseDifficulty)
				{
					gameNotStarted(g2);
				}
				else if(showControls && gameNotStarted)
				{
					drawControls(g2);
				}
				else if(chooseDifficulty && gameNotStarted)
				{
					drawDifficultyChooser(g2);
				}
				else if(!gameNotStarted)
				{
					runLogic();//main logic method
					display(g2);//main display method
				}	
			}
			else
			{
				if(gameNotStarted && !showControls && !chooseDifficulty)//draws title page
				{
					gameNotStarted(g2);
				}
				else if(showControls && !gameNotStarted && !chooseDifficulty)//draws controls page
				{
					drawControls(g2);
				}
				else if(chooseDifficulty && !gameNotStarted && !showControls)//draws difficulty chooser page
				{
					drawDifficultyChooser(g2);
				}
				else
				{	
					drawPauseMenu(g2);
				}	
			}
		}
		else
		{	
			drawGameOver(g2);//draw GameOver
			g.drawImage(offscreen,0,21,this);
			delay(5000);
			reset();
			gameNotStarted = true;
			gameOver = false;
		}
		
		g.drawImage(offscreen,0,0,this);

		start = System.nanoTime();//start timer
		elapsed = (System.nanoTime()-start)/1000000000;//records looptime at this point in seconds
		delay(fix(TARGET_FRAME_TIME-elapsed));
		repaint();
	}
	
	public void gameNotStarted(Graphics g2)
	{
		g2.drawImage(ImageLoader.getImage("SplashScreen"),0,21,this);
		
		g2.setColor(new Color(255,255,255,125));
		((Graphics2D) g2).fill(EntityKeeper.controls);//controls
		((Graphics2D) g2).fill(EntityKeeper.play);//play
		((Graphics2D) g2).fill(EntityKeeper.difficulty);//difficulty
		
		g2.setColor(new Color(255,255,255,200));
		if(EntityKeeper.controls.contains(mousex,mousey))
    			((Graphics2D) g2).fill(EntityKeeper.controls);
		else if(EntityKeeper.play.contains(mousex,mousey))
    			((Graphics2D) g2).fill(EntityKeeper.play);
    		else if(EntityKeeper.difficulty.contains(mousex,mousey))
    			((Graphics2D) g2).fill(EntityKeeper.difficulty);
    		g2.setColor(Color.BLACK);
		
		((Graphics2D) g2).draw(EntityKeeper.controls);//controls
		((Graphics2D) g2).draw(EntityKeeper.play);//play
		((Graphics2D) g2).draw(EntityKeeper.difficulty);
		
		g2.setFont(new Font("Ariel",Font.BOLD,15));
		g2.drawString("CONTROLS", EntityKeeper.controls.x+8, EntityKeeper.controls.y+30);
		g2.drawString("DIFFICULTY", EntityKeeper.difficulty.x+6, EntityKeeper.difficulty.y+30);
		g2.setFont(new Font("Ariel",Font.BOLD,40));
		g2.drawString("PLAY", EntityKeeper.play.x+25, EntityKeeper.play.y+40);
	}
	
	
	/***************************************** 
				Logic methods
	*****************************************/
    
    public void runLogic()//main logic method
    {
       	worldLogic();
       	enemyLogic();
       	sentryLogic();
    }
    
    public void worldLogic()//world logic method
    {
    		while(EntityKeeper.enemyQueue.size() > 0)
    			EntityKeeper.enemies.add(EntityKeeper.enemyQueue.remove(0));
    }
    
    public void enemyLogic()//enemy logic method
    {
	    	for (int k = 0; k < EntityKeeper.enemies.size(); k++)
	    	{
	    		tempEnemy = EntityKeeper.enemies.get(k);
	    		
	    		if(tempEnemy.getMapPosY() > 300)
	    		{
	    			EntityKeeper.enemies.remove(k);//kill enemy if offscreen
	    			player.setHealth((int)(player.getHealth()-tempEnemy.getDamage()));
	    			tempEnemy = null;//java gc will delete original object
	    			
	    			if (player.getHealth() <= 0)
	    				gameOver = true;
	    		}
       	}
    }
    
    public void sentryLogic()//sentry logic method
    {
	    	if(!EntityKeeper.turrets.isEmpty())
	    	{
	    		for(int k = 0; k < EntityKeeper.turrets.size(); k++)
	    		{
	    			boolean chosenTarget = false;
	    			target = new Enemy(-110,-110);
	    			tempTurret = EntityKeeper.turrets.get(k);
	    			int sentryx = tempTurret.getMapPosX();
	    			int sentryy = tempTurret.getMapPosY();
	    			chosenTarget = false;
	    			int targetNum = 0;
	    			
	    			if(!EntityKeeper.enemies.isEmpty())
	    			{
	    				for(int j = 0; j < EntityKeeper.enemies.size(); j++)
	    				{
	    					tempEnemy = EntityKeeper.enemies.get(j);
	    					int enemyx = tempEnemy.getMapPosX()+12;
	    					int enemyy = tempEnemy.getMapPosY()+12;
	    					
	    					if(Math.sqrt(Math.pow(enemyx-sentryx,2)+Math.pow(enemyy-sentryy,2)) <= tempTurret.getRange())
	    					{
	    						target = tempEnemy;
	    						chosenTarget = true;
	    						targetNum = j;
	    					}
	    				}
	    				
	    				if(chosenTarget)
	    				{
	    					if(tempTurret.getCooldown() < 0)
	    					{
	    						tempTurret.setTarget(target);
	    						
	    						if(target.getMapPosX() > -110 && target.getMapPosY() > -110)
	    							EntityKeeper.lasers.add(new Laser(sentryx,sentryy,target.getMapPosX()+12,target.getMapPosY()+12,tempTurret.getType()));
	    						
	    						target.setHealth((int)(target.getHealth()-tempTurret.getDamage()));
	    						tempTurret.setCooldown(tempTurret.getMaxCooldown());
	    						
	    						if(target.isDead())
	    						{
	    							player.addMoney(target.getMoney());
	    							player.addToKillCount();
	    							EntityKeeper.enemies.remove(targetNum);
	    							target = null;//java gc will delete original object
	    						}
	    					}
	    				}
	    			}
	    			tempTurret.setCooldown(tempTurret.getCooldown()-1);
	    		}
	    	}
    }	
    
    
    /***************************************** 
				Display methods
	*****************************************/
    
    public void display(Graphics g2)//main display method
    {	
	    	drawWorld(g2);//draws the world when needed
	    	drawBuildings(g2);//draws the buildings in the world
	    	drawEnemies(g2);//draws the enemies in the enemies array
	    	drawGrid(g2);//draws the grid overlay
	    	drawHUD(g2);//draws the player HUD
    }
    
    public void drawWorld(Graphics g2)//draws the world and clears screen if neccessary
    {	
	    	g2.setColor(new Color(100,165,51));//dark green
	    	g2.fillRect(0,0,(int)screenSize.getWidth(),(int)screenSize.getHeight());
	    	g2.setColor(new Color(200,190,150));//path color
	    	((Graphics2D) g2).fill(EntityKeeper.path);//draw path
    }
    
    public void drawBuildings(Graphics g2)//draws buildings
    {	
	    	if(upgrade)
	    	{
	    		g2.setColor(new Color(255,255,255,150));
			if(choosenTurret != null)
				g2.fillRect(choosenTurret.getMapPosX()-13,choosenTurret.getMapPosY()-13,25,25);
			else if(EntityKeeper.turrets.size() != 0)
				choosenTurret = EntityKeeper.turrets.get(0);
	    	}
			
		if(!EntityKeeper.path.contains(mousex2,mousey2) && EntityKeeper.isOccupied(mousex2,mousey2))
		{	
			g2.setColor(new Color(255,255,255,50));
			g2.fillRect(mousex2,mousey2,25,25);
		}
	    	
	    	for (int k = 0; k < EntityKeeper.turrets.size(); k++)
	    {
	    		tempTurret = EntityKeeper.turrets.get(k);
	    		
	    		tempTurret.setRotation(Math.toDegrees(Math.atan2(tempTurret.getTarget().getMapPosY()-tempTurret.getMapPosY(),
	    		tempTurret.getTarget().getMapPosX()-tempTurret.getMapPosX()))+90);
	    		AffineTransform at = new AffineTransform();
	        		at.translate(tempTurret.getMapPosX(),tempTurret.getMapPosY());//position image
	        		at.rotate(Math.toRadians(tempTurret.getRotation()));
	            	at.translate((double)(-tempTurret.getIcon().getWidth()/2),(double)(-tempTurret.getIcon().getHeight()/2));
	            ((Graphics2D) g2).drawImage(tempTurret.getIcon(),at,this);
	        		
	    }
	    	
	    	if(EntityKeeper.pauseButton.contains(mousex, mousey) || EntityKeeper.buy.contains(mousex, mousey) || 
					EntityKeeper.sell.contains(mousex, mousey) || EntityKeeper.upgrade.contains(mousex, mousey) || !buy)
		{
			drawGhostTurret = false;
		}
		else if(buy)
		{
			drawGhostTurret = true;
		}
	    	
	    	if (drawGhostTurret)
	    	{
	    		if(!EntityKeeper.path.contains(mousex2,mousey2) && !EntityKeeper.isOccupied(mousex2,mousey2))
	    		{
	    			BufferedImage currentImage = ImageLoader.getImage("NoTexture");
	    			
	    			if(turretChooser == 0)
	    				currentImage = ImageLoader.getImage("TurretBullet");
	    			else if(turretChooser == 1)
	    				currentImage = ImageLoader.getImage("TurretFire");
	    			else if(turretChooser == 2)
	    				currentImage = ImageLoader.getImage("TurretTesla");
	    			
	    			((Graphics2D) g2).drawImage(currentImage,mousex2,mousey2,this);
	    			
	    			g2.setColor(new Color(0,0,255,20));
	    			g2.fillOval((int)(mousex2-tempTurret.getStartRange()+12),(int)(mousey2-tempTurret.getStartRange()+12),(int)tempTurret.getStartRange()*2,(int)tempTurret.getStartRange()*2);
	    		}
	    	}
	    	
	    	if (drawDebug || upgrade)//draw turret ranges
	    	{
	    		g2.setColor(new Color(0,0,255,20));
	    		for (int k = 0; k < EntityKeeper.turrets.size(); k++)
	    		{
	    			tempTurret = EntityKeeper.turrets.get(k);
	    			g2.fillOval((int)(tempTurret.getMapPosX()-tempTurret.getRange()),(int)(tempTurret.getMapPosY()-tempTurret.getRange()),(int)tempTurret.getRange()*2,(int)tempTurret.getRange()*2);
	    		}
	    	}	
    }
    
    public void drawEnemies(Graphics g2)//draws enemies
    {
	    	for (int k = 0; k < EntityKeeper.enemies.size(); k++)
	    	{
	    		tempEnemy = EntityKeeper.enemies.get(k);
	    		g2.drawImage(tempEnemy.getIcon(),tempEnemy.getMapPosX(),tempEnemy.getMapPosY(),this);
	    		
	    		if (drawDebug)//draw enemy bounding boxes
	        	{
	    			g2.setColor(new Color(255,0,0));
	    			((Graphics2D) g2).draw(tempEnemy.getBoundingBox());
	        	}
	    	}
	    	
	    	for (int k = 0; k < EntityKeeper.lasers.size(); k++)
	    	{
	    		laser = EntityKeeper.lasers.get(k);
	    		g2.setColor(new Color(0,0,255));
	    		g2.drawLine(laser.getx1(),laser.gety1(),laser.getx2(),laser.gety2());
	    		addParticles(laser.getx2(),laser.gety2(),10,laser.getType());
	    		EntityKeeper.lasers.remove(k);
	    	}
	    	
	    	for (int k = 0; k < EntityKeeper.particles.size(); k++)
	    	{
	    		tempParticle = EntityKeeper.particles.get(k);
	    		tempParticle.setHealth(tempParticle.getHealth()-1);
	    		
	    		if (tempParticle.isDead())//if the particle is out of health then delete
	    		{
	    			EntityKeeper.particles.remove(k);
	    		}
	    		else//if the particle still has health then render with transparency
	    		{
	    			float alpha = tempParticle.getHealth()/100f;//calculate transparency to draw with
	    			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);//set transparency
	    			((Graphics2D) g2).setComposite(ac);//set transparency
	    			
	    			if(tempParticle.getType().equals("Bullet"))
	    			{
	        			g2.drawImage(ImageLoader.getImage("EffectBullet"),tempParticle.getMapPosX(),tempParticle.getMapPosY(),this);
	    			}
	    			else if(tempParticle.getType().equals("Fire"))
	    			{
	        			g2.drawImage(ImageLoader.getImage("EffectFire"),tempParticle.getMapPosX(),tempParticle.getMapPosY(),this);
	    			}
	    			else if(tempParticle.getType().equals("Tesla"))
	    			{
	        			g2.drawImage(ImageLoader.getImage("EffectTesla"),tempParticle.getMapPosX(),tempParticle.getMapPosY(),this);
	    			}
	    			
	    			ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f);//reset transparency
	        		((Graphics2D) g2).setComposite(ac);//reset transparency
	    		}
	    	}
    	
    }
    
    public void drawHUD(Graphics g2)//draws HUD overlay
    {
	    	g2.setColor(new Color(230,230,230));
	    	g2.setFont(new Font("Ariel",Font.BOLD,10));//needs to be set every run
	    	g2.drawString("$"+String.valueOf(player.getMoney()),2,40);
	    	g2.drawString("Round: "+String.valueOf(Logic.currentRound),(int)screenSize.getWidth()/2-30,40);
	    	g2.drawString("Lives: "+String.valueOf(player.getHealth()),(int)screenSize.getWidth()-55,40);
	    	g2.setFont(new Font("Ariel",Font.BOLD,10));//needs to be set every run
	    	
	    	if(EntityKeeper.pauseButton.contains(mousex,mousey))
	    	{
	    		g2.setColor(new Color(255,255,255,200));
	    		((Graphics2D) g2).fill(EntityKeeper.pauseButton);
	    	}
	    	g2.drawImage(ImageLoader.getImage("GUIPause"), EntityKeeper.pauseButton.x, EntityKeeper.pauseButton.y, this);
	    	
	    	if(EntityKeeper.buy.contains(mousex,mousey) || buy)
	    	{
	    		g2.setColor(new Color(255,255,255,100));
	    		((Graphics2D) g2).fill(EntityKeeper.buy);
	    	}
	    	else if(EntityKeeper.sell.contains(mousex,mousey) || sell)
	    	{
	    		g2.setColor(new Color(255,255,255,100));
	    		((Graphics2D) g2).fill(EntityKeeper.sell);
	    	}
	    	else if(EntityKeeper.upgrade.contains(mousex,mousey) || upgrade)
	    	{
	    		g2.setColor(new Color(255,255,255,100));
	    		((Graphics2D) g2).fill(EntityKeeper.upgrade);
	    	}
	    	
	    	if(upgrade)
	    	{
	    		if(EntityKeeper.upgrade1.contains(mousex,mousey))
	        	{
	        		g2.setColor(new Color(255,255,255,100));
	        		((Graphics2D) g2).fill(EntityKeeper.upgrade1);
	        	}
	    		else if(EntityKeeper.upgrade2.contains(mousex,mousey))
	        	{
	        		g2.setColor(new Color(255,255,255,100));
	        		((Graphics2D) g2).fill(EntityKeeper.upgrade2);
	        	}
	    		else if(EntityKeeper.upgrade3.contains(mousex,mousey))
	        	{
	        		g2.setColor(new Color(255,255,255,100));
	        		((Graphics2D) g2).fill(EntityKeeper.upgrade3);
	        	}
	    		
	    		g2.setColor(new Color(255,255,255,200));
	        	g2.drawString("Range",EntityKeeper.upgrade1.x+10,EntityKeeper.upgrade1.y+15);
	        	g2.drawString("Speed",EntityKeeper.upgrade2.x+10,EntityKeeper.upgrade2.y+15);
	        	g2.drawString("Damage",EntityKeeper.upgrade3.x+5,EntityKeeper.upgrade3.y+15);
	        	g2.setColor(new Color(0,0,0,100));
	    		((Graphics2D) g2).draw(EntityKeeper.upgrade1);
	    		((Graphics2D) g2).draw(EntityKeeper.upgrade2);
	    		((Graphics2D) g2).draw(EntityKeeper.upgrade3);
	    	}
	    	
	    	g2.setColor(new Color(255,255,255,200));
	    	g2.drawString("B",EntityKeeper.buy.x+10,EntityKeeper.buy.y+15);
	    	g2.drawString("S",EntityKeeper.sell.x+10,EntityKeeper.sell.y+15);
	    	g2.drawString("U",EntityKeeper.upgrade.x+10,EntityKeeper.upgrade.y+15);
	    	g2.setColor(new Color(0,0,0,100));
		((Graphics2D) g2).draw(EntityKeeper.buy);
		((Graphics2D) g2).draw(EntityKeeper.sell);
		((Graphics2D) g2).draw(EntityKeeper.upgrade);
    }
    
    public void drawGrid(Graphics g2)//draws grid overlay
    {
	    	if(gridToggled)
	    	{
	    		g2.setColor(new Color(0,0,0,20));
	    		for (int k = 0; k < worldSize.width; k++)
	    			g2.drawLine(k*blockSize,0,k*blockSize,worldSize.width*blockSize);
	    		
	    		for (int k = 0; k < worldSize.height+2; k++)
	    			g2.drawLine(0,k*blockSize,worldSize.width*blockSize,k*blockSize);
	    	}
    }
    
    public void drawPauseMenu(Graphics g2)
    {
	    	g2.drawImage(ImageLoader.getImage("SplashScreen"),0,21,this);
	    	g2.setColor(new Color(255,255,255,200));
	    	g2.fillRect(0,0,(int)screenSize.getWidth(),(int)screenSize.getHeight());
	    	g2.setColor(Color.BLACK);
	    	g2.setFont(new Font("Ariel",Font.BOLD,30));//needs to be set every run
	    	g2.drawString("PAUSE", 178, 55);
	    	
	    	g2.setColor(new Color(255,255,255,200));
	    	if(EntityKeeper.pauseMenu.contains(mousex,mousey))
	    	{
	    		((Graphics2D) g2).fill(EntityKeeper.pauseMenu);
	    	}
	    	else if(EntityKeeper.pauseControls.contains(mousex,mousey))
	    	{
	    		((Graphics2D) g2).fill(EntityKeeper.pauseControls);
	    	}
	    	else if(EntityKeeper.pauseDifficulty.contains(mousex,mousey))
	    	{
	    		((Graphics2D) g2).fill(EntityKeeper.pauseDifficulty);
	    	}
	    	else if(EntityKeeper.backButton.contains(mousex,mousey))
	    	{
	    		g2.setColor(new Color(255,255,255));
	    		((Graphics2D) g2).fill(EntityKeeper.backButton);
	    	}
	    	g2.setColor(Color.BLACK);
	    	
	    	((Graphics2D) g2).draw(EntityKeeper.pauseMenu);
	    	((Graphics2D) g2).draw(EntityKeeper.pauseControls);
	    	((Graphics2D) g2).draw(EntityKeeper.pauseDifficulty);
	    	g2.drawImage(ImageLoader.getImage("GUIBack"),EntityKeeper.backButton.x,EntityKeeper.backButton.y,this);
	    	
	    	g2.drawString("MENU", EntityKeeper.pauseMenu.x+32, EntityKeeper.pauseMenu.y+36);
	    	g2.setFont(new Font("Ariel",Font.BOLD,25));//needs to be set every run
	    	g2.drawString("CONTROLS", EntityKeeper.pauseControls.x+5, EntityKeeper.pauseControls.y+36);
	    	g2.drawString("DIFFICULTY", EntityKeeper.pauseDifficulty.x+1, EntityKeeper.pauseDifficulty.y+36);
	    	
	    	showPauseMenu = true;
    }
    
    public void drawControls(Graphics g2)
    {
	    	g2.drawImage(ImageLoader.getImage("SplashScreen"), 0, 21, this);
	    	g2.setColor(new Color(255,255,255,200));
	    	g2.fillRect(0,0,(int)screenSize.getWidth(),(int)screenSize.getHeight());
	    	
	    	g2.setColor(new Color(255,255,255,200));
	    	if(EntityKeeper.backButton.contains(mousex,mousey))
	    	{
	    		g2.setColor(new Color(255,255,255));
	    		((Graphics2D) g2).fill(EntityKeeper.backButton);
	    	}
	    	g2.setColor(Color.BLACK);
	    	g2.drawImage(ImageLoader.getImage("GUIBack"),EntityKeeper.backButton.x,EntityKeeper.backButton.y,this);
	    	
	    	g2.setColor(Color.BLACK);
	    	g2.setFont(new Font("Ariel",Font.BOLD,30));//needs to be set every run
	    	g2.drawString("CONTROLS", 135, 55);
	    	
	    	g2.setFont(new Font("Ariel",Font.BOLD,20));//needs to be set every run
	    	g2.drawString("Left Click", 100, 100);
	    	g2.drawString("Scroll Wheel", 82, 120);
	    	g2.drawString("P", 138, 140);
	    	
	    	g2.drawString("|  Place Turret", 210, 100);
	    	g2.drawString("|  Change Turret", 210, 120);
	    	g2.drawString("|  Pause Game", 210, 140);
	    	
	    	g2.setFont(new Font("Ariel",Font.BOLD,30));//needs to be set every run
	    	g2.drawString("INSTRUCTIONS", 100, 190);
	    	g2.setFont(new Font("Ariel",Font.BOLD,15));//needs to be set every run
	    	g2.drawString("     Strategically place turrets in order to pop all the", 10, 220);
	    	g2.drawString(" incomming balloons. Red balloons are the most weak", 10, 240);
	    	g2.drawString("     followed by Blue, Yellow, Green, and finally Black", 10, 260);
	    	g2.drawString(" balloons. Your goal is to hold out as long as possible.", 10, 280);
    	
    }
    
    public void drawDifficultyChooser(Graphics g2)
    {					
	    	g2.drawImage(ImageLoader.getImage("SplashScreen"),0,21,this);
	    	g2.setColor(new Color(255,255,255,200));
	    	g2.fillRect(0,0,(int)screenSize.getWidth(),(int)screenSize.getHeight());
	    	g2.setColor(Color.BLACK);
	    	g2.setFont(new Font("Ariel",Font.BOLD,30));//needs to be set every run
	    	g2.drawString("CHOOSE DIFFICULTY", 75, 55);
	    	
	    	g2.setColor(new Color(255,255,255,200));
	    	if(EntityKeeper.easy.contains(mousex,mousey))
	    	{
	    		((Graphics2D) g2).fill(EntityKeeper.easy);
	    	}
	    	else if(EntityKeeper.medium.contains(mousex,mousey))
	    	{
	    		((Graphics2D) g2).fill(EntityKeeper.medium);
	    	}
	    	else if(EntityKeeper.hard.contains(mousex,mousey))
	    	{
	    		((Graphics2D) g2).fill(EntityKeeper.hard);
	    	}
	    	else if(EntityKeeper.backButton.contains(mousex,mousey))
	    	{
	    		g2.setColor(new Color(255,255,255));
	    		((Graphics2D) g2).fill(EntityKeeper.backButton);
	    	}
	    	g2.setColor(Color.BLACK);
	    	
	    	((Graphics2D) g2).draw(EntityKeeper.easy);
	    	((Graphics2D) g2).draw(EntityKeeper.medium);
	    	((Graphics2D) g2).draw(EntityKeeper.hard);
	    	g2.drawImage(ImageLoader.getImage("GUIBack"),EntityKeeper.backButton.x,EntityKeeper.backButton.y,this);
	    	
	    	g2.drawString("EASY", EntityKeeper.easy.x+35, EntityKeeper.easy.y+36);
	    	g2.drawString("MEDIUM", EntityKeeper.medium.x+12, EntityKeeper.medium.y+36);
	    	g2.drawString("HARD", EntityKeeper.hard.x+30, EntityKeeper.hard.y+36);
	    }
	    
	    public void drawGameOver(Graphics g2)//draws gameover screen when called
	    {
	    	g2.setColor(Color.WHITE);
	    	g2.fillRect(0,0,(int)screenSize.getWidth(),(int)screenSize.getHeight());
	    	g2.setColor(Color.BLACK);
	    	g2.setFont(new Font("Ariel",Font.BOLD,30));
	    	g2.drawString("GAME OVER",(int)screenSize.getWidth()/2-100,(int)screenSize.getHeight()/2+20);
    }
    
    
    /***************************************** 
				Getters / Utilites
    *****************************************/
    
    public static void addParticles(int x, int y, int numberOfPoints, String Type)
    {
	    	int newX;
	    	int newY;
	    	
	    	for(int k = 0; k < numberOfPoints; k++)
	    	{
	    		newX = (int)((Math.random()*10)+x-Math.random()*10);
	    		newY = (int)((Math.random()*10)+y-Math.random()*10);
	    		
	    		EntityKeeper.particles.add(new Particle(newX,newY,Type));
	    	}
    }
    
    public void reset()
    {
	    	EntityKeeper.world = new boolean[(int)Logic.worldSize.getWidth()+1][(int)Logic.worldSize.getHeight()+2];
	    	EntityKeeper.enemyQueue.clear();
	    	EntityKeeper.enemies.clear();
	    	EntityKeeper.turrets.clear();
	    	EntityKeeper.lasers.clear();
	    	EntityKeeper.particles.clear();
	    	
	    	player.setMoney(player.getStartMoney());
	    	player.setHealth(player.getStartHealth());
	    	player.setKillCount(0);
	    	currentRound = 0;
	    	
    		buy = false;
		sell = false;
		upgrade = false;
		choosenTurret = null;
    }
    
    
    
    /***************************************** 
				Event listeners
	*****************************************/
   
	public void mouseDragged(MouseEvent e)//"mouse clicked"
	{	
		clicks++;
		mousex = e.getX();
		mousey = e.getY();
		mousex2 = 25*(Math.round(e.getX()/25));
		mousey2 = 25*(Math.round(e.getY()/25));
				
		if(!notPaused)//if game is paused show pause menu
		{
			if(showPauseMenu)//if pause menu shown then detect pause menu buttons
			{
				if(EntityKeeper.pauseMenu.contains(mousex, mousey))//if menu button hit then reset game and go to menu
				{
					notPaused = true;
					gameNotStarted = true;
					showControls = false;
					chooseDifficulty = false;
					gameOver = false;
					showPauseMenu = false;
					reset();
				}
				else if(EntityKeeper.pauseControls.contains(mousex,mousey))//show controls screen
				{
					showControls = true;
					chooseDifficulty = false;
					showPauseMenu = false;
				}
				else if(EntityKeeper.pauseDifficulty.contains(mousex, mousey))//show difficulty selector screen
				{
					showControls = false;
					chooseDifficulty = true;
					showPauseMenu = false;
				}
				else if(EntityKeeper.backButton.contains(mousex, mousey))//show back button
				{
					showControls = false;
					chooseDifficulty = false;
					showPauseMenu = false;
					notPaused = true;
				}	
			}
			else//one of the buttons has been pressed
			{
				if(!showControls && chooseDifficulty)//if only difficulty button pressed then show difficulty screen
				{
					if(EntityKeeper.easy.contains(mousex,mousey))
					{
						waveDifficulty = 1;
						chooseDifficulty = false;
						showPauseMenu = true;
					}
					else if(EntityKeeper.medium.contains(mousex,mousey))
					{
						waveDifficulty = 1.5;
						chooseDifficulty = false;
						showPauseMenu = true;
					}
					else if(EntityKeeper.hard.contains(mousex,mousey))
					{
						waveDifficulty = 2;
						chooseDifficulty = false;
						showPauseMenu = true;
					}
					else if(EntityKeeper.backButton.contains(mousex,mousey))
					{
						chooseDifficulty = false;
						showPauseMenu = true;
					}	
				}
				else if(showControls && !chooseDifficulty)//if only controls button pressed then show controls screen
				{
					if(EntityKeeper.backButton.contains(mousex,mousey))
					{
						showControls = false;
						showPauseMenu = true;
					}	
				}	
			}	
		}
		
		if(gameNotStarted)
		{
			if(!showControls && !chooseDifficulty)//if no button pressed then show selection screen
			{
				if(EntityKeeper.play.contains(mousex,mousey))
				{	
					gameNotStarted = false;
				}
				else if(EntityKeeper.controls.contains(mousex,mousey))
				{	
					showControls = true;
				}
				else if(EntityKeeper.difficulty.contains(mousex,mousey))
				{
					chooseDifficulty = true;
				}	
			}
			else if(showControls && !chooseDifficulty)//if only controls button pressed then show controls screen
			{
				if(EntityKeeper.backButton.contains(mousex,mousey))
				{
					showControls = false;
				}
			}
			else if(!showControls && chooseDifficulty)//if only difficulty button pressed then show difficulty screen
			{
				if(EntityKeeper.easy.contains(mousex,mousey))
				{
					waveDifficulty = 1;
					chooseDifficulty = false;
				}
				else if(EntityKeeper.medium.contains(mousex,mousey))
				{
					waveDifficulty = 1.5;
					chooseDifficulty = false;
				}
				else if(EntityKeeper.hard.contains(mousex,mousey))
				{
					waveDifficulty = 2;
					chooseDifficulty = false;
				}
				else if(EntityKeeper.backButton.contains(mousex,mousey))
				{
					chooseDifficulty = false;
				}	
			}	
		}
		
		if(!gameNotStarted && notPaused)
		{
			if(EntityKeeper.pauseButton.contains(mousex, mousey))
			{
				notPaused = false;
				buy = false;
				sell = false;
				upgrade = false;
			}
			else if(EntityKeeper.buy.contains(mousex, mousey))
			{
				buy = !buy;
				if(buy)
				{
					sell = false;
					upgrade = false;
				}
			}
			else if(EntityKeeper.sell.contains(mousex, mousey))
			{
				sell = !sell;
				if(sell)
				{
					buy = false;
					upgrade = false;
				}
			}
			else if(EntityKeeper.upgrade.contains(mousex, mousey))
			{
				upgrade = !upgrade;
				if(upgrade)
				{
					buy = false;
					sell = false;
				}
			}
			else if(!EntityKeeper.path.contains(mousex2,mousey2) && !EntityKeeper.isOccupied(mousex2,mousey2) && buy 
					&& !((EntityKeeper.upgrade1.contains(mousex,mousey) || EntityKeeper.upgrade2.contains(mousex,mousey) 
							|| EntityKeeper.upgrade3.contains(mousex,mousey)) && upgrade))
			{	
				if(turretChooser == 0 && (player.getMoney() >= tempTurret.getBulletCost()))
				{
					EntityKeeper.turrets.add(new Turret(mousex2+25, mousey2+25, ImageLoader.getImage("TurretBullet"),1,"Bullet"));
					EntityKeeper.setOccupied(mousex2,mousey2,true);
				}
				else if(turretChooser == 1 && (player.getMoney() >= tempTurret.getFireCost()))
				{
					EntityKeeper.turrets.add(new Turret(mousex2+25, mousey2+25, ImageLoader.getImage("TurretFire"),2,"Fire"));
					EntityKeeper.setOccupied(mousex2,mousey2,true);
				}
				else if(turretChooser == 2 && (player.getMoney() >= tempTurret.getTeslaCost()))
				{
					EntityKeeper.turrets.add(new Turret(mousex2+25, mousey2+25, ImageLoader.getImage("TurretTesla"),3,"Tesla"));
					EntityKeeper.setOccupied(mousex2,mousey2,true);
				}
			}
			else if(!EntityKeeper.path.contains(mousex2,mousey2) && EntityKeeper.isOccupied(mousex2,mousey2) && sell)
			{	
				for(int k = 0; k < EntityKeeper.turrets.size(); k++)
				{
					tempTurret = EntityKeeper.turrets.get(k);
										
					if(tempTurret.getMapPosX() == mousex2+13 && tempTurret.getMapPosY() == mousey2+13)
					{
						if(tempTurret.getType().equalsIgnoreCase("Bullet"))
						{
							player.addMoney((int)tempTurret.getBulletCost()/2);
						}
						else if(tempTurret.getType().equalsIgnoreCase("Fire"))
						{
							player.addMoney((int)tempTurret.getFireCost()/2);
						}
						else if(tempTurret.getType().equalsIgnoreCase("Tesla"))
						{
							player.addMoney((int)tempTurret.getTeslaCost()/2);
						}
						
						EntityKeeper.turrets.remove(k);
						EntityKeeper.setOccupied(mousex2,mousey2,false);
					}
				}
			}
			else if(!EntityKeeper.path.contains(mousex2,mousey2) && EntityKeeper.isOccupied(mousex2,mousey2) && upgrade)
			{	
				for(int k = 0; k < EntityKeeper.turrets.size(); k++)
				{
					tempTurret = EntityKeeper.turrets.get(k);
										
					if(tempTurret.getMapPosX() == mousex2+13 && tempTurret.getMapPosY() == mousey2+13)
					{
						choosenTurret = tempTurret;
					}
				}
			}
			else if(EntityKeeper.upgrade1.contains(mousex,mousey) && choosenTurret != null && upgrade)
			{
				choosenTurret.upgrade("MoreRange");
			}
			else if(EntityKeeper.upgrade2.contains(mousex,mousey) && choosenTurret != null && upgrade)
			{
				choosenTurret.upgrade("FasterFire");
			}
			else if(EntityKeeper.upgrade3.contains(mousex,mousey) && choosenTurret != null && upgrade)
			{
				choosenTurret.upgrade("MoreDamage");
			}
		}
	}
	
	public void mouseMoved(MouseEvent e)
	{
		mousex = e.getX();
		mousey = e.getY();
		mousex2 = 25*(Math.round(e.getX()/25));
		mousey2 = 25*(Math.round(e.getY()/25));
	}	
	
	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode()) 
		{ 
			case KeyEvent.VK_B:
			{	
				buy = !buy;
				break;
			}
			case KeyEvent.VK_S:
			{	
				sell = !sell;
				break;
			}
			case KeyEvent.VK_U:
			{	
				upgrade = !upgrade;
				break;
			}
			case KeyEvent.VK_ESCAPE://displayes controls
			{	
				gridToggled = !gridToggled;
				break;
			}
			case KeyEvent.VK_P://toggles pause state
			{	
				notPaused = !notPaused;
				break;
			}
			case KeyEvent.VK_F2://toggles debug
			{	
				drawDebug = !drawDebug;
				break;
			}
		}	
	}
	
	public void mouseWheelMoved(MouseWheelEvent e)//called when mouse scrolls 
	{
	       turretChooser += e.getWheelRotation();
	       if (turretChooser > maxTurrets)
	    	   turretChooser = 0;
	       if (turretChooser < 0)
	    	   turretChooser = maxTurrets;
	}
	
	public static double fix(double d)
	{
		if(d < 0)
			d = 0;
		return d;
	}
	
	public static void delay(double dt)
	{
		if(dt < 0)
			dt = 0;
		
		try 
		{
			Thread.sleep((long)dt);
			
		} catch (InterruptedException e) {}
	}

	public void paint(Graphics g)
	{
		update(g);
	}
   
	public void mousePressed(MouseEvent e)
	{
		mouseDragged(e);
	}
   
	public void mouseReleased(MouseEvent e){}
   
	public void mouseClicked(MouseEvent e){}
   
	public void mouseEntered(MouseEvent e){}
   
	public void mouseExited(MouseEvent e){}

	public void keyTyped(KeyEvent e){}

	public void keyReleased(KeyEvent e){}
	
}