import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;


public abstract class CocoQuery {
	
	protected Version version;
	protected String fieldToSearch;
	protected Analyzer analyzer;
	protected Query query;
	
	public CocoQuery(Version matchVersion,String fieldToSearch,Analyzer analyzer){
		
		this.version=matchVersion;
		this.fieldToSearch=fieldToSearch;
		this.analyzer=analyzer;
	
	}
	
	public abstract Query buildQuery();

}
