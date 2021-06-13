import javax.swing.*;
import java.awt.*;

/**
 * @author lcl100
 * @create 2021-06-12 20:31
 */
public class MainApp extends JFrame {
    public MainApp() throws Exception {
        // 设置窗口基本信息
        setTitle("制作地图");
        setLocation(200, 200);
        setSize(520, 600);
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new MainJpanel(), BorderLayout.CENTER);
        contentPane.add(new MainJpanel().getTopPanel(), BorderLayout.NORTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public static void main(String[] args) throws Exception {
        new MainApp().setVisible(true);
    }
}
