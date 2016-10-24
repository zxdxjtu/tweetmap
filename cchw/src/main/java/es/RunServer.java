package es;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;

public class RunServer implements Runnable {

	public void run() {
		System.out.println("start server connecting");
        Configuration config = new Configuration();
        config.setPort(8081);
        config.setHostname("localhost");

        final SocketIOServer server = new SocketIOServer(config);
//        server.addConnectListener(new ConnectListener(){
//
//			public void onConnect(SocketIOClient arg0) {
//				System.out.println("onConnected!");
//				server.getBroadcastOperations().sendEvent("twit");
//				
//			}
//        	
//        });
//        
//        server.addDisconnectListener(new DisconnectListener(){
//
//			public void onDisconnect(SocketIOClient arg0) {
//				// TODO Auto-generated method stub
//				System.out.println("Disconnected!");
//			}
//        	
//        });
        server.addEventListener("keypass", Note.class, new DataListener<Note>() {
            
        	public void onData(SocketIOClient client, Note data, AckRequest ackRequest) throws IOException {
        			StringBuilder sb = new StringBuilder();
        			String key = data.getKeyword();
        			JestClientFactory factory = new JestClientFactory();
        			 factory.setHttpClientConfig(new HttpClientConfig
        			                        .Builder("http://localhost:9200")
        			                        .multiThreaded(true)
        			                        .build());
        			 JestClient jestClient = factory.getObject();
        			
        			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        	        //searchSourceBuilder.query(QueryBuilders.termQuery("note", "see"));
        	        searchSourceBuilder.query(QueryBuilders.matchQuery("keyword", key));

        	        Search search = new Search.Builder(searchSourceBuilder.toString())
        	                .addIndex("diary").addType("notes").build();
        	        System.out.println(searchSourceBuilder.toString());
        	        JestResult result = jestClient.execute(search);
        	        List<Note> notes = result.getSourceAsObjectList(Note.class);
        	        System.out.println("PreReading----");
        	        for (Note note : notes) {
        	        	System.out.println(note);
        	        	server.getBroadcastOperations().sendEvent("searchresult", note);
        	        }
            	
        	}
        });

        server.start();
        System.out.println("start start!");



      try {
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		server.stop();
		System.out.println("server stopped!");

	}

}
