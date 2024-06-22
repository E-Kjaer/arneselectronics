package OMS.Domain.PDF;

import OMS.Database.DBSalereportData;
import javafx.scene.paint.Color;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ChartUtilities;
import org.jfree.ui.HorizontalAlignment;

import static org.jfree.ui.RectangleEdge.TOP;

public class PDFcreate {
    //Size 595.3 × 841.9 points.
    static String home = System.getProperty("user.home");
    static String imgPath = System.getProperty("user.dir");
    static String imgPath2 = "/src/main/java/OMS/Domain/PDF/Images/";
    private static DBSalereportData dbSalereportData;
    private static float maxWidth = 595.3f;
    private static float maxHeight = 841.9f;



    public static void main(String[] args) throws SQLException {
        //String timestampString = "2024-05-11 00:22:59.047248";
        //Timestamp timestamp = Timestamp.valueOf(timestampString);
        //createSaleReport(timestamp);
        //BlockDiagramExample(800, 450,timestamp);
    }


    public static void createSaleReport(Timestamp timestamp){
        String PDF_NAME = filename(timestamp);
        File file = new File(home+"/Downloads/" + PDF_NAME + ".pdf");
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                standardLook(contentStream, 1, timestamp);
                dataInserterAnalysis(contentStream, document, timestamp);
                dataInserterRevenue(contentStream, document, timestamp);
                dataInserterUser(contentStream, document, timestamp);
            }
            PDPage page2 = new PDPage(PDRectangle.A4);
            document.addPage(page2);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page2)) {
                standardLook(contentStream, 2, timestamp);
                dataInserterAmount(contentStream, document, timestamp);
                tableCreate(contentStream);
                dataInserterRevenue_CurrentAndLast(contentStream, document, timestamp);
            }
            document.save(new File(file + ""));
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String filename(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM_yyyy", Locale.ENGLISH); // MMMM for full month name and yyyy for year
        String monthYear = sdf.format(timestamp);
        int seconds = timestamp.getSeconds();
        String filename = "Sale_Report_" + monthYear + "_" + seconds;
        return filename;
    }

    private static void standardLook(PDPageContentStream contentStream, int pageNumber, Timestamp timestamp) throws IOException {
        PDColor colorBlack = rightColor("#000000");
        PDColor colorTitle = rightColor("#FFFFFF");
        PDColor colorBox = rightColor("#225775");
        PDType1Font textFontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font textFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        int textFontSizeTitle = 15;
        int textFontSizeNormal = 10;
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
        String monthYear = sdf.format(timestamp);

        //Top box
        contentStream.setStrokingColor(colorBlack);
        contentStream.setNonStrokingColor(colorBox);
        contentStream.addRect(0, maxHeight - (50+30), (maxWidth/12)*5.5f, 50);
        contentStream.fill();
        contentStream.stroke();

        //Top box text
        contentStream.beginText();
        contentStream.setNonStrokingColor(colorTitle);
        contentStream.setFont(textFontBold, textFontSizeTitle);
        contentStream.newLineAtOffset(maxWidth/8, (maxHeight - (50+65)/2));
        contentStream.showText("MONTHLY SALE REPORT");
        contentStream.endText();

        //Date "Still example"
        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFont, textFontSizeNormal);
        contentStream.newLineAtOffset((maxWidth/12)*10, (maxHeight - (50+65)/2));
        contentStream.showText(monthYear);
        contentStream.endText();

        //Page number
        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFont, textFontSizeNormal);
        contentStream.newLineAtOffset((maxWidth/12)*10+10, (maxHeight / 24)-10);
        contentStream.showText("Page " + pageNumber);
        contentStream.endText();
    }
    private static void dataInserterAnalysis(PDPageContentStream contentStream, PDDocument document, Timestamp timestamp) throws IOException, SQLException {
        PDColor colorBlack = rightColor("#000000");
        PDType1Font textFontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font textFont = new PDType1Font(Standard14Fonts.FontName.COURIER);
        int textFontSizeNormal = 10;
        String[] data = {"Sale Revenue", "Profit", "New Customer", "Average Amount", };

        //Title
        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFontBold, textFontSizeNormal+4);
        contentStream.newLineAtOffset(maxWidth/9.5f, (maxHeight/12)*(10.25f-0));
        contentStream.showText("Analysis");
        contentStream.endText();
        //Title line
        contentStream.setStrokingColor(colorBlack);
        contentStream.setLineWidth(1.5f);
        contentStream.moveTo(maxWidth/10, (maxHeight/12)*(10.85f-0)-52.5f); // Start point of x-axis
        contentStream.lineTo(maxWidth/1.15f, (maxHeight/12)*(10.85f-0)-52.5f); // End point of x-axis
        contentStream.stroke();

        //Different topics of analysis
        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFont, textFontSizeNormal+3);
        contentStream.newLineAtOffset(maxWidth/9.6f, (maxHeight/12)*(9.7f));
        contentStream.showText(data[0]);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFont, textFontSizeNormal+3);
        contentStream.newLineAtOffset((maxWidth/9.5f)+(62.66f*2.f), (maxHeight/12)*(9.7f));
        contentStream.showText(data[1]);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFont, textFontSizeNormal+3);
        contentStream.newLineAtOffset((maxWidth/12f)+(62.66f*3.7f), (maxHeight/12)*(9.7f));
        contentStream.showText(data[2]);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFont, textFontSizeNormal+3);
        contentStream.newLineAtOffset((maxWidth/12f)+(62.66f*5.8f), (maxHeight/12)*(9.7f));
        contentStream.showText(data[3]);
        contentStream.endText();

        //Lines
        contentStream.setStrokingColor(colorBlack);
        contentStream.setLineWidth(1.5f);
        contentStream.moveTo(165.0f, (maxHeight/12)*(9.2f)); // Start point of x-axis
        contentStream.lineTo(165.0f, ((maxHeight/12)*(9.9f)+6.5f)); // End point of x-axis
        contentStream.stroke();

        contentStream.setStrokingColor(colorBlack);
        contentStream.setLineWidth(1.5f);
        contentStream.moveTo(260.0f, (maxHeight/12)*(9.2f)); // Start point of x-axis
        contentStream.lineTo(260.0f, ((maxHeight/12)*(9.9f)+6.5f)); // End point of x-axis
        contentStream.stroke();

        contentStream.setStrokingColor(colorBlack);
        contentStream.setLineWidth(1.5f);
        contentStream.moveTo(390.0f, (maxHeight/12)*(9.2f)); // Start point of x-axis
        contentStream.lineTo(390.0f, ((maxHeight/12)*(9.9f)+6.5f)); // End point of x-axis
        contentStream.stroke();

        //Data
        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFontBold, textFontSizeNormal + 2);
        float revenue = sizeFit(String.valueOf(DBSalereportData.getRevenue(timestamp)), true);
        contentStream.newLineAtOffset((maxWidth / 9.5f) * (1.35f - revenue), (maxHeight / 12) * (9.4f));
        contentStream.showText(String.format("%.2f", DBSalereportData.getRevenue(timestamp)));
        contentStream.endText();

        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFontBold, textFontSizeNormal + 2);
        float profit = sizeFit(String.valueOf(DBSalereportData.getProfit(timestamp)), false);
        contentStream.newLineAtOffset((maxWidth / 12f) + (62.66f * (2.55f - profit)), (maxHeight / 12) * (9.4f));
        contentStream.showText(String.format("%.2f", DBSalereportData.getProfit(timestamp)));
        contentStream.endText();

        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFontBold, textFontSizeNormal + 2);
        int user = (int) sizeFit(String.valueOf(DBSalereportData.getNewUser(timestamp)), false);
        contentStream.newLineAtOffset((maxWidth / 12f) + (62.66f * (4.3f - user)), (maxHeight / 12) * (9.4f));
        contentStream.showText(String.valueOf(DBSalereportData.getNewUser(timestamp)));
        contentStream.endText();

        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFontBold, textFontSizeNormal + 2);
        float avgAmount = sizeFit(String.valueOf(DBSalereportData.getAvgAmountPerOrder(timestamp)), true);
        contentStream.newLineAtOffset((maxWidth / 12f) + (62.66f * (6.25f - avgAmount)), (maxHeight / 12) * (9.4f));
        contentStream.showText(String.format("%.2f", DBSalereportData.getAvgAmountPerOrder(timestamp)));
        contentStream.endText();


    }
    private static void dataInserterRevenue(PDPageContentStream contentStream, PDDocument document, Timestamp timestamp) throws IOException, SQLException{
        PDColor colorBlack = rightColor("#000000");
        PDType1Font textFontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font textFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        int textFontSizeNormal = 10;

        createAndSaveChartRevenue(800, 450,timestamp);

        //Title
        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFontBold, textFontSizeNormal+4);
        contentStream.newLineAtOffset(maxWidth/9.5f, (maxHeight/12)*(8.7f));
        contentStream.showText("Revenue Per Day");
        contentStream.endText();



        //Title line bottom
        contentStream.setStrokingColor(colorBlack);
        contentStream.setLineWidth(1.5f);
        contentStream.moveTo(maxWidth/10, (maxHeight/12)*(9.35f-0)-52.5f); // Start point of x-axis
        contentStream.lineTo(maxWidth/1.15f, (maxHeight/12)*(9.35f-0)-52.5f); // End point of x-axis
        contentStream.stroke();



        File file = new File(imgPath + imgPath2 + File.separator + "Revenue_Per_Day.png");
        PDImageXObject image = PDImageXObject.createFromFileByExtension(file, document);

        // Get the dimensions of the image
        float imageWidth = image.getWidth();
        float imageHeight = image.getHeight();

        // Define the position and size of the image on the page

        float width = maxWidth/1.4f; // Width of the image
        float height = (width / imageWidth) * imageHeight; // Height of the image to maintain aspect ratio

        // Draw the image onto the PDF page
        contentStream.drawImage(image, (maxWidth/10)+10, (maxHeight/12)*(5.85f-0)-52.5f, width, height);
    }
    private static void dataInserterUser(PDPageContentStream contentStream, PDDocument document, Timestamp timestamp) throws IOException, SQLException{
        PDColor colorBlack = rightColor("#000000");
        PDType1Font textFontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font textFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        int textFontSizeNormal = 10;

        createAndSaveChartUser(800, 450,timestamp);

        //Title
        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFontBold, textFontSizeNormal+4);
        contentStream.newLineAtOffset(maxWidth/9.5f, (maxHeight/12)*(4.6f));
        contentStream.showText("New Customer Per Day");
        contentStream.endText();



        //Title line bottom
        contentStream.setStrokingColor(colorBlack);
        contentStream.setLineWidth(1.5f);
        contentStream.moveTo(maxWidth/10, (maxHeight/12)*(5.25f-0)-52.5f); // Start point of x-axis
        contentStream.lineTo(maxWidth/1.15f, (maxHeight/12)*(5.25f-0)-52.5f); // End point of x-axis
        contentStream.stroke();



        File file = new File(imgPath + imgPath2 + File.separator + "New_Users_Per_Day.png");
        PDImageXObject image = PDImageXObject.createFromFileByExtension(file, document);

        // Get the dimensions of the image
        float imageWidth = image.getWidth();
        float imageHeight = image.getHeight();

        // Define the position and size of the image on the page

        float width = maxWidth/1.4f; // Width of the image
        float height = (width / imageWidth) * imageHeight; // Height of the image to maintain aspect ratio

        // Draw the image onto the PDF page
        contentStream.drawImage(image, (maxWidth/10)+10, (maxHeight/12)*(1.75f-0)-52.5f, width, height);
    }
    private static void dataInserterAmount(PDPageContentStream contentStream, PDDocument document, Timestamp timestamp) throws IOException, SQLException{
        PDColor colorBlack = rightColor("#000000");
        PDType1Font textFontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font textFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        int textFontSizeNormal = 10;

        createAndSaveChartOrder(800, 450,timestamp);

        //Title
        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFontBold, textFontSizeNormal+4);
        contentStream.newLineAtOffset(maxWidth/9.5f, (maxHeight/12)*(10.25f-0));
        contentStream.showText("Avg Amount Per Order Per Day");
        contentStream.endText();



        //Title line bottom
        contentStream.setStrokingColor(colorBlack);
        contentStream.setLineWidth(1.5f);
        contentStream.moveTo(maxWidth/10, (maxHeight/12)*(10.85f-0)-52.5f); // Start point of x-axis
        contentStream.lineTo(maxWidth/1.15f, (maxHeight/12)*(10.85f-0)-52.5f); // End point of x-axis
        contentStream.stroke();


        File file = new File(imgPath + imgPath2 + File.separator + "Avg_Amount_Per_Order_Per_Day.png");
        PDImageXObject image = PDImageXObject.createFromFileByExtension(file, document);

        // Get the dimensions of the image
        float imageWidth = image.getWidth();
        float imageHeight = image.getHeight();

        // Define the position and size of the image on the page

        float width = maxWidth/1.4f; // Width of the image
        float height = (width / imageWidth) * imageHeight; // Height of the image to maintain aspect ratio

        // Draw the image onto the PDF page
        contentStream.drawImage(image, (maxWidth/10)+10, (maxHeight/12)*(7.35f-0)-52.5f, width, height);
    }
    private static void dataInserterRevenue_CurrentAndLast(PDPageContentStream contentStream, PDDocument document, Timestamp timestamp) throws IOException, SQLException{
        PDColor colorBlack = rightColor("#000000");
        PDType1Font textFontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font textFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        int textFontSizeNormal = 10;

        BlockDiagramExample(800, 400, timestamp);

        //Title
        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFontBold, textFontSizeNormal+4);
        contentStream.newLineAtOffset(maxWidth/9.5f, (maxHeight/12)*(3.75f-0));
        contentStream.showText("Current vs Previous Sales Comparison");
        contentStream.endText();



        //Title line bottom
        contentStream.setStrokingColor(colorBlack);
        contentStream.setLineWidth(1.5f);
        contentStream.moveTo(maxWidth/10, (maxHeight/12)*(4.35f-0)-52.5f); // Start point of x-axis
        contentStream.lineTo(maxWidth/1.15f, (maxHeight/12)*(4.35f-0)-52.5f); // End point of x-axis
        contentStream.stroke();

        //Block Diagram
        File file = new File(imgPath + imgPath2 + File.separator + "BlockDiagram_Revenue.png");
        PDImageXObject image = PDImageXObject.createFromFileByExtension(file, document);

        // Get the dimensions of the image
        float imageWidth = image.getWidth();
        float imageHeight = image.getHeight();

        // Define the position and size of the image on the page

        float width = maxWidth/1.4f; // Width of the image
        float height = (width / imageWidth) * (imageHeight); // Height of the image to maintain aspect ratio

        // Draw the image onto the PDF page
        contentStream.drawImage(image, (maxWidth/10)+10, (maxHeight/12)*(1.25f-0)-52.5f, width, height);
    }
    private static void tableCreate(PDPageContentStream contentStream) throws IOException, SQLException {
        PDColor colorBlack = rightColor("#000000");
        PDType1Font textFontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font textFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        int textFontSizeNormal = 10;

        //Title
        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFontBold, textFontSizeNormal+4);
        contentStream.newLineAtOffset(maxWidth/9.5f, (maxHeight/12)*(6.1f-0));
        contentStream.showText("Best Sellers \"Of All Time\" ");
        contentStream.endText();

        //Title line bottom
        contentStream.setStrokingColor(colorBlack);
        contentStream.moveTo(maxWidth/10, (maxHeight/12)*(6.7f-0)-52.5f); // Start point of x-axis
        contentStream.lineTo(maxWidth/1.15f, (maxHeight/12)*(6.7f-0)-52.5f); // End point of x-axis
        contentStream.stroke();

        //Table create
        for (int i = 0; i < 4; i++) {
            contentStream.setStrokingColor(colorBlack);
            contentStream.setLineWidth(1.5f);
            //top
            contentStream.moveTo((maxWidth/10)*1.2f, (maxHeight/24)*(10.8f-(i*0.7f))); // Start point of x-axis
            contentStream.lineTo((maxWidth/1.175f), (maxHeight/24)*(10.8f-(i*0.7f))); // End point of x-axis
        }
        contentStream.moveTo((maxWidth/10)*1.2f, (maxHeight/24)*(11f)+18); // Start point of y-axis
        contentStream.lineTo((maxWidth/1.175f), (maxHeight/24)*(11f)+18); // End point of y-axis
        //Side right
        contentStream.moveTo(maxWidth/1.175f, (maxHeight/24)*(11f)+18); // Start point of y-axis
        contentStream.lineTo(maxWidth/1.175f, (maxHeight/24)*(11f)+18-100); // End point of y-axis
        //Side left
        contentStream.moveTo((maxWidth/10)*1.2f, (maxHeight/24)*(11f)+18); // Start point of y-axis
        contentStream.lineTo((maxWidth/10)*1.2f, (maxHeight/24)*(11f)+18-100); // End point of y-axis
        contentStream.stroke();
        //Middle line
        float[] lineData = {0, 1.04f, 2.55f, 4.05f};
        for (int i = 0; i < lineData.length; i++) {
            for (int j = 0; j < lineData.length; j++) {
                if (j != 0){
                    contentStream.moveTo((maxWidth / 12) * (2f * lineData[j]), (maxHeight/24)*(10.8f-(i*0.7f))); // Start point of y-axis
                    contentStream.lineTo((maxWidth / 12) * (2f * lineData[j]), (maxHeight/24)*(10.8f-(i*0.7f)) + 24.5f); // End point of y-axis
                    contentStream.stroke();
                }
            }
        }
        //Table titles
        String[] tableData = {"Rank", "Name", "Amount", "Price Per Unit"};
        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFont, textFontSizeNormal);
        contentStream.newLineAtOffset((maxWidth/12)*(1.5f), ((maxHeight/24)*(10.8f))+8);
        contentStream.showText(tableData[0]);
        contentStream.newLineAtOffset((maxWidth/12)*(1.5f/2), 0);
        contentStream.showText(tableData[1]);
        contentStream.newLineAtOffset((maxWidth/12)*(1.5f/0.5f), 0);
        contentStream.showText(tableData[2]);
        contentStream.newLineAtOffset((maxWidth/12)*(1.5f/0.5f), 0);
        contentStream.showText(tableData[3]);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFont, textFontSizeNormal);
        contentStream.newLineAtOffset((maxWidth/12)*(1.5f), (maxHeight/24)*(10.8f-(1*0.7f))+8);
        contentStream.showText("1");
        contentStream.endText();
        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFont, textFontSizeNormal);
        contentStream.newLineAtOffset((maxWidth/12)*(1.5f), (maxHeight/24)*(10.8f-(2*0.7f))+8);
        contentStream.showText("2");
        contentStream.endText();
        contentStream.beginText();
        contentStream.setNonStrokingColor(colorBlack);
        contentStream.setFont(textFont, textFontSizeNormal);
        contentStream.newLineAtOffset((maxWidth/12)*(1.5f), (maxHeight/24)*(10.8f-(3*0.7f))+8);
        contentStream.showText("3");
        contentStream.endText();

        ArrayList<Integer> values = DBSalereportData.getBestSellers();

        if (values.isEmpty()) {
            return;
        }

        ArrayList<Integer> values2 = new ArrayList<>();
        int counter = 1;

        for (int i = 0; i < 3; i++) {
            contentStream.beginText();
            contentStream.setNonStrokingColor(colorBlack);
            contentStream.setFont(textFont, textFontSizeNormal);
            contentStream.newLineAtOffset((maxWidth / 12) * (2f * lineData[2])+7, (maxHeight/24)*(10.8f-((i+1)*0.7f))+8);
            contentStream.showText(String.valueOf(values.get(counter)));
            contentStream.endText();
            counter+=2;
        }

        int counter3 = 1;
        int counter4 = 0;
        int id_1 = values.get(0);
        int id_2 = values.get(2);
        int id_3 = values.get(4);

        ArrayList<Object> values3 = DBSalereportData.getProductNamesAndPrices(id_1, id_2, id_3);
        for (int i = 0; i < 3; i++) {
            contentStream.beginText();
            contentStream.setNonStrokingColor(colorBlack);
            contentStream.setFont(textFont, textFontSizeNormal);
            contentStream.newLineAtOffset((maxWidth / 12) * (2f * lineData[1])+7, (maxHeight/24)*(10.8f-((i+1)*0.7f))+8);
            contentStream.showText(String.valueOf(String.valueOf(values3.get(counter4))));
            contentStream.endText();

            contentStream.beginText();
            contentStream.setNonStrokingColor(colorBlack);
            contentStream.setFont(textFont, textFontSizeNormal);
            contentStream.newLineAtOffset((maxWidth / 12) * (2f * lineData[3])+7, (maxHeight/24)*(10.8f-((i+1)*0.7f))+8);
            contentStream.showText(String.valueOf(String.valueOf(values3.get(counter3))));
            contentStream.endText();

            counter3+=2;
            counter4+=2;
        }


        /*
        contentStream.setStrokingColor(colorBlack);
        contentStream.setNonStrokingColor(colorBox);
        contentStream.addRect((maxWidth/10)*1.2f, ((maxHeight/24)*(11f)+18-100), (maxWidth/1.175f), (maxHeight/24)*(11f)+18);
        contentStream.fill();
        contentStream.stroke();
        */

    }



    private static float sizeFit(String size,boolean doubleYesOrNo){
        float numberOfCharacters = size.length();

        if (doubleYesOrNo){
            numberOfCharacters = numberOfCharacters - 6.5f;
        }
        if (numberOfCharacters !=1){
            if (numberOfCharacters <=12 && !doubleYesOrNo){
                return numberOfCharacters /23;
            }else if (numberOfCharacters <=7.5 && doubleYesOrNo){
                return numberOfCharacters /23;
            }
        }
        return 0;
    }
    private static void createAndSaveChartRevenue(int inputWidth,int inputHeight, Timestamp timestamp) throws SQLException {

        ArrayList<Double> values = DBSalereportData.getRevenuePerDay(timestamp);
        double[] data = new double[values.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1; // Fill data with sequential values starting from 1
        }


        // Create a dataset
        XYSeries series = new XYSeries("Revenue");
        for (int i = 0; i < data.length; i++) {
            series.add(data[i], values.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);


        // Create a chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "",
                "Day",
                "Amount",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Customize chart appearance
        Font labelFont = new Font("Arial", Font.BOLD, 15);
        Font labelFont2 = new Font("Arial", Font.PLAIN, 13);
        chart.setBackgroundPaint(convertColor(Color.TRANSPARENT)); // Set background paint
        chart.getTitle().setPaint(convertColor(Color.BLACK)); // Set title paint

        LegendTitle legend = chart.getLegend();
        legend.setPosition(TOP);
        legend.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        legend.setItemPaint(convertColor(Color.BLACK));
        legend.setItemFont(labelFont);
        legend.setBorder(0.0,0.0,1.25,0.0);
        legend.setBackgroundPaint(convertColor(Color.TRANSPARENT));

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(convertColor(Color.WHITE)); // Set plot background paint
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(convertColor(Color.DARKGRAY)); // Set range gridline paint
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(convertColor(Color.DARKGRAY)); // Set domain gridline paint

        plot.getDomainAxis().setLabelFont(labelFont);
        plot.getDomainAxis().setLabelPaint(convertColor(Color.BLACK)); // Set domain axis label paint
        plot.getDomainAxis().setTickLabelFont(labelFont2);
        plot.getDomainAxis().setTickLabelPaint(convertColor(Color.BLACK)); // Set domain axis tick label paint

        plot.getRangeAxis().setLabelFont(labelFont);
        plot.getRangeAxis().setLabelPaint(convertColor(Color.BLACK)); // Set range axis label paint
        plot.getRangeAxis().setTickLabelFont(labelFont2);
        plot.getRangeAxis().setTickLabelPaint(convertColor(Color.BLACK));

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, (convertColor(Color.RED))); // Set line paint
        renderer.setSeriesStroke(0, new BasicStroke(3.0f)); // Set line thickness
        plot.setRenderer(renderer);

        // Save chart as an image file
        String filePath = imgPath + imgPath2 + File.separator + "Revenue_Per_Day.png"; // Specify the full file path including the file name and extension
        File chartFile = new File(filePath);
        try {
            ChartUtilities.saveChartAsPNG(chartFile, chart, inputWidth, inputHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void createAndSaveChartOrder(int inputWidth,int inputHeight, Timestamp timestamp) throws SQLException {

        ArrayList<Double> values = DBSalereportData.getAvgAmountPerOrderPerDay(timestamp);
        double[] data = new double[values.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1; // Fill data with sequential values starting from 1
        }


        // Create a dataset
        XYSeries series = new XYSeries("Amount");
        for (int i = 0; i < data.length; i++) {
            series.add(data[i], values.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);


        // Create a chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "",
                "Day",
                "Amount",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Customize chart appearance
        Font labelFont = new Font("Arial", Font.BOLD, 15);
        Font labelFont2 = new Font("Arial", Font.PLAIN, 13);
        chart.setBackgroundPaint(convertColor(Color.TRANSPARENT)); // Set background paint
        chart.getTitle().setPaint(convertColor(Color.BLACK)); // Set title paint

        LegendTitle legend = chart.getLegend();
        legend.setPosition(TOP);
        legend.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        legend.setItemPaint(convertColor(Color.BLACK));
        legend.setItemFont(labelFont);
        legend.setBorder(0.0,0.0,1.25,0.0);
        legend.setBackgroundPaint(convertColor(Color.TRANSPARENT));

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(convertColor(Color.WHITE)); // Set plot background paint
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(convertColor(Color.DARKGRAY)); // Set range gridline paint
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(convertColor(Color.DARKGRAY)); // Set domain gridline paint

        plot.getDomainAxis().setLabelFont(labelFont);
        plot.getDomainAxis().setLabelPaint(convertColor(Color.BLACK)); // Set domain axis label paint
        plot.getDomainAxis().setTickLabelFont(labelFont2);
        plot.getDomainAxis().setTickLabelPaint(convertColor(Color.BLACK)); // Set domain axis tick label paint

        plot.getRangeAxis().setLabelFont(labelFont);
        plot.getRangeAxis().setLabelPaint(convertColor(Color.BLACK)); // Set range axis label paint
        plot.getRangeAxis().setTickLabelFont(labelFont2);
        plot.getRangeAxis().setTickLabelPaint(convertColor(Color.BLACK));

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, (convertColor(Color.RED))); // Set line paint
        renderer.setSeriesStroke(0, new BasicStroke(3.0f)); // Set line thickness
        plot.setRenderer(renderer);

        // Save chart as an image file
        String filePath = imgPath + imgPath2 + File.separator + "Avg_Amount_Per_Order_Per_Day.png"; // Specify the full file path including the file name and extension
        File chartFile = new File(filePath);
        try {
            ChartUtilities.saveChartAsPNG(chartFile, chart, inputWidth, inputHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void createAndSaveChartUser(int inputWidth,int inputHeight, Timestamp timestamp) throws SQLException {

        ArrayList<Integer> values = DBSalereportData.getNewUserPerDay(timestamp);
        int[] data = new int[values.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1; // Fill data with sequential values starting from 1
        }


        // Create a dataset
        XYSeries series = new XYSeries("Users");
        for (int i = 0; i < data.length; i++) {
            series.add(data[i], values.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);


        // Create a chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "",
                "Day",
                "Amount",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Customize chart appearance
        Font labelFont = new Font("Arial", Font.BOLD, 15);
        Font labelFont2 = new Font("Arial", Font.PLAIN, 13);
        chart.setBackgroundPaint(convertColor(Color.TRANSPARENT)); // Set background paint
        chart.getTitle().setPaint(convertColor(Color.BLACK)); // Set title paint

        LegendTitle legend = chart.getLegend();
        legend.setPosition(TOP);
        legend.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        legend.setItemPaint(convertColor(Color.BLACK));
        legend.setItemFont(labelFont);
        legend.setBorder(0.0,0.0,1.25,0.0);
        legend.setBackgroundPaint(convertColor(Color.TRANSPARENT));

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(convertColor(Color.WHITE)); // Set plot background paint
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(convertColor(Color.DARKGRAY)); // Set range gridline paint
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(convertColor(Color.DARKGRAY)); // Set domain gridline paint

        plot.getDomainAxis().setLabelFont(labelFont);
        plot.getDomainAxis().setLabelPaint(convertColor(Color.BLACK)); // Set domain axis label paint
        plot.getDomainAxis().setTickLabelFont(labelFont2);
        plot.getDomainAxis().setTickLabelPaint(convertColor(Color.BLACK)); // Set domain axis tick label paint

        plot.getRangeAxis().setLabelFont(labelFont);
        plot.getRangeAxis().setLabelPaint(convertColor(Color.BLACK)); // Set range axis label paint
        plot.getRangeAxis().setTickLabelFont(labelFont2);
        plot.getRangeAxis().setTickLabelPaint(convertColor(Color.BLACK));

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, (convertColor(Color.RED))); // Set line paint
        renderer.setSeriesStroke(0, new BasicStroke(3.0f)); // Set line thickness
        plot.setRenderer(renderer);

        // Save chart as an image file
        String filePath = imgPath + imgPath2 + File.separator + "New_Users_Per_Day.png"; // Specify the full file path including the file name and extension
        File chartFile = new File(filePath);
        try {
            ChartUtilities.saveChartAsPNG(chartFile, chart, inputWidth, inputHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void BlockDiagramExample(int inputWidth,int inputHeight, Timestamp timestamp) throws SQLException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        calendar.add(Calendar.MONTH, -1);
        long lastMonthTimestamp = calendar.getTimeInMillis();
        Timestamp lastMonthTimestampSql = new Timestamp(lastMonthTimestamp);
        // Create a dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(DBSalereportData.getRevenue(lastMonthTimestampSql), "Last Month", "");
        dataset.addValue(DBSalereportData.getRevenue(timestamp), "Current Month", "");

        // Create a bar chart
        JFreeChart chart = ChartFactory.createBarChart(
                "", // Title
                "", // X-axis label
                "Revenue", // Y-axis label
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );

        // Customize the chart (optional)

        Font labelFont = new Font("Arial", Font.BOLD, 15);
        Font labelFont2 = new Font("Arial", Font.PLAIN, 13);
        chart.setBackgroundPaint(convertColor(Color.TRANSPARENT)); // Set background paint
        chart.getTitle().setPaint(convertColor(Color.BLACK)); // Set title paint

        LegendTitle legend = chart.getLegend();
        legend.setPosition(TOP);
        legend.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        legend.setItemPaint(convertColor(Color.BLACK));
        legend.setItemFont(labelFont);
        legend.setBorder(0.0,0.0,1.25,0.0);
        legend.setBackgroundPaint(convertColor(Color.TRANSPARENT));

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(convertColor(Color.WHITE)); // Set plot background paint
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(convertColor(Color.DARKGRAY)); // Set range gridline paint
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(convertColor(Color.DARKGRAY)); // Set domain gridline paint

        ((BarRenderer) plot.getRenderer()).setGradientPaintTransformer(null);

        plot.getRenderer().setSeriesPaint(0, convertColor(Color.rgb(239, 70, 55))); // Change color of the first series
        plot.getRenderer().setSeriesPaint(1, convertColor(Color.rgb(0, 102, 204))); // Change color of the second series

        plot.getRenderer().setSeriesOutlineStroke(0, new BasicStroke(0)); // Set outline stroke to zero for the first series
        plot.getRenderer().setSeriesOutlineStroke(1, new BasicStroke(0)); // Set outline stroke to zero for the second series
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        ((BarRenderer) plot.getRenderer()).setDrawBarOutline(false);
        ((BarRenderer) plot.getRenderer()).setShadowVisible(false);


        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLabelFont(labelFont);
        domainAxis.setLabelPaint(convertColor(Color.BLACK)); // Set domain axis label paint
        domainAxis.setTickLabelFont(labelFont2);
        domainAxis.setTickLabelPaint(convertColor(Color.BLACK)); // Set domain axis tick label paint

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setLabelFont(labelFont);
        rangeAxis.setLabelPaint(convertColor(Color.BLACK)); // Set range axis label paint
        rangeAxis.setTickLabelFont(labelFont2);
        rangeAxis.setTickLabelPaint(convertColor(Color.BLACK)); // Set range axis tick label paint

        // Save chart as an image file
        String filePath = imgPath + imgPath2 + File.separator + "BlockDiagram_Revenue.png"; // Specify the full file path including the file name and extension
        File chartFile = new File(filePath);
        try {
            ChartUtilities.saveChartAsPNG(chartFile, chart, inputWidth, inputHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static java.awt.Color convertColor(Color fxColor) {
        return new java.awt.Color((float) fxColor.getRed(),
                (float) fxColor.getGreen(),
                (float) fxColor.getBlue(),
                (float) fxColor.getOpacity());
    }
    private static PDColor rightColor(String hexColor){
        float red = Integer.valueOf(hexColor.substring(1, 3), 16) / 255f;
        float green = Integer.valueOf(hexColor.substring(3, 5), 16) / 255f;
        float blue = Integer.valueOf(hexColor.substring(5, 7), 16) / 255f;
        return new PDColor(new float[]{red, green, blue}, PDDeviceRGB.INSTANCE);
    }


}