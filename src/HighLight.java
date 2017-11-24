import java.util.Observable;

import org.apache.lucene.search.IndexSearcher;


public abstract class HighLight extends Observable{
	
	protected String string;
	
	
	public HighLight(){
		super(); 
		string = new String("");
		
			
	}
	
	public abstract String print(IndexSearcher indexSearcher,int hit);
	
	
	public String getString() {
		return string;
	}

	
	
}
