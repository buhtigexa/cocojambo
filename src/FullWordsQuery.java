import java.util.StringTokenizer;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;


public class FullWordsQuery {

	/*
	 * Esta clase arma una consulta de todos los términos que sea desean buscar 
	 * 
	 * 
	 */
	protected String wordsWanted;
	protected FuzzyQuery	termQuery;
	protected String fieldToSearch;
	
	
	protected StringTokenizer tokenizer;
	protected Query fullWordsQuery;
	protected Term term;
	
	public FullWordsQuery(String fieldToSearch,String wordsToSearch) {
			wordsWanted = wordsToSearch;
			this.fieldToSearch=fieldToSearch;
			tokenizer=new StringTokenizer(wordsWanted," ");
			fullWordsQuery=new BooleanQuery(); 
			term=new Term(fieldToSearch);
	}
	
	public Query buildQuery(){ 
		
		while ( tokenizer.hasMoreTokens()	){
			term = new Term(fieldToSearch, tokenizer.nextToken());		
			termQuery = new FuzzyQuery(term);				
			((org.apache.lucene.search.BooleanQuery) fullWordsQuery).add(termQuery,BooleanClause.Occur.MUST);
		}
		System.out.println(fullWordsQuery);
		return fullWordsQuery;
	}
}
