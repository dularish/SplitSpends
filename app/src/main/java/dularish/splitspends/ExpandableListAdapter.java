        package dularish.splitspends;

        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.os.Handler;
        import android.support.v7.widget.CardView;
        import android.support.v7.widget.RecyclerView;
        import android.text.Editable;
        import android.text.InputType;
        import android.text.TextWatcher;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.BaseExpandableListAdapter;
        import android.widget.EditText;
        import android.widget.ExpandableListView;
        import android.widget.ImageButton;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

        /**
         * Created by pc on 11/3/2017.
         */

        public class ExpandableListAdapter extends BaseExpandableListAdapter {


            public HashMap<String,View> childreferences = new HashMap<String,View>();
            public HashMap<String,View> parentreferences = new HashMap<String, View>();

            public List<CardViewModel> models;
            public Context ctx;
            public ExpandableListView listviewref;
            public ExpandableListAdapter adapterRef = this;

            private int lastFocussedPosition = -1;
            private Handler handler = new Handler();

            public ExpandableListAdapter(List<CardViewModel> models, Context ctx, ExpandableListView expandableListView) {
                this.models = models;
                this.ctx = ctx;
                this.listviewref = expandableListView;
            }

            @Override
            public int getGroupCount() {
                return models.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return models.get(groupPosition).getInnerModelsData().size();
            }

            @Override
            public Object getGroup(int groupPosition) {
                return models.get(groupPosition);
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return models.get(groupPosition).getInnerModelsData().get(childPosition);
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

                final ViewHolder viewholder ;
                String Name = models.get(groupPosition).getName();
                String Amount = models.get(groupPosition).getAmount().toString();
                if(convertView == null){
                    LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater.inflate(R.layout.parent_cardview,null);

                    viewholder = new ViewHolder();
                    viewholder.namevhbox =  (TextView) convertView.findViewById(R.id.namebox);
                    viewholder.amountvhbox = (TextView) convertView.findViewById(R.id.amountbox);
                    viewholder.dropdownvhbutton = (ImageButton) convertView.findViewById(R.id.dropdownbutton);
                    viewholder.deletevhbutton = (ImageButton) convertView.findViewById(R.id.deletebutton);
                    viewholder.outereditvhbutton = (ImageButton) convertView.findViewById(R.id.outereditbutton);
                    convertView.setTag(viewholder);

                }
                else{
                    viewholder = (ViewHolder) convertView.getTag();
                }

                if(models.size() < 3){
                    viewholder.deletevhbutton.setVisibility(View.INVISIBLE);
                }
                else{
                    viewholder.deletevhbutton.setVisibility(View.VISIBLE);
                }

                TextView namebox = (TextView) convertView.findViewById(R.id.namebox);
                TextView amountbox = (TextView) convertView.findViewById(R.id.amountbox);
                final ImageButton dropdownbutton = (ImageButton) convertView.findViewById(R.id.dropdownbutton);
                viewholder.namevhbox.setText(Name);
                viewholder.amountvhbox.setText(Amount);
                viewholder.dropdownvhbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listviewref.isGroupExpanded(groupPosition)){
                            System.out.println("Group Collapsed");
                            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(ctx, R.anim.imagebutton_rotate_back);
                            hyperspaceJumpAnimation.setFillAfter(true);
                            v.startAnimation(hyperspaceJumpAnimation);
                            listviewref.collapseGroup(groupPosition);
                        }
                        else{
                            System.out.println("Group expanded");
                            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(ctx, R.anim.imagebutton_rotate);
                            hyperspaceJumpAnimation.setFillAfter(true);

                            //ImageButton dropdownbtn = (ImageButton) parentreferences.get(String.valueOf(groupPosition)).findViewById(R.id.dropdownbutton);
                            //ImageButton dropdownbtn = (ImageButton) ((LinearLayout)((CardView)listviewref.getChildAt(groupPosition)).getChildAt(0)).getChildAt(0).findViewById(R.id.dropdownbutton);
                            v.startAnimation(hyperspaceJumpAnimation);
                            listviewref.expandGroup(groupPosition);
                        }
                    }
                });
                viewholder.deletevhbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        models.remove(groupPosition);
                        notifyDataSetChanged();
                    }
                });
                //listviewref.expandGroup(groupPosition);//This comment should remind you that on every refresh this method would be executed
                viewholder.outereditvhbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
                        dialog.setTitle("Set Name");
                        LinearLayout layout = new LinearLayout(ctx);
                        layout.setOrientation(LinearLayout.VERTICAL);

                        String existingamount = models.get(groupPosition).getAmount().toString();
                        String existingname = models.get(groupPosition).getName().toString();

                        final EditText nameAlertBox = new EditText(ctx);
                        nameAlertBox.setHint("Name");
                        nameAlertBox.setText(existingname);
                        nameAlertBox.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                        layout.addView(nameAlertBox);

                        final EditText amountAlertBox = new EditText(ctx);
                        amountAlertBox.setHint("Amount");
                        amountAlertBox.setInputType(InputType.TYPE_CLASS_NUMBER);
                        amountAlertBox.setText(existingamount);
                        if(models.get(groupPosition).getInnerModelsData().size() == 1){
                            dialog.setTitle("Set Name & Amount");
                            layout.addView(amountAlertBox);
                        }
                        dialog.setView(layout);
                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                models.get(groupPosition).getInnerModelsData().get(0).setInnerAmount(Integer.parseInt(amountAlertBox.getText().toString()));
                                models.get(groupPosition).setName(nameAlertBox.getText().toString());
                                notifyDataSetChanged();
                            }
                        });
                        dialog.show();
                    }
                });
                parentreferences.put(String.valueOf(groupPosition),convertView);
                //listviewref.setDividerHeight(20);
                return convertView;
            }

            @Override
            public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

                String SubAmount = models.get(groupPosition).getInnerModelsData().get(childPosition).getInnerAmount().toString();
                String Purpose = models.get(groupPosition).getInnerModelsData().get(childPosition).getPurpose();

                //if(convertView == null){
                    LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater.inflate(R.layout.child_cardview,null);
                //}
                final TextView subamountbox = (TextView) convertView.findViewById(R.id.subamountbox);
                TextView purposebox = (TextView) convertView.findViewById(R.id.purposebox);
                ImageButton addchildbutton = (ImageButton) convertView.findViewById(R.id.inneraddbutton);
                ImageButton deletechildbutton = (ImageButton) convertView.findViewById(R.id.innerdeletebutton);
                if(childPosition == 0){
                    deletechildbutton.setVisibility(View.INVISIBLE);
                }
                if(childPosition == (models.get(groupPosition).getInnerModelsData().size() - 1)){
                    addchildbutton.setVisibility(View.VISIBLE);
                }
                else{
                    addchildbutton.setVisibility(View.INVISIBLE);
                }
                ImageButton editchildbutton = (ImageButton) convertView.findViewById(R.id.innereditbutton);
                subamountbox.setText(SubAmount);
                String tagtobeset = String.valueOf(groupPosition) + String.valueOf(childPosition);
                subamountbox.setTag( tagtobeset);
                purposebox.setText(Purpose);
                System.out.println("Child set for GroupPosition - " + groupPosition + " ChildPosition - " + childPosition + " Amt " + SubAmount + " Purpose " + Purpose);

                addchildbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("AddChild button clicked");
                        models.get(groupPosition).addInnerModelsData(new InnerCardViewModel(60,"DefaultAddChild"));
                        notifyDataSetChanged();
                    }
                });
                deletechildbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        models.get(groupPosition).deleteInnerModelsData(childPosition);
                        notifyDataSetChanged();
                    }
                });
                //subamountbox.addTextChangedListener(new TextBoxWatchers(listviewref,subamountbox, groupPosition, childPosition));
                editchildbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Context context = mapView.getContext();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
                        dialog.setTitle("Set SubAmount & Purpose");
                        LinearLayout layout = new LinearLayout(ctx);
                        layout.setOrientation(LinearLayout.VERTICAL);

                        String existingamount = models.get(groupPosition).getInnerModelsData().get(childPosition).getInnerAmount().toString();
                        String existingpurpose = models.get(groupPosition).getInnerModelsData().get(childPosition).getPurpose().toString();

                        final EditText subamountAlertBox = new EditText(ctx);
                        subamountAlertBox.setHint("SubAmount");
                        subamountAlertBox.setInputType(InputType.TYPE_CLASS_NUMBER);
                        subamountAlertBox.setText(existingamount);
                        layout.addView(subamountAlertBox);

                        final EditText purposeAlertBox = new EditText(ctx);
                        purposeAlertBox.setHint("Purpose");
                        purposeAlertBox.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                        purposeAlertBox.setText(existingpurpose);
                        layout.addView(purposeAlertBox);

                        dialog.setView(layout);
                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                models.get(groupPosition).getInnerModelsData().get(childPosition).setInnerAmount(Integer.parseInt(subamountAlertBox.getText().toString()));
                                models.get(groupPosition).getInnerModelsData().get(childPosition).setPurpose(purposeAlertBox.getText().toString());
                                notifyDataSetChanged();
                            }
                        });
                        dialog.show();
                    }
                });
