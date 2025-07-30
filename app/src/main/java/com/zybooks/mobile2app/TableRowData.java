package com.zybooks.mobile2app;

/* *********************************************************************************************
 * This object defines the data for each row in the RecyclerView
 * it defines the second, third, and fourth columns of the table
 * and the getters and setters for each row item
 * *********************************************************************************************/
public class TableRowData {
    private String column2;
    private String column3;
    private int column4;

    // Constructor
    public TableRowData(String col2, String col3, int col4) {
        this.column2 = col2;
        this.column3 = col3;
        this.column4 = col4;
    }

    // Getters
    public String getColumn2() { return column2; }
    public String getColumn3() { return column3; }
    public int getColumn4() { return column4; }

    // Setters
    public void setColumn2(String column2) { this.column2 = column2; }
    public void setColumn3(String column3) { this.column3 = column3; }
    public void setColumn4(int column4) { this.column4 = column4; }
}
