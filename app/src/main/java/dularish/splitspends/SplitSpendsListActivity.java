package dularish.splitspends;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.util.ArrayList;
import java.util.List;

public class SplitSpendsListActivity extends AppCompatActivity {
    Button calculateamountbutton;
    Button addingnewbutton;
    TextView outputView;

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

    public MainActivityAdapter adapterref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final MainActivityAdapter adapterinstance = new MainActivityAdapter();
        final RecyclerView recyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapterinstance);
        adapterref = adapterinstance;


        checkForUpdates();
    }


    private List<CardViewModel> getmodelsFromView(){
        List<CardViewModel> models = new ArrayList<CardViewModel>();
        RecyclerView view = ((RecyclerView) findViewById(R.id.recycler_view));
        int count = ((RecyclerView) findViewById(R.id.recycler_view)).getChildCount();
        for (int i = 0; i<count; i++){
            CardView view2 = (CardView) view.getChildAt(i);
            EditText namebox = (EditText) view2.findViewById(R.id.namebox);
            EditText amountbox = (EditText) view2.findViewById(R.id.amountbox);
            models.add(new CardViewModel(namebox.getText().toString(),Integer.parseInt(amountbox.getText().toString())));
        }

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
