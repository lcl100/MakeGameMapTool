import java.io.*;

/**
 * @author lcl100
 * @create 2021-06-12 17:14
 */
public class Map implements Serializable {
    private byte[][] map;

    public Map() {
    }

    public Map(Byte[][] map) {
        byte[][] temp = new byte[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                temp[i][j] = map[i][j];
            }
        }
        this.map = temp;
    }

    /**
     * 将地图数据以序列化的方式保存到本地文件中
     *
     * @param filePath 要保存的文件路径
     * @throws IOException
     */
    public void save(String filePath) throws IOException {
        ObjectOutput oo = new ObjectOutputStream(new FileOutputStream(filePath));
        oo.writeObject(map);
    }

    /**
     * 将地图数据从序列化文件中读取出来，返回一个二维数组
     *
     * @param filePath
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public byte[][] read(String filePath) throws IOException, ClassNotFoundException {
        ObjectInput oo = new ObjectInputStream(new FileInputStream(filePath));
        return (byte[][]) oo.readObject();
    }

    /**
     * 以txt文本文档保存地图数据，以直接保存一个二维数组，可以直接复制使用
     *
     * @param filePath
     * @throws IOException
     */
    public void saveByTxt(String filePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write("{".getBytes());
        fos.write("\n".getBytes());
        for (int i = 0; i < map.length; i++) {
            fos.write("{".getBytes());
            for (int j = 0; j < map[i].length; j++) {
                fos.write(String.valueOf(map[i][j]).getBytes());
                if (j != map[i].length - 1) {
                    fos.write(", ".getBytes());
                }
            }
            fos.write("}".getBytes());
            if (i != map.length - 1) {
                fos.write(",".getBytes());
            }
            fos.write("\n".getBytes());
        }
        fos.write("}".getBytes());
    }
}
