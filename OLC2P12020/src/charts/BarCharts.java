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


/**
 *
 * @author junio
 */
public class BarCharts {
    private Object data[];
    private String xlab;
    private String ylab;
    private String title;
    private Object labels[];

    public BarCharts(Object[] data, String xlab, String ylab, String title, Object[] labels) {
        this.data = data;
        this.xlab = xlab;
        this.ylab = ylab;
        this.title = title;
        this.labels = labels;
    }
    
    public void initAndShow(){
        JFrame frame = new JFrame("Bar Chart");
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
        Group root = new Group();
        
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(xlab);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(ylab);

        BarChart chart = new BarChart(xAxis, yAxis);
        chart.setTitle(title);
        
        XYChart.Series<String, Number> vals = new XYChart.Series<>();
        ObservableList<XYChart.Series<String, Number>> valueList = FXCollections.observableArrayList();
        for(int i=0; i<data.length; i++){
            //XYChart.Series<String, Number> vals = new XYChart.Series<>();
            vals.getData().add(new XYChart.Data<>(labels[i].toString(), Float.parseFloat(data[i].toString())));
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
