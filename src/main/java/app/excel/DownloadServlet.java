package app.excel;

import app.repositories.ResultTiles;
import app.repositories.UsersRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CityServlet", urlPatterns = "/download")
public class DownloadServlet extends HttpServlet {

    private static final long serialVersionUID = 2067115822080269398L;

    public void init() throws ServletException {
        // Do nothing
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            UsersRepo usersRepo = null;
            ResultTiles resultTiles = null;
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=src\\main\\resources\\templates\\Edytowalny.xlsx");
            EditExcel2 editExcel2 = new EditExcel2();
            /*XSSFWorkbook workbook = editExcel2.editFile(resultTiles, usersRepo);*/
            /*workbook.write(response.getOutputStream());*/
        } catch (Exception e) {
            throw new ServletException("Exception in DownLoad Excel Servlet", e);
        }
    }

}