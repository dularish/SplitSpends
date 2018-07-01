package dularish.splitspends;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by pc on 10/23/2017.
 */

public class Results_Activity_fragment_tab_2 extends Fragment {
    private static String titleofFragment;
    private ResultData dataReceived;
    private TextView resultview;
    private PieChart pieChart;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.results_activity_tab_2,container,false);
        TextView tvlabel = (TextView) view.findViewById(R.id.resultsActivity_tab_header_View_tab_2);
        //tvlabel.setText("Tab Number : " + 2 + " Tab title : " + titleofFragment);
        resultview = (TextView) view.findViewById(R.id.resultsActivity_Output_View_tab_2);

        String outputText = "";
        //outputText = outputText + "Calculation Details : \n";
        outputText = outputText + "\n";
        for (CardViewModel item : dataReceived.contributionsList){
            if(item.getAmount() > dataReceived.getAverage()){
                outputText = outputText + item.getName().toString() + " has spent "
                        + item.getAmount() + " which is " + (item.getAmount() - dataReceived.average) +
                        " more than the Average\n";
            }
            else if(item.getAmount() < dataReceived.getAverage()){
                outputText = outputText + item.getName().toString() + " has spent "
                        + item.getAmount() + " which is " + (dataReceived.getAverage() - item.getAmount()) +
                        " less than the Average\n";
            }
            else {
                outputText = outputText + item.getName().toString() + " has spent "
                        + item.getAmount() + " which is " +
                        " exactly the Average\n";
            }

        }
        outputText = outputText + "\nThe average is " + dataReceived.getAverage() + "\n";

        resultview.setText(outputText);

        Button whatsappshareButton = (Button) view.findViewById(R.id.whatsapp_share_button_tab_2);

        whatsappshareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT,resultview.getText().toString());
                try{
                    startActivity(whatsappIntent);
                }
                catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(getActivity(), "Whatsapp has not been installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pieChart = (PieChart) view.findViewById(R.id.piechart);
        if(dataReceived.CategoryWiseList.size() > 1){
            pieChart.setVisibility(View.VISIBLE);
            generatePieChart();
        }
        else{
            pieChart.setVisibility(View.INVISIBLE);
        }


        return view;
    }

    private void generatePieChart() {
        List<PieEntry> pieEntries = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : dataReceived.CategoryWiseList.entrySet()) {
            pieEntries.add(new PieEntry(entry.getValue(),entry.getKey()));
        }

        PieDataSet pieDS = new PieDataSet(pieEntries,"Spends Split Up");
        pieDS.setSliceSpace(0f);
        pieDS.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(pieDS);
        pieChart.setMinimumWidth(500);
        pieChart.setMinimumHeight(500);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleofFragment = getArguments().getString("title");
        dataReceived = (ResultData) getArguments().getSerializable("dataPassed");
    }

    public static Results_Activity_fragment_tab_2 newInstance(String titlefromargs, ResultData dataReceived){
        Results_Activity_fragment_tab_2 firstFragment = new Results_Activity_fragment_tab_2();

        Bundle args = new Bundle();
        args.putString("title",titlefromargs);
        args.putSerializable("dataPassed",dataReceived);
        firstFragment.setArguments(args);

        return firstFragment;
    }
}
