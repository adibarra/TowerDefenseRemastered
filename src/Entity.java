import java.awt.Rectangle;
import java.awt.image.BufferedImage;

//Alec Ibarra
public class Entity {

	float XPosTarget;
	float YPosTarget;
	int mapPosX;
	int mapPosY;
	int health;
	int maxHealth;
	int sizeX;
	int sizeY;
	int money;
	double damage;
	double cooldown;
	double maxCooldown;
	double rotation;
	double speed;
	boolean dynamic;
	Rectangle boundingBox;
	BufferedImage icon;
	
	/***************************************** 
					Utilities
	*****************************************/
	
	public Entity(){}
	
	public Entity(int mapPosX, int mapPosY, BufferedImage icon){
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.icon = icon;
	}

	public boolean isDead()
	{
		if(health <= 0)
			return true;
		else
			return false;
	}
	
	public void addMoney(int money){
		this.money += money;
	}
	
	public void addMapPosX(int mapPosX){
		this.mapPosX += mapPosX;
	}
	
	public void addMapPosY(int mapPosY){
		this.mapPosY += mapPosY;
	}
	
	public void addMaxHealth(int maxHealth){
		this.maxHealth += maxHealth;
	}
	
	public void updateBoundingBox(){
		boundingBox = new Rectangle(this.mapPosX,this.mapPosY,this.sizeX,this.sizeY);
	}
	
	public boolean moveTo(float targetX, float targetY, float speed)
	{
		if(targetX == -1)
		{
			targetX = XPosTarget;
		}
		else
		{
			XPosTarget = targetX;
		}
		
		if(targetY == -1)
		{
			targetY = YPosTarget;
		}
		else
		{
			YPosTarget = targetY;
		}
		
		if(speed == -1)
		{
			speed = (float)this.speed;
		}
				
		float xDistance = XPosTarget - mapPosX;
		float yDistance = YPosTarget - mapPosY;
		double direction = Math.atan2(yDistance,xDistance);
		
		mapPosX += speed*Math.cos(direction);
		mapPosY += speed*Math.sin(direction);
		
				
		//If the target has been reached then retun true else false
		if(getDistance(XPosTarget,YPosTarget,mapPosX,mapPosY) <= speed)
		{
			mapPosX = (int) targetX;
			mapPosY = (int) targetY;
			return true;
		}
		
		return false;
	}
	
	public static double getDistance(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
	}
	
	/***************************************** 
					Getters
	*****************************************/
	
	public int getMapPosX(){
		return mapPosX;
	}
	
	public int getMapPosY(){
		return mapPosY;
	}
	
	public int getHealth(){
		return health;
	}
	
	public int getMaxHealth(){
		return maxHealth;
	}
	
	public double getDamage(){
		return damage;
	}
	
	public int getSizeX(){
		return sizeX;
	}
	
	public int getSizeY(){
		return sizeY;
	}
	
	public int getMoney(){
		return money;
	}
	
	public double getSpeed(){
		return speed;
	}
	
	public boolean getDynamic(){
		return dynamic;
	}
	
	public double getRotation(){
		return rotation;
	}
	
	public Rectangle getBoundingBox(){
		updateBoundingBox();
		return boundingBox;
	}
	
	public BufferedImage getIcon(){
		return icon;
	}
	
	public double getCooldown(){
		return cooldown;
	}
	
	public double getMaxCooldown(){
		return maxCooldown;
	}
	
	
	
	/***************************************** 
					Setters
	*****************************************/
	
	public void setCooldown(double cooldown){
		this.cooldown =  cooldown;
	}
	
	public void setMaxCooldown(double maxCooldown){
		this.maxCooldown =  maxCooldown;
	}
	
	public void setCoords(int mapPosX, int mapPosY){
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
	}
	
	public void setMapPosX(int mapPosX) {
		this.mapPosX = mapPosX;
	}
	
	public void setMapPosY(int mapPosY) {
		this.mapPosY = mapPosY;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public void setMaxHealth(int maxHealth){
		this.maxHealth = maxHealth;
	}
	
	public void setDamage(double damage){
		this.damage = damage;
	}
	
	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}
	
	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}
	
	public void setMoney(int money){
		this.money = money;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}
	
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	
	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}
	
}
