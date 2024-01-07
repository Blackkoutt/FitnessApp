package com.example.fitnessapp.ui.products;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.Database.Models.Category;
import com.example.fitnessapp.Database.Models.Manufacturer;
import com.example.fitnessapp.Database.Models.ProductWithRelations;
import com.example.fitnessapp.Database.ViewModels.ProductViewModel;
import com.example.fitnessapp.R;
import com.example.fitnessapp.databinding.FragmentProductsBinding;
import com.example.fitnessapp.ui.dashboard.DashboardViewModel;

import java.util.List;

public class ProductsFragment extends Fragment {
    //private FragmentProductsBinding view;
    //private RecyclerView recyclerView;
    private ProductViewModel productViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //DashboardViewModel dashboardViewModel =
        //        new ViewModelProvider(this).get(DashboardViewModel.class);
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        final ProductAdapter adapter = new ProductAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        productViewModel = new ViewModelProvider(getActivity()).get(ProductViewModel.class);
        productViewModel.getAll().observe(getActivity(), adapter::setProducts);

        return view;
        //view = FragmentProductsBinding.inflate(inflater, container, false);
        //RecyclerView recyclerView = view.findViewById

        //View root = view.getRoot();

        //final TextView textView = binding.textDashboard;
        //dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        //return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }



    private class ProductHolder extends RecyclerView.ViewHolder {

        private ProductWithRelations product;
        // private ImageView bookImageView;
        private TextView categoryTextView;
        private TextView unitTextView;
        private TextView detailsTextView;
        private TextView productManufacturerTextView;
        private TextView productNameTextView;
        public ProductHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.product_list_item, parent, false));

            /*itemView.setOnClickListener(this); // Ustawienie onClickListenera na każdym elemencie listy

            // Przy długim naciścnięciu na element
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Pokaż okno dialogowe z informacją czy napewno chcesz usunąć książkę
                    String message = getResources().getString(R.string.formatted_book_info, book.getAuthor(), book.getTitle());
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.book_delete_question)
                            .setMessage(message);

                    // Dodanie onclick listenera na pozytywnym przycisku (YES)
                    builder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Usunięcie książki z bazy
                            bookViewModel.delete(book);
                            // Wyświetlenie informacji o usunieciu książki
                            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_delete),
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

            // Przy geście "swipe"
            itemView.setOnTouchListener(new View.OnTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        String message = getResources().getString(R.string.archived_message, book.getTitle());
                        Snackbar.make(findViewById(R.id.coordinator_layout), message,
                                Snackbar.LENGTH_LONG).show();
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Bez tego nie obsługiwany jest event click i long click
                    if (gestureDetector.onTouchEvent(event)) {
                        return true;
                    }
                    return false;
                }
            });*/

            productManufacturerTextView = itemView.findViewById(R.id.product_manufacturer);
            productNameTextView = itemView.findViewById(R.id.product_name);
            categoryTextView = itemView.findViewById(R.id.product_category);
            unitTextView = itemView.findViewById(R.id.product_unit);
            detailsTextView = itemView.findViewById(R.id.product_details);
            //bookImageView = itemView.findViewById(R.id.book_img);

        }
        public void bind(ProductWithRelations product){
            //Manufacturer man1 = man;
            //int a=1;
            this.product = product;
            int a=1;
            productManufacturerTextView.setText("Producent:"+product.manufacturer.getName());
            productNameTextView.setText("Produkt: "+product.product.getName());
            unitTextView.setText("Jednostka: "+product.unit.getName());
            detailsTextView.setText("Białko: "+product.details.getProtein());
            String categoryNameText ="";
            for(Category cat : product.categories){
                categoryNameText+=cat.getName()+" ";
            }
            categoryTextView.setText("Kategorie: "+categoryNameText);


            //bookImageView.setImageResource(R.drawable.ic_book);
        }

        /*// Obsługa eventu on click na elemencie listy
        @Override
        public void onClick(View v) {
            editedBook = book;
            Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
            Bundle extras = new Bundle();
            extras.putString(KEY_EXTRA_BOOK_TITLE, book.getTitle());
            extras.putString(KEY_EXTRA_BOOK_AUTHOR, book.getAuthor());
            intent.putExtras(extras);

            startActivityForResult(intent, EDIT_BOOK_ACTIVITY_REQUEST_CODE);
        }*/
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
                //Manufacturer man2 = mans.get(position);
                //holder.bind(man2);
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
        void setProducts(/*List<Manufacturer> man*/List<ProductWithRelations> products){
            /*for(ProductWithRelations product : products){
                ProductWithRelations p = product;
                int a=1;
                this.products.add(product);
            }*/
            this.products = products;
            //this.mans = man;
            int a=1;
            notifyDataSetChanged();
        }
    }
}