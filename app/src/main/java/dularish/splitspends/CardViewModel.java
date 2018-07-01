package dularish.splitspends;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 10/21/2017.
 */

public class CardViewModel implements Comparable<CardViewModel>, Serializable {

    public CardViewModel(String name, Integer amount) {
        Name = name;
        Amount = amount;
        shouldDisplayMainEditButton = true;
        innerModelsData.add(new InnerCardViewModel(Amount,"Uncategorised"));
        //innerModelsData.add(new InnerCardViewModel(50,"Trial2"));//should be commented later
        //shouldDisplayMainEditButton = false;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private String Name;

    public Boolean shouldDisplayMainEditButton;

    private Integer Amount;

    private List<InnerCardViewModel> innerModelsData = new ArrayList<InnerCardViewModel>();

    public List<InnerCardViewModel> getInnerModelsData() {
        return innerModelsData;
    }

    public void setInnerModelsData(List<InnerCardViewModel> innerModelsData) {
        this.innerModelsData = innerModelsData;
    }

    public void addInnerModelsData(InnerCardViewModel model){
        this.innerModelsData.add(model);
        if(innerModelsData.size() > 1){
            shouldDisplayMainEditButton =false;
        }
    }

    public void deleteInnerModelsData(int indexToDelete){
        this.innerModelsData.remove(indexToDelete);
        if(innerModelsData.size() > 1){
            shouldDisplayMainEditButton = false;
        }
        else{
            shouldDisplayMainEditButton = true;
        }
    }


    public Integer getAmount() {
        Integer total = 0;
        for (InnerCardViewModel innermodel: innerModelsData) {
            total = total + innermodel.getInnerAmount();
        }
        //return Amount;
        return total;
    }

    public Integer getAmountWithoutTotalling(){
        return Amount;
    }

    public void setAmount(Integer amount) {
        Amount = amount;
    }

    @Override
    public int compareTo(@NonNull CardViewModel tocomparewith) {
        if(this.getAmount() > tocomparewith.getAmount()){
            return 1;
        }
        else
            return -1;
    }
}
