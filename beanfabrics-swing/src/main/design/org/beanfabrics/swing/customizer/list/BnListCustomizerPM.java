/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing.customizer.list;

import java.lang.reflect.Type;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.ViewClassDecorator;
import org.beanfabrics.meta.PathElementInfo;
import org.beanfabrics.model.IListPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.swing.customizer.path.PathContext;
import org.beanfabrics.swing.customizer.path.PathPM;
import org.beanfabrics.swing.customizer.util.AbstractCustomizerPM;
import org.beanfabrics.swing.list.BnList;
import org.beanfabrics.swing.list.CellConfig;
import org.beanfabrics.util.GenericType;

/**
 * The <code>BnListCustomizerPM</code> is the presentation model for the
 * {@link BnListCustomizer}.
 * 
 * @author Michael Karneim
 */
public class BnListCustomizerPM extends AbstractCustomizerPM {
    protected final PathPM pathToList = new PathPM();
    protected final PathPM pathToRowCell = new PathPM();

    public interface Functions {
        void setPathToList(Path path);

        void setCellConfig(CellConfig cellConfig);
    }

    private Functions functions;
    private BnList bnList;
    private Class<? extends PresentationModel> rootModelType;
    private Class<? extends PresentationModel> requiredListModelType;

    public BnListCustomizerPM() {
        //this.title.setText("This is the Beanfabrics customizer for the "+BnList.class.getName()+" component.");
        PMManager.setup(this);
    }

    public void setFunctions(Functions functions) {
        this.functions = functions;
    }

    public void setBnList(BnList bnList) {
        this.bnList = bnList;
        IModelProvider ds = this.bnList.getModelProvider();
        this.rootModelType = ds.getPresentationModelType();

        ViewClassDecorator viewDeco = new ViewClassDecorator(bnList.getClass());
        this.requiredListModelType = viewDeco.getExpectedModelType();

        configurePathToList();
        configurePathToRowCell();
    }

    private void configurePathToList() {
        PathContext ctx = new PathContext(PMManager.getInstance().getMetadata().getPathElementInfo(this.rootModelType), PMManager.getInstance().getMetadata().getTypeInfo(this.requiredListModelType), this.bnList.getPath());

        this.pathToList.setPathContext(ctx);
    }

    @OnChange(path = "pathToList")
    void applyPathToList() {
        if (functions != null && pathToList.isValid()) {
            functions.setPathToList(pathToList.getPath());
        }
        configurePathToRowCell();
    }

    @OnChange(path = "pathToRowCell")
    void applyCellConfig() {
        if (functions != null && pathToRowCell.isValid()) {
            if (pathToRowCell.getPath() == null) {
                functions.setCellConfig(null);
            } else {
                CellConfig cfg = new CellConfig(pathToRowCell.getPath());
                functions.setCellConfig(cfg);
            }
        }
    }

    private void configurePathToRowCell() {
        CellConfig cellConfig = this.bnList == null ? null : this.bnList.getCellConfig();
        Path initialPath = cellConfig == null ? null : cellConfig.getPath();
        Class elementJavaType = getElementJavaType();
        PathElementInfo rootPathElementInfo = elementJavaType == null ? null : PMManager.getInstance().getMetadata().getPathElementInfo(elementJavaType);
        PathContext ctx = new PathContext(rootPathElementInfo, null, initialPath);
        this.pathToRowCell.setPathContext(ctx);
    }

    private Class getElementJavaType() {
        PathElementInfo pathElementInfo = getPathElementInfoOfList();
        if (pathElementInfo == null) {
            return null;
        } else {
            GenericType gt = pathElementInfo.getGenericType();
            GenericType typeParam = gt.getTypeParameter(IListPM.class.getTypeParameters()[0]);
            Type tArg = typeParam.narrow(typeParam.getType(), PresentationModel.class);
            if (tArg instanceof Class) {
                return (Class)tArg;
            } else {
                throw new IllegalStateException("Unexpected type: " + tArg.getClass().getName());
            }
        }
    }

    private PathElementInfo getPathElementInfoOfList() {
        if (this.bnList == null) {
            return null;
        }
        IModelProvider ds = this.bnList.getModelProvider();
        if (ds != null) {
            Path path = this.bnList.getPath();
            if (path != null) {
                Class cls = ds.getPresentationModelType();
                if (cls != null) {
                    PathElementInfo root = PMManager.getInstance().getMetadata().getPathElementInfo(cls);
                    return root.getPathInfo(path);
                }
            }
        }
        IListPM listPM = this.bnList.getPresentationModel();
        if (listPM != null) {
            PathElementInfo info = PMManager.getInstance().getMetadata().getPathElementInfo(listPM.getClass());
            return info;
        } else {
            return null;
        }
    }
}