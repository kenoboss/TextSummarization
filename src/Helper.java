import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

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
						FileSystems.getDefault().getPath(".", name), 
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
}
