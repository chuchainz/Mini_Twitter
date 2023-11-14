package com.example.mini_twitter.User;

/**
 * This interface is called whenever the subject changes (a post has been made)
 */
public interface Observer {
    public void update(String name, String s);
}
