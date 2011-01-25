/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractOperationPM;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Karneim
 */
public class BnButtonTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BnButtonTest.class);
    }

    private IModelProvider provider;
    private BnButton button;
    private TestModel testModel;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp()
        throws Exception {
        this.button = new BnButton();
        this.provider = new ModelProvider();
        this.testModel = new TestModel();
    }

    @Test
    public void testGetModelProvider() {
        this.button.setModelProvider(this.provider);
        assertEquals("button.getLocalProvider()", this.provider, this.button.getModelProvider());
    }

    @Test
    public void testSetModelProvider() {
        this.provider.setPresentationModel(this.testModel);
        this.button.setPath(new Path("this.op"));
        this.button.setModelProvider(this.provider);
        assertNotNull("button.getOperation()", this.button.getPresentationModel());
    }

    @Test
    public void testGetPath() {
        Path path = new Path("this.op");
        this.button.setPath(path);
        assertEquals("button.getPath()", path, this.button.getPath());
    }

    @Test
    public void testSetPath() {
        this.provider.setPresentationModel(this.testModel);
        this.button.setPath(new Path("this.op"));
        this.button.setModelProvider(this.provider);
        assertNotNull("button.getOperation()", this.button.getPresentationModel());
    }

    @Test
    public void testSetPresentationModel() {
        final Counter counter = new Counter();
        final IOperationPM op = new AbstractOperationPM() {
            public void execute()
                throws Throwable {
                counter.increase();
            }
        };
        this.button.setPresentationModel(op);
        this.button.doClick();
        assertEquals("counter.get()", 1, counter.get());
    }

    @Test
    public void testGetPresentationModel() {
        IOperationPM op = new OperationPM();
        this.button.setPresentationModel(op);
        assertEquals("", op, this.button.getPresentationModel());
    }

    @Test
    public void testRefresh() {
        this.provider.setPresentationModel(this.testModel);
        this.button.setPath(new Path("this.op"));
        this.button.setModelProvider(this.provider);
        this.button.setText("abc");
        assertFalse("button.isEnabled()", this.button.isEnabled());
        assertEquals("", this.button.getPresentationModel().getValidationState().getMessage(), this.button.getToolTipText());
    }

    @Test
    public void testIsAutoExecute() {
        this.button.setAutoExecute(false);
        assertFalse("button.isAutoExecute()", this.button.isAutoExecute());
    }

    @Test
    public void testSetAutoExecute()
        throws Throwable {
        final Counter counter = new Counter();
        final IOperationPM op = new AbstractOperationPM() {
            public void execute()
                throws Throwable {
                counter.increase();
            }
        };
        this.button.setPresentationModel(op);
        this.button.setAutoExecute(false);
        this.button.doClick();
        assertEquals("counter.get()", 0, counter.get());
        this.button.setAutoExecute(true);
        this.button.doClick();
        assertEquals("counter.get()", 1, counter.get());
    }

    @Test
    public void getIcon() {
        final IOperationPM op = new AbstractOperationPM() {
            public void execute()
                throws Throwable {
            }
        };
        op.setIconUrl(BnButtonTest.class.getResource("sample.gif"));
        this.button.setPresentationModel(op);
        assertNotNull("this.button.getIcon()", this.button.getIcon());
    }

    private static class TestModel extends AbstractPM {
        protected final AbstractOperationPM op = new AbstractOperationPM() {
            public void execute()
                throws Throwable {
                TestModel.this.op();
            }
        };
        protected final TextPM pM = new TextPM();

        public TestModel() {
            PMManager.setup(this);
            this.pM.setDescription("Insert digits");
            this.pM.setMandatory(true);
            this.pM.getValidator().add(new ValidationRule() {
                public ValidationState validate() {
                    try {
                        Integer.parseInt(pM.getText());
                        return null;
                    } catch (NumberFormatException e) {
                        return new ValidationState("Error: Insert digits only");
                    }
                }
            });
            this.op.setDescription("Execute");
            this.op.getValidator().add(new ValidationRule() {
                public ValidationState validate() {
                    return pM.getValidationState();
                }
            });
        }

        public void op() {
            this.op.check();
        }
    }

    private static class Counter {
        private int count = 0;

        public int get() {
            return count;
        }

        public void increase() {
            count++;
        }
    }
}