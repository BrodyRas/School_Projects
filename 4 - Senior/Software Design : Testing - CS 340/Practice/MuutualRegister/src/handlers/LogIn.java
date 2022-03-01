package handlers;

import Model.Post;
import Model.User;
import Requests.LoginRequest;
import Requests.UsernameRequest;
import Result.AuthResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LogIn {
    private Map<String, User> users = new HashMap<>();
    private Set<Post> allPosts = new HashSet<>();

    private void init() {
        User testUser = new User("Test", "User",
                "q",
                "q",
                "https://thebenclark.files.wordpress.com/2014/03/facebook-default-no-profile-pic.jpg?w=1200");
        User bryson = new User("Bryson", "Rasmussen",
                "Haminations",
                "dode",
                "https://yt3.ggpht.com/a/AGF-l7-nOzLhhRputlie9hFDFjh4gpjTttvm1vMGoA=s900-c-k-c0xffffffff-no-rj-mo");
        User george = new User("George", "Costanza",
                "Costanza69",
                "jerry",
                "https://i.kym-cdn.com/photos/images/original/000/113/867/1299827572337.jpg");
        User follower = new User("Justa", "Follower",
                "Follower",
                "follow",
                "https://cdn.shopify.com/s/files/1/0020/0196/0996/products/follower_191a853b-cc4b-4ba1-b14d-b06fc5455dcb_480x480.png?v=1530412174");
        users.put(testUser.getUsername(), testUser);
        users.put(bryson.getUsername(), bryson);
        users.put(george.getUsername(), george);
        users.put(follower.getUsername(), follower);

        Post brysonPost1 = new Post(bryson,
                "I'm officially banning ALL wrong opinions don't @ me @q or @brody",
                null);
        Post testPost1 = new Post(testUser,
                "Ahaha, ayye",
                null);
        Post georgePost1 = new Post(george,
                "I ain't talkin' to you, #pinhead!",
                "https://vignette.wikia.nocookie.net/spongebob/images/e/ef/Who_you_callin_Pinhead_by_cusackanne-1-.png/revision/latest?cb=20120129131235");
        Post followerPost1 = new Post(follower,
                "Guys, I followed you all, can I please get a follow back? #lonely",
                "https://www.gaystarnews.com/wp-content/uploads/2019/04/gay-life-coach-lonely.jpg");
        Post brysonPost2 = new Post(bryson,
                "Has @Follower been sending y'all weird messages? #creepy",
                null);
        Post testPost2 = new Post(testUser,
                "I've been feeling like a real #pinhead recently",
                null);
        Post georgePost2 = new Post(george,
                "I still don't know what the deal is with airline food #creepy",
                null);
        Post followerPost2 = new Post(follower,
                "I don't need y'all, I'm independent... but please, let's hang out",
                null);
        allPosts.add(brysonPost1);
        allPosts.add(testPost1);
        allPosts.add(georgePost1);
        allPosts.add(followerPost1);
        allPosts.add(brysonPost2);
        allPosts.add(testPost2);
        allPosts.add(georgePost2);
        allPosts.add(followerPost2);

        bryson.addFollower("Costanza69");
        bryson.addFollower("q");
        bryson.addFollower("Follower");
        testUser.addFollower("Costanza69");
        testUser.addFollower("Haminations");
        testUser.addFollower("Follower");
        george.addFollower("Haminations");
        george.addFollower("q");
        george.addFollower("Follower");

        bryson.addFollowing("Costanza69");
        bryson.addFollowing("q");
        testUser.addFollowing("Costanza69");
        testUser.addFollowing("Haminations");
        george.addFollowing("Haminations");
        george.addFollowing("q");
        follower.addFollowing("Haminations");
        follower.addFollowing("Costanza69");
        follower.addFollowing("q");
    }

    public AuthResult handle(LoginRequest request, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Entering login...");
        init();
        String username = request.username;
        String password = request.password;
        User user = users.get(username);
        if(user == null){
            logger.log("User doesn't exist!");
            return new AuthResult("User doesn't exist");
        }
        else{
            if(!user.getPassword().matches(password)){
                logger.log("Bad credentials!");
                return new AuthResult("Invalid credentials");
            }
            else{
                logger.log("Successful login!");
                return new AuthResult(username, "token");
            }
        }
    }
}