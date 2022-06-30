public class Panel {

    private final int row;
    private final int column;
    private AnimalTool tool;

    /**
     * constructor
     * @param row of the panel
     * @param column of the panel
     */
    public Panel(int row, int column){
        this.row = row;
        this.column = column;
        tool = null;
    }

    /**
     * @return the row of the panel
     */
    public int getRow(){
        return row;
    }

    /**
     * @return the column of the panel
     */
    public int getColumn(){
        return column;
    }

    /**
     * @return the tool on the panel
     */
    public AnimalTool getTool() {
        return tool;
    }

    /**
     *
     * @param tool to put on the panel
     */
    public void setTool(AnimalTool tool) {
        this.tool = tool;
    }

    /**
     * remove the tool from the panel
     */
    public void removeTool(){
        this.tool = null;
    }

    /**
     * check if there is tool on the panel
     * @return true if the panel is empty
     */
    public boolean isEmpty(){
        return tool == null;
    }

    /**
     *
     * @return the panel sign if there is no tool, otherwise return the tools name
     */
    public String toString(){
        if(tool == null){
            if(row % 2 == column % 2)
                return "-";
            else
                return "*";
        }
        else {
            return tool.toString();
        }
    }
}
