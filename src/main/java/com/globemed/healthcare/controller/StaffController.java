package com.globemed.healthcare.controller;

import com.globemed.healthcare.dao.UserDao;
import com.globemed.healthcare.patterns.composite.*;
import com.globemed.healthcare.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TreeCell;
import javafx.util.Callback;

import java.util.Comparator;

public class StaffController {

	@FXML
	private TreeView<StaffMember> staffTreeView;
	@FXML
	private TextArea detailsArea;
	@FXML
	private TextField empUsernameField;
	@FXML
	private PasswordField empPasswordField;
	@FXML
	private ChoiceBox<String> empRoleChoice;
	@FXML
	private Button addEmployeeButton;

	private final UserDao userDao = new UserDao();
	private StaffGroup rootGroup;

	@FXML
	public void initialize() {

		boolean isDoctor = Session.getCurrentUser() != null && "Doctor".equals(Session.getCurrentUser().getRole());
		if (empRoleChoice != null) {
			empRoleChoice.getItems().setAll("Doctor", "Nurse", "Pharmacist");
			empRoleChoice.getSelectionModel().select("Nurse");
		}
		if (addEmployeeButton != null) {
			addEmployeeButton.setDisable(!isDoctor);
		}
		if (empUsernameField != null) empUsernameField.setDisable(!isDoctor);
		if (empPasswordField != null) empPasswordField.setDisable(!isDoctor);
		if (empRoleChoice != null) empRoleChoice.setDisable(!isDoctor);


		rootGroup = buildDefaultOrg();


		TreeItem<StaffMember> rootItem = new TreeItem<>(rootGroup);
		rootItem.setExpanded(true);
		populateTreeViewSorted(rootGroup, rootItem);
		staffTreeView.setRoot(rootItem);


		staffTreeView.setCellFactory(new Callback<TreeView<StaffMember>, TreeCell<StaffMember>>() {
			@Override
			public TreeCell<StaffMember> call(TreeView<StaffMember> tv) {
				return new TreeCell<>() {
					@Override
					protected void updateItem(StaffMember item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
						} else {
							setText(item.getName() + " — " + item.getRole());
						}
					}
				};
			}
		});


		staffTreeView.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showDetails(newValue));
	}

	private StaffGroup buildDefaultOrg() {
		StaffGroup globeMed = new StaffGroup("GlobeMed Healthcare");

		StaffGroup cardiology = new StaffGroup("Cardiology Department");
		cardiology.addMember(new Doctor("Dr. Alice", "Cardiologist"));
		cardiology.addMember(new Doctor("Dr. Bob", "Cardiologist"));
		cardiology.addMember(new Nurse("Nurse Carol", "Cardiology"));

		StaffGroup pharmacy = new StaffGroup("Pharmacy Department");
		pharmacy.addMember(new Pharmacist("Pharmacist Dave"));
		pharmacy.addMember(new Pharmacist("Pharmacist Eve"));

		globeMed.addMember(cardiology);
		globeMed.addMember(pharmacy);
		globeMed.addMember(new Nurse("Nurse Frank", "General Ward"));
		return globeMed;
	}

	private void populateTreeViewSorted(StaffGroup group, TreeItem<StaffMember> parentItem) {
		Comparator<StaffMember> comparator = Comparator
				.comparing((StaffMember m) -> (m instanceof StaffGroup) ? 0 : 1)
				.thenComparing(StaffMember::getName, String.CASE_INSENSITIVE_ORDER);

		group.getMembers().stream()
				.sorted(comparator)
				.forEach(member -> {
					TreeItem<StaffMember> treeItem = new TreeItem<>(member);
					parentItem.getChildren().add(treeItem);
					if (member instanceof StaffGroup) {
						populateTreeViewSorted((StaffGroup) member, treeItem);
					}
				});
	}

	private void showDetails(TreeItem<StaffMember> treeItem) {
		if (treeItem != null) {
			StaffMember member = treeItem.getValue();
			String details = "Name: " + member.getName() + "\n";
			details += "Role: " + member.getRole() + "\n";
			detailsArea.setText(details);
		} else {
			detailsArea.clear();
		}
	}

	@FXML
	private void handleAddEmployee() {
		boolean isDoctor = Session.getCurrentUser() != null && "Doctor".equals(Session.getCurrentUser().getRole());
		if (!isDoctor) return;
		String username = empUsernameField.getText();
		String password = empPasswordField.getText();
		String role = empRoleChoice.getValue();
		if (username == null || username.isBlank() || password == null || password.isBlank() || role == null) return;
		

		userDao.createUser(username, password, role);
		

		StaffMember newMember;
		switch (role) {
			case "Doctor": 
				newMember = new Doctor("Dr. " + username, "General Practitioner"); 
				break;
			case "Pharmacist": 
				newMember = new Pharmacist("Pharmacist " + username); 
				break;
			default: 
				newMember = new Nurse("Nurse " + username, "General Ward"); 
				break;
		}
		

		StaffGroup targetDepartment = findOrCreateDepartment(role);
		targetDepartment.addMember(newMember);
		

		refreshTreeView();
		

		empUsernameField.clear();
		empPasswordField.clear();
		empRoleChoice.getSelectionModel().select("Nurse");
	}
	
	private StaffGroup findOrCreateDepartment(String role) {

		for (StaffMember member : rootGroup.getMembers()) {
			if (member instanceof StaffGroup) {
				StaffGroup group = (StaffGroup) member;
				if (role.equals("Doctor") && group.getName().contains("Cardiology")) {
					return group;
				} else if (role.equals("Pharmacist") && group.getName().contains("Pharmacy")) {
					return group;
				} else if (role.equals("Nurse") && group.getName().contains("General")) {
					return group;
				}
			}
		}
		

		return rootGroup;
	}
	
	private void refreshTreeView() {

		TreeItem<StaffMember> rootItem = new TreeItem<>(rootGroup);
		rootItem.setExpanded(true);
		populateTreeViewSorted(rootGroup, rootItem);
		staffTreeView.setRoot(rootItem);
	}
}
