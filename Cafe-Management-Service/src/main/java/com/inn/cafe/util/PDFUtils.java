package com.inn.cafe.util;

import java.io.FileOutputStream;
import java.util.Map;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.cafe.constants.CafeConstants;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(PDFUtils.class);

	public static void generatePDF(Map<String, Object> reqMap, String header, String fileName) {
		try {

			String data = "Name: " + reqMap.get("name") + "\n" + "Contact Number: " + reqMap.get("contactNumber") + "\n"
					+ "Email: " + reqMap.get("email") + "\n" + "Payment Method: " + reqMap.get("paymentMethod");

			Document doc = new Document();
			PdfWriter.getInstance(doc, new FileOutputStream(CafeConstants.STORE_LOCATION + "\\" + fileName + ".pdf"));

			doc.open();
			setRectangleBorderInPDF(doc);

			Paragraph chunk = new Paragraph(header, getFont("Header"));
			chunk.setAlignment(Element.ALIGN_CENTER);
			doc.add(chunk);

			Paragraph dataParagraph = new Paragraph(data + "\n \n", getFont("Data"));
			doc.add(dataParagraph);

			PdfPTable table = new PdfPTable(5);
			table.setWidthPercentage(100);
			addTableHeader(table);

			JSONArray jsonArray = CafeUtils.getJsonArrayFromString(reqMap.get("productDetails")+"");
			for (int i = 0; i < jsonArray.length(); i++) {
				addTableRows(table, CafeUtils.getMapFromJson(jsonArray.getString(i)));
			}
			doc.add(table);

			Paragraph footer = new Paragraph(
					"Total: " + reqMap.get("totalAmount") + "\n" + "Thank you for visting. Visit Again!!",
					getFont("Data"));

			doc.add(footer);
			doc.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
	}

	private static void addTableRows(PdfPTable table, Map<String, Object> mapFromJson) {
		LOGGER.info("addTableRows");
		table.addCell(mapFromJson.get("name") + "");
		table.addCell(mapFromJson.get("category") + "");
		table.addCell(mapFromJson.get("quantity") + "");
		table.addCell(Double.toString((Double) mapFromJson.get("price")));
		table.addCell(Double.toString((Double) mapFromJson.get("total")));
	}

	private static void addTableHeader(PdfPTable table) {
		LOGGER.info("addTableHeader");
		Stream.of("Name", "Category", "Quantity", "Price", "Sub Total").forEach(columnTitle -> {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(2);
			header.setPhrase(new Phrase(columnTitle));
			header.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.setVerticalAlignment(Element.ALIGN_CENTER);
			table.addCell(header);
		});
	}

	private static Font getFont(String type) {
		LOGGER.info("getFont");
		switch (type) {
		case "Header": {
			Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
			headerFont.setStyle(Font.BOLD);
			return headerFont;
		}
		case "Data": {
			Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
			dataFont.setStyle(Font.BOLD);
			return dataFont;
		}
		default:
			return new Font();
		}
	}

	private static void setRectangleBorderInPDF(Document doc) throws DocumentException {
		LOGGER.info("Inside setRectangleBorderInPDF");
		Rectangle rect = new Rectangle(577, 825, 18, 15);
		rect.enableBorderSide(1);
		rect.enableBorderSide(2);
		rect.enableBorderSide(4);
		rect.enableBorderSide(8);
		rect.setBorderColor(BaseColor.BLACK);
		rect.setBorderWidth(1);
		doc.add(rect);
	}
}
