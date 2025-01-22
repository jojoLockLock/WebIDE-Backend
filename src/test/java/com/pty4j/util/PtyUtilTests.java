package com.pty4j.util;

import com.sun.jna.Platform;
import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;
import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PtyUtilTests {

    private Map<String, String> testEnv;

    @Before
    public void setUp() {
        testEnv = new HashMap<>();
        testEnv.put("key1", "value1");
        testEnv.put("key2", "value2");
    }

    @Test
    public void testToStringArray() {
        String[] result = PtyUtil.toStringArray(testEnv);
        assertEquals(2, result.length);
        assertTrue(result[0].matches("key[12]=value[12]"));
        assertTrue(result[1].matches("key[12]=value[12]"));
    }

    @Test
    public void testToStringArrayWithNull() {
        String[] result = PtyUtil.toStringArray(null);
        assertEquals(0, result.length);
    }

    @Test
    public void testGetJarContainingFolderPath() throws Exception {
        String path = PtyUtil.getJarContainingFolderPath(PtyUtil.class);
        assertNotNull(path);
        assertTrue(new File(path).exists());
    }

    @Test
    public void testGetPtyLibFolderPath() throws Exception {
        String path = PtyUtil.getPtyLibFolderPath();
        assertNotNull(path);
        assertTrue(new File(path).exists());
    }

    @Test
    @Ignore("Native library files not available in test environment")
    public void testResolveNativeLibrary() throws Exception {
        File lib = PtyUtil.resolveNativeLibrary();
        assertNotNull(lib);
        assertTrue(lib.getName().contains(getPlatformLibName()));
    }

    @Test
    public void testResolveNativeFile() throws Exception {
        String testFileName = "test.txt";
        File file = PtyUtil.resolveNativeFile(testFileName);
        assertNotNull(file);
        assertTrue(file.getPath().contains(getPlatformFolder()));
    }

    @Test
    public void testResolveNativeFileWithParent() {
        String testFileName = "test.txt";
        File parent = new File(System.getProperty("java.io.tmpdir"));
        File file = PtyUtil.resolveNativeFile(parent, testFileName);
        assertNotNull(file);
        assertTrue(file.getPath().contains(getPlatformFolder()));
    }

    @Test
    public void testIsWinXp() {
        boolean isXp = PtyUtil.isWinXp();
        if (Platform.isWindows()) {
            assertEquals(isXp, PtyUtil.OS_VERSION.equals("5.1") || PtyUtil.OS_VERSION.equals("5.2"));
        } else {
            assertFalse(isXp);
        }
    }

    private String getPlatformFolder() {
        if (Platform.isMac()) return "macosx";
        if (Platform.isWindows()) return "win";
        if (Platform.isLinux()) return "linux";
        if (Platform.isFreeBSD()) return "freebsd";
        if (Platform.isOpenBSD()) return "openbsd";
        return "";
    }

    private String getPlatformLibName() {
        if (Platform.isMac()) return "libpty.dylib";
        if (Platform.isWindows()) return "winpty.dll";
        if (Platform.isLinux() || Platform.isFreeBSD() || Platform.isOpenBSD()) return "libpty.so";
        return "";
    }
}
