package es;

import es.Jest;
import es.Note;
import es.run;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.google.gson.Gson;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.params.Parameters;

@WebServlet(name = "newServlet", urlPatterns = {"/newServlet"}, loadOnStartup=1)
//@WebServlet("/newServlet")
public class newServlet extends HttpServlet{
	
	private static final long serialVersionUID = 489141392031L;
	public void init(){
		System.out.println("init");
		new Thread(new run()).start();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

		System.out.println("achieve servlet");
		String key = request.getParameter("key");
		
		JestClientFactory factory = new JestClientFactory();
		 factory.setHttpClientConfig(new HttpClientConfig
		                        .Builder("http://search-tweet-i6eqzgl7vezziy444zvg6hxdbi.us-west-2.es.amazonaws.com")
		                        .multiThreaded(true)
		                        .build());
		 JestClient jestClient = factory.getObject();
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //searchSourceBuilder.query(QueryBuilders.termQuery("note", "see"));
        searchSourceBuilder.query(QueryBuilders.matchQuery("keyword", key));

        Search search = new Search.Builder(searchSourceBuilder.toString()).setParameter(Parameters.SIZE, 100)
                .addIndex("tweet").addType("notes").build();
        System.out.println(searchSourceBuilder.toString());
        JestResult result = jestClient.execute(search);
        List<Note> notes = result.getSourceAsObjectList(Note.class);
        System.out.println("PreReading----");
        List<Map> datalist = new ArrayList<Map>(); 
        
        for (Note note : notes) {
        	System.out.println("reading");
        	Map<String, Double> data = new HashMap<String, Double>();
        	
        	System.out.println("lati" + note.getLatitude() + ", " + "longi" + note.getLongitude());
        	
        	data.put("lati", note.getLatitude());
        	data.put("longi", note.getLongitude());
        	datalist.add(data);
        	
            
        }
        String json = new Gson().toJson(datalist);
    	response.setContentType("application/json");
    	response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(json);
		
        
    }   
}
