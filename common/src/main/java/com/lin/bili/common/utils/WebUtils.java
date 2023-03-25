package com.lin.bili.common.utils;

import cn.hutool.http.HttpRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频工具类
 */
public class WebUtils {
    /**
     * 单个文件上传
     * @param req
     * @param url
     * @return
     */
    public static File upload(HttpServletRequest req, String url) {
        if (ServletFileUpload.isMultipartContent(req)) {
            FileItemFactory fileItemFactory = new DiskFileItemFactory();
            ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
            try {
                List<FileItem> fileItems = servletFileUpload.parseRequest(req);
                for (FileItem fileItem : fileItems) {
                    if (!fileItem.isFormField()) {
                        File file = new File(url);
                        fileItem.write(file);
                        return file;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
