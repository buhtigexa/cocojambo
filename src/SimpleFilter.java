import java.io.File;


public abstract class SimpleFilter extends Filter{

	protected Object condition;
	SimpleFilter(Object o){
		super();
		condition=o;
	}

}
