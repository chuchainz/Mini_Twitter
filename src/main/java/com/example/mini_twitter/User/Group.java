package com.example.mini_twitter.User;

import java.util.ArrayList;
import java.util.List;

public class Group implements Entity {
    private String uniqueID;
    private String name;
    private List<Entity> members;
    private List<Group> subgroups;
    private long creationTime;

    public Group(String uniqueID, String name) {
        this.uniqueID = uniqueID;
        this.name = name;
        this.members = new ArrayList<>();
        this.subgroups = new ArrayList<Group>();
        this.creationTime = System.currentTimeMillis();
    }

    @Override
    public String getUniqueID() {
        return uniqueID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return "Group";
    }

    @Override
    public String toString() {
        return name; // Customize the display of the Group object in the TreeView
    }

    // Add a member to the group (if not already in one)
    public void addMember(Entity entity) {
        User member = (User) entity;
        // check if member is already in a group
        if (member.getGroupID().isEmpty()) {
            members.add(member);
            member.setGroupID(this.uniqueID);
        } else {
            System.out.println(member + " is already in group " + member.getGroupID() + "!");
        }
    }

    // Add a subgroup to the root node
    public void addSubgroup(Group subgroup) { subgroups.add(subgroup); }

    // Remove a member from the group
    public void removeMember(Entity member) {
        members.remove(member);
    }

    // Get the list of members in the group
    public List<Entity> getMembers() {
        return members;
    }

    public List<Group> getSubgroups() {return subgroups; }

    // Check if the group contains a specific member
    public boolean containsMember(Entity member) {
        return members.contains(member);
    }

    public long getCreationTime() {return creationTime;}

}
