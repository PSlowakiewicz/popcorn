package mini.dataframereader;

import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvReadOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TestMain {
    public static void main(String[] args) throws IOException {
        int ROW_LIMIT = 50;

        CsvReadOptions.Builder builder =
                CsvReadOptions.builder("DataFrameReader/src/mini/dataframereader/.testdata/archive.csv");

        CsvReadOptions options = builder.build();

        Table t1 = Table.read().usingOptions(options).first(10);
        System.out.println(t1);
        //Table dataframe = Table.read().csv("DataFrameReader/src/mini/dataframereader/.testdata/archive.csv");

        System.out.println(t1);
    }
}
