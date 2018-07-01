package dularish.splitspends;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Results extends AppCompatActivity {
    FragmentPagerAdapter vpadapter;
    Intent intentref;
    ResultData dataToPass;

    TextView resultview;
    Button whatsappshareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intentusedToCall = getIntent();
        ResultData dataReceived = (ResultData)intentusedToCall.getSerializableExtra("Result");
        dataToPass = dataReceived;

        ViewPager vpager = (ViewPager) findViewById(R.id.vPager);
        vpadapter = new MyPageAdapter(getSupportFragmentManager());
        vpager.setAdapter(vpadapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpager);

        /*
        String bulletwithspace = getResources().getString(R.string.bulletText) + " ";

        resultview = (TextView) findViewById(R.id.resultsActivity_Output_View);
        resultview.setText("");

        Intent intentusedToCall = getIntent();
        ResultData dateReceived = (ResultData)intentusedToCall.getSerializableExtra("Result");
        resultview.setText(resultview.getText() + "Split Spends Summary : \n");
        for (String item:
             dateReceived.WhoGivesWhomWhat) {
            String itemLine = bulletwithspace + item + "\n";
            resultview.setText(resultview.getText() + itemLine);
        }

        whatsappshareButton = (Button) findViewById(R.id.whatsapp_share_button);

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
                    Toast.makeText(Results.this, "Whatsapp has not been installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        */
    }
    public class MyPageAdapter extends FragmentPagerAdapter{
        private int NUM_ITEMS = 2;
        private String tabTitles[] = new String[] { "Summary", "Calculation Details" };

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0 :
                    Results_Activity_fragment_tab_1 newfragment = new Results_Activity_fragment_tab_1();
                    return newfragment.newInstance("Summary Tab",dataToPass);
                case 1 :
                    Results_Activity_fragment_tab_2 newfragment1 = new Results_Activity_fragment_tab_2();
                    return newfragment1.newInstance("Calculation Details Tab",dataToPass);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
