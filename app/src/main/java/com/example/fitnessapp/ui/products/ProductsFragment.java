package com.example.fitnessapp.ui.products;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.fitnessapp.Database.ViewModels.ProductCategoryViewModel;
import com.example.fitnessapp.Database.ViewModels.ProductDetailsViewModel;
import com.example.fitnessapp.Database.ViewModels.ProductViewModel;
import com.example.fitnessapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

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
    private ProductViewModel productViewModel;
    private ProductCategoryViewModel productCategoryViewModel;
    private ProductDetailsViewModel productDetailsViewModel;
    private Product editedProduct;
    private ProductCategory editedCategory;
    private ProductDetails editedDetails;
    public static final int NEW_PRODUCT_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_PRODUCT_ACTIVITY_REQUEST_CODE = 2;
    private View fragmentView;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            long manufacturerId = Long.parseLong(data.getStringExtra(AddEditProductActivity.EXTRA_EDIT_MANUFACTURER_ID));
            // to pewnie też do poprawy !!!!
            long categoryId = Long.parseLong(data.getStringExtra(AddEditProductActivity.EXTRA_EDIT_CATEGORY_ID));
            String productName = data.getStringExtra(AddEditProductActivity.EXTRA_EDIT_PRODUCT_NAME);
            long unitId = Long.parseLong(data.getStringExtra(AddEditProductActivity.EXTRA_EDIT_UNIT_ID));
            float caloriesAmount = Float.parseFloat(data.getStringExtra(AddEditProductActivity.EXTRA_EDIT_CALORIES_AMOUNT));
            float proteinAmount = Float.parseFloat(data.getStringExtra(AddEditProductActivity.EXTRA_EDIT_PROTEIN_AMOUNT));
            float carbohydratesAmount = Float.parseFloat(data.getStringExtra(AddEditProductActivity.EXTRA_EDIT_CARBOHYDRATES_AMOUNT));
            float fatAmount = Float.parseFloat(data.getStringExtra(AddEditProductActivity.EXTRA_EDIT_FAT_AMOUNT));

            if(requestCode == NEW_PRODUCT_ACTIVITY_REQUEST_CODE){
                Product product = new Product(productName, manufacturerId, unitId);
                long productId = productViewModel.insert(product);

                ProductDetails productDetails = new ProductDetails(caloriesAmount, carbohydratesAmount, fatAmount , proteinAmount, productId);
                productDetailsViewModel.insert(productDetails);

                ProductCategory product_category = new ProductCategory(productId, categoryId);
                productCategoryViewModel.insert(product_category);

                Snackbar.make(fragmentView.findViewById(R.id.coordinator_layout), getString(R.string.product_added_succesful),
                        Snackbar.LENGTH_LONG).show();
            }
            else{
                editedProduct.setManufacturerId(manufacturerId);
                editedProduct.setMeasureUnitId(unitId);
                editedProduct.setName(productName);
                productViewModel.update(editedProduct);

                // category!!!!!

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
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        final ProductAdapter adapter = new ProductAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        productViewModel = new ViewModelProvider(getActivity()).get(ProductViewModel.class);
        productDetailsViewModel = new ViewModelProvider(getActivity()).get(ProductDetailsViewModel.class);
        productCategoryViewModel = new ViewModelProvider(getActivity()).get(ProductCategoryViewModel.class);

        productViewModel.getAll().observe(getActivity(), adapter::setProducts);

        FloatingActionButton addBookButton = view.findViewById(R.id.add_button);
        // Kliknięcie floating button uruchamia nową aktyność do dodawania ksiązki
        addBookButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEditProductActivity.class);
                //intent.putExtra("request_code", NEW_PRODUCT_ACTIVITY_REQUEST_CODE);
                //activityResultLauncher.launch(intent, NEW_PRODUCT_ACTIVITY_REQUEST_CODE);
                startActivityForResult(intent, NEW_PRODUCT_ACTIVITY_REQUEST_CODE);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



    private class ProductHolder extends RecyclerView.ViewHolder {

        private ProductWithRelations product;
        private TextView categoryTextView;
        private TextView unitTextView;
        private TextView detailsTextView;
        private TextView productManufacturerTextView;
        private TextView productNameTextView;
        public ProductHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.product_list_item, parent, false));

            productManufacturerTextView = itemView.findViewById(R.id.product_manufacturer);
            productNameTextView = itemView.findViewById(R.id.product_name);
            categoryTextView = itemView.findViewById(R.id.product_category);
            unitTextView = itemView.findViewById(R.id.product_unit);
            detailsTextView = itemView.findViewById(R.id.product_details);

            itemView.setOnClickListener(this::onClickItem);
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
        }

        private void onClickItem(View view) {
            editedProduct = product.product;
            //editedCategory = product.categories;
            editedDetails = product.details;
            Intent intent = new Intent(getActivity(), AddEditProductActivity.class);
            Bundle extras = new Bundle();
            // Kategori może być wiele - trzeba naprawić !!!!!
            //extras.putString(EXTRA_EDIT_CATEGORY_NAME, product.categories);
            extras.putString(EXTRA_EDIT_MANUFACTURER_NAME, product.manufacturer.getName());
            extras.putString(EXTRA_EDIT_PRODUCT_NAME, product.product.getName());
            extras.putString(EXTRA_EDIT_UNIT_NAME, product.unit.getName());
            extras.putString(EXTRA_EDIT_CALORIES_AMOUNT, String.valueOf(product.details.getCalorificValue()));
            extras.putString(EXTRA_EDIT_PROTEIN_AMOUNT, String.valueOf(product.details.getProtein()));
            extras.putString(EXTRA_EDIT_CARBOHYDRATES_AMOUNT, String.valueOf(product.details.getCarbohydrates()));
            extras.putString(EXTRA_EDIT_FAT_AMOUNT, String.valueOf(product.details.getFat()));
            intent.putExtras(extras);

            startActivityForResult(intent, EDIT_PRODUCT_ACTIVITY_REQUEST_CODE);
        }

        public void bind(ProductWithRelations product){
            this.product = product;
            int a=1;
            productManufacturerTextView.setText("Producent:"+product.manufacturer.getName());
            productNameTextView.setText("Produkt: "+product.product.getName());
            unitTextView.setText("Jednostka: "+product.unit.getName());
            ProductDetails d = product.details;
            detailsTextView.setText("Białko: "+product.details.getProtein());
            String categoryNameText ="";
            for(Category cat : product.categories){
                categoryNameText+=cat.getName()+" ";
            }
            categoryTextView.setText("Kategorie: "+categoryNameText);
        }
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder>{
        private List<ProductWithRelations> products;
        private List<Manufacturer> mans;

        @NonNull
        @Override
        public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
            if(products != null){
                ProductWithRelations product = products.get(position);
                holder.bind(product);
            }
            else {
                Log.d("MainActivity", "No books");
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