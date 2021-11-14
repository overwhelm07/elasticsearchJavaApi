package JavaElasticSearch.JavaElasticSearch;

/**
 * Hello world!
 *
    GET /tesla_employees/_search
	{
	  "query":{
	    "match_all":{}
	  }
	}
	
	
	POST /tesla_employees/_create/1
	{
	  "name": "Elon Musk",
	  "age": 49,
	  "experienceInYears" : 28
	}
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
