package com.lee9213.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 目录实体
 *
 * @author lee9213@163.com
 * @version 1.0
 * @date 2016/8/25 14:42
 */
public class DirectoryBean {

    /**
     * 目录下的所有文件
     */
    private List<FileBean> files;

//    private CheckedTreeNode node;

    private String path;

    public DirectoryBean(String path) {
        this.path = path;
    }

    public List<FileBean> getFiles() {
        return files;
    }

    public void addFile(File file){
        if(this.files == null)files = new ArrayList<>();
        files.add(new FileBean(file,false));
    }
    public void addFile(File file,boolean checked){
        if(this.files == null)files = new ArrayList<>();
        files.add(new FileBean(file,checked));
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
