package com.meancat.usefully.zoo;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.meancat.usefully.util.GzipUtil;
import org.apache.commons.io.FileUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Utilities for dealing with Zookeeper and the Curator Framework
 */
public class Zoo {
    private static final Logger logger = LoggerFactory.getLogger(Zoo.class);

    private CuratorFramework curatorFramework;

    private static final long ONE_MEGABYTE = 1048576L;

    public Zoo(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    public void update(String file, CreateMode createMode, byte[] data) throws Exception {
        byte[] compressed = GzipUtil.compress(data);

        logger.debug("Compressed Size: {}", compressed.length);
        logger.debug("Uncompressed Size: {}", data.length);

        if (!verifySize(compressed)) {
            throw new IOException("File is too large to fit into a zookeeper node");
        }

        if (!exists(file)) {
            logger.debug("Creating file {} with {} bytes", file, compressed.length);
            curatorFramework.create()
                    .withMode(createMode)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(file, compressed);
        } else {
            logger.debug("Updating file {} with {} bytes", file, compressed.length);
            curatorFramework.setData().forPath(file, compressed);
        }
    }


    public Stat stat(String path) throws Exception {
        Stat stat = new Stat();
        curatorFramework.getACL().storingStatIn(stat).forPath(path);

        return stat;
    }

    public ReadResult readStreamWithStat(String path) throws Exception {
        Stat stat = new Stat();

        byte[] data = curatorFramework.getData().storingStatIn(stat).forPath(path);

        if(GzipUtil.isGzipped(data)) {
            data = GzipUtil.decompress(data);
        }

        logger.debug("read {} bytes of data from {}", data.length, path);
        return new ReadResult(stat, new ByteArrayInputStream(data));
    }

    public ByteArrayInputStream readStream(String path) throws Exception {
        byte[] data = curatorFramework.getData().forPath(path);

        if(GzipUtil.isGzipped(data)) {
            data = GzipUtil.decompress(data);
        }

        logger.debug("read {} bytes of data from {}", data.length, path);
        return new ByteArrayInputStream(data);
    }

    public byte[] readBytes(String path) throws Exception {
        byte[] data = curatorFramework.getData().forPath(path);

        if(GzipUtil.isGzipped(data)) {
            data = GzipUtil.decompress(data);
        }

        logger.debug("read {} bytes of data from {}", data.length, path);
        return data;
    }

    public String readString(String path) throws Exception {
        return new String(readBytes(path));
    }

    public boolean remove(String path) throws Exception {
        // TODO this has a race condition and should be redone
        if (!exists(path)) {
            logger.warn("Failed to delete: file {} not found", path);
            return false;
        } else {
            logger.info("Deleting file {}", path);
            curatorFramework.delete().forPath(path);
            return true;
        }
    }

    public Set<String> listFiles(String path) throws Exception {
        return Sets.newHashSet(curatorFramework.getChildren().forPath(path));
    }

    public byte[] getFileData(File f) throws IOException {
        return FileUtils.readFileToByteArray(f);
    }

    public boolean verifySize(byte[] data) {
        return data == null || data.length < ONE_MEGABYTE;
    }

    /**
     * Ensure that the path given exists in Zookeeper.  If it doesn't, create it.
     * @param path The path you want to be.
     * @throws Exception
     */
    public void ensurePath(String path) throws Exception {
        logger.debug("ensure path '{}' exists", path);
        Iterable<String> parts = Splitter.on('/').omitEmptyStrings().trimResults().split(path);
        String prev = "/";
        for(String p : parts) {
            logger.debug("Checking path {}", prev + p);
            createPath(prev + p);
            prev = prev + p + "/";
        }
    }

    /**
     * Create a path here.
     *
     * Given path "/foo/bar/blaz", this expects that /foo/bar already exists
     * and blaz is what we're creating.
     *
     * @param path endpoint to create
     * @throws Exception
     */
    private void createPath(String path) throws Exception {
        if (!exists(path)) {
            curatorFramework.create()
                    .withMode(CreateMode.PERSISTENT)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(path);
        }
    }


    /**
     * Does this path exist in zookeeper?
     *
     * @param path to verify
     * @return true if it exists in it's entirity, false otherwise.
     * @throws Exception
     */
    public boolean exists(String path) throws Exception {
        return curatorFramework.checkExists().forPath(path) != null;
    }

    /**
     * Establish a watch on a given path for a given watcher.
     *
     * @param path to watch
     * @param watcher to be notified of changes
     * @throws Exception
     */
    public void watchPath(String path, Watcher watcher) throws Exception {
        curatorFramework.getData().usingWatcher(watcher).forPath(path);
    }
}
