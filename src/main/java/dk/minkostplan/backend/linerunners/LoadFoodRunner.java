package dk.minkostplan.backend.linerunners;

import dk.minkostplan.backend.entities.Food;
import dk.minkostplan.backend.repository.FoodRepository;
import dk.minkostplan.backend.repository.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

//@Component
//@Slf4j
public class LoadFoodRunner implements CommandLineRunner {

    private final RecipeRepository recipeRepository;
    private final FoodRepository foodRepository;

    @Autowired
    public LoadFoodRunner(RecipeRepository recipeRepository, FoodRepository foodRepository) {
        this.recipeRepository = recipeRepository;
        this.foodRepository = foodRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
      ClassPathResource resource = new ClassPathResource("Frida20190802dav3.xlsx");

      Workbook workbook = new XSSFWorkbook(resource.getInputStream());
      Sheet sheet = workbook.getSheetAt(1);

      for(int i = 2; i < sheet.getLastRowNum(); i++){
          Row currentRow = sheet.getRow(i);
          String foodType = currentRow.getCell(1).getStringCellValue();
          String foodName = currentRow.getCell(2).getStringCellValue();

          try{
              float foodKj =  Float.parseFloat(currentRow.getCell(4).toString().equalsIgnoreCase("iv") ? "0" : currentRow.getCell(4).toString());
              //float foodKcal =  Float.parseFloat(currentRow.getCell(5).toString().equalsIgnoreCase("iv") ? "0" : currentRow.getCell(5).toString());

              float foodProtein =  Float.parseFloat(currentRow.getCell(8).toString().equalsIgnoreCase("iv") ? "0" : currentRow.getCell(8).toString());
              float foodCarbs =  Float.parseFloat(currentRow.getCell(10).toString().equalsIgnoreCase("iv") ? "0" : currentRow.getCell(10).toString());
              float foodFat =  Float.parseFloat(currentRow.getCell(15).toString().equalsIgnoreCase("iv") ? "0" : currentRow.getCell(15).toString());
              float foodKcal =
                      foodProtein * 4
                      + foodCarbs * 4
                      + foodFat * 9;

              float foodAddedSugars =  Float.parseFloat(currentRow.getCell(13).toString().equalsIgnoreCase("iv") ? "0" : currentRow.getCell(13).toString());
              float foodFibers =  Float.parseFloat(currentRow.getCell(14).toString().equalsIgnoreCase("iv") ? "0" : currentRow.getCell(14).toString());
              final Food food = new Food(foodType, foodName, foodKj, foodKcal, foodProtein, foodCarbs, foodFat, foodAddedSugars, foodFibers);

              foodRepository.save(food);
          }catch(NumberFormatException e){
              System.exit(0);
          }





      }
    }
}
