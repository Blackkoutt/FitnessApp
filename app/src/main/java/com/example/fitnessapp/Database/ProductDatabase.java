package com.example.fitnessapp.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.fitnessapp.Database.Dao.CategoryDao;
import com.example.fitnessapp.Database.Dao.ManufacturerDao;
import com.example.fitnessapp.Database.Dao.MeasureUnitDao;
import com.example.fitnessapp.Database.Dao.ProductCategoryDao;
import com.example.fitnessapp.Database.Dao.ProductDao;
import com.example.fitnessapp.Database.Dao.ProductDetailsDao;
import com.example.fitnessapp.Database.Models.Category;
import com.example.fitnessapp.Database.Models.Manufacturer;
import com.example.fitnessapp.Database.Models.MeasureUnit;
import com.example.fitnessapp.Database.Models.Product;
import com.example.fitnessapp.Database.Models.ProductCategory;
import com.example.fitnessapp.Database.Models.ProductDetails;
import com.example.fitnessapp.Database.Models.ProductWithRelations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
                Category.class,
                Product.class,
                Manufacturer.class,
                MeasureUnit.class,
                ProductCategory.class,
                ProductDetails.class
            },
            version = 3,
            autoMigrations = {
                @AutoMigration(from = 1, to = 3)
            },
            exportSchema = true)
public abstract class ProductDatabase extends RoomDatabase {
    private static ProductDatabase databaseInstance;
    public static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    public abstract ProductDao productDao();
    public abstract CategoryDao categoryDao();
    public abstract ManufacturerDao manufacturerDao();
    public abstract MeasureUnitDao measureUnitDao();
    public abstract ProductDetailsDao productDetailsDao();
    public abstract ProductCategoryDao productCategoryDao();

    public static ProductDatabase getDatabase(final Context context){
        if(databaseInstance == null){
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    ProductDatabase.class, "my_db")
                    .addCallback(newCallback1)
                    .build();
        }
        return databaseInstance;
    }

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

                // Kategoria
                Category category = new Category("Napój");
                long categoryId = categoryDAO.insert(category);

                // Producent
                Manufacturer manufacturer = new Manufacturer("Hortex");
                long manufacturerId = manufacturerDAO.insert(manufacturer);

                // Jednostka miary
                MeasureUnit measureUnit1 = new MeasureUnit("ml");
                long unitId1 = measureUnitDAO.insert(measureUnit1);

                MeasureUnit measureUnit2 = new MeasureUnit("szt");
                long unitId2 = measureUnitDAO.insert(measureUnit2);

                MeasureUnit measureUnit3 = new MeasureUnit("g");
                long unitId3 = measureUnitDAO.insert(measureUnit3);

                // Produkt
                Product product = new Product("Sok", manufacturerId, unitId1);
                long productId = productDAO.insert(product);

                // Szczegóły produktu
                ProductDetails productDetails = new ProductDetails(30.9f, 10.2f, 2.9f, 3.1f, productId);
                long detailsId = productDetailsDAO.insert(productDetails);

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
