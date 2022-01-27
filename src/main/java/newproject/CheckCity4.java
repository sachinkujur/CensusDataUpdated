package newproject;

import excelExportAndFileIO.WriteExcelFile;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CheckCity4 {
    //
    private static WebDriver driver;
    private static WebDriverWait wait;
    public String ActualState;
    public String ActualCity;
//    public String PinCode;
    public String State;
    public String City;
    public String QuickFacts = "";

    public static void main(String[] args) throws Exception {
//        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

//        create object of chrome options
        ChromeOptions options = new ChromeOptions();

//        add the headless argument
        options.addArguments("headless");

//        pass the options parameter in the Chrome driver declaration
        driver = new ChromeDriver(options);

        //set the path of the Gecko driver as per the location on local machine
//        System.setProperty("webdriver.gecko.driver", "C:\\Users\\Prefme_Matrix\\OneDrive\\Documents\\geckodriver.exe");
//
//        //Set Firefox Headless mode as TRUE
//        FirefoxOptions options = new FirefoxOptions();
//        options.setHeadless(true);
//
//        //pass the options parameter in the Firefox driver declaration
//        driver = new FirefoxDriver(options);


//        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 20);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        String baseUrl = "https://www.google.com/";
        driver.get(baseUrl);

        getState();

        driver.quit();
    }

    public void readExcel(String filePath, String fileName, String sheetName) throws Exception {

        //Create an object of File class to open xlsx file

        File file = new File(filePath + "\\" + fileName);

        //Create an object of FileInputStream class to read excel file

        FileInputStream inputStream = new FileInputStream(file);

        Workbook Workbook = null;

        //Find the file extension by splitting file name in substring  and getting only extension name

        String fileExtensionName = fileName.substring(fileName.indexOf("."));

        //Check condition if the file is xlsx file

        if (fileExtensionName.equals(".xlsx")) {

            //If it is xlsx file then create object of XSSFWorkbook class

            Workbook = new XSSFWorkbook(inputStream);

        }

        //Check condition if the file is xls file
        else if (fileExtensionName.equals(".xls")) {

            //If it is xls file then create object of HSSFWorkbook class

            Workbook = new HSSFWorkbook(inputStream);

        }

        //Read sheet inside the workbook by its name

        Sheet Sheet = Workbook.getSheet(sheetName);

        //Find number of rows in excel file

        int rowCount = Sheet.getLastRowNum() - Sheet.getFirstRowNum();

        //Create a loop over all the rows of excel file to read it

        for (int i = 0; i < rowCount + 1; i++) {

            Row row = Sheet.getRow(i);

            //Create a loop to print cell values in a row


            //Print Excel data in console
            int j = 0;

            State = row.getCell(j).getStringCellValue();
            City = row.getCell(j + 1).getStringCellValue();

            try {
                driver.findElement(By.xpath("//input[@title='Search']")).click();
                driver.findElement(By.xpath("//input[@title='Search']")).sendKeys(City + " " + State);
                driver.findElement(By.xpath("//input[@title='Search']")).sendKeys(Keys.ENTER);
            } catch (Exception e){
                driver.findElement(By.xpath("(//input[@aria-label='Search'])[1]")).clear();
                driver.findElement(By.xpath("(//input[@aria-label='Search'])[1]")).sendKeys(City + " " + State);
                driver.findElement(By.xpath("(//input[@aria-label='Search'])[1]")).sendKeys(Keys.ENTER);
            }
              Thread.sleep(2000);

            List<WebElement> ActualState = driver.findElements(By.className("tF2Cxc"));
            int lastLink = ActualState.size() - 1;
            int i1 = 0;
            for (WebElement k : ActualState) {
                i1++;
                String StateName = k.getText();
                if (StateName.contains("villageinfo.in")) {
                    if (StateName.contains(State)){
                        if (StateName.contains(City)){
//                            System.out.println(City + " is correct");
                            WriteExcelFile.print(City + " is correct");
                            break;
                        } else {
//                            System.out.println(City + " - name is different");
                            WriteExcelFile.print(City + " - name is different");
                             break;
                        }
                    } else {
//                        System.out.println(City + " not correct");
                        WriteExcelFile.print(City + " not correct");
//                        continue;
                        }
                    } else {
                    if (StateName.contains("indiagrowing.com")){
                        if (StateName.contains(State)){
                            if (StateName.contains(City)){
//                                System.out.println(City + " is correct");
                                WriteExcelFile.print(City + " is correct");
                            } else {
//                                System.out.println(City + " - name is different");
                                WriteExcelFile.print(City + " - name is different");
                            }
                        } else {
//                            System.out.println(City + " not correct");
                            WriteExcelFile.print(City + " not correct");
                        }
                        break;
                    }

                    if (i1 == lastLink){
//                        System.out.println("City not found");
                        WriteExcelFile.print("City not found");
                    }
                    continue;
                }

                break;
            }
            }

    }




        //Main function is calling readExcel function to read data from excel file

        public static void getState () throws Exception {

            //Create an object of ReadExcelFile class

            CheckCity4 objExcelFile = new CheckCity4();

            //Prepare the path of excel file

            String filePath = "C:\\Users\\Prefme_Matrix\\IdeaProjects\\CensusData\\src\\main\\java\\excelExportAndFileIO";

            //Call read file method of the class to read data

            objExcelFile.readExcel(filePath, "ImportExcel.xlsx", "Sheet1");

        }

    }
