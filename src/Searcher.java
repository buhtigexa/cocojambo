import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Observable;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


public class Searcher extends Observable{
	
	protected IndexReader indexReader;
	protected IndexReader newReader;
	protected IndexSearcher indexSearcher;
	protected Directory		directory;
	protected Query			query;
	protected TopDocs 		topDocs;
	protected int recall;			
	protected HighLight highLight;
	protected MiniDocument miniDocument;
	

	/*	SOLO SE PERMITE UNA INSTANCIA POR APLICACION, Y SE CIERRA .close() AL TERMINAR LA APLICACION!.
	 */
	
	@SuppressWarnings("deprecation")
	
								// NO AGREGAR EL CAMPO DE BUSQUEDA EN EL CONSTRUCTOR!!!!!!   
	public Searcher(Analyzer analyzer,Indexer indexer,String indexDir, int recall){
								
	
		newReader = null;
		this.recall = recall;
		
		try {	
		
			if ( (indexer != null) && indexer.isIndexing()  ) indexReader = indexer.getWriter().getReader();
			else 	{
								directory = FSDirectory.open(new File(indexDir));
								indexReader = IndexReader.open(directory);
			}
			
		}
		catch(Exception e )	{ 					e.printStackTrace();			}
	
		
		
	}
	
	
	
	
	
	// UNICA forma para obtener el buscador : este método tambien permite obtener buscadores mientras se esté realizando una indexacion
	
	public IndexSearcher getSearcher(){
		
			IndexReader reader = this.getReader();
			indexSearcher = new IndexSearcher( reader );
			return 	indexSearcher;
	}
	
	
	public void testMatch (Query query){
		
		IndexSearcher indexSearcher = this.getSearcher();

		try		{
					topDocs = indexSearcher.search(query,this.recall);
					if (topDocs!=null)
					for  (ScoreDoc sd : topDocs.scoreDocs)  {
						miniDocument = new MiniDocument(indexSearcher,sd.doc);
						setChanged();
						notifyObservers(	miniDocument	);
					}
		}
		
		catch (Exception e){ e.printStackTrace();}
	}
	
		
	// Hacer el close SOLO cuando se cierra la aplicacion
	public void close(){
		try {
			indexSearcher.close();
			
		}
		 catch (IOException e) { e.printStackTrace(); 	}  
	}
		
	
	protected synchronized IndexReader getReader() {
		
		 try {	 
				newReader = indexReader.reopen();
				if ( newReader != indexReader){
					indexReader.close();
					indexReader = newReader;
			 }
		 }
		 catch (Exception e) {   e.printStackTrace(); }
	  	 return indexReader;
	}
	

	public void start() {
		
		testMatch(	this.query);
		
	}



}
