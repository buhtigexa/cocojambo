import java.io.IOException;
import weka.filters.Filter;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.search.IndexSearcher;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.StringToWordVector;


public class BatchTrainer {
	
	IndexReader reader;
	FastVector fvEsquema;
	Instances instancias;

	Instances procesadas;
	StringToWordVector filter;
	FastVector fvYear;
	Attribute year;
	FastVector fvTitle;
	Attribute title;
	Attribute filename;
	Attribute contents;
	FastVector fvReviewer;
	Attribute reviewer;
	Instances instances;
	
	//Classifier cModel; 
	
	J48	cModel;
	
	public BatchTrainer(Indexer indexer){
		
 
	//cModel = (Classifier)new NaiveBayes();
		cModel = new  J48();
		
	fvYear = new FastVector();
	TermFreqVector tfvYear = null;
	String[] years =null;
	try{	
			reader = indexer.getWriter().getReader();
			int maxDoc = reader.maxDoc();	
		
			reader = indexer.getWriter().getReader();
			for (int d = 0; d < 20; d++)	{
		 		tfvYear = reader.getTermFreqVector(d, "year");
		 		if (tfvYear!=null)	years = tfvYear.getTerms();
		 			if (years!=null) for (int y=0;y < years.length;y++ )
		 							 if (!fvYear.contains(years[0]))	fvYear.addElement(years[0]);
		 	}
	
			title = new Attribute("title",		  (FastVector)null);
			year = new Attribute("year",fvYear);
			fvEsquema= new FastVector(2);
			fvEsquema.addElement(year);    
			fvEsquema.addElement(title);    
			
			instancias = new Instances("Mi data set",fvEsquema,0);
			
	}		 
	catch (Exception e) { 	e.printStackTrace(); 	 }
			
			
	}

	public void populate (){
		
		TermFreqVector tfvYear = null;
		TermFreqVector tfvTitle = null;
		String strTitle=null;
		String[] years=null;
		String[] titles=null;
		String attrYear = null;
		Instance instancia = null;
	try{
		for (int i = 0; i < 3; i++){
			instancia=new Instance(2);
			attrYear = new String();
			strTitle = new String();
			tfvYear = reader.getTermFreqVector(i, "year");
			if (tfvYear!=null){
				years = tfvYear.getTerms();
				if (years!=null)
					attrYear = years[0];
					tfvTitle = reader.getTermFreqVector(i, "title");
					if (tfvTitle !=null){
						titles=tfvTitle.getTerms();
						if (titles != null)
							for (int t = 0; t < titles.length ; t++)
								strTitle = strTitle + " " + titles[t];
			}
			instancia.setValue((Attribute)fvEsquema.elementAt(0),attrYear);
			instancia.setValue((Attribute)fvEsquema.elementAt(1),strTitle);
			
			instancias.add(instancia);
	//		instancia.setDataset(instancias);
			}
			
		}
		System.out.println(instancias);
		
	}	
	catch(Exception e){		e.printStackTrace();		}
	}

	public void train(){
		Instance clasificable = new Instance(0);
		try{
			Instances procesadas = null;
			filter=new StringToWordVector();
			//filter.setInputFormat(instancias);
			//filter.setAttributeIndices("2");
			//procesadas = filter.useFilter(instancias, filter);
			//procesadas.setClassIndex(0);
			//cModel.buildClassifier(procesadas);
			
			Instances dataIn = instancias;
			Instance newData;
			filter.setInputFormat(instancias);
			for (int i=0; i < dataIn.numInstances();i++)
				filter.input(dataIn.instance(i));
			if (filter.batchFinished() ){
				procesadas = filter.getOutputFormat();
				while ((newData = filter.output()) !=null )
						procesadas.add(newData);
			}	
	//		procesadas.setClassIndex(0);
			
			Instance instancia = new Instance(2);
			instancia.setValue((Attribute)fvEsquema.elementAt(0), "1998");
			instancia.setValue((Attribute)fvEsquema.elementAt(1), "8mm");
			
			instancia.setDataset(instancias);
			
			filter.input(instancia);
			Object Instance;
			if (filter.batchFinished())
				clasificable = filter.output();
			
			clasificable.setDataset(procesadas);
			procesadas.add(clasificable);
			cModel.buildClassifier(procesadas);	
			Evaluation eTest = new Evaluation(procesadas);
			eTest.evaluateModel(cModel, procesadas);
			
			System.out.println("Evaluacion del modelo:" +  cModel);
			
			double[] fDistribution = cModel.distributionForInstance(clasificable);
			cModel.classifyInstance(clasificable);
			for (int f=0; f < fDistribution.length;f++)
				System.out.println("Probabilidad de pertenecer a :"  +  fDistribution[f]  );
		}
		catch(Exception e){
				e.printStackTrace();
		}
	}
}		