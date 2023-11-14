package com.example.mini_twitter.User;

import java.util.ArrayList;
import java.util.List;

public class User implements Entity, Subject, Observer {
    private static List<User> users = new ArrayList<>();

    private String uniqueID;
    private String groupID;
    private String name;
    private ArrayList<User> followers;
    private ArrayList<User> followings;
    private ArrayList<String> newsFeed;
    public String toString() { return name; }

    public User(String uniqueID, String name) {
        this.uniqueID = uniqueID;
        this.groupID = "";
        this.name = name;
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.newsFeed = new ArrayList<>();
    }

    public static synchronized User getInstance(String uniqueID, String name) {
        User user = users.stream()
                .filter(u -> u.uniqueID.equals(uniqueID))
                .findAny()
                .orElse(null);
        if (user == null) {
            user = new User(uniqueID, name);
            users.add(user);
        }
        return user;
    }

    @Override
    public String getUniqueID() {
        return uniqueID;
    }

    public String getGroupID() { return groupID; }
    public void setGroupID(String id) { this.groupID = id; }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return "User";
    }

    @Override
    public void register(User u) {
        if (!followers.contains(u)) {
            followers.add(u);
        }
        System.out.println(u + " has started following " + name);
    }

    @Override
    public void unregister(User r) {
        followers.remove(r);
        System.out.println(r + " has stopped following " + name);
    }

    @Override
    public void notifyFollowers(String tweet) {
        for(User follower : followers)
        {
            follower.update(name, tweet);
        }
        System.out.println();
    }

    @Override
    public void update(String followedUser , String tweet) {
        System.out.println(name + " has received "+ followedUser + "'s tweet :: "+  tweet);
    }

    public void postTweet(String tweet) {
        newsFeed.add(tweet);
        for (User follower : followers) {
            follower.addToNewsFeed(tweet);
        }
        notifyFollowers(tweet);
    }

    public List<String> getNewsFeed() {
        return newsFeed;
    }

    public void addToNewsFeed(String tweet) {
        newsFeed.add(tweet);
    }
}
