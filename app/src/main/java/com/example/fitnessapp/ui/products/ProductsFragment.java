package com.example.fitnessapp.ui.products;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.AddEditProductActivity;
import com.example.fitnessapp.Database.Models.Category;
import com.example.fitnessapp.Database.Models.Manufacturer;
import com.example.fitnessapp.Database.Models.Product;
import com.example.fitnessapp.Database.Models.ProductCategory;
import com.example.fitnessapp.Database.Models.ProductDetails;
import com.example.fitnessapp.Database.Models.ProductWithRelations;
import com.example.fitnessapp.Database.ViewModels.MealProductViewModel;
import com.example.fitnessapp.Database.ViewModels.ProductCategoryViewModel;
import com.example.fitnessapp.Database.ViewModels.ProductDetailsViewModel;
import com.example.fitnessapp.Database.ViewModels.ProductViewModel;
import com.example.fitnessapp.ProductDetailsActivity;
import com.example.fitnessapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {
    public static final String EXTRA_EDIT_CATEGORY_NAME = "EXTRA_EDIT_CATEGORY_NAME";
    public static final String EXTRA_EDIT_MANUFACTURER_NAME = "EXTRA_EDIT_MANUFACTURER_NAME";
    public static final String EXTRA_EDIT_UNIT_NAME = "EXTRA_EDIT_UNIT_NAME";
    public static final String EXTRA_EDIT_PRODUCT_NAME="EXTRA_EDIT_PRODUCT_NAME";
    public static final String EXTRA_EDIT_CALORIES_AMOUNT= "EXTRA_EDIT_CALORIES_AMOUNT";
    public static final String EXTRA_EDIT_PROTEIN_AMOUNT= "EXTRA_EDIT_PROTEIN_AMOUNT";
    public static final String EXTRA_EDIT_CARBOHYDRATES_AMOUNT= "EXTRA_EDIT_CARBOHYDRATES_AMOUNT";
    public static final String EXTRA_EDIT_FAT_AMOUNT= "EXTRA_EDIT_FAT_AMOUNT";
    public static final String EXTRA_PRODUCT_DETAILS= "EXTRA_PRODUCT_DETAILS";
    private ProductViewModel productViewModel;
    private ProductCategoryViewModel productCategoryViewModel;
    private ProductDetailsViewModel productDetailsViewModel;
    private MealProductViewModel mealProductViewModel;
    private Product editedProduct;
    private ProductCategory editedCategory;
    private ProductDetails editedDetails;
    public static final int NEW_PRODUCT_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_PRODUCT_ACTIVITY_REQUEST_CODE = 2;
    private View fragmentView;

    // Metoda wywoływana w momencie otrzymania wiadomości zwrotnej z activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Jeśli kod wyniku jest poprawnuy
        if(resultCode == RESULT_OK){

            // Pobierz dane przesłane przez activity
            long manufacturerId = Long.parseLong(data.getStringExtra(AddEditProductActivity.EXTRA_EDIT_MANUFACTURER_ID));
            long[] categoryIds = data.getLongArrayExtra(AddEditProductActivity.EXTRA_EDIT_CATEGORY_ID);
            String productName = data.getStringExtra(AddEditProductActivity.EXTRA_EDIT_PRODUCT_NAME);
            long unitId = Long.parseLong(data.getStringExtra(AddEditProductActivity.EXTRA_EDIT_UNIT_ID));
            float caloriesAmount = Float.parseFloat(data.getStringExtra(AddEditProductActivity.EXTRA_EDIT_CALORIES_AMOUNT));
            float proteinAmount = Float.parseFloat(data.getStringExtra(AddEditProductActivity.EXTRA_EDIT_PROTEIN_AMOUNT));
            float carbohydratesAmount = Float.parseFloat(data.getStringExtra(AddEditProductActivity.EXTRA_EDIT_CARBOHYDRATES_AMOUNT));
            float fatAmount = Float.parseFloat(data.getStringExtra(AddEditProductActivity.EXTRA_EDIT_FAT_AMOUNT));

            // Dodawanie produktu
            if(requestCode == NEW_PRODUCT_ACTIVITY_REQUEST_CODE){
                Product product = new Product(productName, manufacturerId, unitId);
                long productId = productViewModel.insert(product);

                ProductDetails productDetails = new ProductDetails(caloriesAmount, carbohydratesAmount, fatAmount , proteinAmount, productId);
                productDetailsViewModel.insert(productDetails);

                for(long categoryId : categoryIds){
                    ProductCategory product_category = new ProductCategory(productId, categoryId);
                    productCategoryViewModel.insert(product_category);
                }


                Snackbar.make(fragmentView.findViewById(R.id.coordinator_layout), getString(R.string.product_added_succesful),
                        Snackbar.LENGTH_LONG).show();
            }
            // Edycja produktu
            else{
                editedProduct.setManufacturerId(manufacturerId);
                editedProduct.setMeasureUnitId(unitId);
                editedProduct.setName(productName);
                productViewModel.update(editedProduct);

                productCategoryViewModel.delete(editedProduct.getProductId());
                for(long categoryId : categoryIds){
                    ProductCategory product_category = new ProductCategory(editedProduct.getProductId(), categoryId);
                    productCategoryViewModel.insert(product_category);
                }

                editedDetails.setCalorificValue(caloriesAmount);
                editedDetails.setProtein(proteinAmount);
                editedDetails.setCarbohydrates(carbohydratesAmount);
                editedDetails.setFat(fatAmount);
                productDetailsViewModel.update(editedDetails);

                Snackbar.make(fragmentView.findViewById(R.id.coordinator_layout), getString(R.string.product_modified),
                        Snackbar.LENGTH_LONG).show();
            }
        }
        else{
            Snackbar.make(fragmentView.findViewById(R.id.coordinator_layout), getString(R.string.empty_not_saved),
                    Snackbar.LENGTH_LONG).show();
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_products, container, false);
        fragmentView = view;

        // Ustawienie recylerView dla produktów
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        final ProductAdapter adapter = new ProductAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Utworzenie ViewModeli do komunikacji z bazą danych
        productViewModel = new ViewModelProvider(getActivity()).get(ProductViewModel.class);
        productDetailsViewModel = new ViewModelProvider(getActivity()).get(ProductDetailsViewModel.class);
        productCategoryViewModel = new ViewModelProvider(getActivity()).get(ProductCategoryViewModel.class);
        mealProductViewModel = new ViewModelProvider(getActivity()).get(MealProductViewModel.class);

        // Pobranie produktów
        productViewModel.getAll().observe(getActivity(), adapter::setProducts);

        // Uruchomienie aktywności z kodem żądania jako dodaj produkt
        FloatingActionButton addBookButton = view.findViewById(R.id.add_button);
        addBookButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEditProductActivity.class);
                startActivityForResult(intent, NEW_PRODUCT_ACTIVITY_REQUEST_CODE);
            }
        });
        return view;
    }

    // Metoda wywoływana w momencie niszczenia fragmentu
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // Holder produktu
    private class ProductHolder extends RecyclerView.ViewHolder {
        private ProductWithRelations product;
        private TextView categoryTextView;
        private TextView calorificValueTextView;
        private TextView productManufacturerTextView;
        private TextView productNameTextView;
        public ProductHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.product_list_item, parent, false));

            // Pobranie elementów widoku
            productManufacturerTextView = itemView.findViewById(R.id.product_manufacturer);
            productNameTextView = itemView.findViewById(R.id.product_name);
            categoryTextView = itemView.findViewById(R.id.product_category);
            calorificValueTextView = itemView.findViewById(R.id.product_calorificValue);

            // Ustawienie onClickListenera dla produktu - edycja produktu
            itemView.setOnClickListener(this::onClickItem);

            // Ustawienie onLongClickListenera dla produktu - usunięcie produktu
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String message = getResources().getString(R.string.formatted_product_info, product.manufacturer.getName(), product.product.getName());
                    AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));
                    builder.setTitle(R.string.product_delete_question)
                            .setMessage(message);
                    builder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            productCategoryViewModel.delete(product.product.getProductId());
                            productViewModel.delete(product.product);
                            mealProductViewModel.deleteByProductId(product.product.getProductId());
                            productDetailsViewModel.delete(product.details);
                            Snackbar.make(fragmentView.findViewById(R.id.coordinator_layout), getString(R.string.product_deleted),
                                    Snackbar.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create();
                    builder.show();
                    return true;
                }
            });

            // Ustawienie gestu przesunięcia w prawo lub lewo na wybranym produkcie - przejscie do detali
            itemView.setOnTouchListener(new View.OnTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                        intent.putExtra("EXTRA_PRODUCT_DETAILS", product);
                        startActivity(intent);

                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (gestureDetector.onTouchEvent(event)) {
                        return true;
                    }
                    return false;
                }
            });
        }

        // Metoda wywoływana w momencie kliknięcia na wybrany produkt - edycja produktu
        private void onClickItem(View view) {
            editedProduct = product.product;
            editedDetails = product.details;
            Intent intent = new Intent(getActivity(), AddEditProductActivity.class);
            Bundle extras = new Bundle();

            ArrayList<String> actualCategoryNames = new ArrayList<String>();
            for(Category cat: product.categories){
                actualCategoryNames.add(cat.getName());
            }

            // Wysłanie danych do Activity
            extras.putStringArrayList(EXTRA_EDIT_CATEGORY_NAME, actualCategoryNames);
            extras.putString(EXTRA_EDIT_MANUFACTURER_NAME, product.manufacturer.getName());
            extras.putString(EXTRA_EDIT_PRODUCT_NAME, product.product.getName());
            extras.putString(EXTRA_EDIT_UNIT_NAME, product.unit.getName());
            extras.putString(EXTRA_EDIT_CALORIES_AMOUNT, String.valueOf(product.details.getCalorificValue()));
            extras.putString(EXTRA_EDIT_PROTEIN_AMOUNT, String.valueOf(product.details.getProtein()));
            extras.putString(EXTRA_EDIT_CARBOHYDRATES_AMOUNT, String.valueOf(product.details.getCarbohydrates()));
            extras.putString(EXTRA_EDIT_FAT_AMOUNT, String.valueOf(product.details.getFat()));
            intent.putExtras(extras);

            // Uruchomienie activity i oczekiwanie na wynik
            startActivityForResult(intent, EDIT_PRODUCT_ACTIVITY_REQUEST_CODE);
        }

        // Bindowanie produktu
        public void bind(ProductWithRelations product){
            this.product = product;

            productNameTextView.setText(product.product.getName());
            String categoryNameText ="";
            for(int i=0;i<product.categories.size();i++){
                categoryNameText+=product.categories.get(i).getName();
                if(i!=product.categories.size()-1){
                    categoryNameText+=", ";
                }
            }
            categoryTextView.setText(getResources().getString(R.string.product_category_label, categoryNameText));
            productManufacturerTextView.setText(getResources().getString(R.string.product_manufacturer_label, product.manufacturer.getName()));
            calorificValueTextView.setText(getResources().getString(R.string.product_calorific_label, String.valueOf(product.details.getCalorificValue())));
        }
    }

    // Adapter produtku
    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder>{
        private List<ProductWithRelations> products;
        private List<Manufacturer> mans;

        @NonNull
        @Override
        public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductsFragment.ProductHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
            if(products != null){
                ProductWithRelations product = products.get(position);
                holder.bind(product);
            }
            else {
                Log.d("MainActivity", "No products");
            }
        }

        @Override
        public int getItemCount() {
            if(products != null){
                return products.size();
            }
            else{
                return 0;
            }
        }
        void setProducts(List<ProductWithRelations> products){
            this.products = products;
            notifyDataSetChanged();
        }
    }
}