package mx.com.ddwhite.ws.service.utils;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class CustomPrintUtils implements Printable {
	
	// cut that paper!
	public static final byte[] CUT_P = new byte[] { 0x1d, 'V', 1 };
	public static final String PRINTER = "5830 Series";
	public static final int CHARACTERS_BY_ROW = 30;
	public static final String FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";
	public static final int PADDING = 1;
	public static final String LOGO_DDWHITE = "logo-ticket-default.jpg";
	
	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		if (page > 0) { /* We have only one page, and 'page' is zero-based */
			return NO_SUCH_PAGE;
		}
		/*
		 * User (0,0) is typically outside the imageable area, so we must translate by
		 * the X and Y values in the PageFormat to avoid clipping
		 */
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		/* Now we perform our rendering */
		g.setFont(new Font("Roman", 0, 8));
		g.drawString("Hello world !", 0, 10);
		return PAGE_EXISTS;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getPrinters() {
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		PrintService printServices[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
		List<String> printerList = new ArrayList<String>();
		for (PrintService printerService : printServices) {
			printerList.add(printerService.getName());
		}
		return printerList;
	}

	/**
	 * 
	 * @param printerName
	 * @param text
	 */
	public void printString(String printerName, String text) {
		// find the printService of name printerName
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
		PrintService service = findPrintService(printerName, printService);
		DocPrintJob job = service.createPrintJob();
		try {
			byte[] bytes;
			// important for umlaut chars
			bytes = text.getBytes("CP437");
			Doc doc = new SimpleDoc(bytes, flavor, null);
			job.print(doc, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param printerName
	 * @param bytes
	 */
	public void printBytes(String printerName, byte[] bytes) {
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
		PrintService service = findPrintService(printerName, printService);
		DocPrintJob job = service.createPrintJob();
		try {
			Doc doc = new SimpleDoc(bytes, flavor, null);
			job.print(doc, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * THIS NO WORK - javax.print.PrintException: java.awt.print.PrinterException: Paper's imageable width is too small.
	 * @param printerName
	 * @param imagePath
	 */
	public void printImage(String printerName, String imagePath) {
		DocFlavor flavor = DocFlavor.INPUT_STREAM.GIF;
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(OrientationRequested.PORTRAIT);
		pras.add(MediaSizeName.INVOICE);
		PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
		PrintService service = findPrintService(printerName, printService);
		DocPrintJob job = service.createPrintJob();
		try {
			InputStream fis = resourceLoader.getResource("classpath:" + imagePath).getInputStream();
			Doc doc = new SimpleDoc(fis, flavor, null);
			job.print(doc, null);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private PrintService findPrintService(String printerName, PrintService[] services) {
		for (PrintService service : services) {
			if (service.getName().equalsIgnoreCase(printerName)) {
				return service;
			}
		}
		return null;
	}

}
