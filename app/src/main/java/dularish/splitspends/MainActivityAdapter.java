package dularish.splitspends;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by pc on 10/21/2017.
 */

public class MainActivityAdapter extends RecyclerView.Adapter {

    public List<SplitSpendsListModel> models = new ArrayList<SplitSpendsListModel>();
    private Context context;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View listviewItem = inflater.inflate(R.layout.cardview_item_layout,parent,false);

        SplitSpendsListActivityViewHolder viewHolder = new SplitSpendsListActivityViewHolder(listviewItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SplitSpendsListActivityViewHolder) holder).bindData(models.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
