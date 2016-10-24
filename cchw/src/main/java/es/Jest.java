package es;

import java.io.IOException;
import java.util.List;

//import io.searchbox.action.Action;
//import io.searchbox.action.BulkableAction;
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
		                        .Builder("http://search-tweet-i6eqzgl7vezziy444zvg6hxdbi.us-west-2.es.amazonaws.com")
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
	
//	public static void readAllData(final JestClient jestClient)
//            throws Exception {
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        //searchSourceBuilder.query(QueryBuilders.termQuery("note", "see"));
//        searchSourceBuilder.query(QueryBuilders.matchQuery("keyword", "love"));
//
//        Search search = new Search.Builder(searchSourceBuilder.toString())
//                .addIndex(DIARY_INDEX_NAME).addType(NOTES_TYPE_NAME).build();
//        System.out.println(searchSourceBuilder.toString());
//        JestResult result = jestClient.execute(search);
//        List<Note> notes = result.getSourceAsObjectList(Note.class);
//        System.out.println("PreReading----");
//        for (Note note : notes) {
//        	System.out.println("Reading----");
//        	System.out.println(note);
//            System.out.println("Username: " + note.getUsername() + "\n");
//            System.out.println("finished!");
//        }
//    }
	
//	private static void deleteTestIndex(final JestClient jestClient)
//            throws Exception {
//        DeleteIndex deleteIndex = new DeleteIndex.Builder(DIARY_INDEX_NAME)
//                .build();
//        jestClient.execute(deleteIndex);
//    }
//	
//	private static void indexSomeData(final JestClient jestClient)
//            throws Exception {
//        
//		
		
//		// Blocking index
//        final Note note1 = new Note("mthomas", "Note1: do u see this - "
//                + System.currentTimeMillis());
//        Index index = new Index.Builder(note1).index(DIARY_INDEX_NAME)
//                .type(NOTES_TYPE_NAME).build();
//        jestClient.execute(index);

