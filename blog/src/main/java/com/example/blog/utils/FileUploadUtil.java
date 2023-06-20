package com.example.blog.utils;

import com.example.blog.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FileUploadUtil {

    /**
     * 返回文件的文件名和文件后缀
     * @param filename 文件名
     * @return res[0] 文件名 res[1] 文件后缀
     */
    public static String[] divideFileName(String filename){
        String[] res = new String[2];

        StringBuilder name = new StringBuilder();
        List<String> collect = Arrays.stream(filename.split("\\.")).collect(Collectors.toList());
        for(int i = 0; i < collect.size() - 1; ++ i){
            name.append(collect.get(i));
        }
        res[0] = name.toString();
        res[1] = collect.get(collect.size() - 1);

        return res;
    }

    /**
     * 将文件上传到指定位置
     * @param prefix 文件上传位置
     * @param newFilename 新建的文件名字
     * @param file 上穿的文件
     */
    public static boolean createAndTransferFile(String dirPath,String newFilename, MultipartFile file) {
        // 如果目录不存在创建目录
        File dir = new File(dirPath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        File newFile = new File(dir, newFilename);
        try {
            file.transferTo(newFile);
        } catch (IOException ioException) {
            log.error("创建文件时出现异常");
            ioException.printStackTrace();
            return false;
        }
        return true;
    }
}
