//Alec Ibarra
public class Spawner {
	
	static double timeBetweenWaves = (1500/(fix(Logic.currentRound)/4));
	static double timeBetweenRounds = 5000;
	
	static void start()
	{
		new Thread()
		{
			public void run()
			{
				while(true)
				{
					while(!Logic.gameNotStarted && Logic.notPaused && !Logic.showControls && !Logic.chooseDifficulty)
					{	
						timeBetweenWaves = (1500/(fix(Logic.currentRound)/4));
					
						if(EntityKeeper.turrets.size() > 0)
						{
							int groupSize = (int)(5*Logic.waveDifficulty);
							
							if(Logic.wavesRemaining == 0)
							{
								Logic.currentRound++;
								Logic.wavesRemaining = 2*Logic.currentRound;
								Logic.delay(timeBetweenRounds);
								
								if(Logic.currentRound < 2)//Bonus setup time
									Logic.delay(timeBetweenRounds);
							}
							else
							{
								Logic.wavesRemaining--;
			   				}
			   			
							if(Logic.enemiesRemaining == 0)
							{
								Logic.enemiesRemaining = groupSize*Logic.wavesRemaining;
							}
							else
							{
								Logic.enemiesRemaining -= groupSize;
							}
			   		
							int type;
							if(Logic.currentRound > 20)
							{
								type = (int)(Math.random()*5)+1;
							}
							else if(Logic.currentRound > 15)
							{
			   					type = (int)(Math.random()*4)+2;
			   				}
			   				else if(Logic.currentRound > 10)
			    			{
			    				type = (int)(Math.random()*3)+3;
			    			}
			    			else if(Logic.currentRound > 5)
			    			{
			    				type = (int)(Math.random()*2)+4;
			    			}
			    			else
			    			{
			    				type = (int)(Math.random()*1)+5;
			    			}
			    	
			   				if(type == 5)//red enemy
			   				{
			   					for(int k = 0; k < groupSize; k++)
			   						EntityKeeper.enemyQueue.add(new Enemy(50,0-(k*10),3,2,ImageLoader.getImage("BalloonRed"),5));
			       			}
			   				else if(type == 4)//blue enemy
			       			{
			    				for(int k = 0; k < groupSize; k++)
			    					EntityKeeper.enemyQueue.add(new Enemy(50,0-(k*10),2.5,4,ImageLoader.getImage("BalloonBlue"),10));
			       			}
			       			else if(type == 3)//yellow enemy
			       			{
			       				for(int k = 0; k < groupSize; k++)
			       					EntityKeeper.enemyQueue.add(new Enemy(50,0-(k*10),2,8,ImageLoader.getImage("BalloonYellow"),15));
			        		}
			       			else if(type == 2)//green enemy
			        		{
			       				for(int k = 0; k < groupSize; k++)
			       					EntityKeeper.enemyQueue.add(new Enemy(50,0-(k*10),1.5,16,ImageLoader.getImage("BalloonGreen"),20));
			        		}
			        		else if(type == 1)//black enemy
			        		{
			        			for(int k = 0; k < groupSize; k++)
			        				EntityKeeper.enemyQueue.add(new Enemy(50,0-(k*10),1,32,ImageLoader.getImage("BalloonBlack"),25));
			        		}
						}
			   		
						Logic.delay(timeBetweenWaves);
				
					}
					Logic.delay(Logic.TARGET_FRAME_TIME);
				}
			}
			
		}.start();
	}
	
	public static double fix(double num)//to avoid /0 error
	{
		if(num <= 0)
			num = 1;
		
		return num;
	}
	
}
