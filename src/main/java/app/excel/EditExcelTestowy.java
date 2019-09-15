package app.excel;

import app.repositories.ResultTiles;
import app.repositories.UsersRepo;
import org.springframework.stereotype.Service;

@Service
public class EditExcelTestowy {

    private ResultTiles resultTiles;
    private UsersRepo usersRepo;

   /* @Autowired
    public void editFile(ResultTiles resultTiles, UsersRepo usersRepo) throws IOException {
        this.resultTiles = resultTiles;
        this.usersRepo = usersRepo;

        String excel = "C:\\UsersRepo\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\templates\\excel.xlsx";
        String excelEdytowalny = "C:\\UsersRepo\\Arkadiusz Koszela\\Desktop\\demo\\projekt_zaliczeniowy\\springapp\\src\\main\\resources\\templates\\Edytowalny.xlsx";

        FileInputStream fis = new FileInputStream(new File(excel));
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        sheet.setDisplayGridlines(true);
        sheet.setPrintGridlines(true);
        //This data needs to be written (Object[])
        Iterable<EntityResultTiles> wynikiEntityIterable = resultTiles.findAll();

        Map<String, Object[]> entityMap = new TreeMap<>();
        wynikiEntityIterable.forEach(getPurchase -> entityMap.put(getPurchase.getName(), new Object[]{getPurchase.getName(), getPurchase.getPriceAfterDiscount(), getPurchase.getPurchasePrice(), getPurchase.getProfit()}));

        LoadUser wprowadzDane = new LoadUser();
        String nameISurname = null;
        EntityUser user = null;
        if(wprowadzDane.comboBoxUsers != null) {
            nameISurname = wprowadzDane.comboBoxUsers.getValue();

            String[] rozdzielone = nameISurname.split(" ");

            user = usersRepo.findUsersEntityByNameAndSurnameEquals(rozdzielone[0], rozdzielone[1]);
            // DOPISANIE IMIENIA
            Cell editName = null;
            editName = sheet.getRow(2).getCell(1);
            editName.setCellValue(user.getName() + " " + user.getSurname());

            // DOPISANIE ADRESU
            Cell editAdres = null;
            editAdres = sheet.getRow(3).getCell(1);
            editAdres.setCellValue(user.getAdress());

            // DOPISANIE NUMERU
            Cell editNumer = null;
            editNumer = sheet.getRow(4).getCell(1);
            editNumer.setCellValue(user.getTelephoneNumber());
        }

        // STWORZYĆ BAZE KLIENTOW WRAZ Z ICH OFERTAMI ABY MOC SOBIE WYBRAC KLIENTA I PRZYPISAC WSZYSTKIE WARTOSCI KTORE BYLY WPISYWANE
        // POWROT DO UZUPELNIANIA KLIENTA

        Set<String> keyset = entityMap.keySet();
        int poziom = 11;
        for (String key : keyset) {
            Row row = sheet.createRow(poziom++);
            Object[] objArr = entityMap.get(key);
            int pion = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(pion++);
                if (obj instanceof String)
                    cell.setCellValue((String) obj);
                else if (obj instanceof Integer)
                    cell.setCellValue((Integer) obj);
            }
        }
        try {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File(excelEdytowalny));
            workbook.write(out);
            out.close();
            System.out.println(excel + " written successfully on disk.");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
*/
}
