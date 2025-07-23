package	com.testcode;
import static org.testng.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataDrivenExcelTest {

    @DataProvider(name = "mathData")
    public Object[][] mathDataProvider() throws IOException {
        List<Object[]> testData = new ArrayList<>();
        FileInputStream fis = new FileInputStream("datadriven.xlsx");
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        // Skip header row
        boolean firstRow = true;
        for (Row row : sheet) {
            if (firstRow) {
                firstRow = false;
                continue;
            }
            double number1 = row.getCell(0).getNumericCellValue();
            double number2 = row.getCell(1).getNumericCellValue();
            double expectedAdd = row.getCell(2).getNumericCellValue();
            double expectedSub = row.getCell(3).getNumericCellValue();
            double expectedMul = row.getCell(4).getNumericCellValue();
            double expectedDiv = row.getCell(5).getNumericCellValue();
            testData.add(new Object[]{number1, number2, expectedAdd, expectedSub, expectedMul, expectedDiv});
        }
        workbook.close();
        fis.close();
        return testData.toArray(new Object[0][]);
    }

    @Test(dataProvider = "mathData")
    public void c107_testAddition(double number1, double number2, double expectedAdd, double expectedSub, double expectedMul, double expectedDiv) {
        assertEquals(number1 + number2, expectedAdd,  "Addition failed");
    }

    @Test(dataProvider = "mathData")
    public void c108_testSubtraction(double number1, double number2, double expectedAdd, double expectedSub, double expectedMul, double expectedDiv) {
        assertEquals(number1 - number2, expectedSub, "Subtraction failed");
    }

    @Test(dataProvider = "mathData")
    public void c109_testMultiplication(double number1, double number2, double expectedAdd, double expectedSub, double expectedMul, double expectedDiv) {
        assertEquals(number1 * number2, expectedMul, "Multiplication failed");
    }

    @Test(dataProvider = "mathData")
    public void c110_testDivision(double number1, double number2, double expectedAdd, double expectedSub, double expectedMul, double expectedDiv) {
        if (number2 != 0) {
           assertEquals(number1 / number2, expectedDiv, "Division failed");
        }
    }
}