package com.ercanbeyen.restaurantapplication.exporter;

import com.ercanbeyen.restaurantapplication.dto.BillDto;
import com.ercanbeyen.restaurantapplication.model.Order;
import com.ercanbeyen.restaurantapplication.util.TimeUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.experimental.UtilityClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@UtilityClass
public class PDFExporter {
    public ByteArrayOutputStream generatePDFStreamOfBillPaper(BillDto request, Map<String, Double> items) throws DocumentException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();

        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        writeHeader(document);
        addNewLine(document);

        writeTitle(document);
        addNewLine(document);

        writeBody(request, items, document);
        addNewLine(document);

        writeFooter(document);

        document.close();

        return byteArrayOutputStream;
    }

    private void writeTitle(Document document) throws DocumentException {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, BaseColor.RED);

        Paragraph paragraph = new Paragraph("Bill", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
    }

    private void writeBody(BillDto request, Map<String, Double> items, Document document) throws DocumentException {
        writeServiceDetails(request, document);
        addNewLine(document);
        writeOrders(request, items, document);
    }

    private void writeOrders(BillDto request, Map<String, Double> items, Document document) throws DocumentException {
        PdfPTable pdfPTable = new PdfPTable(4);

        final Font font = new Font(Font.FontFamily.TIMES_ROMAN, Font.DEFAULTSIZE, Font.BOLD);

        List.of("Name", "Unit Price", "Amount", "Total Price").forEach(field -> {
            PdfPCell pdfPCell = new PdfPCell();
            pdfPCell.setBackgroundColor(BaseColor.GRAY);
            pdfPCell.setBorderWidth(1);
            Phrase phrase = new Phrase(field, font);
            pdfPCell.setPhrase(phrase);
            pdfPTable.addCell(pdfPCell);
        });

        double sum = 0;

        for (Order order : request.orders()) {
            String name = order.getItemName();
            double unitPrice = items.get(name);
            double amount = order.getAmount();
            double totalPrice = unitPrice * amount;
            sum += totalPrice;

            pdfPTable.addCell(new PdfPCell(new Phrase(name)));
            pdfPTable.addCell(new PdfPCell(new Phrase(String.valueOf(unitPrice))));
            pdfPTable.addCell(new PdfPCell(new Phrase(String.valueOf(amount))));
            pdfPTable.addCell(new PdfPCell(new Phrase(String.valueOf(totalPrice))));
        }

        pdfPTable.addCell(new PdfPCell(new Phrase("Sum", font)));
        pdfPTable.addCell(new PdfPCell(new Phrase("")));
        pdfPTable.addCell(new PdfPCell(new Phrase("")));
        pdfPTable.addCell(new PdfPCell(new Phrase(String.valueOf(sum))));

        document.add(pdfPTable);
    }

    private void writeServiceDetails(BillDto request, Document document) throws DocumentException {
        final Font boldFont = new Font();
        boldFont.setStyle(Font.BOLD);

        Chunk tableNumberInputChunk = new Chunk("Table Number: ", boldFont);
        Chunk tableNumberOutputChunk = new Chunk(request.tableNumber().toString());

        Phrase chunkPhrase = new Phrase();
        chunkPhrase.addAll(List.of(tableNumberInputChunk, tableNumberOutputChunk));

        Paragraph paragraph = new Paragraph(chunkPhrase);
        document.add(paragraph);

        Chunk openDateInputChunk = new Chunk("Opening Date: ", boldFont);

        LocalDateTime openDateTime = request.openDate();
        String openDate = openDateTime.toLocalDate().toString();
        String openTime = TimeUtil.getTimeStatement(openDateTime.toLocalTime());
        Chunk openDateOutputChunk = new Chunk(openDate + " " + openTime);

        chunkPhrase = new Phrase();
        chunkPhrase.addAll(List.of(openDateInputChunk, openDateOutputChunk));

        paragraph = new Paragraph(chunkPhrase);
        document.add(paragraph);

        Chunk updateDateInputChunk = new Chunk("Update Date: ", boldFont);

        LocalDateTime updateDateTime = request.updateDate();
        String updateDate = updateDateTime.toLocalDate().toString();
        String updateTime = TimeUtil.getTimeStatement(updateDateTime.toLocalTime());
        Chunk updateDateOutputChunk = new Chunk(updateDate + " " + updateTime);

        chunkPhrase = new Phrase();
        chunkPhrase.addAll(List.of(updateDateInputChunk, updateDateOutputChunk));

        paragraph = new Paragraph(chunkPhrase);
        document.add(paragraph);

        Chunk printDateInputChunk = new Chunk("Print Date: ", boldFont);

        LocalDateTime printDateTime = LocalDateTime.now();
        String printDate = printDateTime.toLocalDate().toString();
        String printTime = TimeUtil.getTimeStatement(printDateTime.toLocalTime());
        Chunk printDateOutputChunk = new Chunk(printDate + " " + printTime);

        chunkPhrase = new Phrase();
        chunkPhrase.addAll(List.of(printDateInputChunk, printDateOutputChunk));

        paragraph = new Paragraph(chunkPhrase);
        document.add(paragraph);

        Chunk employeeFullNameInputChunk = new Chunk("Employee: ", boldFont);

        String employeeFullName = request.employeeFullName();
        Chunk employeeFullNameOutputChunk = new Chunk(employeeFullName);

        chunkPhrase = new Phrase();
        chunkPhrase.addAll(List.of(employeeFullNameInputChunk, employeeFullNameOutputChunk));

        paragraph = new Paragraph(chunkPhrase);
        document.add(paragraph);
    }

    private void writeHeader(Document document) throws DocumentException, IOException {
        Font font = new Font(Font.FontFamily.HELVETICA, Font.DEFAULTSIZE, Font.BOLD, BaseColor.BLUE);

        Paragraph paragraph = new Paragraph("Restaurant", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);

        writeLogo(document);
    }

    private void writeFooter(Document document) throws DocumentException {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.ITALIC);
        String message = "This bill was generated by the restaurant's system. If you encounter any problems with the bill, please report it to the responsible employee listed on the bill.";

        Paragraph paragraph = new Paragraph(message, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);

        document.add(paragraph);
    }

    private void writeLogo(Document document) throws DocumentException, IOException {
        Image image = Image.getInstance(Path.of("./src/main/resources/static/logo.png")
                .toAbsolutePath()
                .toString());
        image.scalePercent(10);
        image.setAlignment(Element.ALIGN_CENTER);
        document.add(image);
    }

    private void addNewLine(Document document) throws DocumentException {
        Paragraph paragraph = new Paragraph("\n");
        document.add(paragraph);
    }
}
