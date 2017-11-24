import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.util.Version;


public class HighLightTerms extends HighLightWrapper{

	protected String field;
	protected Query query;
	
	public HighLightTerms(HighLight wrapped,String field,Query query){
		super(wrapped);
		this.field=field;
		this.query=query;
	}
	
	@Override
	public String wrappOperation(IndexSearcher indexSearcher, int hit) {
		
		try{
			
			String text =  new String(indexSearcher.doc(hit).get(this.field));
			StringReader reader = 	new StringReader(text);
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_33);
			TokenStream tokenStream = analyzer.tokenStream(this.field,reader );
			QueryScorer scorer = new QueryScorer(query,this.field);
			Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
			Highlighter highlighter = new Highlighter(scorer);
			highlighter.setTextFragmenter(fragmenter);
			highlighter.setMaxDocCharsToAnalyze(500000);
			string = highlighter.getBestFragment(tokenStream,text);
		//	System.out.println("TOKENS que matchean:"  + string);
		}
		catch (Exception e){
				e.printStackTrace();
		}
		return string;
	
	}
	
}
