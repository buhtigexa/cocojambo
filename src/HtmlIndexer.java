import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.TermVector;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.Version;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlIndexer extends Indexer {


		protected Hashtable<String , String> myMetadata;
	    Set<String> textualMetadataFields;
	    Hashtable<String, String> genereKeywords;
	    StringTokenizer tokenizer ;
	    String token;
	  
	    public HtmlIndexer(Analyzer analyzer,String indexDir, String dataDir, FileFilter fileFilter) {
			super(analyzer,indexDir, dataDir, fileFilter);
			
			genereKeywords = new Hashtable<String, String>();
			genereKeywords.put("action", "action");
			genereKeywords.put("adventure", "adventure");
			genereKeywords.put("comedy", "comedy");
			genereKeywords.put("fiction", "fiction");
			genereKeywords.put("apocalyptic", "apocalyptic");
			genereKeywords.put("drama", "drama");
			genereKeywords.put("science fiction", "science fiction");
			genereKeywords.put("science", "science");
			genereKeywords.put("thriller", "thriller");
			genereKeywords.put("western", "western");
			genereKeywords.put("terror", "terror");
			genereKeywords.put("eco-horror", "eco-horror");
			genereKeywords.put("horror","horror");
			genereKeywords.put("biographical","biographical");
			genereKeywords.put("ghost","ghost");
			genereKeywords.put("terrorism","terrorism");
			genereKeywords.put("comedy","comedy");
			genereKeywords.put("belic","belic");
			genereKeywords.put("mystery","mystery");
			genereKeywords.put("romantic","romantic");
			genereKeywords.put("musical","musical");
			genereKeywords.put("documentary","documentary");
			genereKeywords.put("sex","sex");
			genereKeywords.put("crime","crime");
			genereKeywords.put("western","western");
			genereKeywords.put("martial arts","martial arts");
			genereKeywords.put("martial","martial");
			genereKeywords.put("gore","gore");
			genereKeywords.put("anime","anime");
		}


		protected Document getDocument(File f) {
		
			Document doc = new Document();
			Metadata metadata = new Metadata();
			metadata.set( Metadata.RESOURCE_NAME_KEY, f.getName() );
			Parser parser = new AutoDetectParser();
			String strTitle = new String();
			boolean stop;
			String strGenere=new String(" ");
			try {
						stop=false;
						org.xml.sax.ContentHandler handler = new BodyContentHandler(40*1024*1024);
						ParseContext context = new ParseContext();
						InputStream is = new FileInputStream(f);
						context.set(Parser.class, parser);
						parser.parse(is, handler, metadata,	new ParseContext());
						is.close();
						
						doc.add(new Field("contents", handler.toString(), Field.Store.YES, Field.Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS ) );
						doc.add(new Field("filename",f.getCanonicalPath(),Field.Store.YES,Field.Index.ANALYZED,TermVector.YES )); 
								
						
						if (f.getName().toLowerCase().endsWith(".html")){
							
							File input = new File(f.getAbsolutePath());
							org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(input, "UTF-8", "");
							Elements title =  jsoupDoc.select("a").eq(0);
							Elements reviewerElement =  jsoupDoc.select("a").eq(1);
							Elements film = jsoupDoc.select("p").eq(0); 
							Elements summary = jsoupDoc.select("pre").eq(1);
							int beginYear = title.text().indexOf("(");
							int endYear   = title.text().lastIndexOf(")");
							String year = new String();
							
							String reviewer =  reviewerElement.text();
							//String genere = film.text() + " "  +  summary.text();
							strTitle = title.text();
							int intTitle = title.text().indexOf("(");
							if ( intTitle > 0 )
								strTitle = title.text().substring(0, intTitle); 
							if ( beginYear >= 0 && (endYear > beginYear+1) )
								year = title.text().substring(beginYear+1, endYear);
							
							try{ 		int numero = Integer.parseInt(year); }
							catch(NumberFormatException e){ 	year =""; 	}

							tokenizer = new StringTokenizer(handler.toString()," , ; : . - / ( ) ' 0123456789@~");
							while ((tokenizer.hasMoreTokens()) && (!stop)) {
								token = (tokenizer.nextToken()).toLowerCase();
								if (genereKeywords.contains(token)){ 
									stop=true;
									strGenere=genereKeywords.get(token);
							   }
							}
						//			System.out.println("Genero :" + strGenere );
									doc.add(new Field("genere",strGenere,Field.Store.NO,Field.Index.ANALYZED ) );
									doc.add(new Field("title",strTitle,Field.Store.YES,Field.Index.ANALYZED,TermVector.YES));
									doc.add(new Field("year",year,Field.Store.YES,Field.Index.ANALYZED,TermVector.YES) );
									doc.add(new Field("author",reviewer,Field.Store.YES,Field.Index.ANALYZED,TermVector.YES) );
							
						}
							
						
					 setChanged();
					 notifyObservers();
						
			}
			
				catch (Exception e){ 		//e.printStackTrace();  			
				}
			return doc;

			}
		
		protected void setUp(){
		
		}
		public void start() {
		
			try{
				
				setUpIndex(this.analyzer,indexDir);
				index( getDataDir());			
			}
				
			finally {
					this.close();
					}
		}
		
		
		public static void displayTokensWithFullDetails(Analyzer analyzer,String text)	throws IOException {
				TokenStream stream = analyzer.tokenStream("contents",new StringReader(text));
				TermAttribute term = stream.addAttribute(TermAttribute.class);
				PositionIncrementAttribute posIncr =stream.addAttribute(PositionIncrementAttribute.class);
				OffsetAttribute offset =stream.addAttribute(OffsetAttribute.class);
				TypeAttribute type = stream.addAttribute(TypeAttribute.class);
				int position = 0;
				while(stream.incrementToken()) {
				int increment = posIncr.getPositionIncrement();
				if (increment > 0) {
					position = position + increment;
					System.out.println();
					System.out.print(position + ": ");
					}
				System.out.print("[" +	term.term() + ":" +	offset.startOffset() + "->" + 	offset.endOffset() + ":" + 	type.type() + "] ");
				}
				System.out.println();
				}
		
	}



