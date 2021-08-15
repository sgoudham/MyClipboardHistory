package me.goudham;

import dorkbox.os.OS;
import dorkbox.systemTray.Checkbox;
import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.Separator;
import dorkbox.systemTray.SystemTray;
import dorkbox.util.CacheUtil;
import dorkbox.util.Desktop;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

/**
 * Icons from 'SJJB Icons', public domain/CC0 icon set
 */
public
class TestTray {

    public static final URL BLUE_CAMPING = TestTray.class.getResource("icons8-link-60.png");
    public static final URL BLACK_FIRE = TestTray.class.getResource("icons8-link-60.png");

    public static final URL BLACK_MAIL = TestTray.class.getResource("icons8-link-60.png");
    public static final URL GREEN_MAIL = TestTray.class.getResource("icons8-link-60.png");

    public static final URL BLACK_BUS = TestTray.class.getResource("icons8-link-60.png");
    public static final URL LT_GRAY_BUS = TestTray.class.getResource("icons8-link-60.png");

    public static final URL BLACK_TRAIN = TestTray.class.getResource("icons8-link-60.png");
    public static final URL GREEN_TRAIN = TestTray.class.getResource("icons8-link-60.png");
    public static final URL LT_GRAY_TRAIN = TestTray.class.getResource("icons8-link-60.png");

    // from issue 123
    public static final URL NOTIFY_IMAGE = TestTray.class.getResource("icons8-link-60.png");

    public static
    void main(String[] args) {
        // make sure JNA jar is on the classpath!
        new TestTray();
    }

    private final SystemTray systemTray;
    private final ActionListener callbackGray;

    public
    TestTray() {
        SystemTray.DEBUG = true; // for test apps, we always want to run in debug mode
        // SystemTray.FORCE_TRAY_TYPE = SystemTray.TrayType.Swing;
        // for test apps, make sure the cache is always reset. These are the ones used, and you should never do this in production.
        CacheUtil.clear("SysTrayExample");

        // SwingUtil.setLookAndFeel(null); // set Native L&F (this is the System L&F instead of CrossPlatform L&F)
        // SystemTray.SWING_UI = new CustomSwingUI();
        this.systemTray = SystemTray.get("SysTrayExample");
        if (systemTray == null) {
            throw new RuntimeException("Unable to load SystemTray!");
        }

        // SWT/JavaFX "shutdown hooks" have changed. Since it's no longer available with JPMS, it is no longer supported.
        // Developers must add the shutdown hooks themselves.
        systemTray.setTooltip("Mail Checker");
        systemTray.setImage(LT_GRAY_TRAIN);
        systemTray.setStatus("No Mail");

        callbackGray = e->{
            final MenuItem entry = (MenuItem) e.getSource();
            systemTray.setStatus(null)
                    .setImage(BLACK_TRAIN);

            entry.setCallback(null);
//                systemTray.setStatus("Mail Empty");
            systemTray.getMenu().remove(entry);
            entry.remove();
            System.err.println("POW");
        };


        Menu mainMenu = systemTray.getMenu();

        MenuItem greenEntry = new MenuItem("Green Mail", e->{
            final MenuItem entry = (MenuItem) e.getSource();
            systemTray.setStatus("Some Mail!");
            systemTray.setImage(GREEN_TRAIN);

            entry.setCallback(callbackGray);
            entry.setImage(BLACK_MAIL);
            entry.setText("Delete Mail");
            entry.setTooltip(null); // remove the tooltip
//                systemTray.remove(menuEntry);
        });
        greenEntry.setImage(GREEN_MAIL);
        // case does not matter
        greenEntry.setShortcut('G');
        greenEntry.setTooltip("This means you have green mail!");
        mainMenu.add(greenEntry);


        Checkbox checkbox = new Checkbox("Euro € Mail", e->System.err.println("Am i checked? " + ((Checkbox) e.getSource()).getChecked()));
        checkbox.setShortcut('€');
        mainMenu.add(checkbox);

        MenuItem removeTest = new MenuItem("This should not be here", e->{
            try {
                Desktop.browseURL("https://git.dorkbox.com/dorkbox/SystemTray");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        mainMenu.add(removeTest);
        mainMenu.remove(removeTest);

        mainMenu.add(new Separator());

        mainMenu.add(new MenuItem("About", e->{
            try {
                Desktop.browseURL("https://git.dorkbox.com/dorkbox/SystemTray");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }));


        mainMenu.add(new MenuItem("Temp Directory", e->{
            try {
                Desktop.browseDirectory(OS.TEMP_DIR.getAbsolutePath());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }));

        mainMenu.add(new MenuItem("Notify", e->{
            final MenuItem entry = (MenuItem) e.getSource();
            systemTray.setStatus("Notification!");
            systemTray.setImage(NOTIFY_IMAGE);

            entry.setImage(NOTIFY_IMAGE);
            entry.setText("Did notify");
            System.err.println("NOTIFICATION!");
        }));


        Menu submenu = new Menu("Options", BLUE_CAMPING);
        submenu.setShortcut('t');


        MenuItem disableMenu = new MenuItem("Disable menu", BLACK_BUS, e->{
            MenuItem source = (MenuItem) e.getSource();
            source.getParent().setEnabled(false);
        });
        submenu.add(disableMenu);


        submenu.add(new MenuItem("Hide tray", LT_GRAY_BUS, e->systemTray.setEnabled(false)));
        submenu.add(new MenuItem("Remove menu", BLACK_FIRE, e->{
            MenuItem source = (MenuItem) e.getSource();
            source.getParent().remove();
        }));

        submenu.add(new MenuItem("Add new entry to tray",
                e->systemTray.getMenu().add(new MenuItem("Random " + new Random().nextInt(10)))));
        mainMenu.add(submenu);

        MenuItem entry = new MenuItem("Type: " + systemTray.getType().toString());
        entry.setEnabled(false);
        systemTray.getMenu().add(entry);

        systemTray.getMenu().add(new MenuItem("Quit", e->{
            systemTray.shutdown();
            //System.exit(0);  not necessary if all non-daemon threads have stopped.
        })).setShortcut('q'); // case does not matter
    }
}