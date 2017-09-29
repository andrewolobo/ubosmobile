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
import android.widget.Toast;

import org.ubos.apps.ubosstat.DetailVActivity;
import org.ubos.apps.ubosstat.R;

import java.util.List;

import model.Indicator;

public class Rcycview_ind extends RecyclerView.Adapter<Rcycview_ind.ViewHolder> {

    public static final String ITEM_ID_KEY = "item_id_key";
    public static final String ITEM_KEY = "item_key";
    private List<Indicator> mItems;
    private Context mContext;
    private int selectedPos = 0;
    AlertDialog.Builder builder;
    private SharedPreferences.OnSharedPreferenceChangeListener prefsListener;

    //  map integers to booleans so you can know which view has been selected and which hasn't.
    // private SparseBooleanArray selectedItems;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();


    public Rcycview_ind(Context context, List<Indicator> items) {
        this.mContext = context;
        this.mItems = items;
        // selectedItems = new SparseBooleanArray();
    }

    @Override
    public Rcycview_ind.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.list_ind_item_i, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Rcycview_ind.ViewHolder holder, final int position) {
        final Indicator item = mItems.get(position);
        holder.mView.setSelected(selectedPos == position);

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

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long itemId = item.getId();
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
                String d_category = item.getCat_id();
                String d_data = item.getData();

                Intent intent = new Intent(mContext, DetailVActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ITEM_KEY, itemId);
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
                intent.putExtra("ITEM_CAT", d_category);
                intent.putExtra("ITEM_DATA",d_data);

                mContext.startActivity(intent);



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
            mView = itemView;
        }
    }


}