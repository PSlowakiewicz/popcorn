package mini.dataframereader;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CodeBuilder{
    Settings settings;
    StringProperty code;


    public CodeBuilder(Settings settings){
        this.settings = settings;
        this.code = new SimpleStringProperty();
        settings.filepathProperty().addListener(observable -> {generateCode();});
        settings.separatorProperty().addListener(observable -> {generateCode();});
        settings.skipRowsProperty().addListener(observable -> {generateCode();});
        settings.getDfNameProperty().addListener(observable -> {generateCode();});
        settings.separatorProperty().addListener(observable -> {generateCode();});
        settings.headProperty().addListener(observable -> {generateCode();});
    }

    public void generateCode(){
        String library = "import pandas \n";
        String name;
        String readFunction;
        String separator;
        String colNames = "\n\t, names = [ ";
        String colTypes = "\n\t, dtype = { ";
        String skipRows = "";
        String dropCol = "drop(";
        String daty = "\n\t, parse_dates = [";
        String header = "";

        if (settings.getFilepath() == null || settings.getFilepath().isEmpty()){
            code.set("Generated code...");
            return;
        } else {
            readFunction = " = pandas.read_csv( \"" + settings.getFilepath() + "\" \n\t";
        }

        if (settings.getDfName() == null || settings.getDfName().isEmpty()){
            name = "data_frame";
        } else {
            name = settings.getDfName();
        }

        separator = ", sep = '" + settings.getSeparator() + "'";

        if (settings.getSkipRows() != 0) {
            skipRows = ", skiprows = " + settings.getSkipRows();
        }

        if (!settings.isHead()){
            header = ", header = None";
        } else {
            header = ", header = 0";
        }

        if (settings.getColumnNames() != null) {
            for (int i = 0; i < settings.getColumnNames().size(); i++) {
                if (settings.getColumnTypes().get(i).getValue().equals("SKIP")) {
                    if (i == 0 || dropCol.equals("drop(")) {
                        dropCol = dropCol + " \"" + settings.getColumnNames().get(i).getValue() + "\"";
                    } else {
                        dropCol = dropCol + ", \"" + settings.getColumnNames().get(i).getValue() + "\"";
                    }
                } else if (settings.getColumnTypes().get(i).getValue().contains("LOCAL")) {
                    if (i == 0 || daty.equals("\n\t, parse_dates = [")) {
                        daty = daty + " \"" + settings.getColumnNames().get(i).getValue() + "\"";
                    } else {
                        daty = daty + ", \"" + settings.getColumnNames().get(i).getValue() + "\"";
                    }
                } else {
                    String type = settings.getColumnTypes().get(i).getValue();
                    if (type.equals("STRING")){
                        type = "str";
                    } else if (type.equals("INTEGER")){
                        type = "'Int64'";
                    } else if (type.equals("BOOLEAN")){
                        type = "bool";
                    } else {
                        type = "float";
                    }

                    if (i == 0 || colTypes.equals("\n\t, dtype = { ")) {
                        colTypes = colTypes + "\"" + settings.getColumnNames().get(i).getValue() + "\":" + type;
                    } else {
                        colTypes = colTypes + ", \"" + settings.getColumnNames().get(i).getValue() + "\":" + type;
                    }
                }
                if (i == 0){
                    colNames = colNames + (" \"") + (settings.getColumnNames().get(i).getValue()) + ("\"");
                } else {
                    colNames = colNames + (", \"") + (settings.getColumnNames().get(i).getValue()) + ("\"");
                }
            }
            colNames = colNames + ("]");
            colTypes = colTypes + ("}");
            daty = daty + ("]");
        } else {
            colNames = "";
            colTypes = "";
        }

        if (dropCol.equals("drop(")){
            dropCol = "";
        } else {
            dropCol = "\n" + name + "." + dropCol + ", axis = 1)";
        }

        if (daty.equals("\n\t, parse_dates = [") || daty.equals("\n\t, parse_dates = []")){
            daty = "";
        }

        String finalCode = library + name + readFunction + header + separator + skipRows + colNames + daty + colTypes + ")" + dropCol;
        code.set(finalCode);
    }

    public void addingListeners(){
        for (int i = 0; i < settings.getColumnNames().size(); i++) {
            settings.getColumnNames().get(i).addListener((observableValue, stringProperties, t1) -> { generateCode();});
            settings.getColumnTypes().get(i).addListener((observableValue, stringProperties, t1) -> { generateCode();});
        }
    }

    public StringProperty getCodeProperty() { return code; }

}
