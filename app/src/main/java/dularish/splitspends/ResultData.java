package dularish.splitspends;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pc on 10/22/2017.
 */

class ResultData implements Serializable {
    String output_old_format;

    public Integer getAverage() {
        return average;
    }

    public void setAverage(Integer average) {
        this.average = average;
    }

    Integer average;

    List<String> NegativeList = new ArrayList<String>();
    List<String> PositiveList = new ArrayList<String>();
    List<String> WhoGivesWhomWhat = new ArrayList<String>();
    HashMap<String, Integer> CategoryWiseList = new HashMap<String, Integer>();

    List<CardViewModel> contributionsList = new ArrayList<CardViewModel>();
}
