import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Kenoboss
 * @version 1.0.0
 * This class contains helper function for the project TextSummarization
 *
 */
public class Helper {
	
	/** 
	 * @param name
	 * @return byte[]
	 */
	public static byte[] readSmallFileBytes(String name) {
		try {
			Path path = FileSystems.getDefault().getPath(".", name);
			return Files.readAllBytes(path);
		}
		catch ( IOException ioe ) { ioe.printStackTrace(); }
		return null;
	}
	
	/**
	 * This method reads a filename or filepath and returns a list of strings
	 * with the content of the file
	 * @param name
	 * @return List<String>
	 */
	public static List<String> readSmallFileLines(String name) {
		try {
			return Files.readAllLines(
					FileSystems.getDefault().getPath(".", name), 
					Charset.defaultCharset() );
		}
		catch ( IOException ioe ) { ioe.printStackTrace(); }
		return null;
	}
	
	/**
	 * This method reads a filename or filepath and returns a list of strings
	 * with the content of the file
	 * @param name
	 * @return List<String>
	 */
	public static List<String> readLargeFileLines(String name) {
		try ( BufferedReader nbr = 
				Files.newBufferedReader(
						FileSystems.getDefault().getPath(name), 
						Charset.defaultCharset() )
				){
			List<String> lines = new ArrayList<>();
			while (true){
				String line = nbr.readLine();
				if ( line == null ) return lines;
				lines.add(line);
			}

		} 
		catch ( IOException ioe ) { ioe.printStackTrace(); }
		return null;  
	}
	
	/**
	 * This method takes a filename or filepath and string, which has to write
	 * to the file
	 * @param name
	 * @param content
	 */
	public static void writeFileBytes(String name, String content) {
		try {
			Files.write(
					FileSystems.getDefault().getPath(".", name), 
					content.getBytes(), 
					StandardOpenOption.CREATE);
		}
		catch ( IOException ioe ) { ioe.printStackTrace(); }
	}
	
	/**
	 * This method takes a filename or filepath and a list of string, which has to write
	 * to the file
	 * @param name
	 * @param content
	 */
	public static void writeLargeFileLines(String name, List<String> lines) {
		try ( BufferedWriter nbw = 
				Files.newBufferedWriter(
						FileSystems.getDefault().getPath(".", name), 
						Charset.forName("UTF-8"), 
						StandardOpenOption.CREATE)
				){ 
			for ( String line : lines ) {
				nbw.write(line, 0, line.length());
				nbw.newLine();
			}
		}
		catch ( IOException ioe ) { ioe.printStackTrace(); }
	}

	/**
	 * This method takes a filename or filepath and a string as separator and 
	 * an optional String to delete in a line as arguments. The method 
	 * returns a list of list of string as row and columns of a csv file.
	 * @param name
	 * @param seperator
	 * @param toDelete
	 * @return List<List<String>> 
	 */
	public List<List<String>> readCSVFile (String name, String seperator, String toDelete) {
		List<List<String>> lines = new ArrayList<>();
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(name))) {
            while ((line = br.readLine()) != null) {
            	System.out.println(line);
                String[] columns = line.split(seperator);
                if (toDelete != null) {
                	columns = clearArrayString(columns, toDelete);
                }
                lines.add(Arrays.asList(columns));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
	}
	
	/**
	 * This method clears a string from another given string
	 * @param input
	 * @param toDelete
	 * @return String
	 */
	public String clearString (String input, String toDelete) {
		String result = input.trim();
		result = result.replace(toDelete, "");
		
		return result;
	}
	
	/**
	 * This method clears a array of strings from a given string
	 * @param input
	 * @param toDelete
	 * @return String []
	 */
	public String [] clearArrayString (String [] input, String toDelete) {
		String [] result = new String [input.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = clearString(input[i], toDelete);
		}
		return result;
	}

}
