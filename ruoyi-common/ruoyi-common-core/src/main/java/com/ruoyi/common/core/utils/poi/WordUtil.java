package com.ruoyi.common.core.utils.poi;

import com.deepoove.poi.XWPFTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author lsy
 * @description word工具类
 * @date 2025/7/31
 */
public class WordUtil {

    /**
     * 根据模板导出文档
     *
     * @param templatePath 模板路径
     * @param outPath 保存路径
     * @param data 导入数据
     */
    public static void exportWordByTemplate(String templatePath, String outPath, Map<String, Object> data) throws IOException {
        XWPFTemplate xwpfTemplate = XWPFTemplate.compile(templatePath).render(data);
        // 生成证书文件
        File file = new File(outPath);
        if (file.exists()) {
            file.delete();
        } else if (! file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (! file.exists()) {
            file.createNewFile();
        }
        xwpfTemplate.writeAndClose(new FileOutputStream(file));
    }

}
