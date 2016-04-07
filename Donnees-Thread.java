import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
/**
 * Donnees Class
 * attributes :
 * <ul>
 * 	<li><b>dataObject</b> : List of dataObject</li>
 * 	<li><b>sizetOfTrack</b> : Number of dataObject puts in the threads</li>
 * 	<li><b>numberOfThreads</b> : Number max of thread launch in same time</li>
 * 	<li><b>nbRate</b> : Number of rates 1, 2, 3, 4, 5, total</li>
 * 	<li><b>maxUserRates</b> : Number of rates for each users</li>
 *  <li><b>maxPopularMovies</b> : Number of rates for each movies</li>
 * </ul>
 * 
 * 
 * @author jb
 *
 */
public class Donnees{

	private ArrayList<DataObject> dataObjects;
	private static final int sizeOfTrack = 800;
	private static final int numberOfThreads = 1000;
	public static int nbRate[];
	public static Hashtable<Integer, Integer> maxUserRates ;
	public static Hashtable<Integer, Integer> maxPopularMovies ;
	
	Donnees() {
		dataObjects = new ArrayList<DataObject>();
		maxUserRates = new Hashtable<Integer, Integer>();
		maxPopularMovies = new Hashtable<Integer, Integer>();
		nbRate = new int[6]; 
	}
	
