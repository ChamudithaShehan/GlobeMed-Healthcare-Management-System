# GlobeMed Healthcare Management System

## 1. Introduction

The GlobeMed Healthcare Management System is a comprehensive JavaFX application designed to modernize and streamline the operations of a large healthcare organization. It replaces outdated, non-integrated systems with a single, efficient platform for managing patient records, scheduling appointments, handling billing, and coordinating care.

This project is built using Java with JavaFX for the user interface, following the Model-View-Controller (MVC) architecture. A key focus of the development was the application of Object-Oriented Design Patterns to ensure the system is scalable, maintainable, and robust.

## 2. Features

*   **Secure, Role-Based Login:** Authenticates users (Doctors, Nurses, Pharmacists) with different access levels.
*   **Patient Record Management:** Full CRUD (Create, Read, Update, Delete) functionality for patient records, with an "Undo" feature.
*   **Appointment Scheduling:** A centralized system for booking and canceling patient appointments with doctors.
*   **Hierarchical Staff Management:** A view to display the organizational structure of staff.
*   **Billing and Insurance Claims:** A flexible system to generate bills and process payments via different methods.
*   **Medical Report Generation:** Dynamically generate treatment and financial reports for patients.

## 3. Project Structure

The project follows the MVC design pattern to separate concerns:

*   `src/main/java/com/globemed/healthcare/`
    *   `Main.java`: The main entry point for the JavaFX application.
    *   `controller/`: Contains controller classes (e.g., `LoginController`, `PatientController`) that handle user input and update the view and model.
    *   `model/`: Contains the data classes (POJOs) of the application (e.g., `User`, `PatientRecord`, `Appointment`).
    *   `view/`: Contains FXML files that define the user interface layout.
    *   `patterns/`: Contains the implementations of all the required design patterns, organized into sub-packages (e.g., `memento`, `mediator`).
    *   `util/`: Contains utility classes, such as the in-memory `Database` manager.
*   `src/main/resources/com/globemed/healthcare/`
    *   `view/`: Contains FXML files and the `style.css` stylesheet.
    *   `data/`: Contains the JSON files (`users.json`, `patients.json`) that act as the in-memory database.

## 4. Design Patterns Used

This project heavily utilizes design patterns to solve common software design problems.

### Part A: Patient Record Management – Memento Pattern

*   **Purpose:** The Memento pattern is used to capture and restore an object's internal state without violating encapsulation. It's perfect for implementing undo/redo functionality.
*   **Implementation:**
    *   **Originator (`PatientRecord`):** The object whose state needs to be saved. It can create a memento containing a snapshot of its current state.
    *   **Memento (`PatientRecordMemento`):** An object that stores the state of the `PatientRecord`.
    *   **Caretaker (`PatientRecordCaretaker`):** Manages the history of mementos using a `Stack`. The `PatientController` uses the caretaker to save a state when a patient is selected and to undo changes when the "Undo" button is clicked.
*   **Benefits:** It allows for robust undo functionality without coupling the `PatientRecord` to the history management logic. The state is saved externally, keeping the model clean.

### Part B: Appointment Scheduling – Mediator Pattern

*   **Purpose:** The Mediator pattern provides a central object to manage communication between a set of other objects. This prevents objects from referring to each other directly, promoting loose coupling.
*   **Implementation:**
    *   **Mediator (`SchedulerMediator` interface):** Defines the contract for all scheduling operations.
    *   **Concrete Mediator (`ConcreteSchedulerMediator`):** Implements the scheduling logic. It coordinates between patients and doctors to book appointments, check for conflicts, and handle cancellations.
    *   **Colleagues:** The `AppointmentController` acts as a colleague, delegating all scheduling tasks to the mediator instead of trying to manage patients, doctors, and appointments itself.
*   **Benefits:** It centralizes complex scheduling logic, making it easier to manage and modify. The UI controller is simplified as it only needs to talk to the mediator.

### Part C: Billing and Insurance – Bridge Pattern

