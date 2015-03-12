import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Writer class, build the solution file to be submitted
 *
 */
public class OutputWriter {

	private PrintWriter p;
	private final static char EOL = '\n';
	
	public OutputWriter(String filename){	
		try {
			this.p = new PrintWriter(new File("data/" + filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void append(String s){
		this.p.print(s + EOL);
	}
	
	public void close(){
		this.p.close();
	}

}
