import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author lcl100
 * @create 2021-06-12 15:15
 */
class MainJpanel extends JPanel implements MouseListener {

    /* 一些基本参数 */
    // 图片在屏幕中占用的宽高像素值
    private static int IMAGE_WIDTH = 30;
    private static int IMAGE_HEIGHT = 30;
    // 要显示的图形行数和列数
    private static int ROW = 15;
    private static int COLUMN = 15;
    // 要保存二维地图数据的文件路径
    static String SAVE_PATH = "D\\map.txt";
    // 窗口的宽度和高度，在窗口初始化时被赋予
    private static int WINDOW_WIDTH = IMAGE_WIDTH * COLUMN;
    private static int WINDOW_HEIGHT = IMAGE_HEIGHT * ROW;
    // 绘制地图所需要用到的图片文件夹
    private static String IMAGE_DIR_PATH = "";
    // 默认显示的图片块
    private static int DEFAULT_IMAGE = 0;

    List<Byte> list = new ArrayList<>();
    HashMap<Byte, String> pathMap = new HashMap<>();
    static boolean isInitFinished = false;// 只有初始化完成后才开始绘制界面图像
    private Image image;

    private JButton saveMapButton;

    static Byte[][] map;

    public MainJpanel() throws Exception {
        // 添加焦点和事件监听器
        this.setFocusable(true);
        this.addMouseListener(this);
        // 读取一些基本配置
        readProp("prop.properties");
        init();
    }

    /**
     * 从prop.properties配置文件中读取一些基本配置
     *
     * @param propPath properties文件的路径
     * @throws IOException
     */
    private void readProp(String propPath) throws IOException {
        // 创建Properties实例对象
        Properties prop = new Properties();
        // 获取文件输入流
        InputStream is = MainJpanel.class.getClassLoader().getResourceAsStream(propPath);
        // 加载properties配置文件
        prop.load(is);
        // 配置参数
        IMAGE_WIDTH = Integer.parseInt(prop.get("IMAGE_WIDTH").toString());
        IMAGE_HEIGHT = Integer.parseInt(prop.get("IMAGE_HEIGHT").toString());
        ROW = Integer.parseInt(prop.get("ROW").toString());
        COLUMN = Integer.parseInt(prop.get("COLUMN").toString());
        SAVE_PATH = prop.get("SAVE_PATH").toString();
        IMAGE_DIR_PATH = prop.get("IMAGE_DIR_PATH").toString();
        DEFAULT_IMAGE = Integer.parseInt(prop.get("DEFAULT_IMAGE").toString());
    }

    public JPanel getTopPanel() {
        // 创建JPanel
        JPanel topPanel = new JPanel();
        // 设置当前面板为流动布局
        topPanel.setLayout(new FlowLayout());
        // 【保存地图】按钮
        saveMapButton = new JButton("保存地图");
        saveMapButton.setLocation(0, 0);
        saveMapButton.setSize(50, 100);
        // 将按钮添加到面板中
        topPanel.add(saveMapButton);
        // 为按钮注册事件监听器
//        saveMapButton.addActionListener(this);
        saveMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == saveMapButton) {
                    try {
                        new Map(map).saveByTxt(SAVE_PATH);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        return topPanel;
    }

    public void init() throws Exception {
        map = new Byte[ROW][COLUMN];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = (byte) DEFAULT_IMAGE;
            }
        }
        this.list = loadImageDir(IMAGE_DIR_PATH);
        this.isInitFinished = true;// 初始化完成
        repaint();
    }

    private List<Byte> loadImageDir(String dir) throws Exception {
        List<Byte> list = new ArrayList<>();
        File file = new File(dir);
        if (!file.isDirectory()) {
            throw new Exception(dir + "不是一个目录！");
        }
        // 循环遍历file目录中的文件
        for (File f : file.listFiles()) {
            String fileName = f.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);// 获取文件名的后缀
            if ("png".equals(suffix) || "gif".equals(suffix)) {
                String name = fileName.substring(0, fileName.lastIndexOf("."));
                boolean matches = Pattern.matches("[0-9]{1,3}", name);// 简单匹配文件名中只包含数字
                if (matches) {
                    byte num = Byte.valueOf(name);
                    list.add(num);// 将文件名中的数字保存到List集合中
                    pathMap.put(num, f.getAbsolutePath());
                } else {
                    throw new Exception("文件名必须是数字：" + name);
                }
            } else {
                throw new Exception(suffix + "不是可绘制的图片格式！");
            }
        }
        return list;
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        if (isInitFinished) {
            for (int i = 1; i <= ROW; i++) {
                for (int j = 1; j <= COLUMN; j++) {
                    image = Toolkit.getDefaultToolkit().getImage(pathMap.get(map[i - 1][j - 1]));
                    g.drawImage(image, j * IMAGE_HEIGHT, i * IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT, this);
                }
            }
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        // BUTTON1表示鼠标左键；BUTTON3表示鼠标右键
        if (e.getButton() == MouseEvent.BUTTON1) {
            // 鼠标点击的x和y坐标
            int x = e.getX();
            int y = e.getY();
            if (x > COLUMN * getWidth() || y > ROW * getHeight()) {
                return;
            }
            // 根据鼠标点击的x和y坐标计算在二维数组中的行下标和列下标
            int i = (x - 30) / IMAGE_WIDTH;
            int j = (y - 30) / IMAGE_HEIGHT;
            map[j][i] = list.get((list.indexOf(map[j][i]) + 1) % list.size());// 很巧妙的一句代码，用来切换选中的图形块
            repaint();// 重绘
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
