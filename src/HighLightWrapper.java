import org.apache.lucene.search.IndexSearcher;


public  abstract class HighLightWrapper extends HighLight{

	protected HighLight wrapped;
	
	public HighLightWrapper(HighLight wrapped){
		super();
		this.wrapped = wrapped;
	}

	
	@Override
	public String print(IndexSearcher indexSearcher, int hit) {
		
		string = hit + " .- " +  wrapped.print(indexSearcher, hit);
		string = string + " <br /> " +  wrappOperation(indexSearcher,hit)  + "<br />";
		setChanged();
		notifyObservers(this.string);									// BROADCAST string html del documento encontrado
	//	System.out.println(string);
		return string;
	
	}
	
	protected abstract String  wrappOperation(IndexSearcher indexSearcher, int hit);
	
}
