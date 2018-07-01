package dularish.splitspends;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SplitSpendsListActivityViewHolder extends RecyclerView.ViewHolder {

    TextView titleTextView;
    TextView createdByTextView;
    ImageButton optionsImageButton;

    public SplitSpendsListActivityViewHolder(View itemView) {
        super(itemView);
        titleTextView = (TextView) itemView.findViewById(R.id.title_textview_splitspends_list_cardview);
        createdByTextView = (TextView) itemView.findViewById(R.id.createdby_textview_splitspends_list_cardview);
        optionsImageButton = (ImageButton) itemView.findViewById(R.id.options_imagebutton_splitspends_list_cardview);


    }

    public void bindData(SplitSpendsListModel splitSpendsListModel) {
        titleTextView.setText(splitSpendsListModel.getTitle());
        createdByTextView.setText(TextUtils.isEmpty(splitSpendsListModel.getCreatedBy()) ? "" : "Created by " + splitSpendsListModel.getCreatedBy());
    }
}
