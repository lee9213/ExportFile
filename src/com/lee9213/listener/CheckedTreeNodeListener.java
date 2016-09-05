package com.lee9213.listener;

import com.intellij.ui.CheckBoxList;
import com.intellij.ui.CheckboxTree;
import com.intellij.ui.CheckboxTreeListener;
import com.intellij.ui.CheckedTreeNode;
import com.lee9213.bean.DirectoryBean;
import com.lee9213.bean.FileBean;
import com.lee9213.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.TreePath;
import java.io.File;
import java.util.Arrays;
import java.util.Map;

/**
 * CheckBoxTree事件监听
 *
 * @author lee9213@163.com
 * @version 1.0
 * @date 2016/8/25 17:55
 */
public class CheckedTreeNodeListener implements CheckboxTreeListener {

    private CheckBoxList checkBoxList;
    private Map<CheckedTreeNode,DirectoryBean> fileMap;
    private CheckboxTree checkBoxTree;

    public CheckedTreeNodeListener(CheckBoxList checkBoxList, Map<CheckedTreeNode, DirectoryBean> fileMap, CheckboxTree checkBoxTree) {
        this.checkBoxList = checkBoxList;
        this.fileMap = fileMap;
        this.checkBoxTree = checkBoxTree;
    }

    @Override
    public void mouseDoubleClicked(@NotNull CheckedTreeNode checkedTreeNode) {
        TreePath path = checkBoxTree.getSelectionPath();
        checkBoxTree.setDoubleBuffered(true);
        if(!checkBoxTree.isExpanded(path)){
//            checkBoxTree.fireTreeExpanded(path);
            checkBoxTree.expandPath(path);
        }else{
            checkBoxTree.collapsePath(path);
//            checkBoxTree.fireTreeCollapsed(path);
        }

    }

    /**
     * checkbox状态改变
     * @param checkedTreeNode
     */
    @Override
    public void nodeStateChanged(@NotNull CheckedTreeNode checkedTreeNode) {
        DirectoryBean directoryBean = fileMap.get(checkedTreeNode);
        java.util.List<FileBean> fileBeens= directoryBean.getFiles();
        if(fileBeens == null)return;
        for(FileBean fileBean : fileBeens){
            fileBean.setChecked(checkedTreeNode.isChecked());
        }
        String path = Arrays.toString(checkedTreeNode.getUserObjectPath());
        String defaultpath = Arrays.toString(StringUtil.DEFAULT_PATH.getUserObjectPath());
        if(path.equals(defaultpath)){
            checkBoxList.clear();
            if(fileBeens == null || fileBeens.size() == 0)return;
            for(FileBean fileBean : fileBeens){
                File file = fileBean.getFile();
                checkBoxList.addItem(file,file.getName(),fileBean.isChecked());
            }
        }
    }

    @Override
    public void beforeNodeStateChanged(@NotNull CheckedTreeNode checkedTreeNode) {
        checkedTreeNode.setChecked(false);
    }
}
