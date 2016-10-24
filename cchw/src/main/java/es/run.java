package es;

import io.searchbox.client.JestClient;
import es.Jest;

public class run implements Runnable {

	public void run(){
		//while(true){
		try {
	           JestClient jestClient = Jest.jestClient();

	            try {
	                // run test index & searching
	                System.out.println("Start running");
	            	//deleteTestIndex(jestClient);
	                //Jest.createTestIndex(jestClient);
	                //indexSomeData(jestClient);
	                String keywords[] = {"job", "love", "game", "fashion", "Trump", "NewYork","fashion", "food", "LOL","Hilary", "hello"};
	                Jest.doSampleAndInsert(jestClient, keywords);
	                
	                
	                
	                //readAllData(jestClient);
	                System.out.println("running stop");
	            } finally {
	                // shutdown client
	                //jestClient.shutdownClient();
	            }

	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
		}
	//}
}