	public void load(String resourcePath) throws IOException
	{
		
		int cpt=0, cptTime=0;
		boolean attribuee, tooLong;
		double proportion;
		int timeMax=10, sleepTime=1;
		
		Integer key, keyPopular, valuePopular, keyRates, valueRates;
		
		
		//Initialisation of the Rates array
		for (int i = 0 ; i<6; i++) {
			nbRate[i]=0;
		}
		
		BufferedReader lecteurAvecBuffer = null;
		String ligne;

		try
		{
			lecteurAvecBuffer = new BufferedReader(new FileReader(resourcePath));
		}
		catch(FileNotFoundException exc)
		{
			System.out.println("Erreur d'ouverture");
		}
		
		while ((ligne = lecteurAvecBuffer.readLine()) != null) 
		{
			cpt=0;
			
			while ((ligne = lecteurAvecBuffer.readLine()) != null && cpt<sizeOfTrack) {
				String[] array = ligne.split(",");
				dataObjects.add(new DataObject(array[0],array[1],array[2],array[3]));
				cpt++;
			}
			attribuee = false;
			tooLong = false;
			cptTime=0;
			
			//While the track is not allocated to a thread we try
			//if we try too much we exit 
			while (!attribuee && !tooLong) {
				//If there are not too much threads we allocated our track
				if (Thread.activeCount()<numberOfThreads) {
					new Thread (new ThreadActions(dataObjects)).start();
					attribuee=true;
				}
				//Else we wait and retry
				else {
					try {
						Thread.sleep(sleepTime);
						cptTime++;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				tooLong = cptTime>timeMax;
			}
			//We clear our dataObjects List at each iteration
			dataObjects.clear();
			if(tooLong) {
				System.out.println("pb de thread");
			}
		}
		
		lecteurAvecBuffer.close();
		keyRates = (Integer) maxUserRates.keySet().toArray()[0];
		valueRates = maxUserRates.get(keyRates);
		Set<Integer> set= maxUserRates.keySet();
		Iterator<Integer> itr=set.iterator();
		//For each users update there number of rates
		while (itr.hasNext()) {
			key = itr.next();
			//If the user is already know we add HashTable to the maxUserRates value
			if (valueRates < maxUserRates.get(key)) {
				valueRates = maxUserRates.get(key);
				keyRates = key;
			}
		}
		
		set = maxPopularMovies.keySet();
		itr = set.iterator();
		keyPopular = (Integer) maxPopularMovies.keySet().toArray()[0];
		valuePopular = maxPopularMovies.get(keyPopular);
		//For each users update there number of rates
		while (itr.hasNext()) {
			key = itr.next();
			//If the user is already know we add HashTable to the maxUserRates value
			if (valuePopular < maxPopularMovies.get(key)) {
				valuePopular = maxPopularMovies.get(key);
				keyPopular = key;
			}
		}
		
		for (int i = 0; i<5; i++) {
			proportion = ((double) nbRate[i]/ (double) nbRate[5])*100;
			System.out.println((int) proportion+"% des films ont eu la note de "+(i+1));
		}
		System.out.println("Le film le plus populaire est "+keyPopular+" avec "+valuePopular+" votes");
		System.out.println("L'utilisateur le plus actif est "+keyRates+" avec "+valueRates+" notes" );
		
	}
	
	/**
	 * CompleteUserRates update the maxUserRates with the Hashtable in parameter
	 * @param users
	 */
	public synchronized void completeUserRates(Hashtable<Integer, Integer> users) {
		Integer key,value;
		Set<Integer> set = users.keySet();
		Iterator<Integer> itr = set.iterator();
		//For each users update there number of rates
		while (itr.hasNext()) {
			key = itr.next();
			//If the user is already know we add HashTable to the maxUserRates value
			if (maxUserRates.containsKey(key)) {
				//System.out.println(Thread.currentThread().getName()+" key: "+key+" value: "+users.get(key));
				value = maxUserRates.get(key)+users.get(key);
				maxUserRates.put(key,new Integer(value));
			}
			//Else add the user and the value
			else {
				maxUserRates.put(key,new Integer(users.get(key)));
			}
		}
	}
	
	/**
	 * completePopularityMovies update the maxUserRates with the Hashtable in parameter
	 * @param movies
	 */
	public synchronized void completePopularityMovies(Hashtable<Integer, Integer> movies) {
		Integer key,value;
		Set<Integer> set = movies.keySet();
		Iterator<Integer> itr = set.iterator();
		//For each movies update the number of rates
		while (itr.hasNext()) {
			key = itr.next();
			//If the movie is already know we add HashTable to the maxPopularMovies value
			if (maxPopularMovies.containsKey(key)) {
				value = maxPopularMovies.get(key)+movies.get(key);
				maxPopularMovies.put(key,new Integer(value));
			}
			//Else add the movie and the value
			else {
				maxPopularMovies.put(key,new Integer(movies.get(key)));
			}
		}
	}
	/**
	 * completeRatesGroup update the nbRate array with the rate put in parameter
	 * @param rate
	 */
	public synchronized void completeRatesGroup(double rate ) {
		if (rate<2){
			nbRate[0]++;
		}else if(rate<3) {
			nbRate[1]++;
		}
		else if(rate<4) {
			nbRate[2]++;
		}
		else if(rate<5) {
			nbRate[3]++;
		}
		else if(rate<6) {
			nbRate[4]++;
		}
		
		nbRate[5]++;
	}
	
	
	
	
	/**
	 * ThreadActions Class
	 * The Threads threats a track of dataObjects put in parameter
	 * @author jb
	 * attributes :
	 * <ul>
	 * 	<li><b>myDataObjects</b> : List of dataObject</li>
	 * </ul>
	 */
	public class ThreadActions implements Runnable{
		
		private ArrayList <DataObject> myDataObjects;
		
		
		ThreadActions (ArrayList <DataObject> DataObjects){
			
			myDataObjects = new ArrayList <DataObject> (DataObjects);
		}
		
	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//System.out.println(Thread.currentThread().getName());
			Integer value;
			double rate;
			Hashtable<Integer, Integer> popularityTable = new Hashtable<Integer, Integer>();
			Hashtable<Integer, Integer> userRates = new Hashtable<Integer, Integer>();
			
			// For each data Objects we fill our hastables
			for (DataObject DO: myDataObjects){
				rate = DO.getRating();
				completeRatesGroup(rate );
				if (popularityTable.containsKey(DO.getItemId())) {
					value = popularityTable.get(DO.getItemId());
					value++;
					popularityTable.put(DO.getItemId(),new Integer(value));
				}
				else {
					popularityTable.put(DO.getItemId(),new Integer(1));
				}
				
				if(userRates.containsKey(DO.getUserId())) {
					value = userRates.get(DO.getUserId());
					value++;
					userRates.put(DO.getUserId(),new Integer(value));
				}
				else {
					userRates.put(DO.getUserId(),new Integer(1));
				}
			}
			completeUserRates(userRates);
			completePopularityMovies(popularityTable);
			
			
			try {
				Thread.sleep(5);
				//System.out.println(Thread.currentThread().getName());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
}