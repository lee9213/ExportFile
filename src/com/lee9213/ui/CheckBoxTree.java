package com.lee9213.ui;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.*;
import com.intellij.util.Icons;
import com.intellij.util.ui.UIUtil;
import com.lee9213.bean.DirectoryBean;
import com.lee9213.bean.FileBean;
import com.lee9213.util.StringUtil;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.util.*;


/**
 * CheckBoxTree封装
 *
 * @author lee9213@163.com
 * @version 1.0
 * @date 2016/8/25 15:34
 */
public class CheckBoxTree extends CheckboxTree {

    private Map<CheckedTreeNode,DirectoryBean> fileMap;
    private VirtualFile virtualFile;

    public CheckBoxTree( CheckedTreeNode checkedTreeNode, VirtualFile virtualFile,Map<CheckedTreeNode,DirectoryBean> fileMap) {
        super(new CheckboxTreeCellRenderer() {
            @Override
            public void customizeRenderer(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                if (UIUtil.isUnderGTKLookAndFeel()) {
                    final Color background = selected ? UIUtil.getTreeSelectionBackground() : UIUtil.getTreeTextBackground();
                    UIUtil.changeBackGround(this, background);
                }
                final CheckedTreeNode node = (CheckedTreeNode)value;
                final Object userObject = node.getUserObject();

                String text;
                SimpleTextAttributes attributes;
                Icon icon;
                if (userObject == null) {
                    text = "INVISBLE ROOT";
                    attributes = SimpleTextAttributes.ERROR_ATTRIBUTES;
                    icon = null;
                }else {
                    text = (String)userObject;
                    attributes = SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES;
                    icon = expanded ? Icons.DIRECTORY_OPEN_ICON : Icons.DIRECTORY_CLOSED_ICON;
                }
                final ColoredTreeCellRenderer textRenderer = getTextRenderer();
                if (icon != null) {
                    textRenderer.setIcon(icon);
                }
                if (text != null) {
                    textRenderer.append(text, attributes);
                }
            }
        }, checkedTreeNode,new CheckPolicy(true,true,false,false));
        this.fileMap = fileMap;
        this.virtualFile = virtualFile;
    }

    public void init(String projectName, String modulePath,CheckBoxList checkBoxList){

        modulePath = modulePath.substring(0,modulePath.lastIndexOf("/"));
        modulePath = modulePath.substring(0,modulePath.lastIndexOf("/"));


        boolean isDirectory = virtualFile.isDirectory();
        String selectedPath = virtualFile.getPath();
        String filename = selectedPath.substring(selectedPath.lastIndexOf("/")+1);
        selectedPath = selectedPath.substring(selectedPath.indexOf(modulePath)+modulePath.length());
        selectedPath = projectName + selectedPath;
        if(!isDirectory){
            selectedPath = selectedPath.substring(0,selectedPath.lastIndexOf("/"));
        }
        selectedPath = Arrays.toString(selectedPath.split("/"));

        ONE:
        for(Map.Entry<CheckedTreeNode,DirectoryBean> entry : fileMap.entrySet()){
            CheckedTreeNode node = entry.getKey();
            String nodePath = Arrays.toString(node.getUserObjectPath());
            if(nodePath.equalsIgnoreCase(selectedPath)){
                StringUtil.DEFAULT_PATH = node;
                TreePath treePath = new TreePath(node.getPath());
                this.setSelectionPath(treePath);
//                this.onNodeStateChanged(node);
                this.setNodeState(node,true);
                this.setScrollsOnExpand(true);

                DirectoryBean bean = entry.getValue();
                java.util.List<FileBean> fileBeanList = bean.getFiles();
                checkBoxList.clear();

                if(fileBeanList != null && fileBeanList.size() > 0) {
                    for (FileBean fileBean : fileBeanList) {
                        File file = fileBean.getFile();
                        boolean flag = false;
                        if(!isDirectory) {
                            if (file.getName().equals(filename)) {
                                flag = true;
                            }
                        }else{
                            flag = true;
                        }
                        fileBean.setChecked(flag);
                        checkBoxList.addItem(file, file.getName(), flag);
                    }
                }
                initChild(node);
                break;
            }
        }
    }

    public void initChild(CheckedTreeNode parent){
        Enumeration enumeration = parent.children();
        while (enumeration.hasMoreElements()){
            CheckedTreeNode node = (CheckedTreeNode) enumeration.nextElement();
            initChild(node);
            DirectoryBean bean = fileMap.get(node);
            java.util.List<FileBean> fileBeanList = bean.getFiles();
            if(fileBeanList == null || fileBeanList.size() == 0)continue;
            for(FileBean fileBean : fileBeanList){
                fileBean.setChecked(true);
            }
        }
    }
}
