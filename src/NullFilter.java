import java.io.File;


public class NullFilter extends Filter{

	NullFilter(){
		super();
	}
	@Override
	public boolean accept(File f) {
		return true;
	}

	
}
