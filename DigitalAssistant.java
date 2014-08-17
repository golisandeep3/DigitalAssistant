/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitalassistant;

/**
 *
 * @author sandeep
 */
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;
import java.awt.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.Locale;
import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.*;
import javax.mail.search.FlagTerm;
import javax.mail.Flags;
import com.sun.speech.freetts.*;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
//
// Detects faces in an image, draws boxes around them, and writes the results
// to "faceDetection.png".
//
/*import org.opencv.core.Mat;
 import org.opencv.highgui.Highgui;
 import org.opencv.highgui.VideoCapture;*/

class Panel extends JPanel {

    private static final long serialVersionUID = 1L;
    //String pic_name="resources/DSC01638.jpg";  
    Mat picture;
    BufferedImage image;

    /**
     * Converts/writes a Mat into a BufferedImage.
     *
     * @param matrix Mat of type CV_8UC3 or CV_8UC1
     * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY
     */
    public BufferedImage matToBufferedImage(Mat matrix) {
        int cols = matrix.cols();
        int rows = matrix.rows();
        int elemSize = (int) matrix.elemSize();
        byte[] data = new byte[cols * rows * elemSize];
        int type;
        matrix.get(0, 0, data);
        switch (matrix.channels()) {
            case 1:
                type = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case 3:
                type = BufferedImage.TYPE_3BYTE_BGR;
                // bgr to rgb  
                byte b;
                for (int i = 0; i < data.length; i = i + 3) {
                    b = data[i];
                    data[i] = data[i + 2];
                    data[i + 2] = b;
                }
                break;
            default:
                return null;
        }
        BufferedImage image = new BufferedImage(cols, rows, type);
        image.getRaster().setDataElements(0, 0, cols, rows, data);
        return image;
    }

    // Create a constructor method  
    public Panel(Mat frame) {
        super(); // Calls the parent constructor  
        //picture = Highgui.imread(pic_name);  
        // Got to cast picture into Image  
        image = matToBufferedImage(frame);
    }

    @Override
    public void paintComponent(Graphics g) {
        System.out.println("inside paint\n");
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
    }

}

class DisplayImage extends Thread  {

    public JLabel image_label;
    public   static String curr_image = null;

    public DisplayImage(JLabel image_label1,String value) {
        image_label = image_label1;
        curr_image = value;
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    }
    public DisplayImage()
    {
        curr_image ="frame";
    }

    public   BufferedImage matToBufferedImage(Mat matrix) {
        int cols = matrix.cols();
        int rows = matrix.rows();
        int elemSize = (int) matrix.elemSize();
        byte[] data = new byte[cols * rows * elemSize];
        int type;
        matrix.get(0, 0, data);
        switch (matrix.channels()) {
            case 1:
                type = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case 3:
                type = BufferedImage.TYPE_3BYTE_BGR;
                // bgr to rgb  
                byte b;
                for (int i = 0; i < data.length; i = i + 3) {
                    b = data[i];
                    data[i] = data[i + 2];
                    data[i + 2] = b;
                }
                break;
            default:
                return null;
        }
        BufferedImage image = new BufferedImage(cols, rows, type);
        image.getRaster().setDataElements(0, 0, cols, rows, data);
        return image;
    }

