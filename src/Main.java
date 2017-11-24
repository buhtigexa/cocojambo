import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import weka.classifiers.trees.J48;


public class Main {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		Indexer indexador = null;

try{
		
	
	String directorioIndice= "//home/marxel/Documentos/indices/indiceDs3/";
	String directorioDataSet ="//home/marxel/Documentos/dataSet3/";
	int maxResultados=200;
	Version version  = Version.LUCENE_33;
	Analyzer analyzador = new StandardAnalyzer(version);
	
	
	
	indexador = new HtmlIndexer(analyzador,directorioIndice,directorioDataSet,new FileFilter());

//	indexador.index(directorioDataSet);
	
	//BatchTrainer trainer = new BatchTrainer(indexador);
	//trainer.populate();
	//trainer.train();
	
	

	
	
	String campoDeBusqueda ="contents";
	String buscarFrase ="";
	String buscarPalabras="black rain review";
	String buscarAlgunasPalabras="black rain review";//"rain andy";
	String buscarNingunaPalabras="";
	
	
	Query queryResaltador = null;
	Query queryBuscar = null;
	
	QueryFactory fabricaConsultas = new QueryFactory(campoDeBusqueda,version, analyzador);
	
	CompositeCocoQuery queryCompuestaBuscar = (CompositeCocoQuery)fabricaConsultas.getCompositeCocoQuery();
	
			
	/*-----------------------------------------------CONSULTAS POR FRASE -----------------------
	

	if (!buscarFrase.isEmpty() ){
		SimpleCocoQuery  consultaFrase = (SimpleCocoQuery) fabricaConsultas.getPhraseQuery(buscarFrase); 
		//queryBusqueda = consultaFrase.buildQuery();
		queryCompuestaBuscar.addQuery(consultaFrase,Occur.MUST);
	}
	
	
	
	
	/*-----------------------------------------------CONSULTAS POR TODAS LAS PALABRAS -----------------------
	
	
	if (!buscarPalabras.isEmpty()){
		SimpleCocoQuery  consultaTodasPalabras = (SimpleCocoQuery) fabricaConsultas.getFullWordsQuery(buscarPalabras); 
		//queryBuscar = consultaTodasPalabras.buildQuery();
		queryCompuestaBuscar.addQuery(consultaTodasPalabras,BooleanClause.Occur.MUST);
	}
	
	
	
	/*-----------------------------------------------CONSULTAS POR ALGUNAS -----------------------*/
	
	
	if ( !buscarAlgunasPalabras.isEmpty()	){
		SimpleCocoQuery  consultaAlgunas = (SimpleCocoQuery) fabricaConsultas.getOptionalWordsQuery(buscarAlgunasPalabras); 
		//queryBusqueda = consultaAlgunas.buildQuery();
		queryCompuestaBuscar.addQuery(consultaAlgunas, BooleanClause.Occur.SHOULD);
	}
	
	
	

	/*-----------------------------------------------CONSULTAS POR NINGUNA -----------------------
	
	if (!buscarNingunaPalabras.isEmpty() ){	 
		SimpleCocoQuery  consultaNinguna = (SimpleCocoQuery) fabricaConsultas.getNoneWordsQuery(buscarNingunaPalabras); 
		//queryBusqueda = consultaNinguna.buildQuery();
		queryCompuestaBuscar.addQuery(consultaNinguna,BooleanClause.Occur.MUST_NOT); 
		
	}
	
	
	
	
	
	SimpleCocoQuery cocoResaltador =(SimpleCocoQuery) fabricaConsultas.getHighLightQuery(buscarFrase, buscarPalabras, buscarAlgunasPalabras);
	queryResaltador =  cocoResaltador.buildQuery();
	
	
	HighLight imprimePorCampo1 = new HighLightField("filename"); // porque siempre imprimo el nombre del documento que encuentro
	HighLight imprimePorCampo2 = new HighLightField("year");
	HighLight imprimePorCampo3 = new HighLightField("reviewer");
	HighLight imprimePorCampo4 = new HighLightField("title");
	
	HighLight decorador = new HighLightTerms(imprimePorCampo1, campoDeBusqueda, queryResaltador); // decorador que imprime el contenido

	
	/******************************** PRUEBA PARA UNA CLASIFICACION  ****************
	 * */
	
	
	
	
	 	
	
		long start = new Date().getTime();
		
		Searcher searcher = new Searcher(analyzador, indexador,directorioIndice,maxResultados);
		
		searcher.testMatch( queryCompuestaBuscar.buildQuery() ) ;
		
		long end = new Date().getTime();
		long total = end - start;
		System.out.println("Tiempo de busqueda :  "  + total  + " millisegundos");
	
	
	
	
	} catch(Exception e) { e.printStackTrace(); }
		finally{ indexador.close();
		}
	}
}
