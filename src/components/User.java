package components;

import java.util.ArrayList;
import java.util.List;

public class User {
	
	private  String[] userTags;
	private  String userTagString;
	private  List<Post> activePosts;
	private final int userId;
	private static int userCount = 0;
	
	public User(String[] tags, List<Post> posts) {
		this.userId = userCount++;
		this.setUserTags(tags);
		this.userTagString = String.join("", tags);
		this.activePosts = posts;
	}
	
	public String getUserTagString() {
		return userTagString;
	}

	public int getUserId() {
		return userId;
	}
	
	public void sharePosts(List<Post> sharedPosts) {
		
//		System.out.print("Key: " + this.getUserTagString() + " ");      
//        System.out.println("id: " + this.getUserId() + ", ");
		
	    int postcount = 0;
	    for (int i = 0; i < activePosts.size(); i++) {
	        Post post = activePosts.get(i);
	        post.setShared();
	        if (post.isShared()) {
	            sharedPosts.add(post);
	            if(++postcount == 3) {
	            	break;
	            }
	            
	        }
	        
	    }
	}

	public String[] getUserTags() {
		return userTags;
	}

	public void setUserTags(String[] userTags) {
		this.userTags = userTags;
	}
	

}