    public  void run() {
        try {
            while(true)
            {
            
            if (curr_image.equalsIgnoreCase("nitial_image")) {
        ImageIcon icon =new ImageIcon(ImageIO.read( new File("C:\\Users\\sandeep\\Documents\\NetBeansProjects\\DigitalAssistant\\src\\digitalassistant\\initial_image.jpg")) );
                image_label.setIcon(icon);

            } else {
                System.out.println("Hello, OpenCV");
                
        // Load the native library.
                //System.loadLibrary("opencv_java244");
                //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                VideoCapture camera = new VideoCapture(0);
                System.out.println("inage width"+image_label.getWidth());

                camera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, image_label.getWidth());
                camera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, image_label.getHeight());
                Thread.sleep(1000);
                camera.open(0); //Useless
                if (!camera.isOpened()) {
                    System.out.println("Camera Error");
                } else {
                    System.out.println("Camera OK?");
                }

                Mat frame = new Mat();

       // camera.grab();
                //System.out.println("Frame Grabbed");
                // camera.retrieve(frame);
                //System.out.println("Frame Decoded");
                System.out.println("Frame Obtained");

                /* No difference
                 camera.release();
                 */
                System.out.println("Captured Frame Width " + frame.width());
               // JFrame frame1 = new JFrame("BasicPanel");
               // frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                // DetectFace f = new DetectFace();
                
                int count =10;
                while (true) {
                    camera.read(frame);
           // Core.putText(frame,count+"", new Point(frame.width()/4,frame.height()/4), 3, 2,new Scalar(0, 255, 0),3);

            //f.face_detect(frame);
                    BufferedImage image = matToBufferedImage(frame);
                    

                    ImageIcon icon = new ImageIcon(image);
                    
                     icon.getImage().flush();

                    image_label.setIcon(icon);
                   // Thread.sleep(500);
                    //count--;

                }
               // camera.release();
               // curr_image = "initial_image";
                
            }
            
              //  ImageIcon icon =new ImageIcon(ImageIO.read( new File("C:\\Users\\sandeep\\Documents\\NetBeansProjects\\DigitalAssistant\\src\\digitalassistant\\initial_image.jpg")) );
               // image_label.setIcon(icon);

       // camera.read(frame);
                // Highgui.imwrite("camera.jpg", frame);
                // frame1.setVisible(false);
                // System.out.println("OK");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

class Photo {

    public void capture() {
        try {
            System.out.println("Hello, OpenCV");
        // Load the native library.
            //System.loadLibrary("opencv_java244");
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            VideoCapture camera = new VideoCapture(0);
            Thread.sleep(1000);
            camera.open(0); //Useless
            if (!camera.isOpened()) {
                System.out.println("Camera Error");
            } else {
                System.out.println("Camera OK?");
            }

            Mat frame = new Mat();

       // camera.grab();
            //System.out.println("Frame Grabbed");
            // camera.retrieve(frame);
            //System.out.println("Frame Decoded");
            System.out.println("Frame Obtained");

            /* No difference
             camera.release();
             */
            System.out.println("Captured Frame Width " + frame.width());
            JFrame frame1 = new JFrame("BasicPanel");
            frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // DetectFace f = new DetectFace();
            int count = 15;
            

            while (count >0) {
                camera.read(frame);
                //f.face_detect(frame);
                frame1.setSize(frame.width(), frame.height());
          //  Core.putText(frame,count+"", new Point(frame.width()/4,frame.height()/4), 3, 2,new Scalar(0, 255, 0),3);
                // Core.rectangle(frame, new Point(frame.width()/4,frame.height()/4), new Point(frame.width()/4+300,frame.height()/4 +300), new Scalar(0, 255, 0));
                Panel panel1 = new Panel(frame);
                frame1.setContentPane(panel1);
                frame1.setVisible(true);
                Thread.sleep(100);
                count--;
            }

       // camera.read(frame);
            // Highgui.imwrite("camera.jpg", frame);
            // frame1.setVisible(false);
            // System.out.println("OK");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

class DigitalAssistant {
    //public JLabel image_label;

    /* public static void main(String args[]) throws InterruptedException {

     System.out.println("Hello, OpenCV");
     // Load the native library.
     //System.loadLibrary("opencv_java244");
     System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
     VideoCapture camera = new VideoCapture(0);
     Thread.sleep(1000);
     camera.open(0); //Useless
     if (!camera.isOpened()) {
     System.out.println("Camera Error");
     } else {
     System.out.println("Camera OK?");
     }

     Mat frame = new Mat();

     //camera.grab();
     //System.out.println("Frame Grabbed");
     //camera.retrieve(frame);
     //System.out.println("Frame Decoded");
     System.out.println("Frame Obtained");
     Mail m=new Mail();
     m.inboxreader();

     /* No difference
     camera.release();
     */
    /*  System.out.println("Captured Frame Width " + frame.width());
     JFrame frame1 = new JFrame("BasicPanel");
     frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     DetectFace f = new DetectFace();

     while (true) {
     camera.read(frame);
     f.face_detect(frame);
     frame1.setSize(frame.width(), frame.height());
     // Core.rectangle(frame, new Point(frame.width()/4,frame.height()/4), new Point(frame.width()/4+300,frame.height()/4 +300), new Scalar(0, 255, 0));
     Panel panel = new Panel(frame);
     frame1.setContentPane(panel);
     frame1.setVisible(true);
     }*/
        // Highgui.imwrite("camera.jpg", frame);
    // System.out.println("OK");
    //}
    public void start_app(JLabel image_label) {
        //this.image_label = image_label;
        ConfigurationManager cm;

        cm = new ConfigurationManager(DigitalAssistant.class.getResource("helloworld.config.xml"));

        Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
        recognizer.allocate();

        // start the microphone or exit if the programm if this is not possible
        Microphone microphone = (Microphone) cm.lookup("microphone");
        if (!microphone.startRecording()) {
            System.out.println("Cannot start microphone.");
            recognizer.deallocate();
            System.exit(1);
        }

        System.out.println("Say: (Good morning | Hello) ( Bhiksha | Evandro | Paul | Philip | Rita | Will )");

        // loop the recognition until the programm exits.
        XVoice voiceKevin16 = new XVoice("kevin16");
        while (true) {
            System.out.println("Start speaking. Press Ctrl-C to quit.\n");

            Result result = recognizer.recognize();

            if (result != null) {
                String resultText = result.getBestFinalResultNoFiller();
                System.out.println("You said: " + resultText + '\n');
                if (resultText.equalsIgnoreCase("read email") == true) {
                    //  microphone.stopRecording();
                    Mail m = new Mail();
                    m.inboxreader();
                  //  microphone.startRecording();

                } else if (resultText.equalsIgnoreCase("lisa exit") == true) {
                    microphone.stopRecording();
                    //XVoice voiceKevin16 = new XVoice("kevin16");
                    voiceKevin16.say("Thanks for using my system,have a nice day");
                    break;
                } else if (resultText.equalsIgnoreCase("capture photo") == true) {
                    // microphone.stopRecording();
                    //Photo p = new Photo();
                   // p.capture();
                    
                    //DisplayImage d =new DisplayImage(image_label,"frame");
                    //d.start();
                    DisplayImage.curr_image = "frame";

                    // XVoice voiceKevin16 = new XVoice("kevin16");
                    voiceKevin16.say("Your photo is ready");
                   //  microphone.startRecording();

                } else if (resultText.equalsIgnoreCase("fuck you") == true) {
                   // microphone.stopRecording();
                    // XVoice voiceKevin16 = new XVoice("kevin16");
                    voiceKevin16.say("fuck you too");
                    //  microphone.startRecording();
                } else {

                   // microphone.stopRecording();
                    //  XVoice voiceKevin16 = new XVoice("kevin16");
                    voiceKevin16.say("Sorry,I can't hear what you said");
                    System.out.println("Sorry,I can't hear what you said.\n");
                    //microphone.startRecording();
                }
            } else {
                // Voice voiceKevin16 = new Voice("kevin16");
                //   voiceKevin16.say("I can't hear what you said");
                System.out.println("I can't hear what you said.\n");
            }
           // microphone.stopRecording();
            //recognizer.deallocate();
            //recognizer.allocate();
            //microphone.startRecording();

        }
    }

}
/*public class DigitalAssistant
 {
 public static void main(String[] args)
 {
 Voice voiceKevin16 = new Voice("kevin16");

 String[] thingsToSay = new String[]
 {
 "hi everybody",
 "my name is kevin sixteen",
 "my voice is built into free t t s",
 "but it isn't very mellifluous",
 "it could be worse, though",
 "every time my friend alan tries to talk",
 "about anything more exciting than what time it is",
 "he barfs up a bunch of exceptions",
 "and passes out",
 };

 voiceKevin16.say(thingsToSay);
 }
 }*/

class XVoice {

    private String name;
    private com.sun.speech.freetts.Voice systemVoice;

    public XVoice(String name) {
        System.setProperty("mbrola.base", "C:\\Users\\sandeep\\Desktop\\project lisa\\freetts-1.2\\mbrola");
        this.name = "mbrola_us1";
        VoiceManager voices2 = VoiceManager.getInstance();
        Voice[] voices = voices2.getVoices();
        for (int i = 0; i < voices.length; i++) {
            System.out.println("    " + voices[i].getName()
                    + " (" + voices[i].getDomain() + " domain)");
        }
        this.systemVoice = VoiceManager.getInstance().getVoice(this.name);
        this.systemVoice.allocate();
    }

    public void say(String[] thingsToSay) {
        for (int i = 0; i < thingsToSay.length; i++) {
            this.say(thingsToSay[i]);
        }
    }

    public void say(String thingToSay) {
        this.systemVoice.speak(thingToSay);
    }

    public void dispose() {
        this.systemVoice.deallocate();
    }
}

class Mail {

    public void inboxreader() {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", "email", "password");
            System.out.println(store);
            //TextToSpeech tts = new TextToSpeech();
            XVoice voiceKevin16 = new XVoice("kevin16");

            Folder[] folder = store.getDefaultFolder().list(); // get the array folders in server
            int i = 0;
            for (i = 0; i < 5; i++) {
                System.out.println("Press " + i + " to read " + folder[i].getName() + " folder");
            }
            int choice = 0;//in.nextInt(); // getName() method returns the name of folder
            for (i = 0; i < folder.length; i++) {
                if (choice == i) {
                    folder[i].open(Folder.READ_ONLY);
                    FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);

                    // Message[] message = folder[i].getMessages();
                    int n = folder[i].getMessageCount();
                    Message[] message = folder[i].getMessages(n - 5, n);
                    // Message[] message =folder[i].search(ft);
                    // voiceKevin16.say("Hello Sandy How are you ?");
                    voiceKevin16.say("Here are the new messages for you...");

                    for (int j = 0; j < 3; j++) {
                        voiceKevin16.say("Email from," + message[j].getFrom()[0]);
                        voiceKevin16.say("subject," + message[j].getSubject());
                        System.out.println("———— Message " + (j + 1) + " ————");
                        System.out.println("SentDate : " + message[j].getSentDate());
                        System.out.println("From : " + message[j].getFrom()[0]);
                        System.out.println("Subject : " + message[j].getSubject());
                        System.out.print("Message : " + message[j].getContent());

                    }
                    folder[i].close(true);
                    // break;
                }
            }

            store.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}

class TextToSpeech {

    Synthesizer synthesizer;

    public TextToSpeech() {
        try {
            System.setProperty("freetts.voices",
                    "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

            Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
            Synthesizer synthesizer
                    = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
            synthesizer.allocate();
            synthesizer.resume();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void speaktext(String text) {
        try {

            synthesizer.speakPlainText(text, null);
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
            synthesizer.deallocate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class DetectFace {

    public void face_detect(Mat image) {
		//System.out.println("\nRunning DetectFaceDemo");

        // Create a face detector from the cascade file in the resources
        // directory.
        String test1 = getClass().getResource("lbpcascade_frontalface.xml").getPath();
        test1 = test1.replace("/C:", "C:");
        CascadeClassifier faceDetector = new CascadeClassifier(test1);
        /* String test=getClass().getResource("lena.png").getPath();
         test = test.replace("/C:", "C:");
         System.out.println(test);
         Mat image = Highgui.imread(test);*/

        // Detect faces in the image.
        // MatOfRect is a special container class for Rect.
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        //  System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        // Draw a bounding box around each face.
        for (Rect rect : faceDetections.toArray()) {
            Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }

        // Save the visualized detection.
        //  String filename = "faceDetection.png";
        //  System.out.println(String.format("Writing %s", filename));
        // Highgui.imwrite(filename, image);
    }

}

/*public class Hello {
 public static void main(String[] args) {
 System.out.println("Hello, OpenCV");

 // Load the native library.
 System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
 new DetectFaceDemo().run();
 }
 }*/
//public class DigitalAssistant {
/**
 * @param args the command line arguments
 */
   // public static void main(String[] args) {
// TODO code application logic here
// }
    
//}*/
