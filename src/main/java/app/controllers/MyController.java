package app.controllers;

import app.DAOs.DaoTiles;
import app.excel.EditExcel;
import app.repositories.ResultTiles;
import app.repositories.UsersRepo;
import com.itextpdf.text.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@Controller
public class MyController {

    @Autowired
    private DaoTiles daoTiles;

    @Autowired
    ResultTiles resultTiles;

    @Autowired
    UsersRepo usersRepo;

    String PDF = "C:\\UsersRepo\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\java\\pdf.pdf";

    /*@RequestMapping(value = "/getpdf"*//*, method=RequestMethod.POST*//*)
    public ResponseEntity<byte[]> getPDF(*//*@RequestBody String json*//*) throws IOException {
        // convert JSON to Employee
        *//*Employee emp = convertSomehow(json);*//*

        // generate the file
        GeneratePdfReport.generatePDF();

        // retrieve contents of "C:/tmp/report.pdf" that were written in showHelp
        File file = new File(PDF);
        byte[] contents = Files.readAllBytes(file.toPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Here you have to set the actual filename of your pdf
        String filename = "pdf.xls";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }*/

    @RequestMapping(value = "/excel"/*, method=RequestMethod.POST*/)
    public ResponseEntity<byte[]> getExcel(/*@RequestBody String json*/) throws IOException {
        // convert JSON to Employee
        /*Employee emp = convertSomehow(json);*/
        EditExcel editExcel = new EditExcel();
        /*editExcel.editFile(resultTiles, usersRepo);*/
        // generate the file
        String excelEdytowalny = "C:\\UsersRepo\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\templates\\Edytowalny.xlsx";

        // retrieve contents of "C:/tmp/report.pdf" that were written in showHelp
        File file = new File(excelEdytowalny);
        byte[] contents = Files.readAllBytes(file.toPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        // Here you have to set the actual filename of your pdf
        String filename = "excel.xlsx";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        System.out.println("Jestem w My Controller");
        return response;
    }

    @RequestMapping(value = "pdf")
    public void get(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String HTML = "C:\\UsersRepo\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\templates\\poprawiony.html";

        String PDF = "C:\\UsersRepo\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\java\\pdf.pdf";

        String CSS = "C:\\UsersRepo\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\templates\\style.css";
        Document document = null;

        // step 2
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        EditExcel editExcel = new EditExcel();
        /*editExcel.editFile(resultTiles, usersRepo);*/
            /*document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document,
                    baos);
            document.open();

            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new FileInputStream(new File(HTML)));
            document.close();*/
        System.out.println("Done.");
        // setting some response headers
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control",
                "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        // setting the content type
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        // the contentlength
        response.setContentLength(baos.size());
        // write ByteArrayOutputStream to the ServletOutputStream
        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
        os.close();
    }

    @RequestMapping(path = "feedProduktData")
    public void setDatainDB() {
        daoTiles.save("C:\\UsersRepo\\Arkadiusz Koszela\\Desktop\\demo\\springapp\\src\\main\\resources\\Bogen Innovo 10.csv");
    }

    @RequestMapping(path = "feedProduktData2")
    public void setDatainDB2() {
        daoTiles.save("C:\\UsersRepo\\Arkadiusz Koszela\\Desktop\\demo\\springapp\\src\\main\\resources\\Bogen Innovo 12.csv");
    }
}
