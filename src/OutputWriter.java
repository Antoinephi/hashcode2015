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
	
	public OutputWriter(){	
		try {
			this.p = new PrintWriter(new File("data/solution.out"));
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
