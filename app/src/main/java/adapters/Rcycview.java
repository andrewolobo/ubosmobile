package adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ubos.apps.ubosstat.DetailActivity;
import org.ubos.apps.ubosstat.R;

import java.util.List;

import model.Indicator;

public class Rcycview extends RecyclerView.Adapter<Rcycview.ViewHolder> {

    public static final String ITEM_ID_KEY = "item_id_key";
    public static final String ITEM_KEY = "item_key";
    private List<Indicator> mItems;
    private Context mContext;
    private int selectedPos = 0;
    AlertDialog.Builder builder;
    private SharedPreferences.OnSharedPreferenceChangeListener prefsListener;

    private static final String URL = "http://192.168.8.100/cpi_capi/upload_single_record.php";
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


    public Rcycview(Context context, List<Indicator> items) {
        this.mContext = context;
        this.mItems = items;
        // selectedItems = new SparseBooleanArray();
    }

    @Override
    public Rcycview.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.list_item_xml_o, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Rcycview.ViewHolder holder, final int position) {
        final Indicator item = mItems.get(position);
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
            holder.tvName.setText(item.getTitle());
            holder.tvPeriod.setText(item.getPeriod());
            //holder.tvHeadline.setText(item.getHeadline());
            //holder.tvValue.setText(item.getUnit());
            holder.tvChangeType.setText(item.getChangeType());
            holder.tvChangeValue.setText(item.getChange_value());
            holder.tvChangeDesc.setText(item.getChange_desc());
            holder.tvIndexValue.setText(item.getIndex_value());
            System.out.println(item.getTitle());
            System.out.println(item.getPeriod());
            System.out.println(item.getHeadline());
            System.out.println(item.getUnit());
            System.out.println(item.getChangeType());
            System.out.println(item.getChange_value());
            System.out.println(item.getChange_desc());
            System.out.println(item.getIndex_value());
         //   System.out.print("Change"+item.getChangeType());

           // Toast.makeText(mContext, "Change " + item.getChangeType(),
            //        Toast.LENGTH_SHORT).show();
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
           //     Toast.makeText(mContext, "You selected " + item.getTitle() +" " + item.getId(),
           //             Toast.LENGTH_SHORT).show();
                Long d_itemId = item.getId();
                String d_title = item.getTitle();
                String d_period = item.getPeriod();
                String d_index_value = item.getIndex_value();
                String d_change_type = item.getChangeType();
                String d_change_value = item.getChange_value();
                String d_headline = item.getHeadline();
                String d_description = item.getDescription();
                String d_url = item.getUrl();
                String d_updated_on = item.getUpdated_on();
                String d_unit = item.getUnit();

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(ITEM_KEY, d_itemId);
                intent.putExtra("ITEM_TITLE", d_title);
                intent.putExtra("ITEM_PERIOD", d_period);
                intent.putExtra("ITEM_VALUE", d_index_value);
                intent.putExtra("ITEM_CHANGE_TYPE", d_change_type);
                intent.putExtra("ITEM_CHANGE_VALUE", d_change_value);
                intent.putExtra("ITEM_HEADLINE", d_headline);
                intent.putExtra("ITEM_DESCRIPTION", d_description);
                intent.putExtra("ITEM_URL", d_url);
                intent.putExtra("ITEM_UPDATED_ON", d_updated_on);
                intent.putExtra("ITEM_UNIT", d_unit);
                mContext.startActivity(intent);
                //       Intent intent = new Intent(mContext, DetailActivity.class);
                //     intent.putExtra(ITEM_KEY, item);
                //   mContext.startActivity(intent);


            }
        });





        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "You long clicked " + item.getId(),
                        Toast.LENGTH_SHORT).show();

                Log.d("Action","Long Clicked!!");
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
            tvPeriod = (TextView) itemView.findViewById(R.id.period);
            tvHeadline = (TextView) itemView.findViewById(R.id.headline);
            tvValue = (TextView) itemView.findViewById(R.id.value);
            tvChangeType = (TextView) itemView.findViewById(R.id.changeType);
            tvChangeValue = (TextView) itemView.findViewById(R.id.va);
            tvChangeDesc = (TextView) itemView.findViewById(R.id.index_desc);
            tvIndexValue = (TextView) itemView.findViewById(R.id.in);

           // imageView = (ImageView) itemView.findViewById(R.id.imageView);
           // imageSend   = (ImageView) itemView.findViewById(R.id.img_send_item);
            mView = itemView;
        }
    }


}