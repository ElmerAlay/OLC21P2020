package charts;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 *
 * @author junio
 */
public class LineCharts {
    private Object data[];
    private String type;
    private String xlab;
    private String ylab;
    private String title;

    public LineCharts(Object[] data, String type, String xlab, String ylab, String title) {
        this.data = data;
        this.type = type;
        this.xlab = xlab;
        this.ylab = ylab;
        this.title = title;
    }
    
    public void initAndShow(){
        JFrame frame = new JFrame("Line Chart");
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
        
        Axis xAxis = new NumberAxis();
        xAxis.setLabel(xlab);
        Axis yAxis = new NumberAxis();
        yAxis.setLabel(ylab);
        
        
        ObservableList<XYChart.Series> valueList = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> aList = FXCollections.observableArrayList();
        for(int i=0; i<data.length; i++){
            //XYChart.Series<String, Number> vals = new XYChart.Series<>();
            aList.add(new XYChart.Data((i+1), Float.parseFloat(data[i].toString())));
        }
        valueList.addAll(new XYChart.Series("Data", aList));
        
        LineChart chart = new LineChart(xAxis, yAxis, valueList);
        chart.setTitle(title);
        
        if(type.toLowerCase().equals("i"))
            chart.setCreateSymbols(false);
        
        chart.setData(valueList);
        
        root.getChildren().addAll(chart);
        Scene scene = new Scene(root, 550, 450);
        if(type.toLowerCase().equals("p"))
            scene.getStylesheets().add(getClass().getResource("resources/style.css").toExternalForm());
        
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
