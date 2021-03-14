package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.example.citasiznardo.R;

public class RecyclerAux extends RecyclerView.Adapter<RecyclerAux.ViewHolder> {
    private List<Quotation> list;
    private final OnItemClickListener intListener;
    private final OnItemLongClickListener intLongListener;

    public RecyclerAux(List<Quotation> list, OnItemClickListener onIntList, OnItemLongClickListener onIntLongList){
        this.list = list;
        intListener = onIntList;
        intLongListener = onIntLongList;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quotation_list_row, parent, false);
        return new ViewHolder(view, intListener, intLongListener);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvAuthor.setText(list.get(position).getQuoteAuthor());
        holder.tvQuote.setText(list.get(position).getQuoteText());
    }

    public void addItems(List<Quotation> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return list.size();
    }

    public String getAutFromPos(int position){
        return list.get(position).getQuoteAuthor();
    }

    public String getQuoteFromPos(int position){
        return list.get(position).getQuoteText();
    }

    public void removeItem(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAllItems(){
        list.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAuthor;
        public TextView tvQuote;

        public ViewHolder(View itemView, OnItemClickListener intListener, OnItemLongClickListener intLongListener) {
            super(itemView);

            /* Get a reference to the textviews and set listeners to them */
            tvAuthor = itemView.findViewById(R.id.tvListAuthor);
            tvQuote = itemView.findViewById(R.id.tvListQuote);

            itemView.setOnClickListener(v -> {
                intListener.onItemClickListener(getAdapterPosition());
            });
            itemView.setOnLongClickListener(v -> {
                intLongListener.onItemLongClickListener(getAdapterPosition());
                return true;
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClickListener(int position);
    }
}
