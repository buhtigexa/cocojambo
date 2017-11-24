import java.io.File;


public abstract class Filter {

	public Filter(){}
	public abstract boolean accept(File f);
	
}
