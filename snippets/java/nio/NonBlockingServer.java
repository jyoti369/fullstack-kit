package snippets.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * Java NIO — Non-blocking Server
 * Selector allows one thread to handle multiple connections.
 */
public class NonBlockingServer {

    public static void startServer(int port) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server started on port " + port);

        while (true) {
            selector.select(); // blocks until events are ready
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                try {
                    if (key.isAcceptable()) handleAccept(selector, key);
                    else if (key.isReadable()) handleRead(key);
                } catch (IOException e) {
                    key.cancel();
                    key.channel().close();
                }
            }
        }
    }

    private static void handleAccept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
        System.out.println("Accepted: " + client.getRemoteAddress());
    }

    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        int bytesRead = client.read(buffer);
        if (bytesRead == -1) { client.close(); return; }
        buffer.flip();
        byte[] data = new byte[buffer.limit()];
        buffer.get(data);
        String message = new String(data).trim();
        System.out.println("Received: " + message);
        // Echo back
        buffer.rewind();
        client.write(buffer);
    }

    // ---- FileChannel — file copy ----
    public static void fastCopy(String src, String dst) throws IOException {
        try (FileChannel in  = FileChannel.open(java.nio.file.Path.of(src), java.nio.file.StandardOpenOption.READ);
             FileChannel out = FileChannel.open(java.nio.file.Path.of(dst),
                     java.nio.file.StandardOpenOption.WRITE, java.nio.file.StandardOpenOption.CREATE)) {
            in.transferTo(0, in.size(), out); // zero-copy OS call
        }
    }

    // ---- Memory-mapped file ----
    public static void memoryMap(String filePath) throws IOException {
        try (FileChannel channel = FileChannel.open(java.nio.file.Path.of(filePath),
                java.nio.file.StandardOpenOption.READ)) {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            // Buffer is directly mapped to OS memory — no extra copy
            byte firstByte = buffer.get(0); // OS page fault on first access
            System.out.println("First byte: " + firstByte);
        }
    }

    public static void main(String[] args) throws Exception {
        // Start in background thread
        new Thread(() -> { try { startServer(8080); } catch (IOException e) { e.printStackTrace(); } }).start();
        Thread.sleep(100);
        System.out.println("Server is running. Connect on port 8080.");
    }
}


package snippets.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * Java NIO — Non-blocking Server
 * Selector allows one thread to handle multiple connections.
 */
public class NonBlockingServer {

    public static void startServer(int port) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server started on port " + port);

        while (true) {
            selector.select(); // blocks until events are ready
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                try {
                    if (key.isAcceptable()) handleAccept(selector, key);
                    else if (key.isReadable()) handleRead(key);
                } catch (IOException e) {
                    key.cancel();
                    key.channel().close();
                }
            }
        }
    }

    private static void handleAccept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
        System.out.println("Accepted: " + client.getRemoteAddress());
    }

    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        int bytesRead = client.read(buffer);
        if (bytesRead == -1) { client.close(); return; }
        buffer.flip();
        byte[] data = new byte[buffer.limit()];
        buffer.get(data);
        String message = new String(data).trim();
        System.out.println("Received: " + message);
        // Echo back
        buffer.rewind();
        client.write(buffer);
    }

    // ---- FileChannel — file copy ----
    public static void fastCopy(String src, String dst) throws IOException {
        try (FileChannel in  = FileChannel.open(java.nio.file.Path.of(src), java.nio.file.StandardOpenOption.READ);
             FileChannel out = FileChannel.open(java.nio.file.Path.of(dst),
                     java.nio.file.StandardOpenOption.WRITE, java.nio.file.StandardOpenOption.CREATE)) {
            in.transferTo(0, in.size(), out); // zero-copy OS call
        }
    }

    // ---- Memory-mapped file ----
    public static void memoryMap(String filePath) throws IOException {
        try (FileChannel channel = FileChannel.open(java.nio.file.Path.of(filePath),
                java.nio.file.StandardOpenOption.READ)) {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            // Buffer is directly mapped to OS memory — no extra copy
            byte firstByte = buffer.get(0); // OS page fault on first access
            System.out.println("First byte: " + firstByte);
        }
    }

    public static void main(String[] args) throws Exception {
        // Start in background thread
        new Thread(() -> { try { startServer(8080); } catch (IOException e) { e.printStackTrace(); } }).start();
        Thread.sleep(100);
        System.out.println("Server is running. Connect on port 8080.");
    }
}
