package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.example.citasiznardo.R;

public class RecyclerAux extends RecyclerView.Adapter<RecyclerAux.ViewHolder> {
    private List<Quotation> lista;
    private OnItemClickListener intListener;
    private OnItemLongClickListener intLongListener;


    public RecyclerAux(List<Quotation> list, OnItemClickListener onIntList, OnItemLongClickListener onIntLongList){
        this.lista = list;
        intListener = onIntList;
        intLongListener = onIntLongList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quotation_list_row, parent, false);
        return new ViewHolder(view, intListener, intLongListener);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvAuthor.setText(lista.get(position).getQuoteAuthor());
        holder.tvQuote.setText(lista.get(position).getQuoteText());
    }

    public void refresh(){
        notifyDataSetChanged(); // por ejemplo
    }

    public void addItems(List<Quotation> list){
        this.lista = list;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return lista.size();
    }

    public String getAutFromPos(int position){
        return lista.get(position).getQuoteAuthor();
    }

    public String getQuoteFromPos(int position){
        return lista.get(position).getQuoteText();
    }

    public void removeItem(int position){
        lista.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAllItems(){
        lista.removeAll(lista);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAuthor;
        public TextView tvQuote;

        public ViewHolder(View itemView, OnItemClickListener intListener, OnItemLongClickListener intLongListener) {
            super(itemView);
            tvAuthor = (TextView) itemView.findViewById(R.id.tvListAuthor);
            tvQuote = (TextView) itemView.findViewById(R.id.tvListQuote);

            itemView.setOnClickListener(v -> {
                intListener.onItemClickListener(getAdapterPosition());
            });
            itemView.setOnLongClickListener(v -> {
                intLongListener.onItemLongClickListener(getAdapterPosition());
                //        intLongListener.onItemLongClickListener((String) tvQuote.getText());
                return true;
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClickListener(int position);
        //void onItemLongClickListener(String quote);
    }
}
