package ddb.start;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.GroupLayout.ParallelGroup;
import org.jdesktop.layout.GroupLayout.SequentialGroup;
import sun.misc.URLClassPath;

public class Start
  extends JFrame
{
  public static final String PATH_TOOLCHAIN = "java-j2se_1.6-sun";
  public static final String PATH_LIBRARY = "lib";
  public static final String OPERATION_DISK = "OpsDisk";
  public static final String RESOURCE_DIR = "ResourceDir";
  public static final String LOG_DIR = "LogDir";
  public static final String CONFIG_DIR = "ConfigDir";
  public static final String LOCAL_ADDRESS = "LocalAddress";
  public static final String BUILD_TYPE = "BuildType";
  public static final String GUI_TYPE = "GuiType";
  public static final String OPERATION_MODE = "OpMode";
  public static final String LOAD_PREVIOUS = "LoadPrevious";
  public static final String LOCAL_MODE = "LocalMode";
  public static final String JAVA_EXE = "java.exe";
  public static final String VMARGS = "vmargs";
  public static final String RES_DIR = "res.dir";
  public static final String DEBUGVMARGS = "vmargs.debug";
  public static final String CLASSPATH = "classpath.dirs";
  public static final String JAR_DIRS = "jar.dirs";
  public static final String LIVE_OPERATION = "live.operation";
  public static final String REPLAY_OPERATION = "replay.operation";
  public static final String WINDOWS_START = "windows.start";
  public static final String BUILD_RELEASE = "build.release";
  public static final String BUILD_DEBUG = "build.debug";
  public static final String BUILD_DEBUG_WINDOWS = "build.debug.windows";
  public static final String SHOW_OP_TYPE = "show.optype";
  public static final String SHOW_DEBUG_CORE = "show.debug.core";
  public static final String SHOW_DEBUG_GUI = "show.debug.gui";
  public static final String SHOW_LOCAL_MODE = "show.local.mode";
  public static final String SHOW_THREAD_DUMP = "show.thread.dump";
  public static final String WINDOWS = "windows";
  public static final String LINUX = "linux";
  public static final String PATH_VAR = "path.var";
  public static final String TOOL_CHAIN_STR = "tool.chain";
  public static final String PATH_SEP = "path.sep";
  public static final String THREAD_DUMP = "thread.dump";
  public static final String WAIT_FOR_OUTPUT = "wait.for.output";
  public static final String DSZ_KEYWORD = "DSZ_KEYWORD";
  public static final String LIVE_KEYWORD = String.format("live.%s", new Object[] { "DSZ_KEYWORD" });
  public static final String REPLAY_KEYWORD = String.format("replay.%s", new Object[] { "DSZ_KEYWORD" });
  public static final String DSZ_DEFAULT = "Default";
  static Properties prop = new Properties();
  static Properties userDefaults = new Properties();
  public static final String START_PROPERTIES = "start.properties";
  public static final String USER_DEFAULTS = "user.defaults";
  boolean inferTextFieldValues = false;
  JFileChooser directoryFinder = null;
  private static char[] INVALIDCHARACTERS = { '\t', ' ', '\b', '\n', '\r' };
  File themeSearchRoot = null;

  DefaultComboBoxModel liveOperationThemes = new DefaultComboBoxModel();
  DefaultComboBoxModel replayOperationThemes = new DefaultComboBoxModel();
  JRadioButton buildDebug;
  JRadioButton buildRelease;
  JButton configurationBrowse;
  JTextField configurationField;
  JLabel configurationLabel;
  ButtonGroup coreBuild;
  JPanel corePanel;
  JButton goButton;
  ButtonGroup guiBuild;
  JRadioButton guiDebug;
  JPanel guiPanel;
  JRadioButton guiRelease;
  JPanel jPanel1;
  JPanel jPanel2;
  JRadioButton liveOption;
  JCheckBox loadPrevious;
  JTextField localCommsAddressField;
  JLabel localCommsAddressLabel;
  JCheckBox localMode;
  JButton logBrowse;
  JTextField logField;
  JLabel logLabel;
  JButton operationBrowse;
  JTextField operationField;
  JLabel operationLabel;
  JPanel operationPanel;
  ButtonGroup operationType;
  JPanel optionsPanel;
  JRadioButton replayOption;
  JButton resourceBrowse;
  JTextField resourceField;
  JLabel resourceLabel;
  JComboBox themeSelector;
  JCheckBox threadDump;
  JCheckBox waitFor;
  
  static final Pattern[] patterns = { Pattern.compile("[0-9a-fA-F]{1,8}"), Pattern.compile("[Zz][0-2]{0,1}[0-9]{0,2}\\.[0-2]{0,1}[0-9]{0,2}\\.[0-2]{0,1}[0-9]{0,2}\\.[0-2]{0,1}[0-9]{0,2}") };
  
  private static FilenameFilter jars = new FilenameFilter()
  {
    public boolean accept(File paramAnonymousFile, String paramAnonymousString)
    {
      return paramAnonymousString.toLowerCase().endsWith(".jar");
    }
  };
  
  public Start() {
    
    // Configure a Directory File Chooser Window for use later on
    try {
      directoryFinder = new JFileChooser();
      directoryFinder.setFileSelectionMode(1);
    }
    catch (Exception localException1){
      localException1.printStackTrace();
    }

    // Attempt to load properties from "start.properties"
    try {
      prop.load(new FileInputStream("start.properties"));
    }
    catch (FileNotFoundException localFileNotFoundException1) {
      localFileNotFoundException1.printStackTrace();
    }
    catch (IOException localIOException) {
      localIOException.printStackTrace();
    }

    // Initialize the UI Components for Swing
    initComponents();
    
    try {
      
      // Load our user.defaults file into a Property List
      userDefaults.load(new FileInputStream("user.defaults"));

      // What should an "OpsDisk" contain?
      operationField.setText(getStringDefault("OpsDisk", ""));
      
      resourceField.setText(getStringDefault("ResourceDir", ""));
      logField.setText(getStringDefault("LogDir", ""));
      configurationField.setText(getStringDefault("ConfigDir", ""));
      
      File localFile = new File(".");
      if ((operationField.getText() == null) || (operationField.getText().length() == 0)) {
        operationField.setText(localFile.getCanonicalPath());
      } else {
        localFile = new File(operationField.getText());
      }
      
      infer(localFile.getCanonicalFile());

    }

    // Triggered if no user.defaults filed found
    catch (FileNotFoundException localFileNotFoundException2) {
      inferTextFieldValues = true;
    }

    // Triggered upon SNAFU from loading in user.default files (permissions issues, something other than FileNotFoundException)
    catch (Exception localException2) {
      localException2.printStackTrace();
      inferTextFieldValues = true;
    }

    if (inferTextFieldValues) {
      infer(new File("."));
      examine();
    }

  }
  
  // Initializes the UI Components, wires-up event listeners, sets title text, etc.
  private void initComponents() {
    guiBuild = new ButtonGroup();
    operationType = new ButtonGroup();
    coreBuild = new ButtonGroup();
    resourceField = new JTextField();
    logField = new JTextField();
    configurationField = new JTextField();
    operationField = new JTextField();
    resourceLabel = new JLabel();
    logLabel = new JLabel();
    configurationLabel = new JLabel();
    operationLabel = new JLabel();
    resourceBrowse = new JButton();
    logBrowse = new JButton();
    configurationBrowse = new JButton();
    operationBrowse = new JButton();
    goButton = new JButton();
    localCommsAddressLabel = new JLabel();
    localCommsAddressField = new JTextField();
    jPanel1 = new JPanel();
    operationPanel = new JPanel();
    liveOption = new JRadioButton();
    replayOption = new JRadioButton();
    optionsPanel = new JPanel();
    loadPrevious = new JCheckBox();
    localMode = new JCheckBox();
    corePanel = new JPanel();
    buildDebug = new JRadioButton();
    buildRelease = new JRadioButton();
    guiPanel = new JPanel();
    guiRelease = new JRadioButton();
    guiDebug = new JRadioButton();
    waitFor = new JCheckBox();
    threadDump = new JCheckBox();
    jPanel2 = new JPanel();
    themeSelector = new JComboBox();
    setDefaultCloseOperation(2);
    setTitle("DanderSpritz Operation Center");
    setCursor(new Cursor(0));
    setLocationByPlatform(true);
    setName("startFrame");
    setResizable(false);
    resourceField.setToolTipText(prop.getProperty("tooltip.resource"));
    resourceField.addKeyListener(new KeyAdapter()
    {
      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
        Start.this.enterPressed(paramAnonymousKeyEvent);
      }
    });
    logField.setToolTipText(prop.getProperty("tooltip.log"));
    logField.addKeyListener(new KeyAdapter()
    {
      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
        Start.this.enterPressed(paramAnonymousKeyEvent);
      }
    });
    configurationField.setToolTipText(prop.getProperty("tooltip.config"));
    configurationField.addKeyListener(new KeyAdapter()
    {
      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
        Start.this.enterPressed(paramAnonymousKeyEvent);
      }
    });
    operationField.setToolTipText(prop.getProperty("tooltip.disk"));
    operationField.addKeyListener(new KeyAdapter()
    {
      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
        Start.this.enterPressed(paramAnonymousKeyEvent);
      }
    });
    resourceLabel.setText(prop.getProperty("label.resource"));
    logLabel.setText(prop.getProperty("label.log"));
    configurationLabel.setText(prop.getProperty("label.config"));
    operationLabel.setText(prop.getProperty("label.disk"));
    resourceBrowse.setText(prop.getProperty("label.browse"));
    resourceBrowse.setToolTipText(prop.getProperty("tooltip.resource.browse"));
    resourceBrowse.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        Start.this.resourceBrowseActionPerformed(paramAnonymousActionEvent);
      }
    });
    logBrowse.setText(prop.getProperty("label.browse"));
    logBrowse.setToolTipText(prop.getProperty("tooltip.log.browse"));
    logBrowse.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        Start.this.logBrowseActionPerformed(paramAnonymousActionEvent);
      }
    });
    configurationBrowse.setText(prop.getProperty("label.browse"));
    configurationBrowse.setToolTipText(prop.getProperty("tooltip.config.browse"));
    configurationBrowse.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        Start.this.configurationBrowseActionPerformed(paramAnonymousActionEvent);
      }
    });
    operationBrowse.setText(prop.getProperty("label.browse"));
    operationBrowse.setToolTipText(prop.getProperty("tooltip.disk.browse"));
    operationBrowse.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        Start.this.operationBrowseActionPerformed(paramAnonymousActionEvent);
      }
    });
    goButton.setText(prop.getProperty("label.start"));
    goButton.setToolTipText(prop.getProperty("tooltip.start"));
    goButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        Start.this.goButtonActionPerformed(paramAnonymousActionEvent);
      }
    });
    localCommsAddressLabel.setText(prop.getProperty("label.comms"));
    localCommsAddressField.setText("z0.0.0.1");
    localCommsAddressField.addKeyListener(new KeyAdapter()
    {
      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
        Start.this.enterPressed(paramAnonymousKeyEvent);
      }
    });
    operationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), prop.getProperty("label.opMode")));
    operationType.add(liveOption);
    liveOption.setSelected(true);
    liveOption.setText(prop.getProperty("label.live"));
    liveOption.setToolTipText(prop.getProperty("tooltip.live"));
    liveOption.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    liveOption.setMargin(new Insets(0, 0, 0, 0));
    liveOption.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        Start.this.liveOptionActionPerformed(paramAnonymousActionEvent);
      }
    });
    operationType.add(replayOption);
    replayOption.setText(prop.getProperty("label.replay"));
    replayOption.setToolTipText(prop.getProperty("tooltip.replay"));
    replayOption.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    replayOption.setMargin(new Insets(0, 0, 0, 0));
    replayOption.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        Start.this.replayOptionActionPerformed(paramAnonymousActionEvent);
      }
    });
    GroupLayout localGroupLayout1 = new GroupLayout(operationPanel);
    operationPanel.setLayout(localGroupLayout1);
    localGroupLayout1.setHorizontalGroup(localGroupLayout1.createParallelGroup(1).add(localGroupLayout1.createSequentialGroup().add(localGroupLayout1.createParallelGroup(1).add(liveOption).add(replayOption)).addContainerGap(45, 32767)));
    localGroupLayout1.setVerticalGroup(localGroupLayout1.createParallelGroup(1).add(localGroupLayout1.createSequentialGroup().add(liveOption).addPreferredGap(0).add(replayOption).addContainerGap(-1, 32767)));
    optionsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), prop.getProperty("label.options")));
    loadPrevious.setText(prop.getProperty("label.loadPrevious"));
    loadPrevious.setToolTipText(prop.getProperty("tooltip.loadPrevious"));
    loadPrevious.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    loadPrevious.setMargin(new Insets(0, 0, 0, 0));
    localMode.setText(prop.getProperty("label.localMode"));
    localMode.setToolTipText(prop.getProperty("tooltip.localMode"));
    localMode.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    localMode.setMargin(new Insets(0, 0, 0, 0));
    localMode.setVisible(isShowLocal());
    GroupLayout localGroupLayout2 = new GroupLayout(optionsPanel);
    optionsPanel.setLayout(localGroupLayout2);
    localGroupLayout2.setHorizontalGroup(localGroupLayout2.createParallelGroup(1).add(localGroupLayout2.createSequentialGroup().addContainerGap().add(localGroupLayout2.createParallelGroup(1).add(loadPrevious).add(localMode)).addContainerGap(-1, 32767)));
    localGroupLayout2.setVerticalGroup(localGroupLayout2.createParallelGroup(1).add(localGroupLayout2.createSequentialGroup().add(loadPrevious).addPreferredGap(0).add(localMode).addContainerGap(-1, 32767)));
    corePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), prop.getProperty("label.core")));
    corePanel.setVisible(isShowDebugCore());
    coreBuild.add(buildDebug);
    buildDebug.setText(prop.getProperty("label.debug"));
    buildDebug.setToolTipText(prop.getProperty("tooltip.debug"));
    buildDebug.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    buildDebug.setMargin(new Insets(0, 0, 0, 0));
    coreBuild.add(buildRelease);
    buildRelease.setSelected(true);
    buildRelease.setText(prop.getProperty("label.release"));
    buildRelease.setToolTipText(prop.getProperty("tooltip.release"));
    buildRelease.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    buildRelease.setMargin(new Insets(0, 0, 0, 0));
    GroupLayout localGroupLayout3 = new GroupLayout(corePanel);
    corePanel.setLayout(localGroupLayout3);
    localGroupLayout3.setHorizontalGroup(localGroupLayout3.createParallelGroup(1).add(localGroupLayout3.createSequentialGroup().addContainerGap().add(localGroupLayout3.createParallelGroup(1).add(buildRelease).add(buildDebug)).addContainerGap(-1, 32767)));
    localGroupLayout3.setVerticalGroup(localGroupLayout3.createParallelGroup(1).add(localGroupLayout3.createSequentialGroup().add(buildRelease).addPreferredGap(0).add(buildDebug).addContainerGap(-1, 32767)));
    guiPanel.setBorder(BorderFactory.createTitledBorder(prop.getProperty("label.gui")));
    guiPanel.setVisible(isShowDebugGui());
    guiBuild.add(guiRelease);
    guiRelease.setText(prop.getProperty("label.release"));
    guiRelease.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    guiRelease.setMargin(new Insets(0, 0, 0, 0));
    guiBuild.add(guiDebug);
    guiDebug.setText(prop.getProperty("label.debug"));
    guiDebug.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    guiDebug.setMargin(new Insets(0, 0, 0, 0));
    GroupLayout localGroupLayout4 = new GroupLayout(guiPanel);
    guiPanel.setLayout(localGroupLayout4);
    localGroupLayout4.setHorizontalGroup(localGroupLayout4.createParallelGroup(1).add(localGroupLayout4.createSequentialGroup().addContainerGap().add(localGroupLayout4.createParallelGroup(1).add(guiRelease).add(guiDebug)).addContainerGap(-1, 32767)));
    localGroupLayout4.setVerticalGroup(localGroupLayout4.createParallelGroup(1).add(localGroupLayout4.createSequentialGroup().add(guiRelease).addPreferredGap(0).add(guiDebug).addContainerGap(-1, 32767)));
    waitFor.setText("Wait For Output");
    waitFor.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    waitFor.setMargin(new Insets(0, 0, 0, 0));
    waitFor.setVisible(false);
    threadDump.setText("Thread Dump");
    threadDump.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    threadDump.setMargin(new Insets(0, 0, 0, 0));
    threadDump.setVisible(isShowThreadDump());
    jPanel2.setBorder(BorderFactory.createTitledBorder("Theme"));
    themeSelector.setModel(new DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    GroupLayout localGroupLayout5 = new GroupLayout(jPanel2);
    jPanel2.setLayout(localGroupLayout5);
    localGroupLayout5.setHorizontalGroup(localGroupLayout5.createParallelGroup(1).add(localGroupLayout5.createSequentialGroup().addContainerGap().add(themeSelector, 0, 179, 32767).addContainerGap()));
    localGroupLayout5.setVerticalGroup(localGroupLayout5.createParallelGroup(1).add(localGroupLayout5.createSequentialGroup().add(themeSelector, -2, -1, -2).addContainerGap(25, 32767)));
    GroupLayout localGroupLayout6 = new GroupLayout(jPanel1);
    jPanel1.setLayout(localGroupLayout6);
    localGroupLayout6.setHorizontalGroup(localGroupLayout6.createParallelGroup(1).add(localGroupLayout6.createSequentialGroup().addContainerGap().add(operationPanel, -2, -1, -2).addPreferredGap(0).add(optionsPanel, -2, -1, -2).addPreferredGap(0).add(corePanel, -2, -1, -2).addPreferredGap(0).add(guiPanel, -2, -1, -2).addPreferredGap(0).add(jPanel2, -1, -1, 32767).addPreferredGap(0).add(localGroupLayout6.createParallelGroup(1, false).add(threadDump, -1, -1, 32767).add(waitFor)).addContainerGap()));
    localGroupLayout6.setVerticalGroup(localGroupLayout6.createParallelGroup(1).add(localGroupLayout6.createSequentialGroup().addContainerGap().add(localGroupLayout6.createParallelGroup(1).add(localGroupLayout6.createSequentialGroup().add(threadDump).addPreferredGap(0).add(waitFor)).add(2, jPanel2, -1, -1, 32767).add(operationPanel, -1, -1, 32767).add(optionsPanel, -1, -1, 32767).add(corePanel, -1, -1, 32767).add(guiPanel, -1, -1, 32767)).addContainerGap()));
    GroupLayout localGroupLayout7 = new GroupLayout(getContentPane());
    getContentPane().setLayout(localGroupLayout7);
    localGroupLayout7.setHorizontalGroup(localGroupLayout7.createParallelGroup(1).add(localGroupLayout7.createSequentialGroup().addContainerGap().add(localGroupLayout7.createParallelGroup(1).add(localGroupLayout7.createSequentialGroup().add(localGroupLayout7.createParallelGroup(1).add(localGroupLayout7.createSequentialGroup().add(localGroupLayout7.createParallelGroup(1).add(resourceLabel).add(logLabel).add(configurationLabel)).addPreferredGap(0).add(localGroupLayout7.createParallelGroup(1).add(configurationField, -1, 771, 32767).add(operationField, -1, 771, 32767).add(logField, -1, 771, 32767).add(2, resourceField, -1, 771, 32767).add(localCommsAddressField, -1, 771, 32767)).add(6, 6, 6)).add(localGroupLayout7.createSequentialGroup().add(operationLabel).addPreferredGap(0, 775, 32767))).addPreferredGap(0).add(localGroupLayout7.createParallelGroup(2).add(localGroupLayout7.createParallelGroup(1).add(resourceBrowse).add(logBrowse).add(configurationBrowse)).add(operationBrowse))).add(localCommsAddressLabel).add(localGroupLayout7.createSequentialGroup().add(jPanel1, -2, -1, -2).addPreferredGap(0, -1, 32767).add(goButton))).addContainerGap()));
    localGroupLayout7.setVerticalGroup(localGroupLayout7.createParallelGroup(1).add(localGroupLayout7.createSequentialGroup().addContainerGap().add(localGroupLayout7.createParallelGroup(3).add(operationLabel).add(operationBrowse).add(operationField, -2, -1, -2)).addPreferredGap(0).add(localGroupLayout7.createParallelGroup(1).add(localGroupLayout7.createParallelGroup(3).add(resourceLabel).add(resourceBrowse)).add(localGroupLayout7.createSequentialGroup().add(3, 3, 3).add(resourceField, -2, -1, -2))).addPreferredGap(0).add(localGroupLayout7.createParallelGroup(3).add(logLabel).add(logBrowse).add(logField, -2, -1, -2)).addPreferredGap(0).add(localGroupLayout7.createParallelGroup(3).add(configurationLabel).add(configurationBrowse).add(configurationField, -2, -1, -2)).addPreferredGap(0).add(localGroupLayout7.createParallelGroup(2).add(localGroupLayout7.createSequentialGroup().add(localGroupLayout7.createParallelGroup(3).add(localCommsAddressLabel).add(localCommsAddressField, -2, -1, -2)).addPreferredGap(0).add(jPanel1, -2, -1, -2)).add(goButton)).addContainerGap(-1, 32767)));
    
    // Tell the UI Frame to resize itself and all that fun crap
    pack();

  }
  
  private void replayOptionActionPerformed(ActionEvent paramActionEvent)
  {
    if (replayOption.isSelected()) {
      loadPrevious.setSelected(true);
    }
    if (replayOption.isSelected()) {
      themeSelector.setModel(replayOperationThemes);
    } else {
      themeSelector.setModel(liveOperationThemes);
    }
  }
  
  private void liveOptionActionPerformed(ActionEvent paramActionEvent)
  {
    if (liveOption.isSelected()) {
      loadPrevious.setSelected(false);
    }
    if (replayOption.isSelected()) {
      themeSelector.setModel(replayOperationThemes);
    } else {
      themeSelector.setModel(liveOperationThemes);
    }
  }
  
  private void operationBrowseActionPerformed(ActionEvent paramActionEvent)
  {
    if (setDirectory(operationField, "Select the operations disk")) {
      infer(new File(operationField.getText()));
    }
  }
  
  private void goButtonActionPerformed(ActionEvent paramActionEvent)
  {
    DanderSpritzBegin();
  }
  
  private void configurationBrowseActionPerformed(ActionEvent paramActionEvent)
  {
    if (setDirectory(configurationField, "Select the configuration directory")) {
      infer(new File(configurationField.getText() + "/../"));
    }
  }
  
  private void logBrowseActionPerformed(ActionEvent paramActionEvent)
  {
    if (setDirectory(logField, "Select the log directory")) {
      infer(new File(logField.getText() + "/../"));
    }
  }
  
  private void resourceBrowseActionPerformed(ActionEvent paramActionEvent)
  {
    if (setDirectory(resourceField, "Select the resource directory")) {
      infer(new File(resourceField.getText() + "/../"));
    }
  }
  
  private void enterPressed(KeyEvent paramKeyEvent)
  {
    if (paramKeyEvent.getKeyCode() == 10) {
      DanderSpritzBegin();
    } else {
      examine();
    }
  }
  
  boolean setDirectory(JTextField paramJTextField, String paramString)
  {
    if (directoryFinder == null)
    {
      JOptionPane.showMessageDialog(this, "The File Selector dialog did not initialize.  You must enter your paths manually.", "File Selector not available", 2);
      return false;
    }
    directoryFinder.setDialogTitle(paramString);
    if (paramJTextField.getText().trim().length() > 0)
    {
      File localFile = new File(paramJTextField.getText().trim());
      directoryFinder.setSelectedFile(localFile);
      directoryFinder.setCurrentDirectory(localFile.getParentFile());
    }
    if (directoryFinder.showDialog(this, "Select") == 0)
    {
      paramJTextField.setText(directoryFinder.getSelectedFile().getAbsolutePath());
      return true;
    }
    return false;
  }
  
  /**
   * Infers the values for multiple textFields and other items
   * 
   * 1. If the Operations Field text is blank, then set it to the Current Working Directory (CWD)
   * 2. If the Resource Field text is blank, then set it to <CWD>/Resources/
   * 3. If the Log Field text is blank, then set it to <CWD>/Logs/
   * 4. If the Configuration Field text is blank, then set it to <CWD>/UserConfiguration/
   * 
   */
  void infer(File fileOrPathName)
  {

    // If no filepath / filename was specified, then there's nothing to infer
    if (fileOrPathName == null) {
      return;
    }

    try
    {
      
      if (operationField.getText().trim().length() == 0) {
        operationField.setText(new File(fileOrPathName.getAbsolutePath()).getCanonicalPath());
      }

      if (resourceField.getText().trim().length() == 0)
      {
        File localFile = new File(fileOrPathName.getAbsolutePath(), "/Resources/");
        resourceField.setText(localFile.getCanonicalPath());
        searchOutThemes(localFile);
      }
      
      if (logField.getText().trim().length() == 0) {
        logField.setText(new File(fileOrPathName.getAbsolutePath(), "/Logs/").getCanonicalPath());
      }
      
      if (configurationField.getText().trim().length() == 0) {
        configurationField.setText(new File(fileOrPathName.getAbsolutePath(), "/UserConfiguration/").getCanonicalPath());
      }

    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }

    examine();

  }
  
  /**
   * Called from void infer(File fileOrPathName)
   * Called from boolean examine()
   */
  void searchOutThemes(File fileOrPathName)
  {
    if (fileOrPathName == themeSearchRoot) {
      return;
    }
    if (fileOrPathName == null)
    {
      themeSelector.setEnabled(false);
      themeSelector.setSelectedItem(null);
      return;
    }
    if (fileOrPathName.equals(themeSearchRoot)) {
      return;
    }
    themeSearchRoot = fileOrPathName;
    liveOperationThemes.removeAllElements();
    replayOperationThemes.removeAllElements();
    String str1 = "Gui/Config/";
    Vector localVector = new Vector();
    localVector.add(".");
    File[] arrayOfFile1 = fileOrPathName.listFiles();
    if (arrayOfFile1 != null) {
      for (localTreeSet2 : arrayOfFile1) {
        if (localTreeSet2.isDirectory()) {
          localVector.add(localTreeSet2.getName());
        }
      }
    }
    ??? = Pattern.compile("systemStartup_([^.]+).xml");
    Pattern localPattern = Pattern.compile("replay_([^.]+).xml");
    TreeSet localTreeSet1 = new TreeSet();
    TreeSet localTreeSet2 = new TreeSet();
    Iterator localIterator = localVector.iterator();
    String str2;
    while (localIterator.hasNext())
    {
      str2 = (String)localIterator.next();
      File localFile1 = new File(themeSearchRoot, String.format("%s/%s", new Object[] { str2, str1 }));
      arrayOfFile1 = localFile1.listFiles();
      if (arrayOfFile1 != null) {
        for (File localFile2 : arrayOfFile1) {
          if (localFile2.isFile())
          {
            Matcher localMatcher = ((Pattern)???).matcher(localFile2.getName());
            if (localMatcher.matches()) {
              localTreeSet1.add(localMatcher.group(1));
            }
            localMatcher = localPattern.matcher(localFile2.getName());
            if (localMatcher.matches()) {
              localTreeSet2.add(localMatcher.group(1));
            }
          }
        }
      }
    }
    liveOperationThemes.addElement("Default");
    localIterator = localTreeSet1.iterator();
    while (localIterator.hasNext())
    {
      str2 = (String)localIterator.next();
      liveOperationThemes.addElement(str2);
    }
    replayOperationThemes.addElement("Default");
    localIterator = localTreeSet2.iterator();
    while (localIterator.hasNext())
    {
      str2 = (String)localIterator.next();
      replayOperationThemes.addElement(str2);
    }
  }
  
  /**
   * Entry-point for Start.jar
   */
  public static void main(String[] paramArrayOfString)
  {
    
    // Set the Default Look-and-Feel for Swing UI Components
    String str1 = "com.birosoft.liquid.LiquidLookAndFeel";
    System.setProperty("swing.defaultlaf", str1);
    try
    {
      UIManager.setLookAndFeel(str1);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }

    try
    {

      Start localStart = new Start();
      int i;

      // Combo-box for "Live Operation Themes"?
      liveOperationThemes.setSelectedItem("Default");

      // LIVE_KEYWORD = live.DSZ_KEYWORD
      // user.defaults.live.DSZ_KEYWORD = Simple
      String str2 = getStringDefault(LIVE_KEYWORD);
      if (str2 != null)
      {
        for (i = 0; i < liveOperationThemes.getSize(); i++) {
          if (str2.equals(liveOperationThemes.getElementAt(i)))
          {
            liveOperationThemes.setSelectedItem(str2);
            break;
          }
        }
      }
      
      str2 = getStringDefault(REPLAY_KEYWORD);
      replayOperationThemes.setSelectedItem("Default");
      
      if (str2 != null)
      {
        for (i = 0; i < replayOperationThemes.getSize(); i++)
        {
          if (str2.equals(replayOperationThemes.getElementAt(i)))
          {
            replayOperationThemes.setSelectedItem(str2);
            break;
          }
        }
      }
      
      if (getBooleanDefault("OpMode", Boolean.valueOf(true)).booleanValue()) {
        liveOption.setSelected(true);
        themeSelector.setModel(liveOperationThemes);
      }
      else
      {
        replayOption.setSelected(true);
        themeSelector.setModel(replayOperationThemes);
      }
      
      String str3 = resourceField.getText();
      if (str3.endsWith(prop.getProperty("res.dir", "Dsz")))
      {
        str3 = str3.substring(0, str3.lastIndexOf(prop.getProperty("res.dir", "Dsz")));
        resourceField.setText(str3);
      }
      operationPanel.setVisible(localStart.isShowOpType());
      if (localStart.isShowDebugCore())
      {
        buildRelease.setSelected(getBooleanDefault("BuildType", Boolean.valueOf(true)).booleanValue());
        buildDebug.setSelected(!getBooleanDefault("BuildType", Boolean.valueOf(true)).booleanValue());
      }
      else
      {
        buildRelease.setSelected(true);
        buildDebug.setSelected(false);
      }
      if (localStart.isShowDebugGui())
      {
        guiRelease.setSelected(getBooleanDefault("GuiType", Boolean.valueOf(true)).booleanValue());
        guiDebug.setSelected(!getBooleanDefault("GuiType", Boolean.valueOf(true)).booleanValue());
      }
      else
      {
        guiRelease.setSelected(true);
        guiDebug.setSelected(false);
      }
      if (localStart.isShowLocal()) {
        localMode.setSelected(getBooleanDefault("LocalMode", Boolean.valueOf(false)).booleanValue());
      } else {
        localMode.setSelected(false);
      }
      loadPrevious.setSelected(getBooleanDefault("LoadPrevious", Boolean.valueOf(false)).booleanValue());
      if (localStart.isShowThreadDump()) {
        threadDump.setSelected(getBooleanDefault("thread.dump", Boolean.valueOf(false)).booleanValue());
      }
      waitFor.setSelected(getBooleanDefault("wait.for.output", Boolean.valueOf(false)).booleanValue());
      int j = 0;
      for (int k = 0; k < paramArrayOfString.length; k++)
      {
        String str4 = paramArrayOfString[k];
        String[] arrayOfString = str4.split("=", 2);
        if (arrayOfString.length == 0)
        {
          doHelp(localStart);
          System.exit(0);
        }
        String str5 = arrayOfString[0].toLowerCase();
        if (str5.equals("-core"))
        {
          if (arrayOfString.length == 2)
          {
            arrayOfString[1] = arrayOfString[1].toLowerCase();
            if (arrayOfString[1].equals("debug")) {
              buildDebug.setSelected(true);
            } else if (arrayOfString[1].equals("release")) {
              buildRelease.setSelected(true);
            } else {
              doHelp(localStart);
            }
          }
          else
          {
            doHelp(localStart);
          }
        }
        else if (str5.equals("-gui"))
        {
          if (arrayOfString.length == 2)
          {
            arrayOfString[1] = arrayOfString[1].toLowerCase();
            if (arrayOfString[1].equals("debug")) {
              guiDebug.setSelected(true);
            } else if (arrayOfString[1].equals("release")) {
              guiRelease.setSelected(true);
            } else {
              doHelp(localStart);
            }
          }
          else
          {
            doHelp(localStart);
          }
        }
        else if (str5.equals("-debug"))
        {
          buildDebug.setSelected(true);
        }
        else if (str5.equals("-release"))
        {
          buildRelease.setSelected(true);
        }
        else if (str5.equals("-local"))
        {
          localMode.setSelected(true);
        }
        else if (str5.equals("-previous"))
        {
          loadPrevious.setSelected(true);
        }
        else if (str5.equals("-live"))
        {
          liveOption.setSelected(true);
        }
        else if (str5.equals("-replay"))
        {
          replayOption.setSelected(true);
        }
        else if ((str5.equals("-opsdisk")) && (arrayOfString.length == 2))
        {
          operationField.setText(arrayOfString[1]);
        }
        else if ((str5.equals("-resource")) && (arrayOfString.length == 2))
        {
          resourceField.setText(arrayOfString[1]);
        }
        else if ((str5.equals("-log")) && (arrayOfString.length == 2))
        {
          logField.setText(arrayOfString[1]);
        }
        else if ((str5.equals("-config")) && (arrayOfString.length == 2))
        {
          configurationField.setText(arrayOfString[1]);
        }
        else if (str5.equals("-load"))
        {
          j = 1;
        }
        else
        {
          doHelp(localStart);
          System.exit(0);
        }
      }

      if ((j != 0) && (localStart.isReady())) {
        localStart.DanderSpritzBegin();
      } else {
        localStart.setVisible(true);
      }

    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
      JOptionPane.showMessageDialog(null, "Start.jar requires Java SE6", "Incorrect Runtime", 0);
    }
  }
  
  /**
   * Returns a string-value property located in user.defaults if it exists, 
   * otherwise it will return a blank string.
   *
   * @param   propertyName  the name of the property to lookup in user.defaults
   * @return                the value of the property in user.defaults or a blank-string if not found
   */
  public static String getStringDefault(String propertyName) {
    return getStringDefault(propertyName, "");
  }
  
  /**
   * Returns a string-value property located in user.defaults if it exists, 
   * otherwise it will return the defaultValue parameter being passed.
   * 
   * @param   propertyName  the name of the property to lookup in user.defaults
   * @param   defaultValue  the default value to return if one cannot be found in user.defaults
   * @return                the value of the property in user.defaults or the value of defaultValue if not found
   */
  public static String getStringDefault(String propertyName, String defaultValue) {
    try {
      return userDefaults.getProperty(propertyName, defaultValue);
    }
    catch (Exception localException) {}
    return defaultValue;
  }
  
  /**
   * Returns a boolean-value property located in user.defaults if it exists, 
   * otherwise it will return the Boolean.TRUE.
   *
   * @param   propertyName  the name of the property to lookup in user.defaults
   * @return                the value of the property in user.defaults or a blank-string if not found
   */
  public static Boolean getBooleanDefault(String propertyName) {
    return getBooleanDefault(propertyName, Boolean.TRUE);
  }
  
  /**
   * Returns a boolean-value property located in user.defaults if it exists, 
   * otherwise it will return the defaultValue parameter being passed.
   *
   * @param   propertyName  the name of the property to lookup in user.defaults
   * @param   defaultValue  the default value to return if one cannot be found in user.defaults
   * @return                the value of the property in user.defaults or the value of defaultValue if not found
   */
  public static Boolean getBooleanDefault(String propertyName, Boolean defaultValue) {
    try {
      return Boolean.valueOf(Boolean.parseBoolean(userDefaults.getProperty(propertyName, paramBoolean.toString())));
    }
    catch (Exception localException) {}
    return defaultValue;
  }
  
  /**
   * Persists a property's key/value pair into user.defaults given the property's 
   * name and its corresponding string-value.
   * 
   * A null propertyValue parameter will remove the key/value pair in user.defaults.
   * 
   * @param   propertyName  the name of the property to store in user.defaults
   * @param   propertyValue the value of the property to store in user.defaults
   */
  public static void setStringDefault(String propertyName, String propertyValue) {
    if (propertyValue == null) {
      userDefaults.remove(propertyName);
    } else {
      userDefaults.setProperty(propertyName, propertyValue);
    }
  }
  
  /**
   * Persists a property's key/value pair into user.defaults given the property's 
   * name and its corresponding boolean-value.
   * 
   * A null propertyValue parameter will remove the key/value pair in user.defaults.
   * 
   * @param   propertyName  the name of the property to store in user.defaults
   * @param   propertyValue the value of the property to store in user.defaults
   */
  public static void setBooleanDefault(String propertyName, Boolean propertyValue) {
    if (propertyValue == null) {
      setStringDefault(propertyName, null);
    } else {
      setStringDefault(propertyName, propertyValue.toString());
    }
  }
  
  /**
   * 
   * 
   * @param   paramStart  <fill this in>
   */
  public static void doHelp(Start paramStart) {

    StringWriter localStringWriter = new StringWriter();
    PrintWriter localPrintWriter = new PrintWriter(localStringWriter);
    StringBuilder localStringBuilder = new StringBuilder();
    localPrintWriter.println("Help for Start.jar");
    localPrintWriter.println("Start.jar [-previous] [-live|-replay] [-opsdisk=DIR] [-resource=DIR] [-log=DIR] [-config=DIR] [-load]");
    localPrintWriter.println("\t[-previous]:  Automatically load previous operations");
    localPrintWriter.println("\t[-live]:  A live operation");
    localPrintWriter.println("\t[-replay]:  A replay operation");
    localPrintWriter.println("\t[-opsdisk=DIR]:  Set the operations disk value to the given DIR");
    localPrintWriter.println("\t[-resource=DIR]:  Set the resource directory to the given DIR");
    localPrintWriter.println("\t[-log=DIR]:  Set the log directory to the given DIR");
    localPrintWriter.println("\t[-config=DIR]:  Set the user configuration directory to the given DIR");
    
    if ((paramStart.isShowDebugCore()) || (paramStart.isShowLocal()) || (paramStart.isShowDebugGui())) {
      localPrintWriter.println("\nExtra Parameters:");
      if (paramStart.isShowDebugCore()) {
        localPrintWriter.println("\t[-core=<debug|release>]");
        localPrintWriter.println("\t\t[debug]: Tells DanderSpritz to load the debug version of the core");
        localPrintWriter.println("\t\t[release]: Tells DanderSpritz to load the release version of the core");
      }
      if (paramStart.isShowDebugGui()) {
        localPrintWriter.println("\t[-gui=<debug|release>]");
        localPrintWriter.println("\t\t[-debug]: Tells DanderSpritz to load the debug version of the gui");
        localPrintWriter.println("\t\t[-release]: Tells DanderSpritz to load the release version of the gui");
      }
      if (paramStart.isShowLocal()) {
        localPrintWriter.println("\t[-local]:  Turns on local mode");
      }
    }

    JTextArea localJTextArea = new JTextArea();
    localJTextArea.setFont(new Font("Monospaced", 0, 14));
    localJTextArea.setTabSize(4);
    JScrollPane localJScrollPane = new JScrollPane(localJTextArea);
    Dimension localDimension = new Dimension(900, 350);
    localJScrollPane.setMinimumSize(localDimension);
    localJScrollPane.setPreferredSize(localDimension);
    localJScrollPane.setSize(localDimension);
    localJScrollPane.setMaximumSize(localDimension);
    localJTextArea.setText(localStringWriter.toString());
    JOptionPane.showMessageDialog(null, localJScrollPane, "Start Help", 1);

  }
  
  /**
   * Performs an evaluation on the path provided and returns a value
   * depending on many factors (see @return values)
   * 
   * @param   path                        the path to evaluate against
   * @param   suppressDirectoryCreation   whether or not to automatically create a directory for the path provided, if one does not already exist
   * 
   * @return    - true:  if a file corresponding to the path does exist AND is of type directory
   *            - true:  if a file corresponding to the path does not exist AND we were successful creating a new directory
   *            - false: if the path contains invalid characters
   *            - false: if a file corresponding to the path does not exist AND directory creation is suppressed
   *            - false: if all else fails
   */
  private boolean evaluatePath(String path, boolean suppressDirectoryCreation)
  {

    // Ensure that the path doesn't contain any invalid characters
    for (int k : INVALIDCHARACTERS) {
      if (path.indexOf(k) >= 0) {
        return false;
      }
    }

    // Return true if the path exists and it is a directory
    File file = new File(path);
    if (file.exists() && file.isDirectory()) {
      return true;
    }

    if (!file.exists())
    {

      // Do not automatically create a directory
      if (supressDirectoryCreation) {
        return false;
      }

      return file.mkdirs();

    }

    return false;

  }
  
  /**
   * Returns a value indicating whether or not the following fields have valid values:
   * 
   * - Operation Field (should be a directory path)
   * - Resource Field (should be a directory path)
   * - Log Field (should be a directory path)
   * - Configuration Field (should be a directory path)
   * - Local Communications Address (should be an IP Address)
   * 
   * @return    value indicating whether or not the required configuration fields are valid
   */
  public boolean evaluate()
  {

    if (!evaluatePath(operationField.getText(), true)) {
      return error("Operation Disk location '" + operationField.getText() + "' does not exist or is not a directory");
    }

    if (!evaluatePath(resourceField.getText(), true)) {
      return error("Resource location '" + resourceField.getText() + "' does not exist or is not a directory");
    }

    if (!evaluatePath(logField.getText(), false)) {
      return error("Log directory '" + logField.getText() + "' is not a directory");
    }

    if (!evaluatePath(configurationField.getText(), false)) {
      return error("Configuration directory '" + configurationField.getText() + "' is not a directory");
    }

    if (!isValidId(localCommsAddressField.getText())) {
      return error(String.format("Comms Address '%s' is invalid", new Object[] { localCommsAddressField.getText() }));
    }

    return true;

  }
  
  /**
   * Returns a value indicating whether the provided address
   * is a valid Communications (IP) Address using a predefined
   * set of RegEx Patterns.
   * 
   * @param   communicationsAddress   the Communications (IP) Address to test for validity
   */
  private boolean isValidId(String communicationsAddress)
  {
    for (Pattern localPattern : patterns) {
      if (localPattern.matcher(paramString).matches()) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Displays a Swing UI Message Dialog with the
   * specified Error Message text.
   * 
   * @return <why does this return false all the time?>
   */
  public boolean error(String errorMessage)
  {
    JOptionPane.showMessageDialog(this, errorMessage, "Invalid parameters", 0);
    return false;
  }
  
  /**
   * Returns a value indicating whether or not we're ready <for what?> if:
   * 
   * 1. We're not attempting to guess the user.defaults file and infer it
   * 2. evaluate returns true (wtf does taht do?)
   * 3. examine returns true (wtf does that do?)
   * 
   * @return    value indicating if we're ready <for what?>
   */
  public boolean isReady()
  {
    return (!inferTextFieldValues) && (evaluate()) && (examine());
  }
  
  /**
   * Returns a value indicating whether:
   * 1. The Directory exists
   * 2. The Directory was successfully created if it didn't already exist
   * 
   * @param   directoryPath   the path to the directory to test for / create
   * @return                  true if either the directory already exists, or the function was successful in creating it
   */
  public boolean isDir(String directoryPath)
  {
    if (paramString == null) {
      return false;
    }
    if (paramString.length() == 0) {
      return false;
    }
    File localFile = new File(paramString);
    if (localFile.exists()) {
      return localFile.isDirectory();
    }
    return localFile.mkdirs();
  }
  
  public void DanderSpritzBegin()
  {
    if (!evaluate()) {
      return;
    }
    new Thread(new Runnable()
    {
      public void run()
      {
        Start.this.beginImpl();
      }
    }, "Start DanderSpritz").start();
  }
  
  /**
   * The start of the DanderSpritz Implementation
   */
  private void beginImpl()
  {

    // Check to see if the configuration area is read-only or not,
    // if it is, then throw a dialog up and let the user decide what
    // to do to proceed.
    File localFile1 = new File(configurationField.getText(), "testfile.dsz");
    File localFile2 = localFile1.getParentFile();
    int i = 0;
    localFile2.mkdirs();
    if (localFile1.exists())
    {
      if (localFile1.delete()) {
        i = 1;
      }
    }
    else {
      try
      {
        FileOutputStream localFileOutputStream1 = new FileOutputStream(localFile1);
        localFileOutputStream1.write(90);
        localFileOutputStream1.close();
        localFile1.delete();
        i = 1;
      }
      catch (Exception localException1) {}
    }
    if (i == 0)
    {
      int j = JOptionPane.showConfirmDialog(null, "Danderspritz is unable to write to the user configuration area.\nWithout the ability to write there, several plugins will not work.\nDanderspritz will now change the user configuration area to your temp directory,\nand you will lose any existing customization you have performed.\nSelect 'No' to ignore this problem, or 'Cancel' to change the directory.", "User Configuration is Read-Only", 1, 2, null);
      switch (j)
      {
      case 0: 
        localFile2 = new File(System.getProperty("java.io.tmpdir"), "UserConfiguration");
        break;
      case 1: 
        break;
      case 2: 
        return;
      }
    }

    EventQueue.invokeLater(new Runnable()
    {
      public void run()
      {
        setVisible(false);
        dispose();
      }
    });

    String str1 = System.getProperty("com.sun.management.jmxremote.port");
    if (str1 != null)
    {
      File localFile3 = new File(logField.getText(), String.format("Dump-%s.txt", new Object[] { str1 }));
      if (!localFile3.exists()) {
        try
        {
          FileOutputStream localFileOutputStream2 = new FileOutputStream(localFile3);
          localFileOutputStream2.close();
        }
        catch (Exception localException3)
        {
          Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, localException3);
        }
      }
    }

    setStringDefault("OpsDisk", operationField.getText());
    setStringDefault("ResourceDir", resourceField.getText());
    setStringDefault("LogDir", logField.getText());
    setStringDefault("ConfigDir", configurationField.getText());
    setStringDefault("LocalAddress", localCommsAddressField.getText());

    if ((liveOperationThemes.getSelectedItem() != null) && (!liveOperationThemes.getSelectedItem().equals("Default"))) {
      setStringDefault(LIVE_KEYWORD, liveOperationThemes.getSelectedItem().toString());
    } else {
      setStringDefault(LIVE_KEYWORD, null);
    }

    if ((replayOperationThemes.getSelectedItem() != null) && (!replayOperationThemes.getSelectedItem().equals("Default"))) {
      setStringDefault(REPLAY_KEYWORD, replayOperationThemes.getSelectedItem().toString());
    } else {
      setStringDefault(REPLAY_KEYWORD, null);
    }

    setBooleanDefault("OpMode", Boolean.valueOf(liveOption.isSelected()));
    setBooleanDefault("BuildType", Boolean.valueOf(buildRelease.isSelected()));
    setBooleanDefault("GuiType", Boolean.valueOf(guiRelease.isSelected()));
    setBooleanDefault("LocalMode", Boolean.valueOf(localMode.isSelected()));
    setBooleanDefault("LoadPrevious", Boolean.valueOf(loadPrevious.isSelected()));
    setBooleanDefault("thread.dump", Boolean.valueOf(threadDump.isSelected()));
    setBooleanDefault("wait.for.output", Boolean.valueOf(waitFor.isSelected()));
    
    try
    {
      userDefaults.store(new FileOutputStream("user.defaults"), "Autogenerated DanderSpritz configuration.  Do not edit manually");
    }
    catch (Exception localException2) {}
    
    ProcessBuilder localProcessBuilder = new ProcessBuilder(new String[0]);
    Vector localVector1 = new Vector();
    localProcessBuilder.command(localVector1);
    String str2 = resourceField.getText();
    localVector1.add(prop.getProperty("java.exe", "java"));
    Object localObject3;
    for (localObject3 : prop.getProperty("vmargs", "").split("\\s")) {
      if (localObject3.length() > 0) {
        localVector1.add(localObject3);
      }
    }
    if (guiDebug.isSelected()) {
      for (localObject3 : prop.getProperty("vmargs.debug", "").split("\\s")) {
        if (localObject3.length() > 0) {
          localVector1.add(localObject3);
        }
      }
    }
    localVector1.add(String.format("-Djava.endorsed.dirs=%s/ExternalLibraries/%s/endorsed", new Object[] { str2, "java-j2se_1.6-sun" }));
    ??? = new Vector();
    Vector localVector2 = new Vector();
    addJars((List)???, new File(String.format("%s/ExternalLibraries/%s", new Object[] { str2, "java-j2se_1.6-sun" })));
    localVector2.add("Ops");
    localVector2.add(".");
    localVector2.add(prop.getProperty("res.dir", "Dsz"));
    File localFile5;
    for (localFile5 : new File(str2).listFiles()) {
      if ((localFile5.isDirectory()) && (!localVector2.contains(localFile5.getName()))) {
        localVector2.add(localFile5.getName());
      }
    }
    ??? = localVector2.iterator();
    Object localObject6;
    while (((Iterator)???).hasNext())
    {
      localObject4 = (String)((Iterator)???).next();
      File localFile4 = new File(String.format("%s/%s/Gui", new Object[] { str2, localObject4 }));
      if (localFile4.exists())
      {
        localFile5 = new File(localFile4, "Config");
        if (localFile5.exists()) {
          ((List)???).add(localFile5.getAbsolutePath());
        }
        localObject6 = new File(localFile4, String.format("%s/%s", new Object[] { "lib", "java-j2se_1.6-sun" }));
        if (((File)localObject6).exists()) {
          addJars((List)???, (File)localObject6);
        }
      }
    }
    int n = 0;
    if (System.getProperty("os.name").toLowerCase().startsWith(prop.getProperty("windows.start", "win"))) {
      n = 1;
    }
    Object localObject4 = new URL[((List)???).size()];
    for (int i3 = 0; i3 < ((List)???).size(); i3++) {
      try
      {
        localObject4[i3] = new File((String)((List)???).get(i3)).toURI().toURL();
      }
      catch (MalformedURLException localMalformedURLException)
      {
        localMalformedURLException.printStackTrace();
      }
    }
    Object localObject5 = ClassLoader.getSystemClassLoader();
    int i4 = 0;
    Object localObject8;
    Object localObject9;
    if ((localObject5 instanceof URLClassLoader)) {
      try
      {
        localObject6 = (URLClassLoader)localObject5;
        localObject7 = URLClassLoader.class;
        localObject8 = ((Class)localObject7).getDeclaredField("ucp");
        ((Field)localObject8).setAccessible(true);
        localObject9 = (URLClassPath)((Field)localObject8).get(localObject6);
        for (URL localURL : localObject4) {
          ((URLClassPath)localObject9).addURL(localURL);
        }
        i4 = 1;
      }
      catch (Exception localException4)
      {
        localException4.printStackTrace();
      }
    }
    if (i4 == 0) {
      localObject5 = new URLClassLoader((URL[])localObject4, ClassLoader.getSystemClassLoader());
    }
    Vector localVector3 = new Vector();
    localVector3.add(String.format("-logDir=%s", new Object[] { logField.getText() }));
    localVector3.add(String.format("-resourceDir=%s", new Object[] { str2 }));
    localVector3.add(String.format("-comms=%s", new Object[] { localCommsAddressField.getText() }));
    localVector3.add(String.format("-build=%s", new Object[] { prop.getProperty(String.format("%s.%s", new Object[] { n != 0 ? "windows" : "linux", buildRelease.isSelected() ? "build.release" : "build.debug" })) }));
    localVector3.add(String.format("-local=%s", new Object[] { localMode.isSelected() ? "true" : "false" }));
    localVector3.add(String.format("-config=%s", new Object[] { localFile2.getAbsolutePath() }));
    localVector3.add(String.format("-loadPrevious=%s", new Object[] { loadPrevious.isSelected() ? "true" : "false" }));
    localVector3.add(String.format("-threadDump=%s", new Object[] { threadDump.isSelected() ? "true" : "false" }));
    Object localObject7 = null;
    if (liveOption.isSelected()) {
      localObject7 = prop.getProperty("live.operation");
    } else {
      localObject7 = prop.getProperty("replay.operation");
    }
    if (n != 0) {
      addLibraryPath(String.format("%s\\ExternalLibraries\\%s", new Object[] { str2, prop.getProperty(String.format("%s.%s", new Object[] { "windows", "tool.chain" })) }));
    } else {
      addLibraryPath(String.format("%s/ExternalLibraries/%s", new Object[] { str2, prop.getProperty(String.format("%s.%s", new Object[] { "linux", "tool.chain" })) }));
    }
    System.setProperty("windows.tool.chain", prop.getProperty(String.format("%s.%s", new Object[] { "windows", "tool.chain" })));
    System.setProperty("linux.tool.chain", prop.getProperty(String.format("%s.%s", new Object[] { "linux", "tool.chain" })));
    if ((themeSelector.getSelectedItem() != null) && (!themeSelector.getSelectedItem().equals("Default"))) {
      System.setProperty("DSZ_KEYWORD", themeSelector.getSelectedItem().toString());
    }
    try
    {
      Thread.currentThread().setContextClassLoader((ClassLoader)localObject5);
      localObject8 = Class.forName((String)localObject7, true, (ClassLoader)localObject5);
      localObject9 = ((Class)localObject8).getMethod("main", new Class[] { [Ljava.lang.String.class });
      ??? = Class.forName("ds.core.DSConstants", true, (ClassLoader)localObject5);
      Method localMethod = ((Class)???).getMethod("setLoader", new Class[] { ClassLoader.class });
      localMethod.invoke(null, new Object[] { localObject5 });
      String[] arrayOfString = new String[localVector3.size()];
      arrayOfString = (String[])localVector3.toArray(arrayOfString);
      ((Method)localObject9).invoke(null, new Object[] { arrayOfString });
    }
    catch (Exception localException5)
    {
      localException5.printStackTrace();
      JOptionPane.showMessageDialog(null, "Unable to start DanderSpritz.  The OpsDisk appears incomplete.", "Invalid OpsDisk", 0);
      examine();
      setVisible(true);
    }
  }
  
  private void addJars(List<String> paramList, File fileOrPathName)
  {
    if (!fileOrPathName.isDirectory()) {
      return;
    }
    for (File localFile : fileOrPathName.listFiles(jars)) {
      paramList.add(localFile.getAbsolutePath());
    }
  }
  
  private void addJarsRecursively(List<String> paramList, File fileOrPathName)
  {
    if (!fileOrPathName.isDirectory()) {
      return;
    }
    addJars(paramList, fileOrPathName);
    for (File localFile : fileOrPathName.listFiles()) {
      if ((localFile.isDirectory()) && (!localFile.getName().equals(".svn"))) {
        addJarsRecursively(paramList, localFile);
      }
    }
  }
  
  public boolean isShowOpType()
  {
    return getBooleanProperty("show.optype", Boolean.valueOf(true));
  }
  
  public boolean isShowDebugCore()
  {
    return getBooleanProperty("show.debug.core", Boolean.valueOf(false));
  }
  
  public boolean isShowDebugGui()
  {
    return getBooleanProperty("show.debug.gui", Boolean.valueOf(false));
  }
  
  public boolean isShowLocal()
  {
    return getBooleanProperty("show.local.mode", Boolean.valueOf(false));
  }
  
  public boolean isShowThreadDump()
  {
    return getBooleanProperty("show.thread.dump", Boolean.valueOf(false));
  }
  
  public boolean getBooleanProperty(String paramString, Boolean paramBoolean)
  {
    try
    {
      return Boolean.parseBoolean(prop.getProperty(paramString, paramBoolean.toString()));
    }
    catch (Throwable localThrowable) {}
    return paramBoolean.booleanValue();
  }
  
  public static void addLibraryPath(String paramString)
  {
    String str1 = null;
    try
    {
      str1 = new File(paramString).getCanonicalPath();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      return;
    }
    try
    {
      Field localField = ClassLoader.class.getDeclaredField("usr_paths");
      localField.setAccessible(true);
      String[] arrayOfString1 = (String[])localField.get(null);
      for (String str2 : arrayOfString1) {
        if (str2.equals(str1)) {
          return;
        }
      }
      ??? = new String[arrayOfString1.length + 1];
      System.arraycopy(arrayOfString1, 0, ???, 0, arrayOfString1.length);
      ???[arrayOfString1.length] = str1;
      localField.set(null, ???);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
  
  /**
   * Called from:
   *  start() - if we're inferring the textField values
   *  isReady() - when checking for <what are we checking for?>
   *  enterPressed(KeyEvent paramKeyEvent) - 
   *  infer(File fileOrPathName) - 
   * 
   * @return  a value indicating ...
   */
  private boolean examine()
  {
    
    boolean bool = true;
    Vector localVector1 = new Vector();
    Vector localVector2 = new Vector();
    Vector localVector3 = new Vector();
    Vector localVector4 = new Vector();
    
    // Handle the Operation Field
    localVector1.add(new File(String.format("%s", new Object[] { operationField.getText() })));
    localVector1.add(new File(String.format("%s%s%s", new Object[] { operationField.getText(), File.separator, "Bin" })));
    if (!examine(localVector1, operationField)) {
      bool = false;
    }

    // Handle the Resource Field 
    localVector2.add(new File(String.format("%s", new Object[] { resourceField.getText() })));
    localVector2.add(new File(String.format("%s/%s/%s/%s/%s", new Object[] { resourceField.getText(), File.separator, "Dsz/Gui/lib", "java-j2se_1.6-sun", "Core.jar" })));
    if (!examine(localVector2, resourceField)) {
      bool = false;
    } else {
      searchOutThemes(new File(resourceField.getText()));
    }

    // Handle the Configuration Field (no paths involved for fileList param)
    if (!examine(localVector3, configurationField)) {
      bool = false;
    }

    // Handle the Log Field (no paths involved for fileList param)
    if (!examine(localVector4, logField)) {
      bool = false;
    }

    if (!isValidId(localCommsAddressField.getText()))
    {
      setConfig(localCommsAddressField, false);
      bool = false;
    }
    else
    {
      setConfig(localCommsAddressField, true);
    }
    
    goButton.setEnabled(bool);
    
    return bool;

  }
  
  /**
   * Configures the Foreground and Background colors of a
   * Text Field, making it look either enabled or disabled.
   * 
   * @param   textField           the textField to configure
   * @param   isTextFieldEnabled  true if the textField is enabled, false if not
   */
  private void setConfig(JTextField textField, boolean isTextFieldEnabled)
  {
    if (isTextFieldEnabled)
    {
      textField.setBackground(Color.WHITE);
      textField.setForeground(Color.BLACK);
    }
    else
    {
      textField.setBackground(Color.GRAY);
      textField.setForeground(Color.WHITE);
    }
  }
  
  /**
   * Configures the Foreground and Background colors of a textField
   * to make it look enabled or disabled.
   * 
   * 1. Any invalid characters in the textfield will disable it
   * 2. If there are any files/directories passed in and they
   *    don't exist, this will also disable the textfield
   * 
   * 
   * @param   fileList    the List of File objects to examine and check for existence
   * @param   textField   the textField to operate against
   * @return              value indicating whether the textField is enabled or disabled
   */
  private boolean examine(List<File> fileList, JTextField textField)
  {
    
    boolean isTextFieldEnabled = true;
    
    // Loop through the Invalid Characters array and
    // check to see if the value is in the textField's
    // current text value. If so, mark the field as
    // disabled.
    for (int k : INVALIDCHARACTERS) {
      if (textField.getText().indexOf(k) >= 0)
      {
        isTextFieldEnabled = false;
        break;
      }
    }

    Iterator fileListIterator = fileList.iterator();
    while (fileListIterator.hasNext())
    {
      File localFile = (File)fileListIterator.next();
      isTextFieldEnabled = (isTextFieldEnabled) && (localFile.exists());
    }

    setConfig(textField, isTextFieldEnabled);

    return isTextFieldEnabled;

  }

}
