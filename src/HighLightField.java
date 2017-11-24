import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.IndexSearcher;


public class HighLightField extends HighLight{

	protected String field;
	
	public HighLightField(String field){
		
		super();
		this.field=field;
	
	}
	
	@Override
	public String print(IndexSearcher indexSearcher, int hit) {
		
		try {
			Document doc  = indexSearcher.doc(hit);
			string = doc.get(this.field);
		//	System.out.println("Field :" + field );
			
					
		}  catch (Exception e) { e.printStackTrace(); 	}
		return string;
		
	}

}
