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
    private List<Quotation> lista;

    public RecyclerAux(List<Quotation> list){
        this.lista = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_favourite, parent, false);
        RecyclerAux.ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvAutor.setText(lista.get(position).getQuoteAuthor());
        holder.tvQuote.setText(lista.get(position).getQuoteText());
    }

    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAutor;
        private TextView tvQuote;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAutor = (TextView) itemView.findViewById(R.id.tvListAuthor);
            tvQuote = (TextView) itemView.findViewById(R.id.tvListQuote);
        }

    }
}
