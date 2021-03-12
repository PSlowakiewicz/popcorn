package mini.dataframereader;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

public class Settings {
    StringProperty filepath;
    StringProperty dfName;
    StringProperty separator;
    ListProperty<StringProperty> columnNames;
    IntegerProperty skipRows;
    ListProperty<StringProperty> columnTypes;
    BooleanProperty head;
    BooleanProperty separatorChanged;


    public Settings(){
        this.filepath = new SimpleStringProperty();
        this.separator = new SimpleStringProperty(",");
        this.columnNames = new SimpleListProperty<StringProperty>();
        this.skipRows = new SimpleIntegerProperty();
        this.columnTypes = new SimpleListProperty<StringProperty>();
        this.dfName = new SimpleStringProperty();
        this.head = new SimpleBooleanProperty(true);
        this.separatorChanged = new SimpleBooleanProperty(false);
    }

    public boolean isSeparatorChanged() {
        return separatorChanged.get();
    }

    public BooleanProperty separatorChangedProperty() {
        return separatorChanged;
    }

    public void setSeparatorChanged(boolean separatorChanged) {
        this.separatorChanged.set(separatorChanged);
    }

    public boolean isHead() {
        return head.get();
    }

    public BooleanProperty headProperty() {
        return head;
    }

    public void setHead(boolean head) {
        this.head.set(head);
    }

    public String getFilepath() {
        return filepath.get();
    }

    public StringProperty filepathProperty() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath.set(filepath);
    }

    public String getSeparator() {
        return separator.get();
    }

    public StringProperty separatorProperty() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator.set(separator);
    }

    public ObservableList<StringProperty> getColumnNames() {
        return columnNames.get();
    }

    public ListProperty<StringProperty> columnNamesProperty() {
        return columnNames;
    }

    public void setColumnNames(ObservableList<StringProperty> columnNames) {
        this.columnNames.set(columnNames);
    }

    public int getSkipRows() {
        return skipRows.get();
    }

    public IntegerProperty skipRowsProperty() {
        return skipRows;
    }

    public void setSkipRows(int skipRows) {
        this.skipRows.set(skipRows);
    }

    public ObservableList<StringProperty> getColumnTypes() {
        return columnTypes.get();
    }

    public ListProperty<StringProperty> columnTypesProperty() {
        return columnTypes;
    }

    public void setColumnTypes(ObservableList<StringProperty> columnTypes) {
        this.columnTypes.set(columnTypes);
    }

    public StringProperty getDfNameProperty(){
        return dfName;
    }

    public void setDfName(String dfName) {
        this.dfName.set(dfName);
    }

    public String getDfName(){return dfName.get();}
}
