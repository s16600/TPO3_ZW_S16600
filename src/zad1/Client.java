/**
 *
 *  @author Zbanyszek Wojciech S16600
 *
 */

package zad1;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client {
	
	private SocketChannel serverSocketChannel;
	
	public static void main(String[] args) throws IOException {
		new Client();
	}
	
	public Client() throws IOException {
	  
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress("localhost", 1026));
		/*this.*/serverSocketChannel = socketChannel;
		
		ChatWindow cw = new ChatWindow(this);
		new LoginWindow(cw); //blokuje dalsze operacje do wprowadzenia hasła  
	  
		while(true){
			String result = readStringFromSocketChannel(this.serverSocketChannel);
			if(result.length() > 0){
				cw.addText(result);
			}
		}
	}
	
	public static String readStringFromSocketChannel(SocketChannel sc) throws IOException {
		if(!sc.isOpen()) return "";
		
		ByteBuffer buf = ByteBuffer.allocate(1024);
		int bytesRead = sc.read(buf);
		
		return (new String(buf.array())).trim();
	}
	
	public static void writeStringToSocketChannel(String text, SocketChannel sc) throws IOException{
		text += "\n";
		ByteBuffer buffer = ByteBuffer.wrap(text.getBytes());
		sc.write(buffer);
	}
	
	public void sendToServer(String text) throws IOException {
		// TODO Auto-generated method stub
		writeStringToSocketChannel(text, this.serverSocketChannel);
	}
  
}
