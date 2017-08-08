package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ubos.apps.ubosstat.R;

import java.util.List;

import model.Landscape;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

	private static final int PRIME_ROW = 0;
	private static final int NON_PRIME_ROW = 1;

	private List<Landscape> mDataList;
	private LayoutInflater inflater;

	public RecyclerAdapter(Context context, List<Landscape> data) {
		inflater = LayoutInflater.from(context);
		this.mDataList = data;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		switch (viewType) {  // Create the Prime and Non-Prime row Layouts

			case PRIME_ROW:

				ViewGroup primeRow = (ViewGroup) inflater.inflate(R.layout.list_item_prime, parent, false);
				MyViewHolder_PRIME holderPrime = new MyViewHolder_PRIME(primeRow);
				return holderPrime;

			case NON_PRIME_ROW:

				ViewGroup nonPrimeRow = (ViewGroup) inflater.inflate(R.layout.list_item_not_prime, parent, false);
				MyViewHolder_NON_PRIME holderNonPrime = new MyViewHolder_NON_PRIME(nonPrimeRow);
				return holderNonPrime;

			default:

				ViewGroup defaultRow = (ViewGroup) inflater.inflate(R.layout.list_item_not_prime, parent, false);
				MyViewHolder_NON_PRIME holderDefault = new MyViewHolder_NON_PRIME(defaultRow);
				return holderDefault;
		}
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Landscape current = mDataList.get(position);

		switch (holder.getItemViewType()) {

			case PRIME_ROW:

				MyViewHolder_PRIME holder_prime = (MyViewHolder_PRIME) holder;
				holder_prime.setData(current);

				break;

			case NON_PRIME_ROW:

				MyViewHolder_NON_PRIME holder_not_prime = (MyViewHolder_NON_PRIME) holder;
				holder_not_prime.setData(current);

				break;
		}
	}

	@Override
	public int getItemCount() {
		return mDataList.size();
	}

	@Override // This will help us to determine ROW TYPE : i.e. the PRIME or NON-PRIME row.
	public int getItemViewType(int position) {

		Landscape landscape = mDataList.get(position);
		if (landscape.isPrime())
			return PRIME_ROW;
		else
			return NON_PRIME_ROW;
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		public MyViewHolder(View itemView) {
			super(itemView);
		}
	}

	// Holder class for PRIME rows
	public class MyViewHolder_PRIME extends MyViewHolder {

		TextView title;
		ImageView imgThumb, imgRowType;

		public MyViewHolder_PRIME(View itemView) {
			super(itemView);

			title = (TextView) itemView.findViewById(R.id.tvTitle);
			imgThumb = (ImageView) itemView.findViewById(R.id.img_row);
			imgRowType = (ImageView) itemView.findViewById(R.id.img_row_prime);
		}

		public void setData(Landscape current) {
			this.title.setText(current.getTitle());
			this.imgThumb.setImageResource(current.getImageID());
			this.imgRowType.setImageResource(R.drawable.prime);
		}
	}

	// Holder class for NON-PRIME rows
	public class MyViewHolder_NON_PRIME extends MyViewHolder {

		TextView title;
		ImageView imgThumb, imgRowType;

		public MyViewHolder_NON_PRIME(View itemView) {
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.tvTitle);
			imgThumb = (ImageView) itemView.findViewById(R.id.img_row);
			imgRowType = (ImageView) itemView.findViewById(R.id.img_row_not_prime);
		}

		public void setData(Landscape current) {
			this.title.setText(current.getTitle());
			this.imgThumb.setImageResource(current.getImageID());
			this.imgRowType.setImageResource(R.drawable.not_prime);
		}
	}
}