*   **Purpose:** The Bridge pattern decouples an abstraction from its implementation, so that the two can vary independently. It's useful when you have a class and what it "does" can be implemented in different ways.
*   **Implementation:**
    *   **Abstraction (`BillingSystem`):** An abstract class that defines high-level billing operations.
    *   **Implementor (`Billing` interface):** An interface that defines the low-level payment processing methods.
    *   **Refined Abstraction (`ConsultationBillingSystem`):** A specific type of bill.
    *   **Concrete Implementors (`DirectBilling`, `InsuranceBilling`):** Different ways a bill can be paid.
    *   The `BillingController` lets the user choose a payment method (an implementor) and then uses the `BillingSystem` (the abstraction) to process the bill, bridging the two.
*   **Benefits:** You can add new payment methods (e.g., `CryptoBilling`) or new bill types (e.g., `PharmacyBilling`) without changing the existing classes.

### Part D: Staff Roles and Permissions – Composite Pattern

*   **Purpose:** The Composite pattern composes objects into tree structures to represent part-whole hierarchies. It lets clients treat individual objects and compositions of objects uniformly.
*   **Implementation:**
    *   **Component (`StaffMember` interface):** A common interface for both individual staff and groups.
    *   **Leaf (`Doctor`, `Nurse`, `Pharmacist`):** Individual objects in the hierarchy.
    *   **Composite (`StaffGroup`):** A class that can contain a list of `StaffMember`s (which can be leaves or other composites).
    *   The `StaffController` builds a sample hierarchy and uses a `TreeView` to display it, demonstrating how a group and an individual can be treated the same way.
*   **Benefits:** It simplifies the code that works with complex hierarchical structures. You can add new types of staff or groups easily.

### Part E: Medical Reports – Visitor Pattern

*   **Purpose:** The Visitor pattern separates an algorithm from the objects on which it operates. This allows you to add new operations to a class structure without modifying the classes themselves.
*   **Implementation:**
    *   **Element (`Reportable` interface):** An interface with an `accept(visitor)` method, implemented by `PatientRecord`, `Appointment`, etc.
    *   **Visitor (`ReportVisitor` interface):** Defines a `visit()` method for each type of element.
    *   **Concrete Visitors (`TreatmentSummaryVisitor`, `FinancialReportVisitor`):** Each visitor implements a different reporting algorithm. For example, the `TreatmentSummaryVisitor` collects medical data, while the `FinancialReportVisitor` collects billing data.
*   **Benefits:** It makes it extremely easy to add new reports. To create a new report, you just create a new visitor class; you don't need to touch any of the existing model classes.

### Part F: Security – Compound Pattern (Decorator + Chain of Responsibility)

*   **Purpose:** This part uses two patterns together to create a robust and flexible authentication system.
*   **Implementation:**
    1.  **Chain of Responsibility:** An authentication request is passed along a chain of handlers.
        *   `UsernamePasswordHandler`: Checks if the credentials are correct.
        *   `RoleCheckHandler`: Checks if the user has a valid role.
    2.  **Decorator:** The core authentication service is wrapped with decorators to add extra functionality dynamically.
        *   `BasicAuthenticationService`: The base component that uses the chain.
        *   `LoggingDecorator`: Adds logging before and after the login attempt.
        *   `EncryptionDecorator`: Simulates adding an encryption layer.
*   **Benefits:** The chain makes the authentication process modular and easy to extend (e.g., add a two-factor auth handler). The decorator allows for adding security features like logging without modifying the core authentication logic.

## 5. How to Run the Application

You will need Java (JDK 11 or higher) and Apache Maven installed.

1.  Open a terminal or command prompt in the root directory of the project.
2.  Run the following Maven command:
    ```bash
    mvn clean javafx:run
    ```
3.  This will compile the project, download dependencies, and launch the application.

## 6. User Roles and Permissions

The system has three pre-defined user roles with different levels of access. You can log in with the following credentials (password for all is `pass123`):

*   **Username:** `doctor`
    *   **Access:** Full access to all modules (Patient Records, Appointments, Billing, Reports, Staff Management).
*   **Username:** `nurse`
    *   **Access:** Limited to Patient Records and Appointments.
*   **Username:** `pharmacist`
    *   **Access:** Limited to Billing & Claims.
