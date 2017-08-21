package adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ubos.apps.ubosstat.Indicators;
import org.ubos.apps.ubosstat.R;

import java.util.List;

import model.Category;

public class Rcycview_cat extends RecyclerView.Adapter<Rcycview_cat.ViewHolder> {

    public static final String ITEM_ID_KEY = "item_id_key";
    public static final String ITEM_KEY = "item_key";
    private List<Category> mItems;
    private Context mContext;
    private int selectedPos = 0;
    AlertDialog.Builder builder;
    private SharedPreferences.OnSharedPreferenceChangeListener prefsListener;

    private static final String URL = "http://192.168.8.101/cpi_capi/upload_single_record.php";
    public static final String Price = "price";
    public static final String Item_Qty = "item_Qty";
    public static final String  Item_Unit = "item_Unit";
    public static final String   Item_longitude = "item_longitude";
    public static final String   Item_latitude = "item_latitude";
    public static final String     Item_Date = "item_Date";
    public static final String     Outlet_ld = "outlet_ld";
    public static final String     Item_remarks = "item_remarks";

    public static final String ITEM_ID = "_id";

    //  map integers to booleans so you can know which view has been selected and which hasn't.
    // private SparseBooleanArray selectedItems;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();


    public Rcycview_cat(Context context, List<Category> items) {
        this.mContext = context;
        this.mItems = items;
        // selectedItems = new SparseBooleanArray();
    }

    @Override
    public Rcycview_cat.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.list_cat_item_i, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Rcycview_cat.ViewHolder holder, final int position) {
        final Category item = mItems.get(position);
        holder.mView.setSelected(selectedPos == position);

        /**   if(selectedPos == position){
         // Here I am just highlighting the background
         holder.mView.setBackgroundColor(Color.GREEN);
         }else{
         holder.mView.setBackgroundColor(Color.TRANSPARENT);
         } **/

        if (selectedItems.get(position, false)) {
            //   selectedItems.delete(position);
            holder.mView.setSelected(false);
        }
        else {
            selectedItems.put(position, true);
            holder.mView.setSelected(true);
        }

        try {
            String item_text = item.getCat_name();
            item_text = item_text.replace("_"," ");
            item_text = item_text.toUpperCase();
            holder.tvName.setText(item_text);

            //   System.out.print("Change"+item.getChangeType());

            //   String imageFile = item.getImage();
            // InputStream inputStream = mContext.getAssets().open(imageFile);
            //Drawable d = Drawable.createFromStream(inputStream, null);
            //holder.imageView.setImageDrawable(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long itemId = item.getId();
                // String sitemId = String.valueOf(itemId);

                Intent intent = new Intent(mContext, Indicators.class);
                intent.putExtra(ITEM_KEY, itemId);
                mContext.startActivity(intent);

                //       Intent intent = new Intent(mContext, DetailActivity.class);
                //     intent.putExtra(ITEM_KEY, item);
                //   mContext.startActivity(intent);


            }
        });





        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvPeriod , tvHeadline , tvValue , tvChangeType, tvChangeValue, tvChangeDesc, tvIndexValue;
        public ImageView imageView,  imageSend;
        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.title);

            // imageView = (ImageView) itemView.findViewById(R.id.imageView);
            // imageSend   = (ImageView) itemView.findViewById(R.id.img_send_item);
            mView = itemView;
        }
    }


}