/*
                purposebox.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        models.get(groupPosition).getInnerModelsData().get(childPosition).setPurpose(s.toString());
                    }
                });
        */


                childreferences.put(tagtobeset,convertView);

                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }

            public void addEmptyModel() {
                models.add(new CardViewModel("",0));
                notifyDataSetChanged();
            }

            public Boolean checkUpdateData() {


                final List<CardViewModel> itemsToDelete = new ArrayList<CardViewModel>();
                //final List<CardViewModel> itemsToDelete2ndList = new ArrayList<CardViewModel>();
                //Checking for empty names & empty amount
                for (CardViewModel modelitem: models) {
                    if(modelitem.getName().trim().toString().isEmpty() && modelitem.getAmount() == 0){
                        itemsToDelete.add(modelitem);
                    }
                }

                for (CardViewModel itemtodelte: itemsToDelete) {
                    models.remove(itemtodelte);
                    notifyDataSetChanged();
                    //notifyItemRemoved();
                }
                itemsToDelete.clear();
/*
                //Checking for duplicate names and consolidating them
                for (int i = 0; i< models.size() - 1 ; i++){
                    for(int j = i+1; j<models.size() ; j++){
                        if(models.get(i).getName().toString().equals(models.get(j).getName().toString()) && !models.get(i).getName().toString().isEmpty()){
                            models.get(i).setAmount(models.get(i).getAmount() + models.get(j).getAmount());
                            itemsToDelete2ndList.add(models.get(j));
                        }
                    }
                }

                for (CardViewModel itemtodelte: itemsToDelete2ndList) {
                    models.remove(itemtodelte);
                    notifyDataSetChanged();
                    //notifyItemRemoved();
                }

*/
                //checking for empty names but valid amounts
                for (CardViewModel modelitem: models) {
                    if(modelitem.getName().trim().toString().isEmpty() && modelitem.getAmount() != 0){
                        itemsToDelete.add(modelitem);
                    }
                }
                if(itemsToDelete.size() > 0){
                    return true;
                }
                else{
                    return false;
                }
            }

            public void deleteEmptynameItems() {
                List<CardViewModel> itemsToDelete = new ArrayList<CardViewModel>();

                for (CardViewModel modelitem: models) {
                    if(modelitem.getName().trim().toString().isEmpty() && modelitem.getAmount() != 0){
                        itemsToDelete.add(modelitem);
                    }
                }

                for (CardViewModel itemtodelte : itemsToDelete) {
                    models.remove(itemtodelte);
                    notifyDataSetChanged();
                    //notifyItemRemoved();
                }
            }

            public Boolean IsContainingDuplicateNames(){
                for (int i = 0; i< models.size() - 1 ; i++){
                    for(int j = i+1; j<models.size() ; j++){
                        if(models.get(i).getName().toString().equals(models.get(j).getName().toString()) && !models.get(i).getName().toString().isEmpty()){
                            return true;
                        }
                    }
                }

                return false;
            }

            public ResultData computeTheResults(){
                Integer average = getAverageofModelsAmount(models);
                ResultData output = new ResultData();
                String outputString = "";

                List<CardViewModel> positiveList = new ArrayList<CardViewModel>();
                List<CardViewModel> negativeList = new ArrayList<CardViewModel>();
                HashMap<String,TransferData> OutputFinalList = new HashMap<String, TransferData>();

                for (CardViewModel item:
                        models) {
                    if(item.getAmount() > average){
                        CardViewModel itemForPositiveList = new CardViewModel(item.getName(),item.getAmount() - average);
                        positiveList.add(itemForPositiveList);
                        output.PositiveList.add(item.getName() + " has spent "
                                + item.getAmount() + " which is " + (item.getAmount() - average) + " more than the average");
                    }
                    else{
                        if(item.getAmount() < average){
                            CardViewModel itemForNegativeList = new CardViewModel(item.getName(),average - item.getAmount());
                            negativeList.add(itemForNegativeList);
                            output.NegativeList.add(item.getName() + " has spent "
                                    + item.getAmount() + " which is " + (average - item.getAmount()) + " less than the average");
                        }
                    }
                }

                Collections.sort(positiveList);
                Collections.reverse(positiveList);
                Collections.sort(negativeList);
                Collections.reverse(negativeList);

                //Initializing the list for people who owe money
                for (CardViewModel item:
                        negativeList) {
                    OutputFinalList.put(item.getName(),new TransferData());
                }

                //Displaying Average for testing
                outputString = outputString + "\nAverage : " + average + "\n";

                //Displaying negativeList for testing
                outputString = outputString + "\nNegative List :\n";
                for (CardViewModel item:
                        negativeList) {
                    outputString = outputString + "Name : " + item.getName() + " Amount : " + item.getAmount() + "\n";
                }

                //Displaying positiveList for testing
                outputString = outputString + "\nPositive List :\n";
                for (CardViewModel item:
                        positiveList) {
                    outputString = outputString + "Name : " + item.getName() + " Amount : " + item.getAmount() + "\n";
                }

                do {
                    if(negativeList.size()  == 0){//To be seriously considered if postiveList.size() == 0 can be written or not
                        break;
                    }
                    CardViewModel negativeFirst = negativeList.get(0);
                    CardViewModel positiveFirst = positiveList.get(0);

                    if(negativeFirst.getAmountWithoutTotalling() > positiveFirst.getAmountWithoutTotalling()){
                        negativeFirst.setAmount(negativeFirst.getAmountWithoutTotalling() - positiveFirst.getAmountWithoutTotalling());
                        OutputFinalList.get(negativeFirst.getName()).listOfTransfers.put(positiveFirst.getName(),positiveFirst.getAmountWithoutTotalling());
                        positiveList.remove(0);
                    }
                    else{
                        positiveFirst.setAmount(positiveFirst.getAmountWithoutTotalling() - negativeFirst.getAmountWithoutTotalling());
                        OutputFinalList.get(negativeFirst.getName()).listOfTransfers.put(positiveFirst.getName(),negativeFirst.getAmountWithoutTotalling());
                        negativeList.remove(0);
                    }


                    Collections.sort(positiveList);
                    Collections.reverse(positiveList);
                    Collections.sort(negativeList);
                    Collections.reverse(negativeList);
                }while (negativeList.size() > 0);

                outputString = outputString + "Output :\n";

                for (Map.Entry<String,TransferData> item:
                        OutputFinalList.entrySet()) {
                    for (Map.Entry<String,Integer> innerItem:
                            item.getValue().listOfTransfers.entrySet()) {
                        outputString = outputString + item.getKey().toString() + " should pay " + innerItem.getValue() + " to " + innerItem.getKey().toString() + "\n";
                        output.WhoGivesWhomWhat.add(item.getKey().toString() + " should pay " + innerItem.getValue() + " to " + innerItem.getKey().toString());
                    }
                }

                output.output_old_format = outputString;

                output.contributionsList = models;
                output.setAverage(average);
                output.CategoryWiseList = generateCategoryWiseList();
                return output;
            }
            private Integer getAverageofModelsAmount(List<CardViewModel> models) {
                Integer numbers = models.size();
                Integer total = 0;
                for (int i= 0; i<numbers;i++){
                    total = total + models.get(i).getAmount();
                }
                if(numbers > 0){
                    return total/numbers;
                }
                else{
                    return 0;
                }

            }

            private HashMap<String,Integer> generateCategoryWiseList(){
                HashMap<String, Integer> categoryWiseList = new HashMap<String, Integer>();

                for (CardViewModel model: models) {
                    for (InnerCardViewModel innermodel: model.getInnerModelsData()) {
                        if(categoryWiseList.containsKey(innermodel.getPurpose().toString().toLowerCase())){
                            Integer existingamount = categoryWiseList.get(innermodel.getPurpose().toString().toLowerCase());
                            categoryWiseList.put(innermodel.getPurpose().toString().toLowerCase(),innermodel.getInnerAmount() + existingamount);
                        }
                        else{
                            categoryWiseList.put(innermodel.getPurpose().toString().toLowerCase(),innermodel.getInnerAmount());
                        }
                    }
                }

                return categoryWiseList;
            }

            public void addnewModel(CardViewModel cardViewModel) {
                models.add(cardViewModel);
                notifyDataSetChanged();
            }

            /*
            class TextBoxWatchers implements TextWatcher{
                int groupPos;
                int childPos;
                EditText boxToEdit;
                ExpandableListView listviewreference;

                public TextBoxWatchers(ExpandableListView listviewref, EditText box, int groupPosition, int childPosition) {
                    this.listviewreference = listviewref;
                    boxToEdit = box;
                    this.groupPos = groupPosition;
                    this.childPos = childPosition;
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(models.get(groupPos) != null){
                        switch (boxToEdit.getId()){
                            case R.id.namebox:
                                //models.get(vhref.getAdapterPosition()).setName(s.toString());
                                //notifyItemChanged(position);
                                break;
                            case R.id.subamountbox:
                                if(!s.toString().isEmpty()){
                                    models.get(groupPos).getInnerModelsData().get(childPos).setInnerAmount(Integer.parseInt(s.toString()));
                                }
                                else{
                                    //models.get(vhref.getAdapterPosition()).setAmount(0);
                                }

                                //notifyItemChanged(position);
                                break;
                            default:

                        }
                    }

                }
            }
            */
            public class ViewHolder {
                public TextView namevhbox;
                public TextView amountvhbox;
                public ImageButton dropdownvhbutton;
                public ImageButton outereditvhbutton;
                public ImageButton deletevhbutton;
            }
        }


