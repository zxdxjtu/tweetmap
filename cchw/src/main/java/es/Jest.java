package es;

import java.io.IOException;
import java.util.List;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.JestResultHandler;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Bulk.Builder;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.mapping.PutMapping;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.mapper.core.StringFieldMapper;
import org.elasticsearch.index.mapper.object.RootObjectMapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;


public class Jest{
	private static final String NOTES_TYPE_NAME = "notes";
    private static final String DIARY_INDEX_NAME = "tweet";
    static List<Note> notes = null;
    static int ID = 1;
    
    public Jest(){
		super();
	}
	
	public static JestClient jestClient(){
		 JestClientFactory factory = new JestClientFactory();
		 factory.setHttpClientConfig(new HttpClientConfig
		                        .Builder("YOUR_AWS_ELASTICSEARCH_ENDPOINT")
		                        .multiThreaded(true)
		                        .build());
		 return factory.getObject();
	}
	
	public static void createTestIndex(final JestClient jestClient)
            throws Exception {

        // create new index (if u have this in elasticsearch.yml and prefer
        // those defaults, then leave this out
        Settings.Builder settings = Settings.settingsBuilder();
        settings.put("number_of_shards", 3);
        settings.put("number_of_replicas", 0);
        jestClient.execute(new CreateIndex.Builder(DIARY_INDEX_NAME).settings(
                settings.build().getAsMap()).build());
    }
	
	public static void doSampleAndInsert(final JestClient jestClient, final String[] keywords)throws Exception{
		ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        
        cb.setOAuthConsumerKey("YOUR_CONSUMERKEY");
        cb.setOAuthConsumerSecret("YOUR_CONSUMERSECRET");
        cb.setOAuthAccessToken("YOUR_ACCESSTOKEN");
        cb.setOAuthAccessTokenSecret("YOUR_ACCESSSECRET");
 
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

        //final Builder bulkBuilder =  new Bulk.Builder();
        
        
        StatusListener listener = new StatusListener() {

            public void onException(Exception arg0) {
            	
            }

            public void onDeletionNotice(StatusDeletionNotice arg0) {
                // TODO Auto-generated method stub

            }

            public void onScrubGeo(long arg0, long arg1) {
                // TODO Auto-generated method stub

            }

            public void onStatus(Status status) {
                User user = status.getUser();
                if(status.getGeoLocation()!=null){       
                // gets Username
                String username = status.getUser().getScreenName();
                //System.out.println(username);
                String profileLocation = user.getLocation();
                //System.out.println(profileLocation);
                //System.out.println("GEO location - " + status.getGeoLocation());
                long tweetId = status.getId(); 
                //System.out.println(tweetId);
                String content = status.getText();
                //System.out.println(content +"\n");
                double longitude = status.getGeoLocation().getLongitude();
                double latitude = status.getGeoLocation().getLatitude();
//                Note source = new Note();
//                source.setUsername(username);
//                source.setContent("The Lord of the Rings is an epic high fantasy novel");
                
                String keyword = null;
                //System.out.println("here");
                for(int i = 0; i < keywords.length; i ++){
                	if(content.indexOf(keywords[i]) != -1){
                		keyword = keywords[i];
                		System.out.println("Now keyword is: " + keyword);
                	}
                }
              Note note = new Note(keyword, username, profileLocation, longitude, latitude, tweetId, content);
              
            try {
            	Index index = new Index.Builder(note).index(DIARY_INDEX_NAME)
                        .type(NOTES_TYPE_NAME).id(ID + "").build();
				//You can either upload use bulk
                jestClient.execute(index);
				ID++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           }

             
            }

            public void onTrackLimitationNotice(int arg0) {
                // TODO Auto-generated method stub

            }

			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
				
			}

        };
        FilterQuery fq = new FilterQuery();
    

        fq.track(keywords);

        twitterStream.addListener(listener);
        twitterStream.filter(fq);  
        try {
			while(true){
        	Thread.sleep(Integer.MAX_VALUE);
			}

        }finally {
			twitterStream.shutdown();
		}
	}
	//didn't use
	public void createMapping(JestClient client) throws IOException{
		PutMapping putMapping = new PutMapping.Builder(
		        "my_index",
		        "my_type",
		        "{ \"my_type\" : { \"properties\" : { \"message\" : {\"type\" : \"string\", \"store\" : \"yes\"} } } }"
		).build();
		client.execute(putMapping);
	}
	
}
