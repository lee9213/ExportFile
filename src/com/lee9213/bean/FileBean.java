package com.lee9213.bean;

import java.io.File;

/**
 * 文件实体
 *
 * @author lee9213@163.com
 * @version 1.0
 * @date 2016/8/25 14:40
 */
public class FileBean {

    private File file;
    private boolean checked;

    public FileBean(){}
    public FileBean(File file, boolean checked) {
        this.file = file;
        this.checked = checked;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
