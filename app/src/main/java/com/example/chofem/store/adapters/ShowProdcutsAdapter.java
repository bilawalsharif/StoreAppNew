package com.example.chofem.store.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.chofem.store.R;
import com.example.chofem.store.interfaces.ProductListner;
import com.example.chofem.store.responses.GetStoreProductResponse;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;


public class ShowProdcutsAdapter extends RecyclerView.Adapter<ShowProdcutsAdapter.Holder> {
    Context mContext;
    private List<GetStoreProductResponse.PRODUCT> pRODUCT = null;
    private int pos;
    private ProductListner productListner;

    public ShowProdcutsAdapter(Context context, List<GetStoreProductResponse.PRODUCT> pRODUCT) {
        this.mContext = context;
        this.pRODUCT = pRODUCT;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.custom_row_show_product, parent, false);
        Holder viewHolder = new Holder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        pos = position;
        final GetStoreProductResponse.PRODUCT obj= new GetStoreProductResponse.PRODUCT();

        // final FavrouitModel myListData = list.get(position);
        //holder.txtCouriorName.setText(courriorModelArraylist.get(position).getFirstName());
        //holder.txtCouriorContact.setText(courriorModelArraylist.get(position).getPhoneNumber());

/*        Glide.with(mContext)
                .load(pRODUCT.get(position).getPrimaryImage())
                .centerCrop()
                .placeholder(R.drawable.student)
                .into(holder.imgViewProduct);*/

        Picasso.get()
                .load(pRODUCT.get(position).getPrimaryImage())
                .noFade()
                .into(holder.imgViewProduct);

        holder.txtProductName.setText(pRODUCT.get(position).getPName());
        holder.txtProductPrice.setText(pRODUCT.get(position).getPPrice());
        holder.txtProductStatus.setText(pRODUCT.get(position).getPStatus());
        holder.txtProductType.setText(pRODUCT.get(position).getPPrice());
        holder.btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.setPName(pRODUCT.get(position).getPName());
                obj.setPDescription(pRODUCT.get(position).getPDescription());
                obj.setPrimaryImage(pRODUCT.get(position).getPrimaryImage());
                obj.setPPrice(pRODUCT.get(position).getPPrice());
                obj.setPCategory(pRODUCT.get(position).getPCategory());
                obj.setPId(pRODUCT.get(position).getPId());
                productListner.updateProduct(position,obj);
            }
        });
        holder.btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.setPId(pRODUCT.get(position).getPId());
                productListner.deleteProduct(position,obj);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pRODUCT.size();
    }

    //    public void setClickListener(TransalatorAdapterInterface itemClickListener) {
//        this.clickListener = itemClickListener;
//    }
    public class Holder extends RecyclerView.ViewHolder {
        public TextView txtProductName, txtProductType, txtProductPrice, txtProductStatus;
        public FrameLayout contactFrame;
        public LinearLayout linear;
        public CircleImageView imgViewProduct;
        public ImageView btnUpdateProduct,btnDeleteProduct;

        public Holder(View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductType = itemView.findViewById(R.id.txtProductType);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtProductStatus = itemView.findViewById(R.id.txtProductStatus);
            imgViewProduct = itemView.findViewById(R.id.imgViewProduct);
            btnDeleteProduct=itemView.findViewById(R.id.btnDeleteProduct);
            btnUpdateProduct=itemView.findViewById(R.id.btnUpdateProduct);


//            imgDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    final String ID=SharedPref.getInstance(context).getUserName();
//                    new AlertDialog.Builder(context)
//                            .setTitle("Alert")
//                            .setMessage("Do You want to Delete this this Empty Leg?")
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                                public void onClick(DialogInterface dialog, int whichButton) {
//
//                                    ref = FirebaseDatabase.getInstance().getReference("courierEmptyLeg");
//                                    ref.child(ID).child(list.get(getAdapterPosition()).getNodeID()).removeValue();
////                                    Date currentTime = Calendar.getInstance().getTime();
////                                    DonarRequestModel donarRequestModel = new DonarRequestModel(hospitalID, list.get(getAdapterPosition()).getUserID(), "pending", currentTime.toString());
////                                    mRefRequest.child(hospitalID).setValue(donarRequestModel);
//                                   Snackbar.make(v, "Empty Leg Removed", Snackbar.LENGTH_SHORT).show();
//
//                                    notifyDataSetChanged();
//                                }
//                            })
//                            .setNegativeButton(android.R.string.no, null).show();
//                }
//            });


        }
    }


    /*    public void showDialog(CourriorModel courriorModel) {
            mdialog = new Dialog(context);
            mdialog.setContentView(R.layout.courier_dialog);
            //  mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView txtCouriorName = mdialog.findViewById(R.id.txtCouriorName);
            final TextView txtCouriorContact = mdialog.findViewById(R.id.txtCouriorContact);
            ImageView imgCopyContact = mdialog.findViewById(R.id.imgCopyContact);
            txtCouriorName.setText(courriorModel.getFirstName());
            txtCouriorContact.setText(courriorModel.getPhoneNumber());

            imgCopyContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //place your TextView's text in clipboard
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                    clipboard.setText(txtCouriorContact.getText());
                    Toast.makeText(context, "Text Copied!!!", Toast.LENGTH_SHORT).show();
                }
            });
            mdialog.show();
        }*/
    public ShowProdcutsAdapter setListner(ProductListner productListner) {
        this.productListner=productListner;
        return this;
    }
}