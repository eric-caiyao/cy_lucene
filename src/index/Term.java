package index;

public class Term {
    private String fieldName;
    private String termValue;
    public Term(String fieldName, String termValue){
        this.fieldName = fieldName;
        this.termValue = termValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getTermValue() {
        return termValue;
    }

    public void setTermValue(String termValue) {
        this.termValue = termValue;
    }

    public boolean equals(Object obj) {
        String fieldName = ((Term)obj).fieldName;
        String termValue = ((Term)obj).termValue;
        return this.fieldName.equalsIgnoreCase(fieldName) && this.termValue.equalsIgnoreCase(termValue);
    }
}
