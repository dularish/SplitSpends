package dularish.splitspends;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainExpListActivity extends AppCompatActivity {
    ExpandableListView expandableListView;
    Button addingnewbutton;
    Button calculateamountbutton;
    Context contextref;
    private Button saveToDataBaseButton;
    private Button loadFromDataBaseButton;
    private TextView tview;


    //Some changes and built
    //TextView outputView;
    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        contextref = this;
        setContentView(R.layout.activity_main_exp_list);
        try{
            expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

            final ExpandableListAdapter adapterref = new ExpandableListAdapter(getexampleData(),this,expandableListView);
            expandableListView.setAdapter(adapterref);

            addingnewbutton = (Button) findViewById(R.id.addContributor);
            addingnewbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(v.getContext());
                    dialog.setTitle("Enter Name & Amount");
                    LinearLayout layout = new LinearLayout(v.getContext());
                    layout.setOrientation(LinearLayout.VERTICAL);


                    final EditText nameAlertBox = new EditText(v.getContext());
                    nameAlertBox.setHint("Name");

                    nameAlertBox.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    layout.addView(nameAlertBox);

                    final EditText amountAlertBox = new EditText(v.getContext());
                    amountAlertBox.setHint("Amount");
                    amountAlertBox.setInputType(InputType.TYPE_CLASS_NUMBER);
                    layout.addView(amountAlertBox);

                    dialog.setView(layout);
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapterref.addnewModel(new CardViewModel(nameAlertBox.getText().toString(),Integer.parseInt(amountAlertBox.getText().toString())));
                        }
                    });
                    dialog.show();

                }
            });
            //outputView = (TextView) findViewById(R.id.outputview);

            calculateamountbutton = (Button) findViewById(R.id.calculateBtn);
            calculateamountbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    //getmodelsFromView();
                    //adapterinstance.notifyDataSetChanged();
                    //recyclerview.invalidate();

                    if(adapterref.IsContainingDuplicateNames()){
                        AlertDialog.Builder alertdialog = new AlertDialog.Builder(v.getContext());
                        alertdialog.setTitle("Invalid Data");
                        alertdialog.setMessage("The list contains duplicate names. Please correct the list");
                        alertdialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertdialog.show();
                        //Toast.makeText(MainExpListActivity.this, "The list contains duplicate names. Please correct the list", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(adapterref.checkUpdateData()){
                        AlertDialog.Builder alertdialog = new AlertDialog.Builder(v.getContext());
                        alertdialog.setTitle("Invalid Data");
                        alertdialog.setMessage("One or more entries have empty name but non-zero amounts. Delete them?");
                        alertdialog.setCancelable(true);
                        alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapterref.deleteEmptynameItems();
                                ResultData DataToPass = adapterref.computeTheResults();
                                //outputView.setText(adapterref.computeTheResults().output_old_format);
                                adapterref.notifyDataSetChanged();

                                Intent resultPageIntent = new Intent(v.getContext(),Results.class);
                                resultPageIntent.putExtra("Result",DataToPass);
                                startActivity(resultPageIntent);
                            }
                        });
                        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(v.getContext(), "Please correct the list", Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertdialog.show();
                    }
                    else{
                        ResultData DataToPass = adapterref.computeTheResults();
                        //outputView.setText(adapterref.computeTheResults().output_old_format);
                        adapterref.notifyDataSetChanged();

                        Intent resultPageIntent = new Intent(v.getContext(),Results.class);
                        resultPageIntent.putExtra("Result",DataToPass);
                        startActivity(resultPageIntent);
                    }

                }
            });
            final FirebaseDatabase database = FirebaseDatabase.getInstance();

            //FirebaseDatabase.getInstance().goOnline();
            DatabaseReference myRef = database.getInstance().getReference("message");

            myRef.setValue("Hello, World12345");
            //myRef.wait();
            //FirebaseDatabase.getInstance().goOffline();
            saveToDataBaseButton = (Button) findViewById(R.id.savetodbbutton);
            saveToDataBaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //FirebaseDatabase.getInstance().goOnline();
                    String jsonString = getJsonStringOfObject(adapterref.models);
                    DatabaseReference myref = database.getInstance().getReference("testKey");
                    myref.setValue(jsonString);
                    //FirebaseDatabase.getInstance().goOffline();
                }
            });
            tview = (TextView) findViewById(R.id.outputview);
            loadFromDataBaseButton = (Button) findViewById(R.id.loadfromdbbutton);
            loadFromDataBaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //FirebaseDatabase.getInstance().goOnline();
                    DatabaseReference myref = database.getInstance().getReference("testKey");
                    myref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            tview.setText( (String)dataSnapshot.getValue());
                            Toast t1 = Toast.makeText(getApplicationContext(), (String)dataSnapshot.getValue(), Toast.LENGTH_LONG);
                            t1.show();
                            Gson gson = new Gson();
                            List<CardViewModel> modelFromJson = gson.fromJson((String)dataSnapshot.getValue(), new TypeToken<List<CardViewModel>>(){}.getType());
                            adapterref.models = modelFromJson;
                            adapterref.notifyDataSetChanged();
                            //FirebaseDatabase.getInstance().goOffline();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            });
            /*
            String outputString = loadAStringFile();
            Toast t1 = Toast.makeText(getApplicationContext(), outputString, Toast.LENGTH_LONG);
            t1.show();

            tview.setText(outputString);
            saveAnObjectToFile(adapterref.models);*/
            checkForUpdates();
        }catch(Exception e){
            Log.e("SplitSpendsListActivity","An error occurred in the application :" + e.getMessage());
            Toast.makeText(this,"An unexpected error occured",Toast.LENGTH_SHORT);
        }
    }



    private void saveAStringToFile(String s){
        try{
            Gson gson = new Gson();
            String jsonString = gson.toJson(s);

            String filename = "justASampleStringFile";
            FileOutputStream outputStream;

            outputStream = openFileOutput(filename,Context.MODE_PRIVATE);
            outputStream.write(jsonString.getBytes());
            outputStream.close();
        }
        catch(Exception ex){
            Log.e("MainPage", "An exception caught while saving the file : " + ex.getMessage());
            Toast.makeText(this,"An error occurred while saving", Toast.LENGTH_SHORT);
        }
    }

    private void saveAnObjectToFile(Object s){
        try{
            Gson gson = new Gson();
            String jsonString = gson.toJson(s);

            String filename = "justASampleStringFile";
            FileOutputStream outputStream;

            outputStream = openFileOutput(filename,Context.MODE_PRIVATE);
            outputStream.write(jsonString.getBytes());
            outputStream.close();
        }
        catch(Exception ex){
            Log.e("MainPage", "An exception caught while saving the file : " + ex.getMessage());
            Toast.makeText(this,"An error occurred while saving", Toast.LENGTH_SHORT);
        }
    }

    private String getJsonStringOfObject(Object s){
        try{
            Gson gson = new Gson();
            String jsonString = gson.toJson(s);

            return jsonString;
        }
        catch(Exception ex){
            Log.e("MainPage", "An exception caught while getting JsonString of Object : " + ex.getMessage());
            Toast.makeText(this,"An error occurred while getting jsonString", Toast.LENGTH_SHORT);
            return "";
        }
    }

    private String loadAStringFile(){
        try{
            InputStream ipstream = openFileInput("justASampleStringFile");

            InputStreamReader streamReader = new InputStreamReader(ipstream);
            BufferedReader buffreader = new BufferedReader(streamReader);
            String line, completeText = "";

            while ( (line = buffreader.readLine()) != null){
                completeText += line;
            }
            return completeText;
        }
        catch (Exception ex){
            Toast.makeText(this,"An error occurred while reading file", Toast.LENGTH_SHORT);
            return "error";
        }
    }

    private List<CardViewModel> getexampleData(){
        List<CardViewModel> models = new ArrayList<CardViewModel>();

        models.add(new CardViewModel("Rick",1500));
        models.add(new CardViewModel("Jack",2500));
        models.add(new CardViewModel("Rick",3500));

        return models;
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }
}
