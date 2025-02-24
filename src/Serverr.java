import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Serverr implements ActionListener {

    static JFrame frame = new JFrame();
    static JPanel panel1;
    static JPanel panel2;
    static JLabel label1;
    static ImageIcon image1;
    static JTextField texts;
    static Box vertical = Box.createVerticalBox();
    static ServerSocket server;
    static Socket socket;
    static DataInputStream in;
    static DataOutputStream out;

    Serverr(){

        image1 = new ImageIcon("server.png");
        Image i1 = image1.getImage().getScaledInstance(25,25,Image.SCALE_DEFAULT);
        ImageIcon server = new ImageIcon(i1);
        JLabel label2 = new JLabel(server);
        label2.setBounds(5,10,25,25);

        JButton button = new JButton("Send");
        button.setBounds(320,600,105,40);
        button.addActionListener(this);
        frame.add(button);

        label1 = new JLabel();
        label1.setText("Server");
        label1.setFont(new Font("MV Boli",Font.PLAIN,20));
        label1.setBounds(35, 10, 150, 25);
        label1.setBackground(new Color(240,248,255));
        label1.setOpaque(true);

        panel1 = new JPanel();
        panel1.setBackground(new Color(240,248,255));
        panel1.setBounds(0,0,450,50);
        panel1.setLayout(null);
        panel1.add(label2);
        panel1.add(label1);

        panel2 = new JPanel();
        panel2.setBounds(5,60,423,530);
        frame.add(panel2);

        texts = new JTextField();
        texts.setBounds(5,600,310,40);
        texts.setFont(new Font("SAN_SERIF",Font.PLAIN,16));
        frame.add(texts);

        frame.add(panel1);
        frame.setSize(450,700);
        frame.setLocation(200,50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.white);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            JLabel msg  = new JLabel(texts.getText());

            JPanel message = formatLabel(msg);

            panel2.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(message,BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            panel2.add(vertical,BorderLayout.PAGE_START);

            String text = texts.getText();
            out.writeUTF(text);

            texts.setText("");

            frame.repaint();
            frame.invalidate();
            frame.validate();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static JPanel formatLabel(JLabel msg){
        JPanel msgBox = new JPanel();
        msgBox.setLayout(new BoxLayout(msgBox,BoxLayout.Y_AXIS));

        msg.setFont(new Font("Noto Sans",Font.PLAIN,14));
        msg.setBackground(new Color(25,118,210));
        msg.setOpaque(true);
        msg.setForeground(Color.white);
        msg.setBorder(new EmptyBorder(15,15,15,20));

        msgBox.add(msg);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(calendar.getTime()));

        msgBox.add(time);

        return msgBox;
    }
    public static void main(String[] args){
        new Serverr();

        try{
             server = new ServerSocket(6001);
             while(true){
                socket = server.accept();
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                while (true){
                    String message = in.readUTF();
                    JLabel msg = new JLabel(message);
                    JPanel panel = formatLabel(msg);
                    msg.setBackground(new Color(25,118,210));

                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel,BorderLayout.LINE_START);
                    vertical.add(left);
                    frame.validate();
                }
            }
        }catch (UnknownHostException u){
            System.out.println(u);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