//        // Asynch index
//        final Note note2 = new Note("mthomas", "Note2: do u see this - "
//                + System.currentTimeMillis());
//        index = new Index.Builder(note2).index(DIARY_INDEX_NAME)
//                .type(NOTES_TYPE_NAME).build();
//        jestClient.executeAsync(index, new JestResultHandler<JestResult>() {
//            public void failed(Exception ex) {
//            }
//
//            public void completed(JestResult result) {
//                note2.setId((String) result.getValue("_id"));
//                System.out.println("completed==>>" + note2);
//            }
//        });
//
//        // bulk index
//        final Note note3 = new Note("mthomas", "Note3: do u see this - "
//                + System.currentTimeMillis());
//        final Note note4 = new Note("mthomas", "Note4: do u see this - "
//                + System.currentTimeMillis());
//        Bulk bulk = new Bulk.Builder()
//                .addAction(
//                        new Index.Builder(note3).index(DIARY_INDEX_NAME)
//                                .type(NOTES_TYPE_NAME).build())
//                .addAction(
//                        new Index.Builder(note4).index(DIARY_INDEX_NAME)
//                                .type(NOTES_TYPE_NAME).build()).build();
//        JestResult result = jestClient.execute(bulk);
//
//        Thread.sleep(2000);
//
//        System.out.println(result.toString());
//    }
	
	public static void doSampleAndInsert(final JestClient jestClient, final String[] keywords)throws Exception{
		ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        
        cb.setOAuthConsumerKey("0QCOd01kMqhrBiv2uI2TGmoYx");
        cb.setOAuthConsumerSecret("ISuJEdHms5etk2WqKANdVT3gZcTR446Hwyl9TBA6ZNyd52S3gN");
        cb.setOAuthAccessToken("790220452400599040-5QdnY69HhSOtxTfUf4ox4D6H5vOcyl7");
        cb.setOAuthAccessTokenSecret("Wnu5xq47d8RDi8m0N0hyuo7V84byXF9i2WPNyUzMckZie");
        
//        cb.setOAuthConsumerKey("sS7kb2Aem4qRic99uvHIL9FJ7");
//        cb.setOAuthConsumerSecret("YmIUH0T88dlKmhcOk89kZctSaWOFfhMxjhGzbkz52qoYd4lOm5");
//        cb.setOAuthAccessToken("2982765431-qTC62xjv0JNhs8Y2l8Mwo0msAYj4bzF7bFkgvwF");
//        cb.setOAuthAccessTokenSecret("NkL53ic4n1TgnQa69m6avCpqtSgOPOGRslZ4NxE9biEsM");

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
                //System.out.println("keyword: " + keyword);
              Note note = new Note(keyword, username, profileLocation, longitude, latitude, tweetId, content);
              //System.out.println("here1");
              //notes.add(note);
              //System.out.println("here2");
              
//              Bulk bulk = new Bulk.Builder()
//              .addAction(
//                      new Index.Builder(note).index(DIARY_INDEX_NAME)
//                              .type(NOTES_TYPE_NAME).build()).build();
 
              //Index index = new Index.Builder(note1).index(DIARY_INDEX_NAME)
              //.type(NOTES_TYPE_NAME).build();
              //bulkBuilder.addAction(index);
              
//            bulkBuilder.addAction(
//                    new Index.Builder(new Note(keyword, username, profileLocation, status.getGeoLocation().toString(), tweetId, content)).index(DIARY_INDEX_NAME)
//                            .type(NOTES_TYPE_NAME).build()).build();
              
            try {
            	Index index = new Index.Builder(note).index(DIARY_INDEX_NAME)
                        .type(NOTES_TYPE_NAME).id(ID + "").build();
				jestClient.execute(index);
				ID++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
//              System.out.println("bulkBuilder: " + bulkBuilder.toString());
           }
              //System.out.println("here3");
              //JestResult result;
              //Index index = new Index.Builder(note).index(NOTES_TYPE_NAME).type(NOTES_TYPE_NAME).build();
              //System.out.println(note.toString());
              //System.out.println("here4");
				//JestResult result;
			

             
            }

            public void onTrackLimitationNotice(int arg0) {
                // TODO Auto-generated method stub

            }

			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
				
			}

        };
        FilterQuery fq = new FilterQuery();
    
        //String keywords[] = {"love"};

        fq.track(keywords);

        twitterStream.addListener(listener);
        twitterStream.filter(fq);  
        try {
			while(true){
        	Thread.sleep(Integer.MAX_VALUE);
			}
//			Bulk bulk = bulkBuilder.build();
//			JestResult result;
//			try {
//				//System.out.println("here5");
//				result = jestClient.execute(bulk);
//				System.out.println("succeed: " + result.toString());
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			} 

        }finally {
			twitterStream.shutdown();
		}
	}
	
	public void createMapping(JestClient client) throws IOException{
		PutMapping putMapping = new PutMapping.Builder(
		        "my_index",
		        "my_type",
		        "{ \"my_type\" : { \"properties\" : { \"message\" : {\"type\" : \"string\", \"store\" : \"yes\"} } } }"
		).build();
		client.execute(putMapping);
	}
	
//	public static void main(String[] args) {
//        try {
//           JestClient jestClient = jestClient();
//
//            try {
//                // run test index & searching
//                //deleteTestIndex(jestClient);
//                createTestIndex(jestClient);
//                //indexSomeData(jestClient);
//                String keywords[] = {"love", "game"};
//                doSampleAndInsert(jestClient, keywords);
//                readAllData(jestClient);
//            } finally {
//                // shutdown client
//                jestClient.shutdownClient();
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
	
//	public void run(){
//		//while(true){
//		try {
//	           JestClient jestClient = jestClient();
//
//	            try {
//	                // run test index & searching
//	                System.out.println("Start running");
//	            	//deleteTestIndex(jestClient);
//	                createTestIndex(jestClient);
//	                //indexSomeData(jestClient);
//	                String keywords[] = {"job", "love", "game", "fashion", "Trump", "NewYork","fashion", "food", "LOL","Hilary", "hello"};
//	                doSampleAndInsert(jestClient, keywords);
//	                //readAllData(jestClient);
//	                System.out.println("running stop");
//	            } finally {
//	                // shutdown client
//	                jestClient.shutdownClient();
//	            }
//
//	        } catch (Exception ex) {
//	            ex.printStackTrace();
//	        }
//		}
	//}
}
