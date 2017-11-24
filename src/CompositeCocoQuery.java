import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;


public class CompositeCocoQuery extends CocoQuery{

	
	public CompositeCocoQuery(Version matchVersion,String fieldToSearch,Analyzer analyzer){
	
		super(matchVersion, fieldToSearch, analyzer);
		query = new BooleanQuery();
		
	}
	
	public void addQuery(CocoQuery queryComponent,Occur occur){
		
		((BooleanQuery)query).add(queryComponent.buildQuery(), occur);
		
	}

	@Override
	public Query buildQuery() {
		System.out.println("Composite Query :" +  query.toString());
		return query;
		
	}

}
