
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//Alec Ibarra
public class World {

	public static void loadWorld()
	{
		StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("res/SaveGame.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        //String worldData = sb.toString();
	}
	
	public static void saveWorld()
	{
		BufferedWriter writer = null;
	    try {
	        File gameSave = new File("res/SaveGame.txt");
	        writer = new BufferedWriter(new FileWriter(gameSave));
	        //writer.write(walls.toString());
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	try {
	            writer.close();
	        } catch (Exception e) {
	        }
	    }
	}
	
	public static void deleteWorld()
	{
		BufferedWriter writer = null;
	    try {
	        File gameSave = new File("res/SaveGame.txt");
	        writer = new BufferedWriter(new FileWriter(gameSave));
	        writer.write("");
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	try {
	            writer.close();
	        } catch (Exception e) {
	        }
	    }
	}
}
