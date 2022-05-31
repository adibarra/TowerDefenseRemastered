import java.awt.image.BufferedImage;

//Alec Ibarra
public class Turret extends Entity{

	String type;
	Enemy target;
	int bulletCost = 20;
	int fireCost = 40;
	int teslaCost = 60;
	double range = 35.5;
	double startRange = 35.5;
		
	Turret()
	{
		mapPosX = 0;
		mapPosY = 0;
		health = 100;
		maxHealth = 100;
		damage = 1;
		speed = 0;	
		sizeX = 20;
		sizeY = 20;
		money = 0;
		cooldown = 0;
		maxCooldown = 50;
		target = new Enemy();
	}
	
	Turret(int mapPosX, int mapPosY, BufferedImage icon, int damage, String type)
	{
		this.icon = icon;
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.type = type;
		this.damage = damage;
		health = 100;
		maxHealth = 100;
		speed = 0;	
		sizeX = 20;
		sizeY = 20;
		money = 0;
		cooldown = 0;
		maxCooldown = 50;
		dynamic = false;
		target = new Enemy(mapPosX,mapPosY);
		rotation = -90;
		updateBoundingBox();
		
		this.mapPosX -= icon.getWidth()/2;
		this.mapPosY -= icon.getHeight()/2;
		
		if(type.equalsIgnoreCase("Bullet"))
		{
			Logic.player.setMoney(Logic.player.getMoney()-getBulletCost());
		}
		else if(type.equalsIgnoreCase("Fire"))
		{
			Logic.player.setMoney(Logic.player.getMoney()-getFireCost());
		}
		else if(type.equalsIgnoreCase("Tesla"))
		{
			Logic.player.setMoney(Logic.player.getMoney()-getTeslaCost());
		}
	}
	
	public void upgrade(String upgrade)
	{
		if(upgrade.equalsIgnoreCase("FasterFire"))
		{
			if(Logic.player.getMoney() >= 50)
			{
				Logic.player.addMoney(-50);
				setMaxCooldown(getMaxCooldown()/1.2f);
			}
		}
		else if(upgrade.equalsIgnoreCase("MoreDamage"))
		{
			if(Logic.player.getMoney() >= 50)
			{
				Logic.player.addMoney(-50);
				setDamage(getDamage()*1.5f);
			}
		}
		else if(upgrade.equalsIgnoreCase("MoreRange"))
		{
			if(Logic.player.getMoney() >= 50)
			{
				Logic.player.addMoney(-50);
				setRange(getRange()*1.5f);
			}
		}
	}
	
	public void setTarget(Enemy target)
	{
		this.target = target;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public Enemy getTarget()
	{
		return target;
	}
	
	public String getType()
	{
		return type;
	}

	public int getBulletCost() {
		return bulletCost;
	}

	public void setBulletCost(int bulletCost) {
		this.bulletCost = bulletCost;
	}

	public int getFireCost() {
		return fireCost;
	}

	public void setFireCost(int fireCost) {
		this.fireCost = fireCost;
	}

	public int getTeslaCost() {
		return teslaCost;
	}

	public void setTeslaCost(int teslaCost) {
		this.teslaCost = teslaCost;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public double getStartRange() {
		return startRange;
	}

	public void setStartRange(double startRange) {
		this.startRange = startRange;
	}
	
	

}
