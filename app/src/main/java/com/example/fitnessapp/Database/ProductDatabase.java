package com.example.fitnessapp.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.fitnessapp.Database.Dao.CaloricLimitDao;
import com.example.fitnessapp.Database.Dao.CategoryDao;
import com.example.fitnessapp.Database.Dao.ManufacturerDao;
import com.example.fitnessapp.Database.Dao.MealCategoryDao;
import com.example.fitnessapp.Database.Dao.MealDao;
import com.example.fitnessapp.Database.Dao.MealProductDao;
import com.example.fitnessapp.Database.Dao.MeasureUnitDao;
import com.example.fitnessapp.Database.Dao.ProductCategoryDao;
import com.example.fitnessapp.Database.Dao.ProductDao;
import com.example.fitnessapp.Database.Dao.ProductDetailsDao;
import com.example.fitnessapp.Database.Models.CaloricLimit;
import com.example.fitnessapp.Database.Models.Category;
import com.example.fitnessapp.Database.Models.Converters;
import com.example.fitnessapp.Database.Models.Manufacturer;
import com.example.fitnessapp.Database.Models.Meal;
import com.example.fitnessapp.Database.Models.MealCategory;
import com.example.fitnessapp.Database.Models.MealProduct;
import com.example.fitnessapp.Database.Models.MeasureUnit;
import com.example.fitnessapp.Database.Models.Product;
import com.example.fitnessapp.Database.Models.ProductCategory;
import com.example.fitnessapp.Database.Models.ProductDetails;
import com.example.fitnessapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
                Meal.class,
                MealCategory.class,
                MealProduct.class,
                Category.class,
                Product.class,
                Manufacturer.class,
                MeasureUnit.class,
                ProductCategory.class,
                ProductDetails.class,
                CaloricLimit.class
            },
            version = 5,
            autoMigrations = {
                @AutoMigration(from = 3, to = 5)
            },
            exportSchema = true)
@TypeConverters({Converters.class})
public abstract class ProductDatabase extends RoomDatabase {
    private static ProductDatabase databaseInstance;
    public static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    public abstract ProductDao productDao();
    public abstract MealDao mealDao();
    public abstract MealCategoryDao mealCategoryDao();
    public abstract CaloricLimitDao caloricLimitDao();
    public abstract MealProductDao mealProductDao();
    public abstract CategoryDao categoryDao();
    public abstract ManufacturerDao manufacturerDao();
    public abstract MeasureUnitDao measureUnitDao();
    public abstract ProductDetailsDao productDetailsDao();
    public abstract ProductCategoryDao productCategoryDao();
    private static Context _context;

    public static ProductDatabase getDatabase(final Context context){
        if(databaseInstance == null){
            _context = context;
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    ProductDatabase.class, "my_db")
                    .addCallback(newCallback1)
                    .build();
        }
        return databaseInstance;
    }

    // Seedowanie bazy przy tworzeniu - ponowna instalacja aplikacji i jej uruchomienie powoduje wywołanie tej metody
    private static final RoomDatabase.Callback newCallback1 = new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(()->{

                // Pobranie Dao
                CategoryDao categoryDAO= databaseInstance.categoryDao();
                ManufacturerDao manufacturerDAO= databaseInstance.manufacturerDao();
                MeasureUnitDao measureUnitDAO = databaseInstance.measureUnitDao();
                ProductCategoryDao productCategoryDAO = databaseInstance.productCategoryDao();
                ProductDao productDAO = databaseInstance.productDao();
                ProductDetailsDao productDetailsDAO = databaseInstance.productDetailsDao();
                MealDao mealDao = databaseInstance.mealDao();
                MealCategoryDao mealCategoryDao = databaseInstance.mealCategoryDao();
                MealProductDao mealProductDao = databaseInstance.mealProductDao();

                // Wyczyszczenie tabel i sekwencji
                /*categoryDAO.clearTable();
                categoryDAO.resetTableId();
                manufacturerDAO.clearTable();
                manufacturerDAO.resetTableId();
                measureUnitDAO.clearTable();
                measureUnitDAO.resetTableId();
                productCategoryDAO.clearTable();
                productCategoryDAO.resetTableId();
                productDAO.clearTable();
                productDAO.resetTableId();
                productDetailsDAO.clearTable();
                productDetailsDAO.resetTableId();*/

                // Dodanie katelorii posiłków
                MealCategory mealCategory1 = new MealCategory(_context.getResources().getString(R.string.breakfast));
                MealCategory mealCategory2 = new MealCategory(_context.getResources().getString(R.string.second_breakfast));
                MealCategory mealCategory3 = new MealCategory(_context.getResources().getString(R.string.lunch));
                MealCategory mealCategory4 = new MealCategory(_context.getResources().getString(R.string.tea));
                MealCategory mealCategory5 = new MealCategory(_context.getResources().getString(R.string.dinner));
                MealCategory mealCategory6 = new MealCategory(_context.getResources().getString(R.string.snack));
                mealCategoryDao.insert(mealCategory1);
                mealCategoryDao.insert(mealCategory2);
                mealCategoryDao.insert(mealCategory3);
                mealCategoryDao.insert(mealCategory4);
                mealCategoryDao.insert(mealCategory5);
                mealCategoryDao.insert(mealCategory6);

                // Dodanie kategorii produktów
                Category category = new Category("Napój");
                long categoryId = categoryDAO.insert(category);
                Category category1 = new Category("Nabiał");
                long categoryId1 = categoryDAO.insert(category1);

                // Dodanie producenta
                Manufacturer manufacturer = new Manufacturer("Hortex");
                long manufacturerId = manufacturerDAO.insert(manufacturer);

                // Dodanie jednostek miary
                MeasureUnit measureUnit1 = new MeasureUnit("ml");
                long unitId1 = measureUnitDAO.insert(measureUnit1);

                MeasureUnit measureUnit2 = new MeasureUnit("szt");
                long unitId2 = measureUnitDAO.insert(measureUnit2);

                MeasureUnit measureUnit3 = new MeasureUnit("g");
                long unitId3 = measureUnitDAO.insert(measureUnit3);

                // Dodanie produktu
                Product product = new Product("Sok", manufacturerId, unitId1);
                long productId = productDAO.insert(product);

                // Dodanie szczegółów produktu
                ProductDetails productDetails = new ProductDetails(30.9f, 10.2f, 2.9f, 3.1f, productId);
                long detailsId = productDetailsDAO.insert(productDetails);

                // Dodanie kateogii do produktu
                List<Long> categoryIdsList = new ArrayList<>();
                categoryIdsList.add(categoryId);
                for(long catID : categoryIdsList){
                    ProductCategory product_category = new ProductCategory(productId, catID);
                    productCategoryDAO.insert(product_category);
                }
            });
        }
    };
}
