import java.util.StringTokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;


public  class SimpleCocoQuery extends CocoQuery {
	/*
	 * 
	 *  Frases : "\"\\" + "\""   +  stringParsed + "\\"   + "\"\"" ;
	 *  Todas las palabras : prefix =" +"; suffix ="~ 0.5";
	 *  Alguna de las palabras: prefix =" "; suffix = " ";
	 * 
	 */
	
	protected QueryParser queryParser;
	protected String lookFor;
	protected String terms;
	protected StringTokenizer tokenizer;
	protected String stringParsed;
	protected String suffix;
	protected String prefix;
	
	public SimpleCocoQuery(Version matchVersion,String fieldToSearch,Analyzer analyzer,String lookFor,String prefix,String sufix){
		
		super(matchVersion,fieldToSearch,analyzer);
		queryParser = new QueryParser(matchVersion,fieldToSearch,analyzer);
		this.lookFor=lookFor;
		tokenizer = new StringTokenizer(lookFor," ");
		terms = new String();
		stringParsed=new String();
		this.prefix=prefix;
		this.suffix=sufix;
		
	}
	
	public Query buildQuery () {
		
		query = new BooleanQuery();
		try {
			   query = queryParser.parse(tokenize());
			} 
				catch (ParseException e) { 	e.printStackTrace();	}
		
			System.out.println(" Query :" + query.toString());
			return query;
	}
	
	public String tokenize(){
	
		while (tokenizer.hasMoreTokens()){
			terms = prefix + tokenizer.nextToken() + suffix;
			stringParsed=stringParsed + terms;
		}
		System.out.println(stringParsed);
		return  stringParsed;
	}
		
}
