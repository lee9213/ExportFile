package com.lee9213.listener;

import com.intellij.ui.CheckBoxList;
import com.intellij.ui.CheckedTreeNode;
import com.lee9213.bean.DirectoryBean;
import com.lee9213.bean.FileBean;
import com.lee9213.ui.CheckBoxTree;
import com.lee9213.util.StringUtil;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 文件夹单击事件
 *
 * @author lee9213@163.com
 * @version 1.0
 * @date 2016/8/24 17:58
 */
public class TreeSelectedListener implements TreeSelectionListener {

    private CheckBoxList checkBoxList;
    private Map<CheckedTreeNode,DirectoryBean> fileMap;

    public TreeSelectedListener(CheckBoxList checkBoxList,Map<CheckedTreeNode,DirectoryBean>fileMap) {
        this.checkBoxList = checkBoxList;
        this.fileMap = fileMap;
    }

    /**
     * 改变选中文件夹
     * @param event
     */
    @Override
    public void valueChanged(TreeSelectionEvent event) {
        CheckBoxTree checkBoxTree = (CheckBoxTree) event.getSource();
        CheckedTreeNode node = (CheckedTreeNode) checkBoxTree.getLastSelectedPathComponent();
        if(node == null)return;

        StringUtil.DEFAULT_PATH = node;
        DirectoryBean bean = fileMap.get(node);
        List<FileBean> fileBeanList = bean.getFiles();
        checkBoxList.clear();
        if(fileBeanList == null || fileBeanList.size() == 0)return;
        for(FileBean fileBean : fileBeanList){
            File file = fileBean.getFile();
            checkBoxList.addItem(file,file.getName(),fileBean.isChecked());
        }
    }
}
