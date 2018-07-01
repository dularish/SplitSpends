package dularish.splitspends;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by pc on 10/23/2017.
 */

public class Results_Activity_fragment_tab_1 extends android.support.v4.app.Fragment {
    private String titleofFragment;
    TextView resultview;
    ResultData dataReceived;

    String ResultGenerated;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.results_activity_tab_1,container,false);
        TextView tvlabel = (TextView) view.findViewById(R.id.resultsActivity_tab_header_View_tab_1);
        //tvlabel.setText("Tab Number : " + 1 + " Tab title : " + titleofFragment);

        String bulletwithspace = getResources().getString(R.string.bulletText) + " ";
        //ResultGenerated = "Split Spends Summary : \n";
        ResultGenerated = "\n";
        for (String item:
                dataReceived.WhoGivesWhomWhat) {
            String itemLine = bulletwithspace + item.toString() + "\n";
            ResultGenerated = ResultGenerated + itemLine;
        }

        resultview = (TextView) view.findViewById(R.id.resultsActivity_Output_View_tab_1);
        resultview.setText(ResultGenerated);

        Button whatsappshareButton = (Button) view.findViewById(R.id.whatsapp_share_button_tab_1);

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

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleofFragment = getArguments().getString("title");
        //ResultGenerated = getArguments().getString("resultGenerated");
        dataReceived = (ResultData) getArguments().getSerializable("dataPassed");

    }

    public  Results_Activity_fragment_tab_1 newInstance(String titlefromargs,  ResultData dataReceived){
        Results_Activity_fragment_tab_1 firstFragment = new Results_Activity_fragment_tab_1();

        Bundle args = new Bundle();
        args.putString("title",titlefromargs);
        args.putSerializable("dataPassed",dataReceived);

        firstFragment.setArguments(args);
        return firstFragment;
    }
}
