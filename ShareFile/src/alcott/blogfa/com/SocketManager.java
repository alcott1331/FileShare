package alcott.blogfa.com;


import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketManager
{


    //---Start Client
    public static class Client
    {
        private String host="";
        private int port = 1313;
        private Socket socket;
        private Socket socket_info = null;
        private File File_TO_Send = null;
        private boolean sendDone = false;
        private int Buffer = 1024*10;
        public Client(String Host, int Port,int buffer){this.host=Host; this.port=Port;this.Buffer=buffer;}
        public boolean Init(File file_To_send)
        {
            this.File_TO_Send = file_To_send;

            try {
                if(!this.host.equals("") && this.host.length() > 4)
                {
                    if(this.port != 0 && this.port > 0)
                    {
                        //Begin
                        this.SendProperty(this.File_TO_Send.getName());

                        InetAddress address = InetAddress.getByName(this.host);
                        this.socket = new Socket(address,port);
                        this.socket.setReceiveBufferSize(Buffer);
                        this.socket.setSendBufferSize(Buffer);
                        this.socket.setSoTimeout(5000);
                        this.socket.setKeepAlive(false);

                        if(this.socket.isConnected())
                        {
                            System.out.println("--) Socket Connected!");
                            this.SocketHandle(this.socket);

                        }else {System.err.println("Socket Cont Connect~");return false;}
                        //End
                    }else {System.err.println("Port Number False"); return false;}
                }else {System.err.println("Host Address False");return false;}
                return true;
            }catch (Exception e){e.printStackTrace();return false;}
        }

        private void SendProperty(final String info)
        {
            try {
                if(socket_info == null) {
                    socket_info = new Socket(host, port + 1);
                    socket_info.getOutputStream().write(info.getBytes());
                    socket_info.close();
                    socket_info = null;
                }

            } catch (IOException e) {e.printStackTrace();}


        }


        private void SocketHandle(Socket socket)
        {
            try {

                    BufferedInputStream reader = new BufferedInputStream(( new FileInputStream(this.File_TO_Send)));
                    OutputStream stream = (socket.getOutputStream());
                    byte[] buff = new byte[Buffer];
                    int count=0;

                    while( (count = reader.read(buff,0,buff.length)) >= 0 )
                    {
                        stream.write(buff,0,count);
                        System.out.println("Send Byte = [ "+(reader.available() - count)+" ]");
                    }
                    this.sendDone = true;
                    stream.close();
                    reader.close();
                    socket.close();
                    System.out.println("\t\t\tSend Done");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //---End Client

//######################################################################################################################
//######################################################################################################################

    //###Start Server
    public static class Server
    {
        private int port=1313;
        private String FileNameToSave="x";
        private String Path=".";
        private ServerSocket serverSocket;
        private ServerSocket socket_info = null;
        private Socket AccSocketInfo = null;
        private int Buffer_size = 1024*10;
        public Server(int PortNumber,int buffer_size){this.port=PortNumber;this.Buffer_size=buffer_size;}

        public boolean Init(String Path)
        {
            this.Path = Path;
            if(this.port != 0 && this.port > 0)
            {

                try
                {
                    this.Init_getInfo();
                    this.serverSocket = new ServerSocket(this.port);
                    System.out.println(" listening . ..  ...");
                    Socket AccSocket = this.serverSocket.accept();
                    AccSocket.setReceiveBufferSize(this.Buffer_size);
                    AccSocket.setSendBufferSize(this.Buffer_size);
                    this.HandleSocket(AccSocket);

                    return true;
                }catch (Exception e)
                {
                    e.printStackTrace();
                    return false;
                }
            }else {System.err.println("Server Port False");return false;}
        }

        private void Init_getInfo()
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(socket_info == null){
                            socket_info = new ServerSocket(port+1);
                            AccSocketInfo = socket_info.accept();
                        }

                        BufferedReader reader = new BufferedReader( new InputStreamReader( AccSocketInfo.getInputStream()));
                        FileNameToSave = reader.readLine();
                        System.out.println("--) File Name:"+FileNameToSave);
                        reader.close();

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        private void HandleSocket(Socket socket)
        {
            try
            {
                byte[] buff = new byte[this.Buffer_size];
                InputStream reader = ((socket.getInputStream()));
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(Path+"/"+FileNameToSave));
                int count = 0;
                while ( (count = reader.read(buff,0,buff.length)) >= 0)
                {
                    outputStream.write(buff,0,count);
                    System.out.print("*Received Byte: "+(reader.available()+count)+"\n");
                }
                outputStream.close();
                System.out.println("#Done");
                socket.close();
            }catch (Exception e)
            {
                e.printStackTrace();
                System.err.println("Server Socket Handle ERROR");
            }
        }

    }
    //###End Server
}