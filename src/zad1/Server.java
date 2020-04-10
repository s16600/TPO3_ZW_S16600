/**
 *
 *  @author Zbanyszek Wojciech S16600
 *
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class Server {
	private static ServerSocketChannel serverSocketChannel;
	private static Selector selector;

  public static void main(String[] args) throws IOException {
	  new Server();
  }
  
	public Server() throws IOException {
	  
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress("localhost", 1026));
			selector = Selector.open();
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch(Exception exc) {
			exc.printStackTrace();
			System.exit(1);
		}
		System.out.println("Server started.");
		serviceConnections();
  }
  
	public static void writeStringToSocketChannel(String text, SocketChannel sc) throws IOException{
		text += "\n";
		ByteBuffer buffer = ByteBuffer.wrap(text.getBytes());
		sc.write(buffer);
	}

	public static String readStringFromSocketChannel(SocketChannel sc) throws IOException {
		if(!sc.isOpen()) return "";
		
		ByteBuffer buf = ByteBuffer.allocate(1024);
		int bytesRead = sc.read(buf);
		
		return (new String(buf.array())).trim();
	}
	
	private void serviceConnections() throws IOException{
		while(true) {
				selector.select();
				Set keys = selector.selectedKeys();
				Iterator iter = keys.iterator();
				while(iter.hasNext()) {
					SelectionKey key = (SelectionKey) iter.next();
					if (key.isAcceptable()) {
						SocketChannel channel = serverSocketChannel.accept();
						if (channel != null){
							channel.configureBlocking(false);
							channel.register(selector, (SelectionKey.OP_READ | SelectionKey.OP_WRITE) );
						}
						continue;
					}

					if (key.isReadable()) {
						SocketChannel channel = (SocketChannel) key.channel();
						String result_string = /*NioHelper.*/readStringFromSocketChannel(channel);
						if(result_string.length() > 0){
							this.sendMessageToAllClients(result_string);
						}
						continue;
					}
				}
		}
	}
	
	private void sendMessageToAllClients(String message) throws IOException{
			selector.select();
			Set keys = selector.selectedKeys();
			Iterator iter = keys.iterator();
			while(iter.hasNext()) {
				SelectionKey key = (SelectionKey) iter.next();
				if (key.isWritable()) {
					SocketChannel cc = (SocketChannel) key.channel();
					writeStringToSocketChannel(message, cc);
				}
			}
	}

}
