package com.example.techgicus_ebilling.techgicus_ebilling.batch.reader;

import com.example.techgicus_ebilling.techgicus_ebilling.batch.dto.ExcelRowData;
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.examples.Archiver;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Component
@StepScope // Makes a fresh copy(bean created) every time the job runs
public class ZipExcelItemReader implements ItemReader<ExcelRowData> {

    // These variables remember what we're currently reading
    private Workbook currentWorkbook;     // The open Excel file (.xlsx)
    private Sheet currentSheet;           // The current sheet inside the Excel (sheet 0 or 1)
    private int currentRowIndex = 3;      // Start reading from row 3 (index 3) to skip headers
    private String currentFileType = ""; // Tells us if it's sale, purchase, or party report

    // Tools for reading ZIP and RAR files
    private ArchiveInputStream zipStream; // Used when the uploaded file is .zip
    private Archive junrarArchive;       // Used when the uploaded file is .rar
    private FileHeader currentRarHeader;  // Current file inside a RAR archive


    // These values come from the job parameters (sent when uploading the file)
    @Value("#{jobParameters['archiveFilePath']}")
    private String archiveFilePath; // Full path to the uploaded ZIP/RAR file on the server

    @Value("#{jobParameters['archiveType']}")
    private String archiveType;

    private boolean initialized = false;

    private int currentSheetIndex = 0;  // 0 = main sheet, 1 = item details sheet

    private static final Logger log = LoggerFactory.getLogger(ZipExcelItemReader.class);

    // This method runs the first time to open the ZIP or RAR file
    private void initialize() throws Exception {
        if (initialized) return; // Don't run setup again

        File archiveFile = new File(archiveFilePath);
        if (!archiveFile.exists()) {
            throw new IllegalStateException("Uploaded file not found: " + archiveFilePath);
        }

        if ("rar".equalsIgnoreCase(archiveType)) {
            // Open the RAR file using junrar library
            junrarArchive = new Archive(archiveFile);
            log.info("Opened RAR file: " + archiveFile.getName());
        } else {
            // Open the ZIP file using Apache Commons Compress
            InputStream fileStream = new FileInputStream(archiveFile);
            zipStream = new ZipArchiveInputStream(fileStream);
            log.info("Opened ZIP file: " + archiveFile.getName());
        }

        initialized = true;
        moveToNextExcelFile(); // Find and open the first Excel file inside
    }

    // This method finds the next .xlsx file inside the ZIP/RAR
    private void moveToNextExcelFile() throws Exception {
        if (currentWorkbook != null) {
            currentWorkbook.close(); // Close the previous Excel file
        }

        InputStream excelStream = null;

        if ("rar".equalsIgnoreCase(archiveType)) {
            // Loop through files in RAR
            currentRarHeader = junrarArchive.nextFileHeader();
            while (currentRarHeader != null) {
                String entryName = currentRarHeader.getFileName().toLowerCase();
                if (entryName.endsWith(".xlsx") && !currentRarHeader.isDirectory()) {
                    System.out.println(">>> Found Excel in RAR: " + currentRarHeader.getFileName());
                    excelStream = junrarArchive.getInputStream(currentRarHeader);
                    openWorkbookAndStartSheet(excelStream, entryName);
                    return; // Stop looking – we found one
                }
                currentRarHeader = junrarArchive.nextFileHeader();
            }
        } else {
            // Loop through files in ZIP
            ArchiveEntry entry;
            while ((entry = zipStream.getNextEntry()) != null) {
                String entryName = entry.getName().toLowerCase();
                if (entryName.endsWith(".xlsx") && !entry.isDirectory()) {
                    System.out.println(">>> Found Excel in ZIP: " + entry.getName());
                    excelStream = zipStream;
                    openWorkbookAndStartSheet(excelStream, entryName);
                    return;
                }
            }
        }

        // No more Excel files found
        currentWorkbook = null;
        cleanup();
    }

    private void openWorkbookAndStartSheet(InputStream stream, String fileName) throws Exception {
        currentWorkbook = new XSSFWorkbook(stream);

        // Reset for new file
        currentSheetIndex = 0;
        currentSheet = currentWorkbook.getSheetAt(0);
        currentRowIndex = 3;

      //  String sheetName = currentWorkbook.getSheetName(0).toLowerCase().trim();

        // Determine base type from FILENAME (more reliable)
        //String baseType = determineFileType(sheetName);
        String baseType = determineFileType(fileName.toLowerCase());

        // Always start with "-main" for Sheet 0
        currentFileType = baseType + "-main";

        log.info(">>> Reading MAIN sheet: " + currentWorkbook.getSheetName(0) + " → Type: " + currentFileType);
    }

