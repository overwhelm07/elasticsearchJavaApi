import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JavaElasticConnector {
	public static class EmployeePojo {
		 
	    public String firstName;
	    public String lastName;
	    private Date startDate;
	 
	    public EmployeePojo(String firstName, String lastName, Date startDate) {
	        this.firstName = firstName;
	        this.lastName = lastName;
	        this.startDate = startDate;
	    }
	 
	    public String name() {
	        return this.firstName + " " + this.lastName;
	    }
	 
	    public Date getStart() {
	        return this.startDate;
	    }
	}

	public static RestHighLevelClient client = new RestHighLevelClient(
			RestClient.builder(new HttpHost("localhost", 9200, "http")));
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		insertExample();
//		searchExapmle();

	}

	private static void insertExample() {
		CreateIndexRequest request = new CreateIndexRequest("sampleindex");
        request.settings(Settings.builder().put("index.number_of_shards", 1).put("index.number_of_replicas", 2));
		CreateIndexResponse createIndexResponse;
		try {
			//createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
			//System.out.println("response id: " + createIndexResponse.index());
			//string value
			IndexRequest indexRequest = new IndexRequest("sampleindex");
			indexRequest.id("001");
			indexRequest.source("SampleKey","SampleValue");
			IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
			System.out.println("response id: "+indexResponse.getId());
			System.out.println("response name: "+indexResponse.getResult().name());
			
			//inserting map data
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put("keyOne", 10); 
			map.put("keyTwo", 30);
			map.put("KeyThree", 20);
			 
			indexRequest = new IndexRequest("sampleindex");
			indexRequest.id("002");
			indexRequest.source(map);
			indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
			System.out.println("response id: "+indexResponse.getId());
			System.out.println("response name: "+indexResponse.getResult().name());
			
			//pojo sample
			EmployeePojo emp = new EmployeePojo("Elon", "Musk",  new Date());
			 
			indexRequest = new IndexRequest("sampleindex");
			indexRequest.id("003");
			//indexRequest.source(new ObjectMapper().getMapper(emp)
			indexRequest.source(new ObjectMapper().writeValueAsString(emp), XContentType.JSON);
			indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
			System.out.println("response id: "+indexResponse.getId());
			System.out.println("response name: "+indexResponse.getResult().name());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	private static void searchExapmle() {
		SearchRequest searchRequest = new SearchRequest();
	    searchRequest.indices("tesla_employees");
	    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	    searchSourceBuilder.query(QueryBuilders.matchAllQuery());
	    searchRequest.source(searchSourceBuilder);
	    Map<String, Object> map=null;
	     
	    try {
	        SearchResponse searchResponse = null;
	        searchResponse =client.search(searchRequest, RequestOptions.DEFAULT);
	        if (searchResponse.getHits().getTotalHits().value > 0) {
	            SearchHit[] searchHit = searchResponse.getHits().getHits();
	            for (SearchHit hit : searchHit) {
	                map = hit.getSourceAsMap();
	                  System.out.println("map:"+Arrays.toString(map.entrySet().toArray()));
	                    
	                
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}