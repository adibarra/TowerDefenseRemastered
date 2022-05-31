//Alec Ibarra
public class Player extends Entity{

	int killCount = 0;
	int startMoney = 0;
	int startHealth = 0;
		
	Player()
	{
		money = 200;//200 to start
		health = 300;//300 to start
		startMoney = this.getMoney();
		startHealth = this.getHealth();
	}
	
	public void getStartMoney(int startMoney)
	{
		this.startMoney = startMoney;
	}
	
	public void setStartHealth(int startHealth)
	{
		this.startHealth = startHealth;
	}
	
	public void addToKillCount()
	{
		killCount++;
	}
	
	public void setKillCount(int killCount)
	{
		this.killCount = killCount;
	}
	
	public int getStartMoney()
	{
		return startMoney;
	}
	
	public int getStartHealth()
	{
		return startHealth;
	}
	
	public int getKillCount()
	{
		return killCount;
	}
	
}
