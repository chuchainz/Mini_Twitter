package com.example.mini_twitter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.example.mini_twitter.User.User;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// Import statements...

public class OpenUserViewWindow extends Stage {
    private User selectedUser;
    private List<User> users;

    private ObservableList<String> observableTweets;
    private ListView<String> tweetListView;

    public OpenUserViewWindow(User selectedUser, List<User> users) {
        this.selectedUser = selectedUser;
        this.users = users;

        getIcons().add(new Image("icon.png"));
        setTitle("User View");

        VBox userViewLayout = new VBox(10);
        HBox hbox = new HBox(10);

        Label userNameLabel = new Label("User: " + selectedUser.getName());
        userViewLayout.getChildren().add(userNameLabel);

        Button followButton = new Button("Follow");
        TextField followInput = new TextField();
        followInput.setPromptText("Name");

        TextField tweetTextField = new TextField();
        tweetTextField.setPromptText("Enter your tweet");

        Button tweetButton = new Button("Tweet");
        tweetListView = new ListView<>();

        observableTweets = FXCollections.observableArrayList(selectedUser.getNewsFeed());
        tweetListView.setItems(observableTweets);

        followButton.setOnAction(event -> {
            String followerName = followInput.getText();
            if (!followerName.isEmpty()) {
                User follower = findUserByName(followerName);
                if (follower != null) {
                    follower.register(selectedUser);
                    followInput.clear();
                } else {
                    System.out.println("User not found: " + followerName);
                }
            }
        });

        tweetButton.setOnAction(event -> {
            String tweetText = tweetTextField.getText();
            if (!tweetText.isEmpty()) {
                String formattedDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date());
                String tweetWithDate = "(" + formattedDate + "): " + selectedUser.getName() + " tweeted: " + tweetText;
                selectedUser.postTweet(tweetWithDate);

                Platform.runLater(() -> {
                    observableTweets.add(tweetWithDate);
                    tweetTextField.clear();
                });
            }
        });

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> updateTweetListView());

        hbox.getChildren().addAll(followButton, followInput);
        userViewLayout.getChildren().addAll(hbox, tweetListView, tweetTextField, tweetButton, refreshButton);

        Scene userViewScene = new Scene(userViewLayout, 400, 300);
        setScene(userViewScene);
        userViewScene.getStylesheets().add("style.css");
    }

    private void updateTweetListView() {
        observableTweets.setAll(selectedUser.getNewsFeed());
    }

    private User findUserByName(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }
}

