package mini.dataframereader.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import mini.dataframereader.CodeBuilder;
import mini.dataframereader.Proccessing;
import mini.dataframereader.Settings;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.Destination;
import tech.tablesaw.io.csv.CsvWriter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainViewController {

    @FXML
    public BorderPane borderPane;

    @FXML
    public Text warningField;

    @FXML
    public TextField pathPrompt;

    @FXML
    public Button firstButton;

    @FXML
    public TableView tableView;

    @FXML
    public Button refreshButton;

    @FXML
    public Button chooseFileButton;

    @FXML
    public TextField skipRows;

    @FXML
    public TextField dfName;

    @FXML
    public TextArea codeText;

    @FXML
    public Button copyCode;

    @FXML
    public HBox namesHBox;

    @FXML
    public HBox typesHBox;

    @FXML
    public CheckBox headCheckBox;

    @FXML
    public TextField separatorTextField;

    @FXML
    public ScrollPane scrollPane;

    Proccessing proccessing;
    Settings settings;
    CodeBuilder codeBuilder;





    @FXML
    public void initialize(){
        settings = new Settings();
        proccessing = new Proccessing(settings);
        codeBuilder = new CodeBuilder(settings);
        //settings.getDfNameProperty().bind(dfName.textProperty());

        codeText.textProperty().bindBidirectional(codeBuilder.getCodeProperty());

    }

    @FXML
    public void copyCode(ActionEvent actionEvent){
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();

        refresh(actionEvent);
        content.putString(codeText.getText());
        content.putHtml(codeText.getText());
        clipboard.setContent(content);
    }

    @FXML
    public void loadFIleFromPath() {
        String path = pathPrompt.getText();
        if(isPathOk(path)) {
            loadDataframe(path);
        }
    }
    @FXML 
    public void clearTable(ActionEvent actionEvent) {
        tableView.getColumns().clear();
        tableView.getItems().clear();

        //Odłączenie okienka z tekstem od sterego code buildera
        codeText.textProperty().unbindBidirectional(codeBuilder.getCodeProperty());

        settings = new Settings();
        proccessing = new Proccessing(settings);

        codeBuilder = new CodeBuilder(settings);
        //Połączenie okienka z tekstem z nowym codeBuilderem
        // Teraz wszystko raczej działa całkiem nieźle
        codeText.textProperty().bindBidirectional(codeBuilder.getCodeProperty());

        chooseFileButton.setDisable(false);
        firstButton.setDisable(false);
        namesHBox.getChildren().clear();
        typesHBox.getChildren().clear();
        warningField.setText("");
    }

    // wyswietlanie ramki danych w tableView
    private void printDataframe(Table dataframe){
        tableView.getColumns().clear();
        tableView.getItems().clear();

        List<String> columns = dataframe.columnNames();
        ObservableList<ObservableList> data = FXCollections.observableArrayList();

        // wypelnianie listy rzedow
        for (int i = 0; i < dataframe.rowCount(); i++) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int j = 0; j < dataframe.columnCount(); j++) {
                String display;
                if(dataframe.get(i,j) == null)
                    display = "NA";
                else
                    display = dataframe.get(i,j).toString();
                row.add(display);

            }
            data.add(row);
        }

        // inicjalizowanie obiektow TableColumn (javafx)
        for (int i = 0; i < columns.size(); i++) {
            final int finalIdx = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns.get(i));

            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));

            column.prefWidthProperty().setValue(150);
            column.setMinWidth(150);
            column.setMaxWidth(150);
            tableView.getColumns().add(column);

        }
        // wsadzenie danych do obiektu tableView
        tableView.setItems(data);

    }

    public void chooseFlie(ActionEvent actionEvent) throws IOException {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)" , "*.csv");
        warningField.setText("Choosing file...");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = (Stage) borderPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        String path = "";
        try {
            path = file.getCanonicalPath();
        }catch (NullPointerException e){
        }
        System.out.println(path);
        if (isPathOk(path)) {
            loadDataframe(path);
            warningField.setFill(Paint.valueOf("#51d704"));
            warningField.setText("file loaded!");
        }
    }

    public void saveFile(ActionEvent actionEvent) {
        refresh(actionEvent);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV file","*.csv");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Save data");
        fileChooser.setInitialFileName("dataframe.csv");
        Stage stage = (Stage) borderPane.getScene().getWindow();

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            Table dataframe = proccessing.getDataframeForSave();
            saveTableToFile(dataframe, file);
        }
    }
    private void saveTableToFile(Table dataframe, File file) {
        try {
            CsvWriter csvWriter;
            csvWriter = new CsvWriter();
            Destination destination = new Destination(file);
            csvWriter.write(dataframe, destination);
            warningField.setFill(Paint.valueOf("#51d704"));
            warningField.setText("File saved!");
        } catch (IOException ex) {
            ex.getCause();
        }
    }

    public boolean isPathOk(String path){
        if (path == null){return false;}
        if (!path.endsWith(".csv")){
            warningField.setText("Wrong path to .csv");
            warningField.setFill(Paint.valueOf("#C70039"));
            return false;}
        return true;
    }

    // tymczasowa funkcja do wprowadzania zmian z kontrolek w zycie (aktualizacja ramki danych)
    public void refresh(ActionEvent actionEvent){
        if(settings.isSeparatorChanged()){
            namesHBox.getChildren().clear();
            typesHBox.getChildren().clear();
            settings.setSeparatorChanged(false);
            loadDataframe(settings.getFilepath());
        }
        else{
            proccessing.reloadDataframe();
            printDataframe(proccessing.getDataframe());
        }

        codeBuilder.generateCode();
        warningField.setText("Preview is updated.");
        warningField.setFill(Paint.valueOf("#51d704"));
    }


    void loadDataframe(String path){
        settings.setFilepath(path);
        proccessing.loadData();
        printDataframe(proccessing.getDataframe());
        chooseFileButton.setDisable(true);
        firstButton.setDisable(true);
        skipRows.textProperty().bindBidirectional(settings.skipRowsProperty(), new NumberStringConverter());
        skipRows.textProperty().addListener(notifyToRefreshString);
        headCheckBox.selectedProperty().bindBidirectional(settings.headProperty());
        headCheckBox.selectedProperty().addListener(notifyToRefreshBoolean);
        settings.getDfNameProperty().bind(dfName.textProperty());


        separatorTextField.textProperty().bindBidirectional(settings.separatorProperty());

        final String firstSeparator = settings.getSeparator();
        separatorTextField.textProperty().addListener(((observableValue, s, t1) -> {
            if(!t1.equals(firstSeparator)){
                settings.setSeparatorChanged(true);
                System.out.println("Zmieniono separator!");
            }
            else
                settings.setSeparatorChanged(false);
        }));

        separatorTextField.textProperty().addListener(notifyToRefreshString);

        for (int i = 0; i < settings.getColumnNames().size(); i++) {
            String colName = settings.getColumnNames().get(i).getValue();
            TextField textField = new TextField(colName);
            textField.setPrefWidth(150);
            textField.textProperty().bindBidirectional(settings.columnNamesProperty().get(i));
            textField.textProperty().addListener(notifyToRefreshString);
            textField.setMaxWidth(150);
            textField.setMinWidth(150);
            namesHBox.getChildren().add(textField);
        }


        for (int i = 0; i < settings.getColumnTypes().size(); i++) {

            ChoiceBox<String> choiceBox = new ChoiceBox<String>(
                    FXCollections.observableList(Arrays.asList("BOOLEAN", "FLOAT", "INTEGER",
                            "LOCAL_DATE_TIME", "LOCAL_DATE", "LOCAL_TIME", "SKIP", "STRING"))
            );
            choiceBox.setPrefWidth(150);

            choiceBox.valueProperty().bindBidirectional(settings.getColumnTypes().get(i));
            int finalI = i;
            choiceBox.valueProperty().addListener(((observableValue, s, t1) -> {
                if (t1.equals("SKIP"))
                    namesHBox.getChildren().get(finalI).disableProperty().setValue(true);
                else
                    namesHBox.getChildren().get(finalI).disableProperty().setValue(false);
            }));
            choiceBox.valueProperty().addListener(notifyToRefreshString);
            typesHBox.getChildren().add(choiceBox);
        }

        codeBuilder.addingListeners();

    }

    ChangeListener<String> notifyToRefreshString = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            warningField.setFill(Paint.valueOf("#FFC300"));
            warningField.setText("Need to refresh.");
        }
    };

    ChangeListener<Boolean> notifyToRefreshBoolean = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            warningField.setFill(Paint.valueOf("#FFC300"));
            warningField.setText("Need to refresh.");
        }
    };

}
