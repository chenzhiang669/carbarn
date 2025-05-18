package com.carbarn.inter.utils.alibaba;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class PlateMasking {
    public static byte[] writeImageToByteArray(BufferedImage image, String picture_type) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 将 BufferedImage 写入到 ByteArrayOutputStream 中，格式为 "jpg"
            ImageIO.write(image, picture_type, baos);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static BufferedImage readImageFromByteArray(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            System.out.println("字节数组为空或长度为0！");
            return null;
        }

        try (ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)) {
            // 使用 ImageIO.read 方法从 ByteArrayInputStream 中读取图片
            return ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] masking(int plateX,
                            int plateY,
                            int plateWidth,
                            int plateHeight,
                            String picture_name,
                            String picture_type,
                            byte[] content) {
        try {
//            BufferedImage image = ImageIO.read(inputFile);
            BufferedImage image = readImageFromByteArray(content);

            // 打码处理
            maskPlate(image, plateX, plateY, plateWidth, plateHeight);

            byte[] imageBytes = writeImageToByteArray(image, picture_type);

            // 保存打码后的图片
            File outputFile = new File("D:\\carbarn\\汽车车牌打码\\" + picture_name + "." + picture_type);
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(imageBytes);
            System.out.println("车牌打码完成，结果已保存到：" + outputFile.getAbsolutePath());
            return imageBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] masking(int plateX,
                                 int plateY,
                                 int plateWidth,
                                 int plateHeight,
                                 String picture_type,
                                 byte[] content) {
        try {
//            BufferedImage image = ImageIO.read(inputFile);
            BufferedImage image = readImageFromByteArray(content);

            // 打码处理
            maskPlate(image, plateX, plateY, plateWidth, plateHeight);

            byte[] imageBytes = writeImageToByteArray(image, picture_type);
            return imageBytes;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 对车牌区域进行打码
     *
     * @param image  图像
     * @param x      车牌左上角X坐标
     * @param y      车牌左上角Y坐标
     * @param width  车牌宽度
     * @param height 车牌高度
     */
    public static void maskPlate(BufferedImage image, int x, int y, int width, int height) {
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE); // 设置打码颜色为黑色
        g2d.fillRect(x, y, width, height); // 填充矩形区域
        g2d.dispose();
    }

    public static void main(String[] args) {
        String file = "D:\\carbarn\\汽车车牌打码\\E0Ko6D0XJm1746072549645.jpg";
        byte[] imageBytes = ChepaiOcr.readImageToByteArray(file);

        masking(63, 670, 108,83 , "aaaaa", "jpg", imageBytes);

    }
}
