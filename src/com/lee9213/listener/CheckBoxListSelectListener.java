package com.lee9213.listener;

import com.intellij.ui.CheckBoxList;
import com.intellij.ui.CheckBoxListListener;
import com.intellij.ui.CheckedTreeNode;
import com.lee9213.bean.DirectoryBean;
import com.lee9213.bean.FileBean;
import com.lee9213.ui.CheckBoxTree;
import com.lee9213.util.StringUtil;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 文件监听事件
 * @author lee9213@163.com
 * @version 1.0
 * @date 2016/8/26 11:40
 */
public class CheckBoxListSelectListener implements CheckBoxListListener {

    private CheckBoxList checkBoxList;
    private Map<CheckedTreeNode,DirectoryBean> fileMap;
    private CheckBoxTree checkBoxTree;

    public CheckBoxListSelectListener(CheckBoxList checkBoxList, Map<CheckedTreeNode, DirectoryBean> fileMap,CheckBoxTree checkBoxTree) {
        this.checkBoxList = checkBoxList;
        this.fileMap = fileMap;
        this.checkBoxTree = checkBoxTree;
    }

    @Override
    public void checkBoxSelectionChanged(int index, boolean flag) {

        CheckedTreeNode node = StringUtil.DEFAULT_PATH;

        if(flag){
            DirectoryBean directoryBean = fileMap.get(node);
            File selectFile = (File) checkBoxList.getItemAt(index);
            if(flag && !node.isChecked()){
                checkBoxTree.setNodeState(node,true);
//                checkBoxTree.setInheritsPopupMenu(true);
//                checkBoxTree.setExpandsSelectedPaths(true);
//                checkBoxTree.setShowsRootHandles(true);
//                checkBoxTree.isExpanded(new TreePath(node.getPath()));
//                checkBoxTree.addTreeExpansionListener();
//                checkBoxTree.addTreeWillExpandListener();
//                checkBoxTree.addVetoableChangeListener();
//                checkBoxTree.addAncestorListener();
//                checkBoxTree.addHierarchyBoundsListener();
//                checkBoxTree.addHierarchyListener();
//                node.setChecked(false);
//                checkBoxTree.setNodeState(node,true);
//                checkBoxTree.clearSelection();
//                checkBoxTree.cancelEditing();
//                checkBoxTree.computeVisibleRect(new Rectangle());


//                checkBoxTree.firePropertyChange("checked",false,true);
//                checkBoxTree.hasBeenExpanded();
//                checkBoxTree.makeVisible(new TreePath(node.getPath()));
//                checkBoxTree.setExpandableItemsEnabled(true);
//                checkBoxTree.setShowsRootHandles(true);
//                checkBoxTree.setAnchorSelectionPath();
//                checkBoxTree.setDragEnabled(true);
//                checkBoxTree.setLeadSelectionPath(new TreePath(node.getPath()));
//                checkBoxTree.setFocusable(false);
//
//                checkBoxTree.setEditable(false);
//                checkBoxTree.setEnabled(false);
//                checkBoxTree.setOpaque(false);
//                checkBoxTree.setFocusTraversalPolicyProvider(false);
//                checkBoxTree.setInvokesStopCellEditing(false);
//                checkBoxTree.setRequestFocusEnabled(false);
//                checkBoxTree.setHoldSize(true);
//                checkBoxTree.setFocusCycleRoot(true);
//                checkBoxTree.setIgnoreRepaint(true);
//                checkBoxTree.setPaintBusy(true);
                checkBoxTree.setFocusTraversalPolicyProvider(true);
                checkBoxTree.setFocusTraversalPolicy(new DefaultFocusTraversalPolicy(){

                });

            }
            List<FileBean> fileBeens = directoryBean.getFiles();
            for(FileBean fileBean : fileBeens){
                File file = fileBean.getFile();
                if(file.getAbsolutePath().equalsIgnoreCase(selectFile.getAbsolutePath())){
                    fileBean.setChecked(flag);
                    break;
                }
            }


        }else{
            boolean checked = true;
            int count = checkBoxList.getItemsCount();
            for(int i=0;i<count;i++){
                if(checkBoxList.isItemSelected(i)){
                    checked = false;
                }
            }
            if(checked){
                checkBoxTree.setNodeState(node,false);
            }
        }
    }
}
