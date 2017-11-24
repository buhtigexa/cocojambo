
import java.util.Hashtable;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.dom4j.Visitor;


public class MiniDocument {

		/*
		 *    Encapsula el contenido de hit relevante al query de busqueda
		 * 
		 * 
		 */
	
	protected Hashtable<String, String> documentFields;
	protected IndexReader indexReader;
	protected Document doc ;
	protected List<Fieldable> list; 	
	
	public MiniDocument(IndexSearcher indexSearcher, int idDoc ){
	
		if (documentFields!=null)	documentFields.clear();
		
		documentFields = new Hashtable<String, String>();
		String fieldName=new String();
		try{
			
			indexReader = indexSearcher.getIndexReader();
			doc = indexReader.document(idDoc);
			list =  doc.getFields();
			if (list!=null)
				for (int f=0;f < list.size();f++)	{
					fieldName = (	list.get(f)	).name();
					System.out.println("MiniDocumento  :"  + Integer.toString(idDoc)  +"  " +  fieldName +  "--" +  doc.get(fieldName));
					documentFields.put(	fieldName, 	doc.get(fieldName)	);
				}
			documentFields.put("idDoc",Integer.toString(idDoc));
			}
		
		catch(Exception e){	   e.printStackTrace();	}
		
	}
	
	
	public String get	(String fieldName){
		
		String fieldContents = new String();
		if (	documentFields.contains(fieldName)		)
			fieldContents=documentFields.get(fieldName);
		return fieldContents;
	}

	public void set	(String fieldName,String fieldValue){
		
		if (documentFields.containsKey(fieldName)){
			documentFields.remove(fieldName);
			documentFields.put(fieldName, fieldValue);
			System.out.println("Nuevo valor para :" + fieldName +  ": " +  documentFields.get(fieldName));
			}
			
	}
	
	
	public MiniDocument(){
		
		if (documentFields!=null)	documentFields.clear();
			documentFields = new Hashtable<String, String>();
		
	}
	
	
}
