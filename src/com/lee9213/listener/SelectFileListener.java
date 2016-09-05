package com.lee9213.listener;

import com.intellij.ui.CheckedTreeNode;
import com.lee9213.bean.DirectoryBean;
import com.lee9213.bean.FileBean;
import com.lee9213.ui.CheckBoxTree;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * 全选和取消全选监听事件
 *
 * @author lee9213@163.com
 * @version 1.0
 * @date 2016/8/26 11:37
 */
public class SelectFileListener implements ActionListener {

    private CheckBoxTree checkBoxTree;
    private Map<CheckedTreeNode,DirectoryBean> fileMap;

    public SelectFileListener(CheckBoxTree checkBoxTree, Map<CheckedTreeNode, DirectoryBean> fileMap) {
        this.checkBoxTree = checkBoxTree;
        this.fileMap = fileMap;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();
        boolean flag = false;
        if("Select All".equalsIgnoreCase(button.getText())){
            flag = true;
        }
        TreeModel treeModel = checkBoxTree.getModel();
        CheckedTreeNode node = (CheckedTreeNode) treeModel.getRoot();
        Enumeration enumeration = node.children();
        while(enumeration.hasMoreElements()){
            CheckedTreeNode child = (CheckedTreeNode) enumeration.nextElement();
            checkBoxTree.setNodeState(child,flag);
        }
        for(DirectoryBean bean : fileMap.values()){
            List<FileBean> fileBeens = bean.getFiles();
            if(fileBeens == null)continue;
            for(FileBean fileBean : fileBeens){
                fileBean.setChecked(flag);
            }
        }
    }
}
