package alcott.blogfa.com;


import java.io.File;

public class ConsoleManager {
    private String[] args = null;

    public ConsoleManager(String[] args) {
        this.args = args;
        this.Handle();
    }

    private void Handle() {
        if(this.args.length ==0)this.Help();
        for (byte i = 0; i < this.args.length; i++) {
            if (this.args[i].compareTo("send") == 0)
            {
                String  FilePath="";
                String IP="";
                int port=1313;
                int buffer_size=1024*10;
                for(byte x=i;x<this.args.length;x++)
                {
                    if(this.args[x].compareTo("--file") == 0){FilePath = this.args[x+1];}
                    if(this.args[x].compareTo("--ip") == 0){IP = this.args[x+1];}
                    if(this.args[x].compareTo("--port") == 0){port = Integer.parseInt(this.args[x+1]);}
                    if(this.args[x].compareTo("--buffer-size") == 0){buffer_size = Integer.parseInt(this.args[x+1]);}
                }
                if(!new File(FilePath).exists()){System.err.println("File not exists!");System.exit(0);}
                if(IP.length() <= 6 || IP == "" || IP == null){System.err.println("IP false!");System.exit(0);}
                if(port < 50){System.out.println("Port Number false! [port > 50]");System.exit(0);}
                System.out.println("File: "+FilePath+"\nIP: "+IP+"\nPort: "+port+"\nBuffer: ["+buffer_size+"] bytes");
                new SocketManager.Client(IP,port,buffer_size).Init(new File(FilePath));
            }



            if(this.args[i].compareTo("receive") == 0)
            {
                int port = 1313;
                String save_path=".";
                int buffer=1024*10;
                for(byte x=i;x<this.args.length;x++)
                {
                    if(this.args[x].compareTo("--port")==0){port = Integer.parseInt(this.args[x+1]);}
                    if(this.args[x].compareTo("--save-path")==0){save_path=this.args[x+1];}
                    if(this.args[x].compareTo("--buffer-size")==0){buffer=Integer.parseInt(this.args[x+1]);}
                }
                if(port < 50){System.out.println("Port Number false! [port > 50]");System.exit(0);}
                if(save_path == "" || save_path ==null){System.out.println("Port Number false!");System.exit(0);}
                System.out.println("port: "+port+"\nsave_path: "+save_path+"\nBuffer-size: ["+buffer+"] bytes");
                new SocketManager.Server(port,buffer).Init(save_path);
            }

            if(this.args[i].compareTo("-h")==0||
                    this.args[i].compareTo("--h")==0||
                    this.args[i].compareTo("-help")==0||
                    this.args[i].compareTo("--help")==0){this.Help();System.exit(0);}


        }
    }

    private void Help() {

        System.out.println("-_-_-_- Send File Via TCP Socket -_-_-_-"+
        "\n\nTwo methods are available:\nmethod 1 -: \n"+
        "\tsend\n"+
        "\t\t--file\t\t\t* File Path to send\n"+
        "\t\t--ip\t\t\t* Target IP\n"+
        "\t\t--port\t\t\t[Optional] Default 1331 #-> Port > 50\n"+
        "\t\t--buffer-size\t\t[Optional] Default 10240\n"+
        "Example: send --file ./file.txt --ip 192.168.0.88\n");

        System.out.println("method 2 -:\n\treceive\n"+
        "\t\t--port\t\t\t[Optional] Default 1331 #-> Port > 50\n"+
        "\t\t--save-path\t\t[Optional] Default ./\n"+
        "\t\t--buffer-size\t\t[Optional] Default 10240\n"+
        "Example: receive --port 1313 --save-path ./downloads\n\n\n\t\t\t--[MonkeyMan]--");

        System.exit(0);

    }




    //--------------------
}
