package mini.dataframereader;

import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import tech.tablesaw.io.AddCellToColumnException;
import tech.tablesaw.io.csv.CsvReadOptions;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Proccessing {
    Table dataframe;
    Settings settings;
    int rowLimit;

    ObservableList<ObservableList> tableforView;


    public Proccessing(Settings settings){
        this.settings = settings;
        this.rowLimit = 100;
    }
    public Proccessing(Settings settings, int rowLimit){
        this.settings = settings;
        this.rowLimit = rowLimit;
    }

    public void loadData(){
        CsvReadOptions.Builder builder = CsvReadOptions.builder(settings.getFilepath())
                .separator(settings.getSeparator().charAt(0));

        CsvReadOptions options = builder.build();

        try {
            dataframe = Table.read().csv(options).first(rowLimit);
        } catch (IOException e) {
            e.printStackTrace();
        }


        ObservableList<StringProperty> colNamesList = FXCollections.observableArrayList();
        for (String colName : dataframe.columnNames()){
            colNamesList.add(new SimpleStringProperty(colName));
        }

        // odczytywanie nazw kolummn i nastawienie ich w settings
        settings.columnNames.setValue(colNamesList);

        List<ColumnType> typesList = Arrays.asList(dataframe.columnTypes());
        List<StringProperty> stringPropertyTypeList = new ArrayList<>();
        for (var item : typesList){
            stringPropertyTypeList.add(new SimpleStringProperty(item.toString()));
        }
        settings.columnTypes.setValue(FXCollections.observableList(stringPropertyTypeList));

    }

    public Table getDataframe() {
        return dataframe;
    }

    // odzielna metoda zwracająca tabele do zapisu, rozni się tym, że kolumny SKIP rzeczywiśćie są pominięte
    public Table getDataframeForSave(){
        CsvReadOptions.Builder builder = CsvReadOptions.builder(settings.getFilepath())
                .separator(settings.getSeparator().charAt(0))
                .columnTypes(oListToColumnTypeArray(settings.columnTypes, true));

        CsvReadOptions options = builder.build();

        // nazwa dataframe_ aby odroznic od pola w klasie
        Table dataframe_;
        try {
            dataframe_ = Table.read().csv(options);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }

        if(dataframe_.rowCount()-settings.skipRows.get() < 0){
            settings.skipRows.setValue(0);
        }
        dataframe_ = dataframe_.last(dataframe_.rowCount()-settings.skipRows.get());

        Integer offset = 0;
        for (int i = 0; i < settings.columnNamesProperty().size(); i++) {
            if(settings.columnTypesProperty().get(i).getValue().equals("SKIP")){
                offset++;
                continue;
            }
            Column column = dataframe_.column(i-offset);

            column.setName(settings.getColumnNames().get(i).getValue());
        }
        
        System.out.println("Zatktualizowano tabele do eksportu");

        return dataframe_;
    }

    // metoda odświerzająca kolumnę z nowymi ustawieniami settings
    public void reloadDataframe(){
        CsvReadOptions.Builder builder = CsvReadOptions.builder(settings.getFilepath())
                .separator(settings.getSeparator().charAt(0))
                .columnTypes(oListToColumnTypeArray(settings.columnTypes, false))
                .header(settings.headProperty().get());

        CsvReadOptions options = builder.build();

        try {
            dataframe = Table.read().csv(options).first(rowLimit);
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (AddCellToColumnException e){
            System.out.println("Blad konwersji, zamiana wszystkich kolumn na String.");
            for (int i = 0; i < settings.columnTypes.size(); i++) {
                settings.columnTypesProperty().get(i).set("STRING");
            }

            builder = CsvReadOptions.builder(settings.getFilepath())
                    .separator(settings.getSeparator().charAt(0))
                    .columnTypes(oListToColumnTypeArray(settings.columnTypes, false))
                    .header(settings.headProperty().get());

            options = builder.build();

            try {
                dataframe = Table.read().csv(options).first(rowLimit);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // oznaczanie kolumny jako odrzuconej
        for (int i = 0; i < settings.columnTypes.size(); i++) {
            if (settings.columnTypes.get(i).getValue().equals("SKIP")){
                StringColumn colToSkip = (StringColumn) dataframe.columns().get(i);

                for (int j = 0; j < colToSkip.size(); j++) {
                    colToSkip.set(j, "---");
                }
            }
        }

        if(dataframe.rowCount()-settings.skipRows.get() < 0){
            settings.skipRows.setValue(0);
        }
        dataframe = dataframe.last(dataframe.rowCount()-settings.skipRows.get());

        for (int i = 0; i < dataframe.columnCount(); i++) {
            Column column = dataframe.column(i);
            column.setName(settings.getColumnNames().get(i).getValue());
        }
        System.out.println("Zatktualizowano tabele");
    }


    ColumnType[] oListToColumnTypeArray(ObservableList<StringProperty> observableList, Boolean savingFile){
        int length = observableList.size();
        ColumnType[] columnTypesArray = new ColumnType[length];
        for (int i = 0; i < length; i++) {
            if (observableList.get(i).getValue().equals("SKIP") && !savingFile){
                columnTypesArray[i] = ColumnType.valueOf("STRING");
            }
            else
                columnTypesArray[i] = ColumnType.valueOf(observableList.get(i).getValue());
        }
        return columnTypesArray;
    }

}
