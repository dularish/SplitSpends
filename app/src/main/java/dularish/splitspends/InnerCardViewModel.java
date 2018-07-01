package dularish.splitspends;

import java.io.Serializable;

/**
 * Created by pc on 10/29/2017.
 */

public class InnerCardViewModel implements Serializable {

    public Integer Amount;

    public String Purpose;


    public InnerCardViewModel(Integer amount, String purpose) {
        Amount = amount;
        Purpose = purpose;
    }

    public Integer getInnerAmount() {
        return Amount;
    }

    public void setInnerAmount(Integer amount) {
        Amount = amount;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }
}
