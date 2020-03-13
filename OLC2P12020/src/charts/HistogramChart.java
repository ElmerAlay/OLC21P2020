package charts;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import stadisticFunctions.SupportFunctions;


/**
 *
 * @author junio
 */
public class HistogramChart {
    private Object data[];
    private String xlab;
    private String title;

    public HistogramChart(Object[] data, String xlab, String title) {
        this.data = data;
        this.xlab = xlab;
        this.title = title;
    }
    
    public void initAndShow(){
        JFrame frame = new JFrame("Histogram");
        //Button btn_salir = new Button("Salir");
        final JFXPanel fxpanel = new JFXPanel();
        frame.add(fxpanel);
        //frame.add(btn_salir);
        frame.setSize(550,450);
        frame.setVisible(true);
        //frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        
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
        int cont[] = {0,0,0,0,0,0};
        for(int i=0; i<data.length; i++){
            if(Float.parseFloat((data[i]).toString())>0 && Float.parseFloat((data[i]).toString())<=5){
                cont[0]++;
            }
            if(Float.parseFloat((data[i]).toString())>5 && Float.parseFloat((data[i]).toString())<=10){
                cont[1]++;
            }
            if(Float.parseFloat((data[i]).toString())>10 && Float.parseFloat((data[i]).toString())<=15){
                cont[2]++;
            }
            if(Float.parseFloat((data[i]).toString())>15 && Float.parseFloat((data[i]).toString())<=20){
                cont[3]++;
            }
            if(Float.parseFloat((data[i]).toString())>20 && Float.parseFloat((data[i]).toString())<=25){
                cont[4]++;
            }
            if(Float.parseFloat((data[i]).toString())>25 && Float.parseFloat((data[i]).toString())<=30){
                cont[5]++;
            }
        }
        
        Group root = new Group();
        
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(xlab);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("");

        BarChart chart = new BarChart(xAxis, yAxis);
        chart.setTitle(title);
        chart.setCategoryGap(0);
        chart.setBarGap(0);
        
        XYChart.Series<String, Number> vals = new XYChart.Series<>();
        ObservableList<XYChart.Series<String, Number>> valueList = FXCollections.observableArrayList();
        for(int i=1; i<=6; i++){
            vals.getData().add(new XYChart.Data<>(""+(i*5), cont[i-1]));
        }
        valueList.addAll(vals);
        chart.setData(valueList);
        
        root.getChildren().addAll(chart);
        Scene scene = new Scene(root, 550, 450);
        
        return scene;
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
