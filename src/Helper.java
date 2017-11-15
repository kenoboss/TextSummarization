import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * 
 * @author Kenoboss
 * @version 1.0.0
 * This class contains helper function for the project TextSummarization
 *
 */
public class Helper {

	public static byte[] readSmallFileBytes(String name) {
		try {
			Path path = FileSystems.getDefault().getPath(".", name);
			return Files.readAllBytes(path);
		}
		catch ( IOException ioe ) { ioe.printStackTrace(); }
		return null;
	}

	public static List<String> readSmallFileLines(String name) {
		try {
			return Files.readAllLines(
					FileSystems.getDefault().getPath(".", name), 
					Charset.defaultCharset() );
		}
		catch ( IOException ioe ) { ioe.printStackTrace(); }
		return null;
	}

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

	public static void writeFileBytes(String name, String content) {
		try {
			Files.write(
					FileSystems.getDefault().getPath(".", name), 
					content.getBytes(), 
					StandardOpenOption.CREATE);
		}
		catch ( IOException ioe ) { ioe.printStackTrace(); }
	}

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


	public List<List<String>> readCSVFile (String name, String seperator, String toDelete) {
		List<List<String>> lines = new ArrayList<>();
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(name))) {
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(seperator);
                columns = clearArrayString(columns, toDelete);
                lines.add(Arrays.asList(columns));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
	}
	
	public String clearString (String input, String toDelete) {
		String result = input.trim();
		result = result.replace(toDelete, "");
		
		return result;
	}
	
	public String [] clearArrayString (String [] input, String toDelete) {
		String [] result = new String [input.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = clearString(input[i], toDelete);
		}
		return result;
	}

}
