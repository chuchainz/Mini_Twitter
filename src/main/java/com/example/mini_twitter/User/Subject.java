package com.example.mini_twitter.User;

/**
 * This interface deals with adding, removing, and updating all observers (followers)
 */
public interface Subject {
    public void register(User u);
    public void unregister(User u);
    public void notifyFollowers(String s);
}
