package com.lee9213.util;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.CheckedTreeNode;

/**
 * String操作工具类
 *
 * @author lee9213@163.com
 * @version 1.0
 * @date 2016/8/25 16:50
 */
public class StringUtil {

    /**
     * 默认选中节点路径
     */
    public static CheckedTreeNode DEFAULT_PATH = null;

    public static String objectToPath(Object[] nodes) {
        String path = "";
        for (int counter = 0; counter < nodes.length; counter++) {
            path += nodes[counter].toString() + "/" ;
        }
        return path;
    }

    public static Module getModule(Project project, DataContext dataContext, String selectedFilePath){
        //获取当前module
        Module module = DataKeys.MODULE.getData(dataContext);
        if(null == module){
            ModuleManager moduleManager = ModuleManager.getInstance(project);
            Module[] modules = moduleManager.getModules();
            for(Module m : modules){
                String modulepath = m.getModuleFilePath();
                modulepath = modulepath.substring(0,modulepath.lastIndexOf("/"));
                if(selectedFilePath.startsWith(modulepath)){
                    module = m;
                    break;
                }
            }
        }
        return module;
    }
}