package model;

public class UserProfile {
    public String username, email;
    public int posts, followers, following;

    public UserProfile(String u, String e, int p, int f1, int f2) {
        username = u;
        email = e;
        posts = p;
        followers = f1;
        following = f2;
    }
}
