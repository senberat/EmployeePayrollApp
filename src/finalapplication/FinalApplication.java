/*
 * FinalApplication.java
 * Employee Payroll Application
 * Berat Sen
 * Date: 7 Apr 2019
 * Description: This is the GUI for the application which includes two scenes
 */
package finalapplication;

import java.io.IOException;
import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import proj24178.utils.*;

/**
 *
 * @author Berat
 */
public class FinalApplication extends Application {

    TableView<Employee> table;
    private TextField txtName, txtPosition, txtSalary;
    private Button btnAdd, btnDelete, btnUpdate, btnSearch;
    ObservableList<Employee> employees = FXCollections.observableArrayList();
    ObservableList<Employee> search = FXCollections.observableArrayList();
    private Employee rowData;
    private Button btnTotal, btnReturn;
    public Stage stage;
    private String reName, rePos, reSal;
    private String nameReturn, posReturn, salaryReturn;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Scene scene = mainScene();
        stage = primaryStage;
        EmployeeList list = new EmployeeList();
        employees.addAll(list.readFile());
        primaryStage.setTitle("Payroll Application");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public Scene mainScene() throws IOException {

        BorderPane root = new BorderPane();

        EmployeeList list = new EmployeeList();
       

        TableColumn<Employee, Integer> idCol = new TableColumn<>("ID");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Employee, String> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Employee, String> positionCol
            = new TableColumn<>("position");
        positionCol.setMinWidth(200);
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));

        TableColumn<Employee, String> salaryCol = new TableColumn<>("Salary");
        salaryCol.setMinWidth(100);
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));

        TableColumn<Employee, Double> taxCol = new TableColumn<>("Tax");
        taxCol.setMinWidth(100);
        taxCol.setCellValueFactory(new PropertyValueFactory<>("tax"));

        TableColumn<Employee, Double> netPayCol = new TableColumn<>("Net pay");
        netPayCol.setMinWidth(100);
        netPayCol.setCellValueFactory(new PropertyValueFactory<>("netpay"));

        table = new TableView<>();

        table.setItems(list.readFile());

        table.setRowFactory(e -> {
            TableRow<Employee> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                rowData = row.getItem();
                txtName.setText(rowData.getName());
                txtPosition.setText(rowData.getPosition());
                txtSalary.setText(String.valueOf(rowData.getSalary()));
            });
            return row;
        });

        table.getColumns().addAll(idCol, nameCol, positionCol, salaryCol,
            taxCol, netPayCol);

        table.getStyleClass().add("table-custom-style");

        VBox input = new VBox(25);

        txtName = new TextField();
        txtName.setPromptText("Name");

        txtPosition = new TextField();
        txtPosition.setPromptText("Position");

        txtSalary = new TextField();
        txtSalary.setPromptText("Salary");

        HBox buttons = new HBox(10);
        btnAdd = new Button("_Add");
        btnDelete = new Button("_Delete");
        btnUpdate = new Button("_Update");
        btnSearch = new Button("_Search");

        btnTotal = new Button("_Total Tax");

        buttons.getChildren().addAll(btnAdd, btnDelete, btnUpdate, btnSearch);

        input.getChildren().addAll(txtName, txtPosition, txtSalary, buttons,
            btnTotal);
        btnTotal.setMaxWidth(300);

        btnAdd.setOnAction(e -> infoAction());
        btnDelete.setOnAction(e -> deleteAction());
        btnUpdate.setOnAction(e -> editAction());
        btnSearch.setOnAction(e -> searchAction());

        btnTotal.setOnAction(e -> totalTax());

        root.setRight(table);
        root.setLeft(input);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("finalcss.css");

        return scene;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void infoAction() {
        Employee em = new Employee();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation dialog");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to add the record?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Validator val1 = new Validator(txtSalary.getText());
            if (txtName.getText().trim().equals("")) {
                txtName.getStyleClass().add("error");
                Alert showName = new Alert(Alert.AlertType.ERROR);
                showName.setTitle("ERROR");
                showName.setHeaderText(null);
                showName.setContentText("Name field cannot be empty");
                showName.showAndWait();
            } else if (txtPosition.getText().trim().equals("")) {
                txtPosition.getStyleClass().add("error");
                Alert showPos = new Alert(Alert.AlertType.ERROR);
                showPos.setTitle("ERROR");
                showPos.setHeaderText(null);
                showPos.setContentText("Position field cannot be empty");
                showPos.showAndWait();
            } else if (!val1.isValidDouble()) {
                txtSalary.getStyleClass().add("error");
                Alert showError = new Alert(Alert.AlertType.ERROR);
                showError.setTitle("ERROR");
                showError.setHeaderText(null);
                showError.setContentText("Salary must be a number");
                showError.showAndWait();
            } else {
                txtName.getStyleClass().remove("error");
                txtPosition.getStyleClass().remove("error");
                txtSalary.getStyleClass().remove("error");
                String name = txtName.getText();
                String pos = txtPosition.getText();
                double salary = Double.parseDouble(txtSalary.getText());
                employees.add(new Employee(name, pos, salary,
                    em.getTax(), em.getNetpay()));

                EmployeeList list = new EmployeeList();
                try {
                    list.writeEmployees(new Employee(name, pos, salary,
                        em.getTax(), em.getNetpay()));
                } catch (IOException ex) {
                    Alert showFile = new Alert(Alert.AlertType.ERROR);
                    showFile.setTitle("ERROR");
                    showFile.setHeaderText(null);
                    showFile.setContentText("File does not exist");
                    showFile.showAndWait();
                }
                try {
                    table.setItems(list.readFile());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            txtName.clear();
            txtPosition.clear();
            txtSalary.clear();
        }

    }

    private void deleteAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation dialog");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to delete the record?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            ObservableList<Employee> emSelected, allEm;
            allEm = table.getItems();
            emSelected = table.getSelectionModel().getSelectedItems();

            emSelected.forEach(allEm::remove);

            for (int i = 0; i < employees.size(); i++) {

                if (employees.get(i).getName().equals(rowData.getName())
                    && employees.get(i).getPosition().
                        equals(rowData.getPosition())) {
                    employees.remove(i);

                }
            }

            EmployeeList list = new EmployeeList();

            try {
                list.resetFile();
            } catch (IOException ex) {

            }

            for (int i = 0; i < employees.size(); i++) {
                try {
                    list.deleteFromList(employees.get(i));
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            txtName.clear();
            txtPosition.clear();
            txtSalary.clear();
        }
    }

    public void editAction() {
        Validator val = new Validator(txtSalary.getText());
        Employee em = new Employee();
        EmployeeList list = new EmployeeList();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation dialog");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to edit the record?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            if (val.isValidDouble()) {
                for (int i = 0; i < employees.size(); i++) {

                    if (employees.get(i).getName().equals(rowData.getName())
                        && employees.get(i).getPosition().
                            equals(rowData.getPosition())) {

                        employees.set(i, new Employee(txtName.getText(),
                            txtPosition.getText(),
                            Double.parseDouble(txtSalary.getText()),
                            em.getTax(),
                            em.getSalary()));

                    }

                }
            } else {
                txtSalary.getStyleClass().add("error");
                Alert showError = new Alert(Alert.AlertType.ERROR);
                showError.setTitle("ERROR");
                showError.setHeaderText(null);
                showError.setContentText("Salary must be a number");
                showError.showAndWait();
            }
            try {
                list.resetFile();
            } catch (IOException ex) {

            }

            for (int i = 0; i < employees.size(); i++) {
                try {
                    list.deleteFromList(employees.get(i));
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            try {
                table.setItems(list.readFile());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            
            txtName.clear();
            txtPosition.clear();
            txtSalary.clear();
        }
    }

    public void searchAction() {
        TextInputDialog input = new TextInputDialog();
        input.setTitle("Search position");
        input.setHeaderText(null);
        input.setContentText("Enter a position: ");

        Optional<String> result = input.showAndWait();

        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getPosition().trim().toLowerCase()
                .equals(result.get().trim().toLowerCase())) {
                search.add(employees.get(i));
            }
            
        }

        if (!search.isEmpty()) {
            try {
                stage.setScene(search(search));
            } catch (IOException ex) {
            }
        } else {
            Alert showError = new Alert(Alert.AlertType.ERROR);
            showError.setTitle("ERROR");
            showError.setHeaderText(null);
            showError.setContentText("No suitable records found");
            showError.showAndWait();
        }
        
       

    }

    public void reName(String name) {
        reName = name;
    }

    public void rePos(String pos) {
        rePos = pos;
    }

    public void reSal(String sal) {
        reSal = sal;
    }

    public void returning() {
        txtName.setText(reName);
        txtPosition.setText(rePos);
        txtSalary.setText(reSal);
        rowData.setName(reName);
        rowData.setPosition(rePos);
        rowData.setSalary(Double.parseDouble(reSal));
        for (int i = 0; i < search.size(); i++) {
            if (!search.isEmpty()) {
                search.removeAll(search);
                search.clear();
            }
        }
        
    }

    public void totalTax() {

        double x = 0;
        for (int i = 0; i < employees.size(); i++) {
            x += employees.get(i).getTax();
        }

        Alert showTax = new Alert(Alert.AlertType.INFORMATION);
        showTax.setTitle("Total Tax");
        showTax.setHeaderText(null);
        showTax.setContentText("Total tax to be paid: " + x);
        showTax.showAndWait();

    }

    public Scene search(ObservableList<Employee> returnList)
        throws IOException {
        BorderPane root = new BorderPane();

        

        TableColumn<Employee, Integer> idCol = new TableColumn<>("ID");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Employee, String> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Employee, String> positionCol
            = new TableColumn<>("position");
        positionCol.setMinWidth(200);
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));

        TableColumn<Employee, String> salaryCol = new TableColumn<>("Salary");
        salaryCol.setMinWidth(100);
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));

        TableColumn<Employee, Double> taxCol = new TableColumn<>("Tax");
        taxCol.setMinWidth(100);
        taxCol.setCellValueFactory(new PropertyValueFactory<>("tax"));

        TableColumn<Employee, Double> netPayCol = new TableColumn<>("Net pay");
        netPayCol.setMinWidth(100);
        netPayCol.setCellValueFactory(new PropertyValueFactory<>("netpay"));

        table = new TableView<>();

        table.setItems(returnList);

        table.setRowFactory(e -> {
            TableRow<Employee> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                rowData = row.getItem();
                nameReturn = rowData.getName();
                posReturn = rowData.getPosition();
                salaryReturn = String.valueOf(rowData.getSalary());
            });
            return row;
        });

        table.getColumns().addAll(idCol, nameCol, positionCol, salaryCol,
            taxCol, netPayCol);

        table.getStyleClass().add("table-custom-style");

        btnReturn = new Button("_Return selected");
        btnReturn.setOnAction(e -> returnAction());

        btnReturn.setMaxWidth(300);

        
        root.setTop(table);
        root.setBottom(btnReturn);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("finalcss.css");

       
        return scene;
    }

    public void returnAction() {
        try {
            reName(nameReturn);
            rePos(posReturn);
            reSal(salaryReturn);
            try {
                stage.setScene(mainScene());
            } catch (IOException ex) {

            }
            returning();

        } catch (NullPointerException e) {

        }

    }

}
