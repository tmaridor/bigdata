
public class DataObject {

	public DataObject(String userId,String itemId, String rating, String timestamp)
	{
		this.userId = Integer.parseInt(userId);
		this.itemId = Integer.parseInt(itemId);
		this.rating = Integer.parseInt(rating);
		this.timestamp = Integer.parseInt(timestamp);
	}
	public DataObject(int userId, int itemId, int rating, int timestamp)
	{
		this.userId = userId;
		this.itemId = itemId;
		this.rating = rating;
		this.timestamp = timestamp;		
	}
	
	private int userId;
	private int itemId;
	private int rating;
	private int timestamp;
	
	public int getUserId() {
		return userId;
	}
	public int getItemId() {
		return itemId;
	}
	public int getRating() {
		return rating;
	}
	public int getTimestamp() {
		return timestamp;
	}
	
	
}
