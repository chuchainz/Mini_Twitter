package com.example.mini_twitter;

import com.example.mini_twitter.User.Entity;
import com.example.mini_twitter.User.PositiveWordCounter;
import com.example.mini_twitter.User.User;
import com.example.mini_twitter.User.Group;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class MiniTwitter extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private List<User> users = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();

    // Helper functions for the purpose of printing to terminal
    private static void printTree(Group group, int level) {
        // Print the current group
        System.out.println(indent(level) + group.getName());

        // Print users in the current group
        for (Entity user : group.getMembers()) {
            System.out.println(indent(level + 1) + user.getName());
        }

        // Recursively print subgroups
        for (Group subgroup : group.getSubgroups()) {
            printTree(subgroup, level + 1);
        }
    }

    // Helper function for printing to terminal to make it cohesive
    private static String indent(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("  "); // Adjust the number of spaces for indentation
        }
        return sb.toString();
    }

    @Override
    public void start(Stage primaryStage) {

        /* Testing observer pattern */
        User peter = new User("1", "Peter");
        User jimmy = new User("2", "Jimmy");

        User kyanh = new User("3", "Kyanh");
        User simon = new User("4", "Simon");
        User thien = new User("5", "Thien");

        users.add(peter);
        users.add(jimmy);
        users.add(kyanh);
        users.add(simon);
        users.add(thien);

        // jimbo and ke-on should see peter's tweets
        peter.register(jimmy);
        peter.register(kyanh);

        // samoan and theen should see jimmy's tweets
        jimmy.register(simon);
        jimmy.register(thien);

        peter.postTweet("(01/01/2020 01:00:00): Peter tweeted: bro if this don't work in the terminal ima die");
        jimmy.postTweet("(01/01/2020 01:00:00): Jimmy tweeted: ofc it didn't work ඞ M O G U S");

        // kyanh shouldn't see future tweets
        // peter.unregister(kyanh);

        peter.postTweet("(01/01/2020 01:00:00): Peter tweeted: hahahahah kyanh stupid");

        /* Testing Groups */

        // Creating root node
        Group root = new Group("R", "Root");

        // Add users and groups to the root and other groups
        Group groupA = new Group("1", "Group A");
        Group groupB = new Group("2", "Group B");
        Group groupC = new Group("3", "Group C");
        Group groupD = new Group("4", "Group D");

        // Take our users and add them into groups (probably to make it easier, create groups FIRST, then users SECOND)
        root.addSubgroup(groupA);
        root.addSubgroup(groupB);

        groupA.addMember(peter);
        groupA.addMember(jimmy);

        groupB.addMember(kyanh);
        groupB.addMember(simon);
        // Adding a subgroup in a subgroup
        groupB.addSubgroup(groupC);

        groupC.addMember(thien);

        root.addSubgroup(groupD);
        // shouldn't work cuz user's in a group already
        groupD.addMember(peter);

        // Retrieve and print information from the tree
        printTree(root, 0);
        /* Reference
        - Root
          - Group A
            - Peter
            - Jimmy
          - Group B
            - Kyanh
            - Simon
            - Group C
              - Thien
          - Group D
        */

        /* Terminal testing done building UI below */
        primaryStage.setTitle("Mini Twitter");
        primaryStage.getIcons().add(new Image("icon.png"));

        BorderPane mainLayout = new BorderPane();

        TreeItem<Entity> rootItem = new TreeItem<>(new Group("0", "Root"));

        for (User user : users) {
            TreeItem<Entity> userItem = new TreeItem<>(user);
            rootItem.getChildren().add(userItem);
        }

        TreeView<Entity> treeView = new TreeView<>(rootItem);
        treeView.setPrefWidth(200);
        //treeView.setPrefWidth(900);

        // Create a VBox for the buttons on the right
        VBox buttonBox = new VBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER);

        // User ID components
        TextArea userIdTextArea = new TextArea();
        userIdTextArea.setPromptText("User ID");
        userIdTextArea.setPrefWidth(150);
        userIdTextArea.setPrefHeight(10);
        Button addUserButton = new Button("Add User");
        addUserButton.setOnAction(event -> {
            String userID = userIdTextArea.getText();
            if (!userID.isEmpty()) {
                User newUser = User.getInstance(userID, userID);
                users.add(newUser);
                TreeItem<Entity> userItem = new TreeItem<>(newUser);
                rootItem.getChildren().add(userItem);
                userIdTextArea.clear();
            }
        });

        // Group ID components
        TextArea groupIdTextArea = new TextArea();
        groupIdTextArea.setPromptText("Group ID");
        groupIdTextArea.setPrefWidth(150);
        groupIdTextArea.setPrefHeight(10);
        Button addGroupButton = new Button("Add Group");
        addGroupButton.setOnAction(event -> {
            String groupID = groupIdTextArea.getText();
            if (!groupID.isEmpty()) {
                Group existingGroup = groups.stream().filter(group -> group.getUniqueID().equals(groupID)).findFirst().orElse(null);
                if (existingGroup != null) {
                    String userID = userIdTextArea.getText().trim();
                    if (!userID.isEmpty()) {
                        User existingUser = existingGroup.getMembers().stream()
                                .filter(member -> member instanceof User && member.getUniqueID().equals(userID))
                                .map(member -> (User) member)
                                .findFirst()
                                .orElse(null);

                        if (existingUser != null) {
                            System.out.println(existingUser + " is already in group " + existingGroup.getUniqueID() + "!");
                        } else {
                            User newUser = new User(userID, userID);
                            existingGroup.addMember(newUser);
                            TreeItem<Entity> userItem = new TreeItem<>(newUser);
                            TreeItem<Entity> groupItem = rootItem.getChildren().stream()
                                    .filter(item -> item.getValue() instanceof Group && item.getValue().getUniqueID().equals(groupID))
                                    .findFirst()
                                    .orElse(null);

                            if (groupItem != null) {
                                groupItem.getChildren().add(userItem);
                                groupItem.setExpanded(true);
                            }
                        }
                    }
                } else {
                    Group newGroup = new Group(groupID, groupID);
                    groups.add(newGroup);
                    TreeItem<Entity> groupItem = new TreeItem<>(newGroup);
                    root.addSubgroup(newGroup);
                    String[] userIds = userIdTextArea.getText().split(",");
                    for (String userID : userIds) {
                        userID = userID.trim();
                        if (!userID.isEmpty()) {
                            User newUser = new User(userID, userID);
                            newGroup.addMember(newUser);
                            TreeItem<Entity> userItem = new TreeItem<>(newUser);
                            groupItem.getChildren().add(userItem);
                        }
                    }
                    rootItem.getChildren().add(groupItem);
                    groupIdTextArea.clear();
                    userIdTextArea.clear();
                    groupItem.setExpanded(true);
                }
            }
        });

        Button openUserViewButton = new Button("Open User View");
        openUserViewButton.setOnAction(event -> {
            TreeItem<Entity> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedTreeItem != null && selectedTreeItem.getValue() instanceof User) {
                User selectedUser = (User) selectedTreeItem.getValue();
                OpenUserViewWindow(selectedUser);
            }
        });

        Button userTotal = new Button("Show User Total");
        userTotal.setOnAction(event -> {
            int userCount = rootItem.getChildren().size();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User Total");
            alert.setHeaderText(null);
            alert.setContentText("User Total: " + userCount);
            alert.showAndWait();
        });

        Button groupTotal = new Button("Show Group Total");
        groupTotal.setOnAction(event -> {
            int totalGroups = countGroups(rootItem);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Group Total");
            alert.setHeaderText(null);
            alert.setContentText("Group Total: " + totalGroups);
            alert.showAndWait();
        });

        Button messageTotal = new Button("Show Messages Total");
        messageTotal.setOnAction(event -> {
            int totalUniqueTweets = getTotalUniqueTweets();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Total Unique Messages");
            alert.setHeaderText(null);
            alert.setContentText("Total Unique Messages: " + totalUniqueTweets);
            alert.showAndWait();
        });

        Button positivePercentage = new Button("Show Positive Percentage");
        positivePercentage.setOnAction(event -> {
            TreeItem<Entity> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedTreeItem != null && selectedTreeItem.getValue() instanceof User) {
                User selectedUser = (User) selectedTreeItem.getValue();
                double positivePercentageValue = PositiveWordCounter.calculatePositivePercentage(selectedUser);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Positive Percentage");
                alert.setHeaderText(null);
                alert.setContentText("Positive Percentage: " + positivePercentageValue + "%");
                alert.showAndWait();
            }
        });

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setPadding(new Insets(10));
        inputGrid.add(new Label("User ID:"), 0, 0);
        inputGrid.add(userIdTextArea, 1, 0);
        inputGrid.add(addUserButton, 2, 0);
        inputGrid.add(new Label("Group ID:"), 0, 1);
        inputGrid.add(groupIdTextArea, 1, 1);
        inputGrid.add(addGroupButton, 2, 1);

        HBox hbox1 = new HBox(openUserViewButton);
        HBox hbox2 = new HBox(userTotal, groupTotal);
        HBox hbox3 = new HBox(messageTotal, positivePercentage);

        hbox1.setAlignment(Pos.CENTER);
        hbox2.setAlignment(Pos.CENTER);
        hbox3.setAlignment(Pos.CENTER);

        // padding for the HBoxes (both vertical and horizontal)
        hbox1.setPadding(new Insets(10, 0, 10, 0));
        hbox2.setPadding(new Insets(10, 0, 10, 0));
        hbox3.setPadding(new Insets(10, 0, 10, 0));
        hbox1.setSpacing(20);
        hbox2.setSpacing(20);
        hbox3.setSpacing(20);

        inputGrid.add(hbox1, 1, 4);
        inputGrid.add(hbox2, 1, 5);
        inputGrid.add(hbox3, 1, 6);

        mainLayout.setLeft(treeView);
        mainLayout.setCenter(inputGrid);
        //mainLayout.setRight(buttonBox);

        Scene scene = new Scene(mainLayout, 700, 400);
        scene.getStylesheets().add("style.css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void OpenUserViewWindow(User selectedUser) {
        OpenUserViewWindow userViewStage = new OpenUserViewWindow(selectedUser, users);
        userViewStage.show();
    }

    private int getTotalUniqueTweets() {
        Set<String> uniqueTweets = new HashSet<>();
        for (User user : users) {
            List<String> userTweets = user.getNewsFeed();
            for (String tweet : userTweets) {
                uniqueTweets.add(tweet);
            }
        }
        return uniqueTweets.size();
    }
    private int countGroups(TreeItem<Entity> rootItem) {
        int count = 0;
        for (TreeItem<Entity> child : rootItem.getChildren()) {
            if (child.getValue() instanceof Group) {
                count++;
                count += countGroups(child); // Recursively count subgroups
            }
        }
        return count;
    }
}