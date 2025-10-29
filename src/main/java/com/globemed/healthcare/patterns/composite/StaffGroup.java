package com.globemed.healthcare.patterns.composite;

import java.util.ArrayList;
import java.util.List;

public class StaffGroup implements StaffMember {
    private String groupName;
    private List<StaffMember> members = new ArrayList<>();

    public StaffGroup(String groupName) {
        this.groupName = groupName;
    }

    public void addMember(StaffMember member) {
        members.add(member);
    }

    public void removeMember(StaffMember member) {
        members.remove(member);
    }

    public List<StaffMember> getMembers() {
        return members;
    }

    @Override
    public void showDetails() {
        System.out.println("Group: " + groupName);
        for (StaffMember member : members) {
            member.showDetails();
        }
    }

    @Override
    public String getName() {
        return groupName;
    }

    @Override
    public String getRole() {
        return "Group";
    }
}
