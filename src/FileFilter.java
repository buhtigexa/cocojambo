import java.io.File;
public class FileFilter extends SimpleFilter{
			
		FileFilter(){
			super(null);
		}
		public boolean accept(File f){
			return  ((!f.isDirectory() && !f.isHidden()) && ( f.exists() && f.canRead()) );
		}
	}

