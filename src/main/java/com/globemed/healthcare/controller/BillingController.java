package com.globemed.healthcare.controller;

import com.globemed.healthcare.model.BillItem;
import com.globemed.healthcare.model.Invoice;
import com.globemed.healthcare.model.PatientRecord;
import com.globemed.healthcare.patterns.bridge.*;
import com.globemed.healthcare.dao.PatientDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.binding.Bindings;

import java.util.UUID;

public class BillingController {


	@FXML private ComboBox<PatientRecord> patientComboBox;
	@FXML private Button newBillButton;
	@FXML private TableView<BillItem> billItemsTable;
	@FXML private TableColumn<BillItem, String> descriptionColumn;
	@FXML private TableColumn<BillItem, Double> costColumn;
	@FXML private HBox addItemPane;
	@FXML private TextField itemDescriptionField;
	@FXML private TextField itemCostField;
	@FXML private Button addItemButton;
	@FXML private Label totalAmountLabel;
	@FXML private ComboBox<String> paymentMethodComboBox;
	@FXML private Button processBillButton;
	@FXML private TextArea logArea;

	private final ObjectProperty<Invoice> currentInvoice = new SimpleObjectProperty<>(null);
	private final ObservableList<BillItem> billItems = FXCollections.observableArrayList();
	private final PatientDao patientDao = new PatientDao();

	@FXML
	public void initialize() {

		patientComboBox.setItems(FXCollections.observableArrayList(patientDao.listAll()));
		patientComboBox.setConverter(new StringConverter<PatientRecord>() {
			@Override public String toString(PatientRecord patient) { return patient == null ? "" : patient.getFirstName() + " " + patient.getLastName() + " (" + patient.getPatientId() + ")"; }
			@Override public PatientRecord fromString(String string) { return null; }
		});

		paymentMethodComboBox.setItems(FXCollections.observableArrayList("Direct Billing", "Insurance Billing"));

		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
		billItemsTable.setItems(billItems);
		billItemsTable.setPlaceholder(new Label("No items in this bill yet. Click 'Start New Bill' and add items."));
		billItemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


		addItemPane.disableProperty().bind(currentInvoice.isNull());
		processBillButton.disableProperty().bind(currentInvoice.isNull().or(Bindings.isEmpty(billItems)));


		updateTotal();
	}

	@FXML
	private void handleNewBill() {
		PatientRecord selectedPatient = patientComboBox.getValue();
		if (selectedPatient == null) {
			log("Please select a patient first.");
			return;
		}
		currentInvoice.set(new Invoice("INV-" + UUID.randomUUID().toString().substring(0, 4), selectedPatient));
		billItems.clear();
		updateTotal();
		log("Started new bill " + currentInvoice.get().getInvoiceId() + " for " + selectedPatient.getFirstName());
	}

	@FXML
	private void handleAddItem() {
		String description = itemDescriptionField.getText();
		String costStr = itemCostField.getText();
		if (description.isEmpty() || costStr.isEmpty()) {
			log("Item description and cost cannot be empty.");
			return;
		}
		try {
			double cost = Double.parseDouble(costStr);
			BillItem item = new BillItem(description, cost);
			Invoice invoice = currentInvoice.get();
			if (invoice == null) {
				log("Please start a new bill first.");
				return;
			}
			invoice.addItem(item);
			billItems.add(item);
			updateTotal();
			itemDescriptionField.clear();
			itemCostField.clear();
		} catch (NumberFormatException e) {
			log("Invalid cost format. Please enter a number.");
		}
	}

	@FXML
	private void handleProcessBill() {
		Invoice invoice = currentInvoice.get();
		if (invoice == null || invoice.getItems().isEmpty()) {
			log("Cannot process an empty bill.");
			return;
		}
		String paymentMethod = paymentMethodComboBox.getValue();
		if (paymentMethod == null) {
			log("Please select a payment method.");
			return;
		}


		Billing billingImplementor;
		if (paymentMethod.equals("Direct Billing")) {
			billingImplementor = new DirectBilling();
		} else {
			billingImplementor = new InsuranceBilling();
		}


		BillingSystem billingSystem = new ConsultationBillingSystem(billingImplementor);

		String result = billingSystem.generateAndProcessBill(invoice);
		log(result);

		currentInvoice.set(null);
		billItems.clear();
		updateTotal();
	}

	private void updateTotal() {
		Invoice invoice = currentInvoice.get();
		double total = invoice == null ? 0.0 : invoice.getTotalAmount();
		totalAmountLabel.setText(String.format("$%.2f", total));
	}

	private void log(String message) {
		logArea.appendText(message + "\n");
	}
}
