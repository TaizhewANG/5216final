package au.edu.canberra.mtfinalassignment;

// Please do not change this class name
public class ClassifiedItem {
    // Please do not change anything in this class
    private String itemName; // do not change
    private String classifiedResult; // do not change
    private String imageFileName; // do not change

    public ClassifiedItem(String itemName, String classifiedResult, String imageFileName) {
        this.setItemName(itemName);
        this.setClassifiedResult(classifiedResult);
        this.setImageFileName(imageFileName);
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getClassifiedResult() {
        return classifiedResult;
    }

    public void setClassifiedResult(String classifiedResult) {
        this.classifiedResult = classifiedResult;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    @Override
    public String toString() {
        return itemName;
}
}
