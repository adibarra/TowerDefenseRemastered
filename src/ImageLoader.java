import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

//Alec Ibarra
public class ImageLoader {
	
	private static ArrayList<GameIcon> icons = new ArrayList<GameIcon>();
	
	public ImageLoader()
	{		
		icons.add(new GameIcon("BalloonBlack"));
		icons.add(new GameIcon("BalloonBlue"));
		icons.add(new GameIcon("BalloonGreen"));
		icons.add(new GameIcon("BalloonRed"));
		icons.add(new GameIcon("BalloonYellow"));
		icons.add(new GameIcon("Bird"));
		icons.add(new GameIcon("EffectBullet"));
		icons.add(new GameIcon("EffectFire"));
		icons.add(new GameIcon("EffectTesla"));
		icons.add(new GameIcon("GUIBack"));
		icons.add(new GameIcon("GUIBack2"));
		icons.add(new GameIcon("GUIPause"));
		icons.add(new GameIcon("Icon128x128"));
		icons.add(new GameIcon("Icon16x16"));
		icons.add(new GameIcon("Icon32x32"));
		icons.add(new GameIcon("Icon64x64"));
		icons.add(new GameIcon("NoTexture"));
		icons.add(new GameIcon("SplashScreen"));
		icons.add(new GameIcon("TurretBullet"));
		icons.add(new GameIcon("TurretFire"));
		icons.add(new GameIcon("TurretTesla"));
	}
	
	public static BufferedImage getImage(String imageName)
	{
		if(imageName.equalsIgnoreCase("none"))
		{
			return null;
		}
		
		for(int k = 0; k < icons.size(); k++)
		{
			if(icons.get(k).getIconName().equalsIgnoreCase(imageName))
			{
				return icons.get(k).getIcon();
			}
		}
		
		return getImage("noTexture");
	}

}

class GameIcon
{
	private BufferedImage icon;
	private String iconName;
	
	public GameIcon(String iconName)
	{
		try
		{
			setIconName(iconName);
			setIcon(ImageIO.read(getClass().getClassLoader().getResource(iconName+".png")));
			
		} catch (IOException | IllegalArgumentException e)
		{
			System.out.println("Failed to load: "+iconName+".png");
		}
	}

	public BufferedImage getIcon() {
		return icon;
	}

	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
}
