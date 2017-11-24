
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class DataRetrieval extends Observable {

	protected IndexReader newReader,reader; 
	protected Directory dir;
	protected IndexSearcher searcher;
	protected int nHits;
	
	public DataRetrieval (Indexer indexer,String indexDir) {

		nHits = 20;
		
		try {	
				if ( (indexer != null) && indexer.isIndexing()  ) reader = (indexer.getWriter()).getReader();
				else 	{
		 						    dir = FSDirectory.open(new File(indexDir));
									reader = IndexReader.open(dir);
				}
			}
		catch(Exception e )	{
				e.printStackTrace();
		}
		
		
	}
	
	protected synchronized IndexReader getReader() {
		
		 try {	 
			 newReader = reader.reopen();
			if ( newReader != reader){
					reader.close();
					reader = newReader;
			 }
		 }
		 catch (Exception e) {  System.out.println("Error en el getReader"); e.printStackTrace(); }
	  	 return reader;
	}
	
	public IndexSearcher getSearcher(){
		searcher = new IndexSearcher(this.getReader());
		return 	searcher;
	}
	
	public TopDocs testMatch(Query q){
		
		TopDocs matches = new TopDocs(0, null , 0);
		try {
			matches = this.getSearcher().search(q,nHits);
			if (matches.totalHits > 0)
				System.out.println("Encontrado:" + searcher.doc(matches.scoreDocs[0].doc) );
		} catch (IOException e) {
						e.printStackTrace();
		}
		return matches;
		
	}

}

