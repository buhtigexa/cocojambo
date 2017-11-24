import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;



public abstract class Indexer extends Observable{


		protected Directory dir ;
		protected IndexWriter writer;

		protected String indexDir;
		protected IndexSearcher searcher;
		
		int indexeds;
		
		protected String currentFile;
		protected String dataDir;
		protected IndexReader reader,newReader;
		protected FileFilter filter;
		protected boolean isIndexing;
		protected Analyzer analyzer;
		
		private String status;

		
		protected int totalFiles;
		
		public Indexer(Analyzer analyzer,String indexDir,String dataDir,FileFilter fileFilter) {
			setUp(analyzer,1,new String(),indexDir,dataDir,fileFilter);
		}
		
		public void Indexer(Analyzer analyzer){
			setUp(analyzer,1,new String(),new String(),new String(),new FileFilter());
		}
		
		protected void setUp(Analyzer analyzer, int indexeds,String currentFile,String indexDir,String dataDir,FileFilter fileFilter){
		
			indexeds = 1;
			currentFile=new String();
			this.indexDir=indexDir;
			this.dataDir=dataDir;
			this.filter=fileFilter;
			totalFiles=0;
			setIndexing(false);
			setUpIndex(analyzer,indexDir);
			this.analyzer=analyzer;

		}
		
		public int getTotalFiles() {
			return totalFiles;
		}

		protected void setUpIndex(Analyzer analyzer,String indexDir){
			/*
			  PerFieldAnalyzerWrapper aWrapper =     new 	 PerFieldAnalyzerWrapper(new StandardAnalyzer(Version.LUCENE_33));
			  aWrapper.addAnalyzer("filename", 		new      StandardAnalyzer(Version.LUCENE_33));
			  aWrapper.addAnalyzer("title",   		new 	 SimpleAnalyzer(Version.LUCENE_33));
			  aWrapper.addAnalyzer("contents", 		new      StandardAnalyzer(Version.LUCENE_33));
			 */
			   this.indexDir = indexDir; 
				try {
					dir = FSDirectory.open(new File(indexDir));
					writer = new IndexWriter(dir,analyzer,IndexWriter.MaxFieldLength.UNLIMITED );
				
				} 
				
				catch (Exception e) {
					e.printStackTrace();
					System.out.println(" No hay archivos que indexar o el Indice está bloqueado.!! ");
				}
				
		}
		
		public int getIndexeds() {
				return indexeds;
		}

		public int getPercent() {
			
			int percent = (this.indexeds*100)/this.totalFiles;
			if(percent >= 99) return 100;
			return percent; 
		}
		
		public int index(String dataDir)	 {
			 
			setIndexing(true);
			File [] files = new File(dataDir).listFiles();
			try{
				 for (File f: files) {					
						if ( filter.accept(f) ) { 
						//	System.out.println("Indexando : " + f.getAbsolutePath());
							indexFile(f);
							indexeds++;
					    }
				 }
			}
				catch (Exception e)	{
										System.out.println("No hay nada que indexar en :" + dataDir);
										e.printStackTrace();	
									}
				
				finally {   	setIndexing(false); 			}
			System.out.println(this);
			return indexeds;
		}	
			
		protected void indexFile(File f)  {
			
				Document doc = getDocument(f);
				try {
				  	 writer.addDocument(doc);
					 currentFile = f.getCanonicalPath();
					 setChanged();
				  	 notifyObservers();
					} 
			   catch (IOException e) {	e.printStackTrace(); }
			   
			}

		public void cancel() {		
			this.setIndexing(false);		
		}
		
		
		public void close() {
			try {
				writer.optimize(false);
				writer.close();
				setChanged();
				notifyObservers();
			}
			catch(Exception e){ 
				
				System.out.println("Error al cerrar el indice  :"  +  indexDir);
				e.printStackTrace();
			}
		}
		
		
		public String getCurrentFile() {
			return currentFile;					
		}

	    
	    public String getDataDir() {
			return dataDir;
		}

		public FileFilter getFileFilter() {
			return filter;
		}

		public synchronized boolean isIndexing() {
			return isIndexing;
		}

		public synchronized void setIndexing(boolean isIndexing) {
			this.isIndexing = isIndexing;
		}

		protected abstract Document getDocument(File f);
		
		protected void setStatus(String status) {
			this.status = status;
		}

		public IndexWriter getWriter() {
			return writer;
		}

		public String toString(){
			try {
				TermEnum terms = writer.getReader().terms();
				while (terms.next())
					System.out.println(terms.term().toString());
			} catch (IOException e) {
					e.printStackTrace();
			}
			return currentFile;
			
		}
	}

	

