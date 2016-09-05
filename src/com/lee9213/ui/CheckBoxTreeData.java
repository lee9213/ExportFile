package com.lee9213.ui;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.CheckedTreeNode;
import com.lee9213.bean.DirectoryBean;

import java.io.File;
import java.util.Map;

/**
 * CheckBoxTree绑定数据
 *
 * @author lee9213@163.com
 * @version 1.0
 * @date 2016/8/26 10:34
 */
public class CheckBoxTreeData {

    private Project project;
    private Map<CheckedTreeNode,DirectoryBean> fileMap;

    public CheckBoxTreeData(Project project,Map<CheckedTreeNode,DirectoryBean> fileMap) {
        this.project = project;
        this.fileMap = fileMap;
    }

    /**
     * 绑定数据
     * @return
     */
    public CheckedTreeNode treeNode(){
        CheckedTreeNode root = new CheckedTreeNode(project.getName());
        root.setChecked(false);
        ModuleManager moduleManager = ModuleManager.getInstance(project);
        final Module[] modules = moduleManager.getModules();
        for(Module module : modules){
            CheckedTreeNode node = new CheckedTreeNode(module.getName());
            node.setChecked(false);
            root.add(node);
            String modulePath = module.getModuleFilePath();
            modulePath = modulePath.substring(0,modulePath.lastIndexOf("/"));
            File file = new File(modulePath);
            fileMap.put(node,new DirectoryBean(modulePath));
            listTreeNode(node,file);
        }
        return  root;
    }

    /**
     * 绑定所有子目录的数据
     * @param node
     * @param parent
     */
    public void listTreeNode(CheckedTreeNode node,File parent){
        File[] files = parent.listFiles();
        for(File file : files){
            if(file.isDirectory()){
                CheckedTreeNode childNode = new CheckedTreeNode(file.getName());
                childNode.setChecked(false);
                node.add(childNode);
                fileMap.put(childNode,new DirectoryBean(file.getAbsolutePath()));
                listTreeNode(childNode,file);
            }else{
                fileMap.get(node).addFile(file);
            }
        }
    }
}