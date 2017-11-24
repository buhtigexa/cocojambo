import java.util.StringTokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.Version;


public class QueryFactory {
	
		protected CocoQuery myQuery;
		protected Analyzer analyzer;
		protected Version version;
		protected String prefix;
		protected String suffix;
		protected String field;
	
		public QueryFactory(String field,Version version, Analyzer analyzer) {
			
			this.field= field;
			this.version=version;
			this.analyzer=analyzer;
			prefix = new String();
			suffix = new String();
			
			
		}
		
		public CocoQuery getPhraseQuery(String wordsToSearch){
			
			prefix=" ";
			suffix=" ";
			return new PhraseCocoQuery(version, this.field, analyzer, wordsToSearch,prefix, suffix);
			
		}
		
		public CocoQuery getFullWordsQuery(String wordsToSearch){
			
			prefix =" +";
			suffix =" ";
			return new SimpleCocoQuery(version,this.field, analyzer, wordsToSearch, prefix, suffix); 
		}
		
		
		public CocoQuery getOptionalWordsQuery(String wordsToSearch){
		
			prefix =" ";
			suffix ="*";
			return new SimpleCocoQuery(version,this.field, analyzer, wordsToSearch, prefix, suffix);

		}
		public CocoQuery getNoneWordsQuery(String wordsToSearch){
			
			prefix =" -";
			suffix =" ";
			return new SimpleCocoQuery(version,this.field, analyzer, wordsToSearch, prefix, suffix);

		}
		
		public CocoQuery getHighLightQuery (String wordsToSearch1,String wordsToSearch2,String wordsToSerach3){
			
			prefix =" ";
			suffix =" ";
			String wordsToSearch = new String(wordsToSearch1 + " " + wordsToSearch2 + " "  + wordsToSerach3);
			System.out.println("HighLightQuery : ====> " + wordsToSearch);
			
			return new SimpleCocoQuery(version,this.field,analyzer,wordsToSearch,prefix,suffix);
		}
		
	    public CocoQuery getCompositeCocoQuery (){
			
			return new CompositeCocoQuery(this.version,this.field, this.analyzer);
			
		}
}
