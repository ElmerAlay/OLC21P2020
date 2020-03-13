package charts;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 *
 * @author junio
 */
public class PieCharts {
    private Object data[];
    private Object labels[];
    private String title;

    public PieCharts(Object[] data, Object[] labels, String title) {
        this.data = data;
        this.labels = labels;
        this.title = title;
    }
    
    public void initAndShow(){
        JFrame frame = new JFrame("Pie Chart");
        JFXPanel fxpanel = new JFXPanel();
        frame.add(fxpanel);
        frame.setSize(550,450);
        frame.setVisible(true);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxpanel);
            }
        });
    }
    
    private void initFX(JFXPanel panel){
        Scene scene = createScene();
        panel.setScene(scene);
    }
    
    private Scene createScene(){
        Group root = new Group();
        
        ObservableList<PieChart.Data> valueList = FXCollections.observableArrayList();
        for(int i=0; i<data.length; i++){
            valueList.add(new PieChart.Data(labels[i].toString(), Float.parseFloat(data[i].toString())));
        }
        
        PieChart pieChart = new PieChart(valueList);
        pieChart.setTitle(title);
        pieChart.setLegendSide(Side.RIGHT);
        //pieChart.setLegendVisible(true);
        /*pieChart.getData().stream().forEach(data -> 
                data.nameProperty().bind(
                    Bindings.concat(
                        data.getName(), ": ", data.pieValueProperty(), ""
                    )
                )
        );*/
        pieChart.getData().forEach(this::installTooltip);
        root.getChildren().addAll(pieChart);
        Scene scene = new Scene(root, 550, 450);
        
        return scene;
    }
    
    public void installTooltip(PieChart.Data d) {
        String msg = String.format("%s : %s", d.getName(), d.getPieValue());

        Tooltip tt = new Tooltip(msg);
        tt.setStyle("-fx-background-color: gray; -fx-text-fill: whitesmoke;");

        Tooltip.install(d.getNode(), tt);
    }
    
    public void start(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initAndShow();
            }
        });
    }
}
