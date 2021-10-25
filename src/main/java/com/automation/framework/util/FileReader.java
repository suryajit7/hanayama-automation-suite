package com.automation.framework.util;

import com.automation.framework.data.FileType;
import com.automation.framework.data.entity.BaseEntity;
import com.creditdatamw.zerocell.Reader;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static com.automation.framework.data.FileType.*;


/**
 * Reusable File Reader utility capable of reading all types of files including, but not limited to text, csv, excel, json etc.
 * All methods accept a filename which would be automatically located by the PathFinder service.
 * All methods will either return a List of objects deserialized from given file or a List of String.
 */

@Slf4j
@Service
public class FileReader {

    @Autowired
    private PathFinder pathFinder;


    public List<String> readTxtFile(String filename) throws IOException {
        return hasValidExtension(filename, TXT) ? Files.readAllLines(pathFinder.getFilePathForFile(filename)): List.of("");
    }

    public BaseEntity readJsonFile(String filename) throws IOException {
        return hasValidExtension(filename, JSON) ? new ObjectMapper().readValue(pathFinder.getFilePathForFile(filename).toFile(), BaseEntity.class): BaseEntity.builder().build();
    }


    public <T> List<T> readCsvFile(String filename, Class<T> type) throws IOException {

        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();
        ObjectReader reader = new CsvMapper().readerFor(type).with(csvSchema);

        List<T> genericList = new ArrayList<>();

        if (hasValidExtension(filename, CSV)) {
            try (FileInputStream inputStream = new FileInputStream(pathFinder.getFilePathForFile(filename).toAbsolutePath().toString())) {
                try (MappingIterator<T> iterator = reader.readValues(inputStream)) {
                    iterator.forEachRemaining(genericList::add);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return genericList;
    }

    public BaseEntity readXmlFile(String filename) throws Exception {
        Serializer serializer = new Persister();
        return hasValidExtension(filename, XML) ? serializer.read(BaseEntity.class, pathFinder.getFilePathForFile(filename).toFile()) : BaseEntity.builder().build();
    }

    public <T> List<T> readExcelFile(String filename, Class<T> type, String sheetName) {
        return Reader.of(type)
                .from(pathFinder.getFilePathForFile(filename).toFile())
                .sheet(sheetName)
                .skipHeaderRow(true)
                .list();
    }

    public String getFileExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    private Boolean hasValidExtension(String filename, FileType fileType){
        return getFileExtension(filename).equalsIgnoreCase(fileType.getStringValue());
    }
}