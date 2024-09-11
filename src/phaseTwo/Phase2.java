package phaseTwo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import components.Post;
import components.User;

public class Phase2 {

    //--------------------------Global Variables for Tags-----------------------
    // Declaring list variables as final where values don't change during execution
    private static final List<String> AGE_GROUPS = new ArrayList<>(Arrays.asList("Teens", "YoungAdult", "Adult", "MiddleAge", "Older"));
    private static final List<String> GENDERS = new ArrayList<>(Arrays.asList("Male", "Female"));
    private static final List<String> PROFESSIONS = new ArrayList<>(Arrays.asList("Student", "Freelancer", "Retired", "WebDev", "Actor", "Shopkeeper"));
    private static final List<String> INTERESTS = new ArrayList<>(Arrays.asList("Art", "Gaming", "Animals", "Food", "Anime", "Cars", "IT", "Sports", "Education", "Entertainment"));
    
    // Declare variables as static since they are reused in multiple places
    private static final List<String> alternativeInterests = new ArrayList<>();
    private static final List<Post> sharedPosts = new ArrayList<>();
    private static boolean hasMorePosts;

    //--------------------------User Graph---------------------------
    // Inner UserGraph class to manage users based on tags
    private static class UserGraph {
        private final Map<String, List<User>> userGraph = new HashMap<>();

        // Adds a user to the userGraph and updates lists if the tag is new
        public void addUser(User user) {
            String key = user.getUserTagString();
            List<User> userList = userGraph.computeIfAbsent(key, k -> {
                addTagsToLists(user.getUserTags());
                System.out.println("Added new tag");
                return new ArrayList<>();
            });

            userList.add(user);
            System.out.println("Add user");
        }

        // Updates respective lists with new user tags if not already present
        private void addTagsToLists(String[] userTags) {
            String interest = userTags[0];
            String profession = userTags[1];
            String ageGroup = userTags[2];
            String gender = userTags[3];

            if (!INTERESTS.contains(interest)) {
                INTERESTS.add(interest);
                System.out.println("New interest added: " + interest);
            }
            if (!PROFESSIONS.contains(profession)) {
                PROFESSIONS.add(profession);
                System.out.println("New profession added: " + profession);
            }
            if (!AGE_GROUPS.contains(ageGroup)) {
                AGE_GROUPS.add(ageGroup);
                System.out.println("New ageGroup added: " + ageGroup);
            }
            if (!GENDERS.contains(gender)) {
                GENDERS.add(gender);
                System.out.println("New gender added: " + gender);
            }
        }

        // Displays the current graph structure
        public void showGraph() {
            for (Map.Entry<String, List<User>> entry : userGraph.entrySet()) {
                String key = entry.getKey();
                List<User> users = entry.getValue();
                // Optional: Uncomment to print the graph
                // System.out.print("Key: " + key + " ");
                // for (User user : users) {
                //     System.out.print("id: " + user.getUserId() + ", ");
                // }
                // System.out.println();
            }
        }

        // Retrieves users with the matching tag
        public List<User> getUsersByTag(String tag) {
            return userGraph.getOrDefault(tag, new ArrayList<>());
        }
    }

    //------------------------------Phase 2 Main-------------------------
    public static void main(String[] args) {
        UserGraph userGraph = new UserGraph();
        List<User> users = generateUserList();
        User hostUser = users.get(0);
        int userCountLimit = 20;

        // Adding users to the graph
        for (User user : users) {
            userGraph.addUser(user);
        }

        // Run phaseTwo logic
        phaseTwo(hostUser, userGraph, userCountLimit);
        phaseTwo(hostUser, userGraph, userCountLimit);
        phaseTwo(hostUser, userGraph, userCountLimit);

        // Display the graph
        // userGraph.showGraph();
    }

    // The main logic for sharing posts based on user preferences and available users
    public static List<Post> phaseTwo(User host, UserGraph userGraph, int userCountLimit) {
        System.out.println("Phase Two Start " + host.getUserTagString());

        // Clear and initialize alternative interests
        alternativeInterests.clear();
        alternativeInterests.addAll(INTERESTS);

        // Clear shared posts
        sharedPosts.clear();

        String[] userPreferences = host.getUserTags();
        hasMorePosts = true;

        // Main loop to get posts until the limit is reached
        while (hasMorePosts) {
            String tag = String.join("", userPreferences);
            List<User> users = userGraph.getUsersByTag(tag);

            for (User user : users) {
                user.sharePosts(sharedPosts);
                if (sharedPosts.size() >= userCountLimit) {
                    hasMorePosts = false;
                    break;
                }
            }

            // Modify preferences if necessary and retry with different interests
		if (sharedPosts.size() < userCountLimit && alternativeInterests.size() > 1) {
   		 alternativeInterests.remove(userPreferences[0]);
   		 userPreferences[0] = getRandomElement(alternativeInterests);
            } else {
                break;
            }
        }

        // Print shared posts
        int index = 0;
        for (Post post : sharedPosts) {
            System.out.println(index++ + ": " + post.getTitle() + " " + post.getCaption() + " " + post.isShared());
        }

        return sharedPosts;
    }

    // Generates a list of users with random attributes
    public static List<User> generateUserList() {
        final int TOTAL_USERS = 2000; // Final as this value doesn't change
        List<User> users = new ArrayList<>();

        for (int i = 0; i < TOTAL_USERS; i++) {
            String[] userTags = {
                getRandomElement(INTERESTS),
                getRandomElement(PROFESSIONS),
                getRandomElement(AGE_GROUPS),
                getRandomElement(GENDERS)
            };

            List<Post> posts = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                posts.add(new Post("Post " + i, "Caption for post " + j, 1, "Author"));
            }

            users.add(new User(userTags, posts));
        }

        return users;
    }

    // Utility function to get a random element from a list
    private static <T> T getRandomElement(List<T> list) {
        return list.get((int) (Math.random() * list.size()));
    }
}
