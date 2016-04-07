import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			Donnees donnee=new Donnees();
			try{
				donnee.Load("C:/Users/Laury TOSSA/Documents/java-bigdata/JavaBigData/Datasets/Movielens/ml-100k/u.data");
			}catch(IOException e){
				e.printStackTrace();
			}
			
			donnee.getMostPopularFilm();
			donnee.getUserWithMostRatings();
			donnee.getProportions();
		
	}

}
