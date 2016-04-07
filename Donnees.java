
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Donnees {

	private ArrayList<DataObject> list;
	
	public Donnees(){
		list= new ArrayList<DataObject> ();
	}
	
	public void Load(String resourcePath) throws IOException
	{

		char separator = ' ';
		File file = new File(resourcePath);
	    List<String> lines = Files.readAllLines(file.toPath(),StandardCharsets.UTF_8);
	    for (String line : lines) {
	        String[] array = line.split("	");
	        list.add(new DataObject(array[0],array[1],array[2],array[3]));
	    }
	}
	
	
	//Displays the proportions
	public void getProportions()
	{
		int total=0,taille1=0,taille2=0,taille3=0,taille4=0,taille5=0;
		double proportion=0;
		
		for(DataObject d: list){
			switch(d.getRating()){
			case 1:taille1++;
			break;
			case 2:taille2++;
			break;
			case 3:taille3++;
			break;
			case 4:taille4++;
			break;
			case 5:taille5++;
			break;
			default:;
			}
			total++;
		}
		proportion=taille1/total;
		System.out.println("\n Proportion de 1: "+taille1);
		System.out.println("\n Proportion de 2: "+taille2);
		System.out.println("\n Proportion de 3: "+taille3);
		System.out.println("\n Proportion de 4: "+taille4);
		System.out.println("\n Proportion de 5: "+taille5);
	}
	
	//Displays the user who rated the most
	public void getUserWithMostRatings()
	{
		int cpt=0;
		ArrayList<Integer> notes;
		notes=new ArrayList();
		ArrayList<Integer> users;
		users=new ArrayList();
		int taille=0,nbNote=0, userId=0,max=0;
			
		for(DataObject d: list){
		
			userId=d.getUserId();
			
			for(DataObject d1: list){
				if(d1.getUserId()==userId){
					nbNote++;
				}
			}
			users.add(d.getUserId());
			notes.add(nbNote);
		}
		for(int nb:notes){
			
			if(nb>max){
				max=nb;
				userId=users.get(cpt);
			}
			cpt++;
		}
		System.out.println("\n"+userId);	
	}
	
	//Displays the most rated film
	
	public int getNbRate(int itemid) {
		int nbNote=0;
		for(DataObject d1: list){
				if(d1.getItemId()==itemid){
					nbNote++;
				}
			}
		return nbNote;
	}
	
	public void getMostPopularFilm()
	{
		int max=0, itemId=0;
		for(DataObject d: list)
		{
			if(this.getNbRate(d.getItemId())>max){
				max=getNbRate(d.getItemId());
				itemId=d.getItemId();
			}
		}
		System.out.println("\n"+itemId);	
	}
	
	
}
