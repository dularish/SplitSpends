package dularish.splitspends;

/**
 * Created by pc on 1/15/2018.
 */

public class SplitSpendsListModel {
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public CardViewModel getModel() {
        return model;
    }

    public void setModel(CardViewModel model) {
        this.model = model;
    }

    String Title;
    String CreatedBy;
    CardViewModel model;
}