    // Simple way to guess file type from filename
    private String determineFileType(String fileName) {
      //  log.info("file-name is : "+fileName);
     //  String lower = fileName.toLowerCase();
        if (fileName.contains("salereport")) return "sale";
         else if (fileName.contains("purchasereport")) return "purchase";
        else if (fileName.contains("partyreport")) return "party";
        else return "unknown";
    }

//    // Guess file type from name
//    private void setFileType(String name) {
//        if (name.contains("sale")) {
//            currentFileType = "sale";
//        } else if (name.contains("purchase")) {
//            currentFileType = "purchase";
//        } else if (name.contains("party")) {
//            currentFileType = "party";
//        } else {
//            currentFileType = "unknown";
//        }
//    }

    // Clean up all open files and streams
    private void cleanup() throws Exception {
        if (currentWorkbook != null) currentWorkbook.close();
        if (zipStream != null) zipStream.close();
        if (junrarArchive != null) junrarArchive.close();
    }


    // This is the main method Spring Batch calls to get one row at a time
    @Override
    public ExcelRowData read() throws Exception {
        initialize(); // Open archive if first time

        if (currentWorkbook == null) {
            cleanup();
            return null; // No more files → job finished
        }

        // Read rows from the current sheet
        if (currentRowIndex <= currentSheet.getLastRowNum()) {
            Row row = null;
            if (currentFileType.equals("sale-items")){
                 row = currentSheet.getRow(currentRowIndex=currentRowIndex+2);
            }
            else {
                 row = currentSheet.getRow(currentRowIndex++);
            }

            log.info("current file type "+currentFileType);
            log.info("row : "+row);

            if (row != null) {
                boolean hasData = false;
                int lastCell = row.getLastCellNum();
                for (int i = 0; i < Math.min(10, lastCell > 0 ? lastCell : 10); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    // Check if cell has any real data
                    if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().trim().isEmpty()) {
                        hasData = true;
                        break;
                    }
                    if (cell.getCellType() == CellType.NUMERIC) {
                        hasData = true;
                        break;
                    }
                    if (cell.getCellType() == CellType.BOOLEAN) {
                        hasData = true;
                        break;
                    }
                    // You can add FORMULA if needed
                }

                if (hasData) {
                    log.info("has data : "+currentFileType);
                    return new ExcelRowData(row, currentFileType);
                }
            }

//            if (row != null) {
//                // Check if row has any data in first 5 cells (skip blank rows)
//                boolean hasData = false;
//                int lastCell = row.getLastCellNum();
//                for (int i = 0; i < Math.min(10, lastCell); i++) { // Check first 10 cells
//                   // Cell cell = row.getCell(i);
//                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);  // Safe
//                if (hasData) {
//                    return new ExcelRowData(row, currentFileType);
//                }
//                    if (cell != null &&
//                            (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().trim().isEmpty()) ||
//                            (cell.getCellType() == CellType.NUMERIC) ||
//                            (cell.getCellType() == CellType.BOOLEAN)) {
//                        hasData = true;
//                        break;
//                    }
//                }
//
//                if (hasData) {
//                    return new ExcelRowData(row, currentFileType); // Send row to processor
//               }
//            }
        }



        // Current sheet finished → switch to Sheet 1 (Item Details) if it exists
        if (currentSheetIndex == 0 && currentWorkbook.getNumberOfSheets() > 1) {
            log.info("currentSheetIndex : "+currentSheetIndex+" ,"+"currentWorkbook sheet number"+currentWorkbook.getNumberOfSheets());
            currentSheetIndex = 1;
            currentSheet = currentWorkbook.getSheetAt(1);
            currentFileType = currentFileType.replace("-main", "-items");
            log.info("current file name : "+currentFileType);
            currentRowIndex = 3;  // Skip headers in item sheet
            log.info(">>> Switching to ITEM DETAILS sheet: " + currentWorkbook.getSheetName(1));
            return read();   // Start reading from new sheet
        }

        // No more sheets in this file → go to next Excel file in archive
        moveToNextExcelFile();
        return read();
    }
}
