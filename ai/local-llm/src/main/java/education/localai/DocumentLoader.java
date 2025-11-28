package education.localai;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DocumentLoader implements DocumentsProvider {
    private final Map<String, String> documents = new HashMap<>();

    public void loadFromDirectory(String directoryPath) throws IOException {
        Path dir = Paths.get(directoryPath);

        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
            System.out.println("Created directory: " + directoryPath);
            return;
        }

        Files.list(dir)
                .filter(this::isSupportedFile)
                .forEach(path -> {
                    try {
                        String filename = path.getFileName().toString();
                        String text = extractText(path.toFile());
                        documents.put(filename, text);
                        System.out.println("Loaded: " + filename + " (" + text.length() + " chars)");
                    } catch (Exception e) {
                        System.err.println("Error loading " + path + ": " + e.getMessage());
                    }
                });
    }

    private boolean isSupportedFile(Path path) {
        String filename = path.toString().toLowerCase();
        return filename.endsWith(".pdf") ||
                filename.endsWith(".docx") ||
                filename.endsWith(".xlsx");
    }

    private String extractText(File file) throws IOException {
        String filename = file.toString().toLowerCase();

        if (filename.endsWith(".pdf")) {
            return extractPdf(file);
        } else if (filename.endsWith(".docx")) {
            return extractDocx(file);
        } else if (filename.endsWith(".xlsx")) {
            return extractXlsx(file);
        }

        return "";
    }

    private String extractPdf(File file) throws IOException {
        try (PDDocument doc = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(doc);
        }
    }

    private String extractDocx(File file) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(file))) {
            return doc.getParagraphs().stream()
                    .map(XWPFParagraph::getText)
                    .collect(Collectors.joining("\n"));
        }
    }

    private String extractXlsx(File file) throws IOException {
        StringBuilder text = new StringBuilder();

        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file))) {
            for (Sheet sheet : workbook) {
                text.append("\n=== Sheet: ").append(sheet.getSheetName()).append(" ===\n");

                for (Row row : sheet) {
                    for (Cell cell : row) {
                        text.append(getCellValue(cell)).append("\t");
                    }
                    text.append("\n");
                }
            }
        }

        return text.toString();
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    @Override
    public Map<String, String> getDocuments() {
        return documents;
    }

    public String getDocument(String filename) {
        return documents.get(filename);
    }

    public Set<String> getFilenames() {
        return documents.keySet();
    }

    public int getDocumentCount() {
        return documents.size();
    }
}